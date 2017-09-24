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
public class OechsleScraper implements Scraper {

  private String urlOechsle = "http://www.oechsle.pe/laptops";

  @Override
  public List<Product> parseProducts() {
    return this.getProductsData(this.urlOechsle);
  }

  @Override
  public Shop parseShop() {
    Shop oechsleShop = new Shop();
    oechsleShop.setName("Oechsle Peru");
    oechsleShop.setAddress("Av. Ejército 1009 - CC Real Plaza Arequipa ");
    oechsleShop.setUrl("http://www.oechsle.pe/");
    return oechsleShop;
  }

  private Collection<String> getUrlsProducts(String baseUrl) {
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

  private String getValueData(Document doc, String nameField) {
    /*
    * Returns text of a table.td with class ends in @Param nameField
     */
    String query = "td[class$=" + nameField + "]";
    Elements values = doc.select(query);
    if (values.isEmpty()) {
      return "";
    }
    return values.text();
  }

  private String getProductName(Document doc) {
    Elements values = doc.body().select("div[class^='fn productName']");
    return values.text();
  }

  private String formatPrice(String priceText) {
    String price = priceText.replaceAll(",", "");
    Integer position = priceText.indexOf(" ");
    price = price.substring(position + 1, price.length());
    return price;
  }

  private Vector<Double> getPrices(Document html) {
    /*
    * Returns a vector of prices founded in @Param html
    * */

    Vector<Double> prices = new Vector<Double>();
    String normal_price = html.select("strong.skuListPrice").text();
    String offer_price = html.select("strong.skuBestPrice").text();

    normal_price = formatPrice(normal_price);
    offer_price = formatPrice(offer_price);

    if (Double.parseDouble(normal_price) == 0d) {
      normal_price = offer_price;
    }
    prices.add(Double.parseDouble(normal_price));
    prices.add(Double.parseDouble(offer_price));

    return prices;
  }

  private Document getHtmlFromUrl(String url) throws IOException {
    Document doc = Jsoup.connect(url).get();
    return doc;
  }

  private Product getProduct(String urlProduct) {
    Product product = new Product();
    Vector<Double> prices = new Vector<Double>(2);
    try {
      Document doc = getHtmlFromUrl(urlProduct);
      String nameProduct = getProductName(doc);
      String sku = getValueData(doc, "Sku");
      String model = getValueData(doc, "Modelo");
      String brand = getValueData(doc, "Marca");

      prices = getPrices(doc);
      Double normal_price = prices.elementAt(0);
      Double offer_price = prices.elementAt(1);

      product.setSku(sku);
      product.setName(nameProduct);
      product.setModel(model);
      product.setBrand(brand);
      product.setNormalPrice(normal_price);
      product.setOfferPrice(offer_price);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return product;
  }

  private Vector<Product> getProductsData(String url) {
    /*
      Return a vector of characteristcs of Product contents in @Param url
    */
    Vector<Product> products = new Vector<Product>();
    Collection<String> urls = getUrlsProducts(url);
    Product product = new Product();
    for (String urlProduct : urls) {
      product = getProduct(urlProduct);
      products.addElement(product);
    }

    return products;
  }

}
