package scraper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import domain.Product;
import domain.SubSubCategory;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;


public class SagaScraperTest {


  @Test
  public void getHtmlFromURL() throws Exception {
    assertTrue(true);
  }

  @Test
  public void parse() throws IOException {

    SagaScraper sagaScraper = new SagaScraper();

    File htmlFile = new File("src/test/resources/scraper/sagaSubSubCategoryPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    Product product = sagaScraper.parse(document).get(0);

    assertTrue(
        product.getName().contentEquals("Smartphone Galaxy SM-G610MWDAPEO J7 Prime Dorado 16GB "));
    assertTrue(product.getSku().contentEquals("15979362"));

  }

  @Test
  public void getModel() throws IOException {

    SagaScraper sagaScraper = new SagaScraper();

    File htmlFile = new File("src/test/resources/scraper/sagaSubSubCategoryPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    Product product = sagaScraper.parse(document).get(0);
    System.out.println(product.getSku());

    assertTrue(product.getModel().contentEquals("SM-G610MWDAPEO"));
  }

  @Test
  public void getBrand() throws IOException {
    SagaScraper sagaScraper = new SagaScraper();

    File htmlFile = new File("src/test/resources/scraper/sagaSubSubCategoryPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    Product product = sagaScraper.parse(document).get(0);
    assertTrue(product.getBrand().contentEquals("Samsung"));

  }

  @Test
  public void getPrice() throws IOException {

    SagaScraper sagaScraper = new SagaScraper();

    File htmlFile = new File("src/test/resources/scraper/sagaSubSubCategoryPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    double parse = sagaScraper.getPrice(document);
    assertNotNull(parse);

  }

  @Test
  public void getProducts() throws Exception {
  }
}