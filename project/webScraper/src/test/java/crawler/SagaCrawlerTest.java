package crawler;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Assert.*;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class SagaCrawlerTest {

  @Test
  public void getHtmlFromURL() throws Exception {
    assertTrue(true);
  }

  @Test
  public void buildCategories() throws Exception {
    SagaCrawler sagaCrawler = new SagaCrawler();
    ArrayList<String> nameCategoriesExpected = new ArrayList<>();

    String expected[] = {"electrohogar y tecnología", "muebles y decohogar", "dormitorio",
        "deportes", "niños", "mujer", "hombre", "zapatos", "belleza", "accesorios"};

    File htmlFile = new File("src/test/resources/crawler/sagaPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    Collections.addAll(nameCategoriesExpected, expected);
    ArrayList<String> nameCategoriesCrawled = new ArrayList<>();
    List<Category> nameCat = sagaCrawler.category(document);

    for (Category aNameCat : nameCat) {
      nameCategoriesCrawled.add(aNameCat.getName());
    }

    assertEquals(nameCategoriesCrawled, nameCategoriesExpected);
  }

}