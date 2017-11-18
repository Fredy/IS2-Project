package crawler;

import static org.junit.Assert.assertEquals;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

public class SagaCrawlerTest {

  @Test
  public void subSubCategory() throws IOException {
    List<SubSubCategory> subSubCategoryUrls;
    ArrayList<String> nameCategoriesExpected = new ArrayList<>();
    SagaCrawler sagaCrawler = new SagaCrawler();

    String expected[] = {"tv led", "4k ultra hd", "curvo", "blu ray y dvd", "proyectores",
        "home theater y soundbar", "streaming", "suscripciones", "accesorios tv"};

    File htmlFile = new File("src/test/resources/crawler/sagaPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    Collections.addAll(nameCategoriesExpected, expected);
    ArrayList<String> nameCategoriesCrawled = new ArrayList<>();

    Elements elements = document != null ? document
        .select(
            ".site-wrapper #header > .fb-masthead__nav .fb-masthead__primary-wrapper .fb-masthead__primary-links__item")
        : null;

    if (elements != null) {

      Element element = elements.get(2);

      Elements elsc = element.select(
          ".fb-masthead__child-wrapper > .fb-masthead__child-wrapper-content li.fb-masthead__child-links__item ");

      Elements elssc = elsc.get(0).select(".fb-masthead__grandchild-links li a");
      subSubCategoryUrls = sagaCrawler.subSubCategory(elssc);

      for (SubSubCategory aNameCat : subSubCategoryUrls) {
        nameCategoriesCrawled.add(aNameCat.getName());
      }

    }
    assertEquals(nameCategoriesCrawled, nameCategoriesExpected);
  }

  @Test
  public void subCategory() throws IOException {

    SagaCrawler sagaCrawler = new SagaCrawler();

    String expected[] = {"tv", "computadoras", "linea blanca", "electrodomésticos",
        "teléfonos", "cámara de fotos", "videojuegos", "audio"};

    File htmlFile = new File("src/test/resources/crawler/sagaPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.falabella.com.pe");

    ArrayList<String> nameCategoriesExpected = new ArrayList<>();

    Collections.addAll(nameCategoriesExpected, expected);
    ArrayList<String> nameCategoriesCrawled = new ArrayList<>();

    Elements elements = document != null ? document
        .select(
            ".site-wrapper #header > .fb-masthead__nav .fb-masthead__primary-wrapper .fb-masthead__primary-links__item")
        : null;

    if (elements != null) {

      Element element = elements.get(2);

      Elements elsc = element.select(
          ".fb-masthead__child-wrapper > .fb-masthead__child-wrapper-content li.fb-masthead__child-links__item ");

      List<SubCategory> subCategory = sagaCrawler.subCategory(elsc);

      for (SubCategory aNameCat : subCategory) {
        nameCategoriesCrawled.add(aNameCat.getName());
      }
    }
    assertEquals(nameCategoriesCrawled, nameCategoriesExpected);
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