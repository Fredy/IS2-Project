package scraper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import domain.Product;
import java.io.File;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;


public class SagaScraperTest {

  @Test
  public void getAttr() throws IOException {

    SagaScraper sagaScraper = new SagaScraper();

    File htmlFile = new File("src/test/resources/scraper/sagaProductPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    Product product = sagaScraper.getAttr(document);

    assertTrue(
        product.getName().contentEquals("Smartphone Galaxy SM-G610MWDAPEO J7 Prime Dorado 16GB "));
    assertTrue(product.getSku().contentEquals("15979362"));

  }

  @Test
  public void getModel() throws IOException {

    SagaScraper sagaScraper = new SagaScraper();

    File htmlFile = new File("src/test/resources/scraper/sagaProductPage.html");
    Document documentIn = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    Element elementIn;

    elementIn = documentIn
        .select(
            ".site-wrapper #main > .fb-module-wrapper .fb-accordion-tabs section.fb-accordion-tabs__content .fb-product-information__product-information-tab .fb-product-information-tab__copy ul")
        .get(0);

       /*Brand*/
    String relUrlIn = elementIn.text();

    assertTrue(sagaScraper.getModel(relUrlIn).contentEquals("SM-G610MWDAPEO"));

  }

  @Test
  public void getBrand() throws IOException {
    SagaScraper sagaScraper = new SagaScraper();

    File htmlFile = new File("src/test/resources/scraper/sagaProductPage.html");
    Document documentIn = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    Element elementIn;

    elementIn = documentIn
        .select(
            ".site-wrapper #main > .fb-module-wrapper .fb-accordion-tabs section.fb-accordion-tabs__content .fb-product-information__product-information-tab .fb-product-information-tab__copy ul")
        .get(0);

       /*Brand*/
    String relUrlIn = elementIn.text();

    assertTrue(sagaScraper.getBrand(relUrlIn).contentEquals("Samsung"));

  }

  @Test
  public void getPrice() throws IOException {

    SagaScraper sagaScraper = new SagaScraper();

    File htmlFile = new File("src/test/resources/scraper/sagaProductPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    double parse = sagaScraper.getPrice(document);
    assertNotNull(parse);

  }

}