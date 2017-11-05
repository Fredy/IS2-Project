package scraper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import domain.Product;
import domain.SubSubCategory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class TottusScraperTest {

  @Test
  public void urlToJsonArray() throws Exception {
    TottusScraper tottusScraper = new TottusScraper();
    String jSonArrayScraped = tottusScraper.urlToJsonArray(
        "http://www.tottus.com.pe/tottus/browse/Abarrotes-y-Despensa-Arroz-Arroz-Integral/_/N-7nn7of");
    String comparisonJsonArray =
        "{\"id\":\"40879173\",\"name\":\"Arroz Extra Integral\",\"brand\":\"TOTTUS\",\"price\":\"21.15\",\"position\":0,\"category\":\"Abarrotes y Despensa/Arroz/Arroz Integral\",\"list\":\"plp_categoria_Arroz_Arroz Integral\"},"
      + "{\"id\":\"40737788\",\"name\":\"Arroz Extra Integral\",\"brand\":\"TOTTUS\",\"price\":\"3.25\",\"position\":1,\"category\":\"Abarrotes y Despensa/Arroz/Arroz Integral\",\"list\":\"plp_categoria_Arroz_Arroz Integral\"},"
      + "{\"id\":\"40360383\",\"name\":\"Arroz Integral \",\"brand\":\"COSTENO\",\"price\":\"3.35\",\"position\":2,\"category\":\"Abarrotes y Despensa/Arroz/Arroz Integral\",\"list\":\"plp_categoria_Arroz_Arroz Integral\"}";
    assertEquals(comparisonJsonArray, jSonArrayScraped);
  }

  @Test
  public void oneToVector() throws Exception {
    TottusScraper tottusScraper = new TottusScraper();
    String listOfProducts = tottusScraper.urlToJsonArray(
        "http://www.tottus.com.pe/tottus/browse/Abarrotes-y-Despensa-Arroz-Arroz-Integral/_/N-7nn7of");
    List<String> dataScraped = tottusScraper.oneToVector(listOfProducts);
    List<String> dataExpected = new ArrayList<>();
    String expected[] = {
        "{\"id\":\"40879173\",\"name\":\"Arroz Extra Integral\",\"brand\":\"TOTTUS\",\"price\":\"21.15\",\"position\":0,\"category\":\"Abarrotes y Despensa/Arroz/Arroz Integral\",\"list\":\"plp_categoria_Arroz_Arroz Integral\"}",
        "{\"id\":\"40737788\",\"name\":\"Arroz Extra Integral\",\"brand\":\"TOTTUS\",\"price\":\"3.25\",\"position\":1,\"category\":\"Abarrotes y Despensa/Arroz/Arroz Integral\",\"list\":\"plp_categoria_Arroz_Arroz Integral\"}",
        "{\"id\":\"40360383\",\"name\":\"Arroz Integral \",\"brand\":\"COSTENO\",\"price\":\"3.35\",\"position\":2,\"category\":\"Abarrotes y Despensa/Arroz/Arroz Integral\",\"list\":\"plp_categoria_Arroz_Arroz Integral\"}"};
    Collections.addAll(dataExpected, expected);

    assertEquals(dataExpected, dataScraped);
  }

  @Test
  public void vectorStringsToProducts() throws Exception {
    TottusScraper tottusScraper = new TottusScraper();
    String listOfProducts = tottusScraper.urlToJsonArray(
        "http://www.tottus.com.pe/tottus/browse/Abarrotes-y-Despensa-Arroz-Arroz-Integral/_/N-7nn7of");
    List<String> dataScraped = tottusScraper.oneToVector(listOfProducts);
    List<Product> productScraped = tottusScraper.vectorStringsToProducts(dataScraped,
        "http://www.tottus.com.pe/tottus/browse/Abarrotes-y-Despensa-Arroz-Arroz-Integral/_/N-7nn7of");
    String expectedname = "Arroz Extra Integral";
    Double expectedWebPrice = 3.25;
    String expectedSku = "40737788";
    String expectedBrand = "TOTTUS";

    assertTrue(productScraped.get(1).getBrand().contentEquals(expectedBrand));
    assertTrue(productScraped.get(1).getName().contentEquals(expectedname));
    assertTrue(productScraped.get(1).getSku().contentEquals(expectedSku));
    assertTrue(productScraped.get(1).getWebPrice().equals(expectedWebPrice));

  }

  @Test
  public void parseProducts() throws Exception {
    TottusScraper tottusScraper = new TottusScraper();
    SubSubCategory crawledSubSubCategory = new SubSubCategory();
    crawledSubSubCategory.setUrl(
        "http://www.tottus.com.pe/tottus/browse/Abarrotes-y-Despensa-Arroz-Arroz-Integral/_/N-7nn7of");
    List<Product> crawledProducts = tottusScraper.parseProducts(crawledSubSubCategory);

    String expectedname = "Arroz Extra Integral";
    Double expectedWebPrice = 3.25;
    String expectedSku = "40737788";
    String expectedBrand = "TOTTUS";

    assertTrue(crawledProducts.get(1).getBrand().contentEquals(expectedBrand));
    assertTrue(crawledProducts.get(1).getName().contentEquals(expectedname));
    assertTrue(crawledProducts.get(1).getSku().contentEquals(expectedSku));
    assertTrue(crawledProducts.get(1).getWebPrice().equals(expectedWebPrice));
  }

}
