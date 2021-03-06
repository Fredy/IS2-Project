package scraper;

import domain.Product;
import domain.Shop;
import domain.SubSubCategory;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinioScraper implements Scraper {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  /**
   * Receives a product doc. Returns a String containing a JSON object with the product data. If
   * this doesn't find the data, then it returns an empty string.
   *
   * @param productDoc Jsoup document containing the html of a product
   * @return JSON object in a String
   */
  String extractJsonData(Document productDoc) {
    if (productDoc == null) {
      return "";
    }

    String jsonData = "";
    Elements content = productDoc.body()
        .getElementsByTag("script");

    for (Element element : content) {
      if (element.data().contains("dataLayer")) {
        jsonData = element.data()
            .split("];")[0];
        // The content data have 2 parts, the product data and a function
        // We split them using "];"

        jsonData = jsonData.substring(jsonData.indexOf("[") + 1, jsonData.length());
        // We just need the JSON part of this script:
        break;
      }
    }

    return jsonData;
  }

  /**
   * Receives a page url that contains several products. Return a Vector<String> containing all the
   * products urls.
   *
   * @param pageDoc sub-subcategory document containing html of a page
   * @return product's urls
   */
  Vector<String> getProductsURLs(Document pageDoc) {
    Vector<String> productsUrls = new Vector<>();
    Elements products = pageDoc.body()
        .getElementById("catalogue-product-container")
        .getElementsByClass("catalogue-product row");

    for (Element element : products) {
      String relUrl = element.getElementsByTag("a")
          .attr("abs:href");
      productsUrls.add(relUrl);
    }
    return productsUrls;
  }

  /**
   * Receives an url of a sub-subcategory page. Returns the number of pages on which the products
   * are, because a single page can't contain all the products.
   *
   * @param pageDoc sub-subcategory document
   * @return number of pages in the current sub-subcategory
   */
  int getMaxPages(Document pageDoc) {
    String lastValidPage = pageDoc.body()
        .getElementsByClass("page-item").last()
        .getElementsByTag("a")
        .attr("href");

    // we got something like this:
    // https://www.linio.com.pe/c/computacion/portatiles?page=9
    // we just need the last number.
    int lastPageNum = Integer.parseInt(lastValidPage.split("=")[1]);
    return lastPageNum;
  }

  /**
   * Converts a JSON object into a Product
   *
   * @param jsonData JSON object
   * @return builded product
   */
  Product jsonToObject(String jsonData) {
    if (jsonData == null || jsonData.isEmpty()) {
      return null;
    }
    JSONObject jsonObject = new JSONObject(jsonData);
    String name = jsonObject.getString("product_name");
    double webPrice = jsonObject.getDouble("price");
    double offerPrice = jsonObject.getDouble("special_price");
    String sku = jsonObject.getString("sku_config");
    String brand = jsonObject.getString("brand");

    Product product = new Product();
    product.setName(name);
    product.setWebPrice(webPrice);
    product.setOfferPrice(offerPrice);
    product.setSku(sku);
    product.setBrand(brand);

    return product;
  }

  String getModel(Document productDoc) {
    String res = null;
    if (productDoc == null) {
      return null;
    }

    Elements model = productDoc.body()
        .getElementsByClass("product-description-container")
        .first().getElementsByClass("features-box-section")
        .first().getElementsByTag("tr")
        .get(1).getElementsByTag("td");

    if (model.first().text().equals("Modelo")) {
      res = model.last().text();
    }
    return res;
  }

  /**
   * This function gets a sub-subcategory url, using the above methods it scrape and parse the page
   * and returns all the products in that sub-subcategory.
   *
   * @param url sub-subcategory url
   * @return all the products in current sub-subcategory
   */
  Vector<Product> getProducts(String url) {
    Vector<Product> productsVec = new Vector<>();

    int lastPageNum = 0;
    try {
      Document subsubDoc = this.getHtmlFromURL(url);
      lastPageNum = this.getMaxPages(subsubDoc);
    } catch (IOException e) {
      logger.warn(e.getMessage(), e);
    }

    for (int i = 1; i <= lastPageNum; i++) {
      String pageUrl = url + "?page=" + Integer.toString(i);

      List<String> productsUrls = new Vector<>();
      try {
        Document productsPageDoc = this.getHtmlFromURL(pageUrl);
        productsUrls = this.getProductsURLs(productsPageDoc);
      } catch (IOException e) {
        logger.warn(e.getMessage(), e);
      }

      for (String prodUrl : productsUrls) {
        Document pageDoc;
        try {
          pageDoc = this.getHtmlFromURL(prodUrl);
        } catch (IOException e) {
          logger.warn(e.getMessage(), e);
          pageDoc = null;
        }
        String jsonData = this.extractJsonData(pageDoc);
        Product product = this.jsonToObject(jsonData);
        if (product == null) {
          continue;
        }
        String model = this.getModel(pageDoc);
        product.setModel(model);
        productsVec.add(product);
      }
    }
    return productsVec;
  }

  private Shop getShopData() {
    Shop shop = new Shop();
    shop.setName("Linio Perú");
    shop.setUrl("https://www.linio.com.pe/");
    shop.setAddress("Calle Rio de la plata Nro. 167 Urb. Chacarilla "
        + "de Santa Crus (Piso 7) Lima - Lima - San Isidro");
    return shop;
  }


  @Override
  public List<Product> parseProducts(SubSubCategory subSubCategory) {
    String sscategoryUrl = subSubCategory.getUrl();
    return this.getProducts(sscategoryUrl);
  }

  public Shop parseShop() {
    return this.getShopData();
  }
}