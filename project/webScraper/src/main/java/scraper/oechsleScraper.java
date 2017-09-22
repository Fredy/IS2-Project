package scraper;

import domain.Product;
import domain.Shop;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class oechsleScraper implements Scraper {

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

  public Collection<String> getUrlsProducts(String baseUrl) {
    /*
    * Returns a vector with all urls of products of @Param baseUrl
    */
    Collection<String> urls = new Vector<String>();
    try {
      Document doc = Jsoup.connect(baseUrl).get();
      Elements content = doc.getElementsByAttributeValue("class", "viewDetail");
      for (Element link : content) {
        String href = link.attr("href");
        urls.add(href);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return urls;
  }

  public String getValueData(Document doc, String nameField) {
    /* Returns text of a table.td with class ends in @Param nameField
    * */
    String query = "td[class$=" + nameField + "]";
    Elements values = doc.select(query);
    if (values.isEmpty()) {
      return "Not Found";
    }
    return values.text();
  }

  String formatPrice(String priceText) {
    String price = priceText.replaceAll(",", "");
    Integer position = priceText.indexOf(" ");
    price = price.substring(position + 1, price.length());
    return price;
  }

  Vector<Double> getPrices(Document html) {
    /*
    * Returns a vector of prices founded in @Param html
    * */

    Vector<Double> prices = new Vector<Double>();
    String normal_price = html.select("strong.skuListPrice").text();
    String offer_price = html.select("strong.skuBestPrice").text();
    //String web_price = html.select("div.newDiv").text(); //this is wrong
    normal_price = formatPrice(normal_price);
    offer_price = formatPrice(offer_price);
    //web_price = formatPrice(web_price);
    if (Double.parseDouble(normal_price) == 0d) {
      normal_price = offer_price;
    }
    prices.add(Double.parseDouble(normal_price));
    prices.add(Double.parseDouble(offer_price));
    //prices.add(Double.parseDouble(web_price));
    return prices;
  }

  public Vector<Product> getData(String url) throws IOException {
    /*
      Return a vector of characteristcs of Product contents in @Param url
    */
    Vector<Product> products = new Vector<Product>();
    Collection<String> urls = getUrlsProducts(url);
    Vector<Double> prices = new Vector<Double>(3);
    Product product = new Product();
    for (String urlProduct : urls) {
      Document doc = Jsoup.connect(urlProduct).get();
      String sku = getValueData(doc, "Sku");
      String model = getValueData(doc, "Modelo");
      String brand = getValueData(doc, "Marca");

      prices = getPrices(doc);
      Double normal_price = prices.elementAt(0);
      Double offer_price = prices.elementAt(1);
      //String web_price = prices.elementAt(2);

      product.setModel(model);
      product.setBrand(brand);
      product.setNormalPrice(normal_price);
      product.setOfferPrice(offer_price);
      //product.setWebPrice(web_price);
      products.addElement(product);
    }

    return products;
  }

}
