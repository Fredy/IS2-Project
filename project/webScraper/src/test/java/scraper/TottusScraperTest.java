package scraper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import domain.Product;
import domain.SubSubCategory;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class TottusScraperTest {

  @Test
  public void urlToJsonArray() throws Exception {
    TottusScraper tottusScraper = new TottusScraper();
    File htmlFile = new File("src/test/resources/scraper/tottusSubSubCategoryPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.tottus.com.pe/tottus/");

    String jSonArrayScraped = tottusScraper.urlToJsonArray(document);
    String comparisonJsonArray =
        "{\"id\":\"40879173\",\"name\":\"Arroz Extra Integral\",\"brand\":\"TOTTUS\",\"price\":\"21.15\",\"position\":0,\"category\":\"Abarrotes y Despensa/Arroz/Arroz Integral\",\"list\":\"plp_categoria_Arroz_Arroz Integral\"},"
            + "{\"id\":\"40360383\",\"name\":\"Arroz Integral \",\"brand\":\"COSTENO\",\"price\":\"3.35\",\"position\":1,\"category\":\"Abarrotes y Despensa/Arroz/Arroz Integral\",\"list\":\"plp_categoria_Arroz_Arroz Integral\"}";
    assertEquals(comparisonJsonArray, jSonArrayScraped);
  }

  @Test
  public void oneToVector() throws Exception {
    TottusScraper tottusScraper = new TottusScraper();
    File htmlFile = new File("src/test/resources/scraper/tottusSubSubCategoryPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.tottus.com.pe/tottus/");

    String listOfProducts = tottusScraper.urlToJsonArray(document);

    List<String> dataScraped = tottusScraper.oneToVector(listOfProducts);
    List<String> dataExpected = new ArrayList<>();
    String expected[] = {
        "{\"id\":\"40879173\",\"name\":\"Arroz Extra Integral\",\"brand\":\"TOTTUS\",\"price\":\"21.15\",\"position\":0,\"category\":\"Abarrotes y Despensa/Arroz/Arroz Integral\",\"list\":\"plp_categoria_Arroz_Arroz Integral\"}",
        "{\"id\":\"40360383\",\"name\":\"Arroz Integral \",\"brand\":\"COSTENO\",\"price\":\"3.35\",\"position\":1,\"category\":\"Abarrotes y Despensa/Arroz/Arroz Integral\",\"list\":\"plp_categoria_Arroz_Arroz Integral\"}"};
    Collections.addAll(dataExpected, expected);

    assertEquals(dataExpected, dataScraped);
  }

  @Test
  public void vectorStringsToProducts() throws Exception {
    TottusScraper tottusScraper = new TottusScraper();
    File htmlFile = new File("src/test/resources/scraper/tottusSubSubCategoryPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://www.tottus.com.pe/tottus/");

    String listOfProducts = tottusScraper.urlToJsonArray(document);
    List<String> dataScraped = tottusScraper.oneToVector(listOfProducts);
    List<Product> productScraped = tottusScraper.vectorStringsToProducts(dataScraped, document);
    String expectedname = "Arroz Integral ";
    Double expectedWebPrice = 3.35;
    String expectedSku = "40360383";
    String expectedBrand = "COSTENO";

    assertEquals(productScraped.get(1).getBrand(), expectedBrand);
    assertEquals(productScraped.get(1).getName(), expectedname);
    assertEquals(productScraped.get(1).getSku(), expectedSku);
    assertEquals(productScraped.get(1).getWebPrice(), expectedWebPrice);
  }

  @Test
  public void parseProducts() throws Exception {
    TottusScraper tottusScraper = new TottusScraper();

    SubSubCategory crawledSubSubCategory = new SubSubCategory();
    crawledSubSubCategory.setUrl(
        "http://www.tottus.com.pe/tottus/browse/Abarrotes-y-Despensa-Arroz-Arroz-Integral/_/N-7nn7of");
    List<Product> crawledProducts = tottusScraper.parseProducts(crawledSubSubCategory);

    String expectedname = "Arroz Integral ";
    Double expectedWebPrice = 3.35;
    String expectedSku = "40360383";
    String expectedBrand = "COSTENO";

    assertTrue(crawledProducts.get(1).getBrand().contentEquals(expectedBrand));
    assertTrue(crawledProducts.get(1).getName().contentEquals(expectedname));
    assertTrue(crawledProducts.get(1).getSku().contentEquals(expectedSku));
    assertTrue(crawledProducts.get(1).getWebPrice().equals(expectedWebPrice));
  }

}
