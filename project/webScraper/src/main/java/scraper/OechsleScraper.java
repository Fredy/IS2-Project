package scraper;

import domain.Product;
import domain.Shop;
import domain.SubSubCategory;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OechsleScraper implements Scraper {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Override
  public List<Product> parseProducts(SubSubCategory subSubCategory) {
    return this.getProductsData(subSubCategory.getUrl());
  }

  @Override
  public Shop parseShop() {
    Shop oechsleShop = new Shop();
    oechsleShop.setName("Oechsle Peru");
    oechsleShop.setAddress("Av. Ejército 1009 - CC Real Plaza Arequipa ");
    oechsleShop.setUrl("http://www.oechsle.pe/");
    return oechsleShop;
  }

  Collection<String> getUrls(Document doc) {
    Collection<String> urls = new Vector<>();
    Elements content = doc.getElementsByAttributeValue("class", "viewDetail");
    for (Element link : content) {
      String href = link.attr("href");
      urls.add(href);
    }
    return urls;
  }

  private Collection<String> getUrlsProducts(String baseUrl) {

    Collection<String> urls = new Vector<>();
    try {
      Document doc = Jsoup.connect(baseUrl).get();
      urls = getUrls(doc);
      /*
      Elements content = doc.getElementsByAttributeValue("class", "viewDetail");
      for (Element link : content) {
        String href = link.attr("href");
        urls.add(href);
      }
      */
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return urls;
  }

  String getValueData(Document doc, String nameField) {
    String query = "td[class$=" + nameField + "]";
    Elements values = doc.select(query);
    if (values.isEmpty()) {
      return "";
    }
    return values.text();
  }

  String getProductName(Document doc) {
    Elements values = doc.body().select("div[class^='fn productName']");
    return values.text();
  }

  String formatPrice(String priceText) {
    String price = priceText.replaceAll(",", "");
    Integer position = priceText.indexOf(" ");
    price = price.substring(position + 1, price.length());
    return price;
  }

  Vector<Double> getPrices(Document html) {

    Vector<Double> prices = new Vector<>();
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

  private Document getHtmlFromUrl(String url) throws IOException, HttpStatusException {
    return Jsoup.connect(url).get();
  }

  Product getProduct(String urlProduct) {
    Product product = new Product();
    Vector<Double> prices = new Vector<>(2);
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

      logger.info(">Product: {}", nameProduct);
      logger.info("  sku: {}", sku);
      logger.info("  model: {}", model);
      logger.info("  brand: {}", brand);
      logger.info("  normalPrice: {}", normal_price);
      logger.info("  offerPrice: {}", offer_price);

    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return product;
  }

  Vector<Product> getProductsData(String url) {
    Vector<Product> products = new Vector<>();
    Collection<String> urls = getUrlsProducts(url);
    Product product = new Product();
    for (String urlProduct : urls) {
      logger.debug("[OK] Url visited: {} ", urlProduct);
      product = getProduct(urlProduct);
      products.addElement(product);
    }
    logger.debug("Number of Product Scraped: {}", products.size());
    return products;
  }

}
