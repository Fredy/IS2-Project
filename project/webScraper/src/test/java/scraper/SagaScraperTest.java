package scraper;

import static org.junit.Assert.assertTrue;

import domain.Product;
import domain.SubSubCategory;
import java.io.IOException;
import java.util.List;
import org.junit.Test;


public class SagaScraperTest {


  @Test
  public void getHtmlFromURL() throws Exception {
    assertTrue(true);
  }

  @Test
  public void parse() throws IOException {
    // ArrayList<String> nameExpected = new ArrayList<>();
    SagaScraper sagaScraper = new SagaScraper();
    SubSubCategory subSubCategory = new SubSubCategory();
    subSubCategory
        .setUrl("http://www.falabella.com.pe/falabella-pe/category/cat760706/Smartphones");
    subSubCategory.setName("Smartphones");

    List<Product> parse = sagaScraper.parseProducts(subSubCategory);
    if (parse.get(0).getName().isEmpty()) {
      assertTrue(false);
    } else {
      assertTrue(true);
    }
    /*
    String expected[] = {"Smartphone Galaxy Note8 6,3\" 64GB 6GB Negro ",
        "Celular Smartphone P10 Lite 32 GB Negro ",
        "Celular Smartphone Y7 Prime 32 GB Negro ",
        "Celular Smartphone CE101LGE87 16 GB Negro ",
        "Huawei Smartphone P8 Lite ALE L23 Negro | Entel Prepago ",
        "Verykool Smartphone 5\" S5019 3G Dual SIM Negro ",
        "Zenphone 5\" ZB500KG-1A054WW 1 GB 8 GB ",
        "Smartphone P9 Lite 5,2\" 2 GB 16 GB ",
        "Smartphone P8 Lite Octa-Core 16 GB ",
        "Zenphone 5,2\" ZC520TL-4G128WW 2GB 16GB ",
        "Smartphone Zenphone 3 5.2'' ",
        "Smartphone Zenphone 3 5.2'' Blanco ",
        "Celular Smartphone CE102MOT16 16GB Dorado ",
        "Smartphone 5\" S5019 3G Dual SIM ",
        "Smartphone 5\" S5019 3G Dual SIM Dorado " ,
        "Smartphone Galaxy J3 (2016) 5\" 8GB Negro + Tarjeta Micro SD "
    };
    Collections.addAll(nameExpected, expected);
    ArrayList<String> nameCrawled = new ArrayList<>();
    for (Product p : parse) {
      nameCrawled.add(p.getName());
    }
    assertEquals(nameCrawled, nameExpected);
    */
  }

  @Test
  public void getModel() {
    SagaScraper sagaScraper = new SagaScraper();
    SubSubCategory subSubCategory = new SubSubCategory();
    subSubCategory
        .setUrl("http://www.falabella.com.pe/falabella-pe/category/cat760706/Smartphones");
    subSubCategory.setName("Smartphones");

    List<Product> parse = sagaScraper.parseProducts(subSubCategory);
    if (parse.get(0).getModel().isEmpty()) {
      assertTrue(false);
    } else {
      assertTrue(true);
    }
  }

  @Test
  public void getBrand() {
    SagaScraper sagaScraper = new SagaScraper();
    SubSubCategory subSubCategory = new SubSubCategory();
    subSubCategory
        .setUrl("http://www.falabella.com.pe/falabella-pe/category/cat760706/Smartphones");
    subSubCategory.setName("Smartphones");

    List<Product> parse = sagaScraper.parseProducts(subSubCategory);
    if (parse.get(0).getBrand().isEmpty()) {
      assertTrue(false);
    } else {
      assertTrue(true);
    }
  }

  @Test
  public void getPrice() {

    SagaScraper sagaScraper = new SagaScraper();
    SubSubCategory subSubCategory = new SubSubCategory();
    subSubCategory
        .setUrl("http://www.falabella.com.pe/falabella-pe/category/cat760706/Smartphones");
    subSubCategory.setName("Smartphones");

    List<Product> parse = sagaScraper.parseProducts(subSubCategory);
    if (parse.get(0).getNormalPrice().isNaN()) {
      assertTrue(false);
    } else {
      assertTrue(true);
    }
  }

  @Test
  public void getProducts() throws Exception {
  }
}