package scraper;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class OechsleScraperTest {

  @Test
  public void getProduct() throws Exception {
    OechsleScraper scraper = new OechsleScraper();

    File oechsleHtml = new File("src/test/resources/scraper/oechsleProduct.html");

    Document doc = Jsoup.parse(oechsleHtml, "UTF-8", "http://www.oechsle.com");

    String nameProduct = scraper.getProductName(doc);
    String sku = scraper.getValueData(doc, "sku");
    String model = scraper.getValueData(doc, "Modelo");
    String brand = scraper.getValueData(doc, "Marca");

    assertEquals(nameProduct, "Laptop IP110 8GB 1TB 15.6\" Negro");
    assertEquals(sku, "904971");
    assertEquals(model, "IP110");
    assertEquals(brand, "Lenovo");

  }

  @Test
  public void formatPrice() {
    OechsleScraper scraper = new OechsleScraper();

    String priceFormated = scraper.formatPrice("2.199,00");
    assertEquals(priceFormated, "2.19900");
  }

  @Test
  public void getUrls() throws Exception {
    OechsleScraper scraper = new OechsleScraper();
    File oechsleComputo = new File("src/test/resources/scraper/oechsleComputo.html");
    Document doc = Jsoup.parse(oechsleComputo, "UTF-8", "https://www.oechsle.pe");

    Collection<String> urlsCollection = scraper.getUrls(doc);
    List<String> urls = new ArrayList<>(urlsCollection);
    assertEquals(urls.get(0),
        "http://www.oechsle.pe/lenovo-laptop-ip110-8gb-1tb-15-6--negro-904971/p");
    assertEquals(urls.get(1),
        "http://www.oechsle.pe/lenovo-laptop-ip510-8gb-1tb-15-6--negro-867089/p");
    assertEquals(urls.get(2), "http://www.oechsle.pe/asus-laptop-x540ya-4gb-1tb-15-6-negro/p");
    assertEquals(urls.get(3),
        "http://www.oechsle.pe/acer-laptop-e5-575g-59s-6gb-1tb-15-6-pulgadas-negro/p");
  }

}