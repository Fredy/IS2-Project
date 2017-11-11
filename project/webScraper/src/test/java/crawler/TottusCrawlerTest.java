package crawler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

public class TottusCrawlerTest {

  @Test
  public void buildCategories() throws Exception {
    TottusCrawler tottusCrawler = new TottusCrawler();

    ArrayList<String> nameCategoriesExpected = new ArrayList<>();
    String expected[] = {"OFERTAS", "Abarrotes y Despensa",
        "Bebidas y Licores", "Frutas y Verduras",
        "Carnes, Pollos y Pescados", "Embutidos y Lácteos",
        "Congelados y Panadería", "Perfumería, Aseo y Limpieza",
        "Mascotas y Hogar", "Electrohogar"};
    Collections.addAll(nameCategoriesExpected, expected);
    ArrayList<String> nameCategoriesCrawled = new ArrayList<>();
    List<Category> nameCat = tottusCrawler.buildCategories();
    for (Category aNameCat : nameCat) {
      nameCategoriesCrawled.add(aNameCat.getName());
    }
    assertEquals(nameCategoriesCrawled, nameCategoriesExpected);
  }

  @Test
  public void crawlingCategory() throws Exception {
    TottusCrawler tottusCrawler = new TottusCrawler();
    File htmlFile = new File("src/test/resources/crawler/tottusPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.tottus.com.pe/tottus/");
    List<Category> categoriesCrawled = tottusCrawler.crawlingCategory(document);
    String firstCategoryExpected = "OFERTAS";
    String lastCategoryExpected = "Electrohogar";

    assertTrue(categoriesCrawled.get(0).getName().contentEquals(firstCategoryExpected));
    assertTrue(categoriesCrawled.get(categoriesCrawled.size() - 1).getName()
        .contentEquals(lastCategoryExpected));
  }

  @Test
  public void crawlingSubCategory() throws Exception {
    TottusCrawler tottusCrawler = new TottusCrawler();
    File htmlFile = new File("src/test/resources/crawler/tottusPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.tottus.com.pe/tottus/");
    Elements categoryElements = document.body()
        .getElementsByClass("small-nav-menu");
    Element comparisonElement = categoryElements.get(3);
    List<SubCategory> subCategoriesCrawled = tottusCrawler.crawlingSubCategory(comparisonElement);
    ArrayList<String> nameSubCategoriesCrawled = new ArrayList<>();
    for (SubCategory aNameSubCat : subCategoriesCrawled) {
      nameSubCategoriesCrawled.add(aNameSubCat.getName());
    }
    ArrayList<String> nameSubCategoriesExpected = new ArrayList<>();
    String expected[] = {"frutas", "verduras", "más verduras", "graneles"};
    Collections.addAll(nameSubCategoriesExpected, expected);

    assertEquals(nameSubCategoriesExpected, nameSubCategoriesCrawled);
  }

  @Test
  public void crawlingSubSubCategory() throws Exception {
    TottusCrawler tottusCrawler = new TottusCrawler();
    File htmlFile = new File("src/test/resources/crawler/tottusPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.tottus.com.pe/tottus/");
    Elements categoryElements = document.body()
        .getElementsByClass("small-nav-menu");
    Element comparisonElement = categoryElements.get(1);
    AtomicReference<Elements> subCategoryElements = new AtomicReference<>();
    subCategoryElements.set(comparisonElement
        .getElementsByClass("col-md-2-4"));
    Element subComparisonElement = subCategoryElements.get().get(1);

    List<SubSubCategory> subSubCategoriesCrawled = tottusCrawler
        .crawlingSubSubCategory(subComparisonElement);
    ArrayList<String> nameSubSubCategoriesCrawled = new ArrayList<>();
    for (SubSubCategory aNameSubSubCat : subSubCategoriesCrawled) {
      nameSubSubCategoriesCrawled.add(aNameSubSubCat.getName());
    }

    ArrayList<String> nameSubSubCategoriesExpected = new ArrayList<>();
    String expected[] = {"arroz integral", "arroz especial", "arroz extra", "arroz superior"};
    Collections.addAll(nameSubSubCategoriesExpected, expected);

    assertEquals(nameSubSubCategoriesExpected, nameSubSubCategoriesCrawled);
  }
}
