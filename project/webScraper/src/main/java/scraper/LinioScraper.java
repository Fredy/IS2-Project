package scraper;

import domain.Product;
import domain.Shop;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinioScraper implements Scraper {

  private String baseURL = "https://www.linio.com.pe/c/computacion/portatiles";

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  private String extractJsonData(Document productDoc) {
    /* Receives a product url.
     * Return a String containing a JSON object with the product data.
     * If this doesn't find the data, then it returns an empty string. */
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

  private Vector<String> getProductsURLs(String pageUrl) {
    /* Receives a page url that contains several products.
     * Return a Vector<String> containing all the products urls. */

    Vector<String> productsUrls = new Vector<String>();
    try {
      Document pageDoc = this.getHtmlFromURL(pageUrl);

      Elements products = pageDoc.body()
          .getElementById("catalogue-product-container")
          .getElementsByClass("catalogue-product row");

      for (Element element : products) {
        String relUrl = element.getElementsByTag("a")
            .attr("abs:href");
        // NOTE: this if is used to just get laptops.
        if (relUrl.contains("notebook") || relUrl.contains("laptop") || relUrl.contains("macbook")
            || relUrl.contains("portatil")) {
          productsUrls.add(relUrl);
        } else {
          continue;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return productsUrls;
  }

  private int getMaxPages(String baseUrl) {
    /* Receives an url of a category page.
     * Returns the number of pages on which the products are,
     * because a single page can't contain all the products.
     */
    int lastPageNum = 0;
    try {
      Document pageDoc = this.getHtmlFromURL(baseUrl);

      String lastValidPage = pageDoc.body()
          .getElementsByClass("page-item").last()
          .getElementsByTag("a")
          .attr("href");

      // we got something like this:
      // https://www.linio.com.pe/c/computacion/portatiles?page=9
      // we just need the last number.
      lastPageNum = Integer.parseInt(lastValidPage.split("=")[1]);

    } catch (IOException e) {
      e.printStackTrace();
    }
    return lastPageNum;
  }

  private Product jsonToObject(String jsonData) {
    // TODO: make this function convert jsonData into a Product object.
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

  private String getModel(Document productDoc) {
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

  private Vector<Product> getProducts(String url) {
    /* This function gets a category url, using the above methods it scrape
     * and parse the page and returns all the products in that category. */

    Vector<Product> productsVec = new Vector<Product>();
    int lastPageNum = this.getMaxPages(url);

    for (int i = 1; i <= lastPageNum; i++) {
      String pageUrl = url + "?page=" + Integer.toString(i);
      Vector<String> productsUrls = this.getProductsURLs(pageUrl);
      for (String prodUrl : productsUrls) {
        Document pageDoc;
        try {
          pageDoc = this.getHtmlFromURL(prodUrl);
        } catch (IOException e) {
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
  public List<Product> parseProducts() {
    return this.getProducts(this.baseURL);
  }

  public Shop parseShop() {
    return this.getShopData();
  }
}
