package crawler;

import static org.junit.Assert.assertEquals;

import domain.Category;
import java.io.File;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class OechsleCrawlerTest {

  public void crawlCategories() throws Exception {
    OechsleCrawler crawler = new OechsleCrawler();

    File oechsleHtml = new File("src/test/resources/crawler/oechsle.html");

    Document doc = Jsoup.parse(oechsleHtml, "UTF-8", "http://www.oechsle.com");

    List<Category> categories = crawler.crawlingCategories(doc);

    assertEquals(categories.get(0).getName(), "tecnología");
    assertEquals(categories.get(1).getName(), "electrohogar");
    assertEquals(categories.get(2).getName(), "decohogar");
    assertEquals(categories.get(3).getName(), "muebles");
  }
}