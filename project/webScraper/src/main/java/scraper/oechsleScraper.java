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

  public String getData(String url) throws IOException {
    Collection<String> urls = getUrlsProducts(url);
    Product product = new Product();
    for (String urlProduct : urls) {
      Document doc = Jsoup.connect(urlProduct).get();
      Elements characteristics = doc.getElementsByAttributeValue("class", "group Ficha-Tecnica");
      Element chars = characteristics.tagName("table").last().tagName("tbody");
      System.out.println(chars.text());
      System.out.println("--------------------------------------");
    }
    return "";
  }


}
