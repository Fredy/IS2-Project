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

  private String extractJsonData(String productUrl) {
    /* Receives a product url.
     * Return a String containing a JSON object with the product data.
     * If this doesn't find the data, then it returns an empty string. */

    try {
      Document pageDoc = Jsoup.connect(productUrl)
          .userAgent("Mozilla")
          .get();

      Element content = pageDoc.body()
          .getElementsByTag("script")
          .first();

      // The content data have 2 parts, the product data and a function
      // We split them using "];"
      String jsonData = content.data()
          .split("];")[0];

      if (!jsonData.contains("dataLayer")) {
        return ""; // TODO: if datLayer is not found, then this page doesn't have contents.
      }

      // We just need the JSON part of this script:
      jsonData = jsonData.substring(jsonData.indexOf("[") + 1, jsonData.length());
      return jsonData;
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }

  private Vector<String> getProductsURLs(String pageUrl) {
    /* Receives a page url that contains several products.
     * Return a Vector<String> containing all the products urls. */

    Vector<String> productsUrls = new Vector<String>();
    try {
      Document pageDoc = Jsoup.connect(pageUrl)
          .userAgent("Mozilla")
          .get();

      Elements products = pageDoc.body()
          .getElementById("catalogue-product-container")
          .getElementsByClass("catalogue-product row");

      for (Element element : products) {
        String relUrl = element.getElementsByTag("a")
            .attr("abs:href");
        productsUrls.add(relUrl);
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
      Document pageDoc = Jsoup.connect(baseUrl)
          .userAgent("Mozilla")
          .get();

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

  private void jsonToObject(String jsonData) {
    // TODO: make this function convert jsonData into a Product object.
    if (jsonData == null || jsonData.isEmpty()) {
      return;
    }
    JSONObject jsonObject = new JSONObject(jsonData);
    String name = jsonObject.getString("product_name");
    double webPrice = jsonObject.getDouble("price");
    double offerPrice = jsonObject.getDouble("special_price");
    String sku = jsonObject.getString("sku_config");
    String model = "";
    String brand = jsonObject.getString("brand");
    System.out.print(name);
    System.out.print(" - ");
    System.out.print(webPrice);
    System.out.print(" - ");
    System.out.print(offerPrice);
    System.out.print(" - ");
    System.out.print(sku);
    System.out.print(" - ");
    System.out.print(model);
    System.out.print(" - ");
    System.out.print(brand);
    System.out.println();
  }

  private void getProducts(String url) {
    /* This function gets a category url, using the above methods it scrape
     * and parse the page and returns all the products in that category. */

    // TODO: make it return a vector<Product>
    int lastPageNum = this.getMaxPages(url);
    for (int i = 1; i <= lastPageNum; i++) {
      String pageUrl = url + "?page=" + Integer.toString(i);
      Vector<String> productsUrls = this.getProductsURLs(pageUrl);
      for (String prodUrl : productsUrls) {
        String jsonData = this.extractJsonData(prodUrl);
        this.jsonToObject(jsonData);
      }
    }
  }

  // TODO: product model missing.

  @Override
  public List<Product> parseProduct() {
    return null;
  }

  @Override
  public void saveProduct(List<Product> product) {

  }

  @Override
  public List<Product> getProduct() {
    return null;
  }

  @Override
  public Shop parseShop() {
    return null;
  }

  @Override
  public void saveShop(Shop shop) {

  }

  @Override
  public Shop getShop() {
    return null;
  }
}
