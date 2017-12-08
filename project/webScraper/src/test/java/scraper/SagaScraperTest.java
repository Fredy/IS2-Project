package scraper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import domain.Product;
import domain.Shop;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
    assertTrue(product.getModel().contentEquals("SM-G610MWDAPEO"));
    assertTrue(product.getBrand().contentEquals("Samsung"));
    assertNotNull(product.getNormalPrice());

  }

  @Test
  public void getShop() {
    SagaScraper sagaScraper = new SagaScraper();
    Shop shop = sagaScraper.parseShop();
    assertTrue(shop.getName().contentEquals("Saga Falabella Perú"));
    assertTrue(shop.getUrl().contentEquals("http://www.falabella.com.pe"));
    assertTrue(
        shop.getAddress().contentEquals("Av. Paseo de la República 3220 - San Isidro - Lima"));

  }

  @Test
  public void getUrl() throws IOException {
    SagaScraper sagaScraper = new SagaScraper();

    File htmlFile = new File("src/test/resources/scraper/sagaSubSubCategoryPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    List<String> productUrl = sagaScraper.getUrl(document);
    assertTrue(productUrl.get(0).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/15979362/Smartphone-Galaxy-SM-G610MWDAPEO-J7-Prime-Dorado-16GB/15979362"));
    assertTrue(productUrl.get(1).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/16001489/Smartphone-Galaxy-Note8-6%2C3-64GB-6GB-Negro/16001489"));
    assertTrue(productUrl.get(2).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/15979355/Smartphone-Galaxy-SM-A720FZKLPEO-A7-32GB-Negro/15979355"));
    assertTrue(productUrl.get(3).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/15916154/Celular-Smartphone-P10-Lite-32-GB-Negro/15916154"));
    assertTrue(productUrl.get(4).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/15979335/Smartphone-Galaxy-S8-Plus-SM-G955FZKLPEO-64GB-Negro/15979335"));
    assertTrue(productUrl.get(5).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/15916153/Celular-Smartphone-Y7-Prime-32-GB-Negro/15916153"));
    assertTrue(productUrl.get(6).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/15979368/Smartphone-Galaxy-SM-G570MZKAPEO-J5-Prime-Negro-16GB/15979368"));
    assertTrue(productUrl.get(7).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/15941952/Celular-Smartphone-CE101LGE87-16-GB-Negro/15941952"));
    assertTrue(productUrl.get(8).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/15169358/Huawei-Smartphone-P8-Lite-ALE-L23-Negro-Entel-Prepago/15169358"));
    assertTrue(productUrl.get(9).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/15805185/Verykool-Smartphone-5-S5019-3G-Dual-SIM-Negro/15805185"));
    assertTrue(productUrl.get(10).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/prod5270004/Zenphone-5-ZB500KG-1A054WW-1-GB-8-GB/15133357"));
    assertTrue(productUrl.get(11).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/prod5110001/Smartphone-P9-Lite-5%2C2-2-GB-16-GB/15104817"));
    assertTrue(productUrl.get(12).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/prod5100007/Smartphone-P8-Lite-Octa-Core-16-GB/14882679"));
    assertTrue(productUrl.get(13).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/prod5270005/Zenphone-5%2C2-ZC520TL-4G128WW-2GB-16GB/15133354"));
    assertTrue(productUrl.get(14).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/prod5270006/Smartphone-Zenphone-3-5.2%27%27/15133352"));
    assertTrue(productUrl.get(15).contentEquals(
        "http://www.falabella.com.pe/falabella-pe/product/15133353/Smartphone-Zenphone-3-5.2%27%27-Blanco/15133353"));
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