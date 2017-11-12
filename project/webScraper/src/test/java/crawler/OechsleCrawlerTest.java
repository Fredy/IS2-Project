package crawler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import domain.Category;
import java.io.File;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class OechsleCrawlerTest {

  @Test
  public void crawlCategories() throws Exception {

    OechsleCrawler crawler = new OechsleCrawler();

    File oechsleHtml = new File("src/test/resources/crawler/oechsle.html");

    Document doc = Jsoup.parse(oechsleHtml, "UTF-8", "http://www.oechsle.com");

    List<Category> categories;
    categories = crawler.crawlingCategories(doc);

    assertEquals(categories.get(0).getName(), "tecnología");
    assertEquals(categories.get(1).getName(), "electrohogar");
    assertEquals(categories.get(2).getName(), "decohogar");
    assertEquals(categories.get(3).getName(), "muebles");

    assertTrue(true);
  }

  @Test
  public void crawSubcategories() throws Exception {
    OechsleCrawler crawler = new OechsleCrawler();

    File oechsleHtml = new File("src/test/resources/crawler/oechsle.html");

    Document doc = Jsoup.parse(oechsleHtml, "UTF-8", "http://www.oechsle.com");

    List<Category> categories;
    categories = crawler.crawlingCategories(doc);
    crawler.crawlingSubCategories(doc, categories);

    assertEquals(categories.get(0).getSubCategories().get(0).getName(), "tv-video");
    assertEquals(categories.get(0).getSubCategories().get(1).getName(), "cómputo");
  }

  @Test
  public void crawSubSubcategories() throws Exception {
    OechsleCrawler crawler = new OechsleCrawler();

    File oechsleHtml = new File("src/test/resources/crawler/oechsle.html");

    Document doc = Jsoup.parse(oechsleHtml, "UTF-8", "http://www.oechsle.com");

    List<Category> categories;
    categories = crawler.crawlingCategories(doc);
    crawler.crawlingSubCategories(doc, categories);
    crawler.crawlingSubSubCategories(doc, categories);

    assertEquals(categories.get(0).getSubCategories().get(1).getSubSubCategories().get(0).getName(),
        "laptops");
    assertEquals(categories.get(0).getSubCategories().get(1).getSubSubCategories().get(1).getName(),
        "tablets");
  }
}