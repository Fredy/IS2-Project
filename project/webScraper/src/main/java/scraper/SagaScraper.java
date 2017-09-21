package scraper;

import domain.Product;
import domain.Shop;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SagaScraper implements Scraper {

  private static String url = "http://www.falabella.com.pe/falabella-pe/category/cat1590466/Laptops";

  public static void main(String[] args) throws IOException {
    print("Fetching %s...", url);
    Vector<String> parse = parse(url);
  }

  private static void print(String msg, Object... args) {
    System.out.println(String.format(msg, args));
  }

  private static String trim(String s, int width) {
    if (s.length() > width) {
      return s.substring(0, width - 1) + ".";
    } else {
      return s;
    }
  }


  public static Vector<String> parse(String url) throws IOException {

    Vector<String> productsUrls = new Vector<String>();
    Document doc = Jsoup.connect(url)
        .get();

    String htmlString = doc.html();

    Document document = Jsoup.parse(htmlString);
    Elements elements = document
        .select(
            ".site-wrapper #main > div#fbra_browseProductList .fb-filters .fb-pod-group > div.fb-pod-group__item .fb-pod__item > .fb-pod__header > a[href]");

    for (Element element : elements) {
      String relUrl = "http://www.falabella.com.pe" + element.attr("href");
      System.out.println(relUrl);
      productsUrls.add(relUrl);

      /*Access the other page - Begin*/

      Document docIn = Jsoup.connect(relUrl)
          .get();
      String htmlStringIn = docIn.html();

      Document documentIn = Jsoup.parse(htmlStringIn);
      Elements elementsIn = documentIn
          .select(
              ".site-wrapper #main > .fb-module-wrapper .fb-accordion-tabs section.fb-accordion-tabs__content .fb-product-information__product-information-tab .fb-product-information-tab__copy ul");

      for (Element elementIn : elementsIn) {
        String relUrlIn = elementIn.text();
        System.out.println("other " + relUrlIn);

      }
      /*End*/
    }
    return productsUrls;
  }

  //Product model

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
