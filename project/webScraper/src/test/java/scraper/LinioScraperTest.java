package scraper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import domain.Product;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

public class LinioScraperTest {


  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void extractJsonData() throws Exception {
    LinioScraper scraper = new LinioScraper();

    File htmlFile = new File("src/test/resources/scraper/linioProductPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "https://www.linio.com.pe");

    String jsonData = scraper.extractJsonData(document);
    String comparisonData =
        "{\"mobile_app_log\":0,\"is_linio_plus\":0,\"gender\":\"n\\/a\",\"nls\":null,"
            + "\"experiment_id\":false,\"variation_id\":\"0\",\"category1\":\"computacion\","
            + "\"category2\":\"portatiles\",\"category3\":\"laptops\",\"category_id\":13329,"
            + "\"category_full\":\"computacion\\/portatiles\\/laptops\",\"categoryKey1\":"
            + "\"portatiles\\/laptops\",\"categoryKey2\":\"computacion\",\"rsin\":\"null\","
            + "\"sku_simple\":\"AS282EL01A3LALPE-4082101\",\"sku_config\":\"AS282EL01A3LALPE\","
            + "\"price\":6499.99,\"msrpPrice\":6499.99,\"special_price\":5199,\"product_id\":"
            + "\"3805120\",\"product_name\":\"ASUS 17.3\\\"  "
            + "I7 7700HQ 16GB 1TB 256GB SSD 4GB DDR5 DVD GL753VE-DS74 \",\"is_international\":0,"
            + "\"ean_code\":\"889349607626\",\"is_master\":0,\"brand\":\"Asus\","
            + "\"large_image\":\"\\/\\/i2.linio.com\\/p\\/e6d12a9e5e8a727b3c6517013ea0b108-product.jpg\","
            + "\"small_image\":\"\\/\\/i2.linio.com\\/p\\/5e5bfc05917af20048436c24887a4b13-product.jpg\","
            + "\"new_user\":2,\"pageType\":\"product\",\"ecommerce\":{\"detail\":"
            + "{\"products\":[{\"name\":\"ASUS 17.3\\\"  "
            + "I7 7700HQ 16GB 1TB 256GB SSD 4GB DDR5 DVD GL753VE-DS74 \",\"id\":"
            + "\"AS282EL01A3LALPE\",\"price\":6499.99,\"brand\":\"Asus\",\"category\":"
            + "\"Laptops\",\"variant\":\"default\"}]}},\"mail_hash\":\"n\\/a\",\"customer\":"
            + "{\"fastLaneEnabled\":false}}";

    assertTrue(jsonData.contentEquals(comparisonData));
  }

  @Test
  public void getProductsURLs() throws Exception {
    LinioScraper scraper = new LinioScraper();

    File htmlFile = new File("src/test/resources/scraper/linioSubSubCategoryPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "https://www.linio.com.pe");

    List<String> productsUrls = scraper.getProductsURLs(document);
    List<String> comparisonUrls = new ArrayList<>();
    comparisonUrls.add(
        "https://www.linio.com.pe/p/gatito-plegable-animal-dome-stico-del-gato-del-tu-nel-con-el-anillo-de-bell-plegable-55cm-colorido-s288x8");
    comparisonUrls.add(
        "https://www.linio.com.pe/p/pelota-de-juguete-para-mascotas-se-tenis-juguetes-tipo-de-huellas-de-perro-color-al-azar-tncick");
    comparisonUrls.add(
        "https://www.linio.com.pe/p/pixnor-resina-casa-snooze-nest-bed-fantasma-cabina-cueva-en-chinchilla-hamster-rata-del-animal-dome-stico-v9o0wn");
    comparisonUrls.add(
        "https://www.linio.com.pe/p/miryo-caliente-nueva-mascota-ha-mster-rata-ratones-jerbos-correr-jogging-deportes-rueda-juguetes-al-azar-vaxj1u");
    comparisonUrls.add(
        "https://www.linio.com.pe/p/miryo-calor-natural-cera-mica-emisor-aparatos-reptil-mascota-luz-bombilla-de-cri-a-qfw36c");
    comparisonUrls
        .add("https://www.linio.com.pe/p/greenleaf-arthur-dollhouse-kit-1-pulgadas-escala-jrv215");
    comparisonUrls
        .add("https://www.linio.com.pe/p/gato-fijo-bolsas-de-cachorro-malla-grooming-vee88m");
    comparisonUrls
        .add("https://www.linio.com.pe/p/gato-fijo-bolsas-de-cachorro-malla-grooming-vezn36");
    comparisonUrls.add(
        "https://www.linio.com.pe/p/hateli-cepillo-para-limpieza-de-cabello-para-perros-self-cleaning-slicker-brush-s1hazd");
    comparisonUrls.add(
        "https://www.linio.com.pe/p/hateli-cepillo-para-limpieza-de-cabello-para-perros-self-cleaning-slicker-brush-oqelzt");

    assertEquals(productsUrls, comparisonUrls);
  }

  @Test
  public void getMaxPages() throws Exception {
    LinioScraper scraper = new LinioScraper();

    File htmlFile = new File("src/test/resources/scraper/linioSubSubCategoryPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "https://www.linio.com.pe");

    assertEquals(scraper.getMaxPages(document), 5);
  }

  @Test
  public void jsonToObject() throws Exception {
    LinioScraper scraper = new LinioScraper();
    String jsonData =
        "{\"mobile_app_log\":0,\"is_linio_plus\":0,\"gender\":\"n\\/a\",\"nls\":null,"
            + "\"experiment_id\":false,\"variation_id\":\"0\",\"category1\":\"computacion\","
            + "\"category2\":\"portatiles\",\"category3\":\"laptops\",\"category_id\":13329,"
            + "\"category_full\":\"computacion\\/portatiles\\/laptops\",\"categoryKey1\":"
            + "\"portatiles\\/laptops\",\"categoryKey2\":\"computacion\",\"rsin\":\"null\","
            + "\"sku_simple\":\"AS282EL01A3LALPE-4082101\",\"sku_config\":\"AS282EL01A3LALPE\","
            + "\"price\":6499.99,\"msrpPrice\":6499.99,\"special_price\":5199,\"product_id\":"
            + "\"3805120\",\"product_name\":\"ASUS 17.3\\\"  "
            + "I7 7700HQ 16GB 1TB 256GB SSD 4GB DDR5 DVD GL753VE-DS74 \",\"is_international\":0,"
            + "\"ean_code\":\"889349607626\",\"is_master\":0,\"brand\":\"Asus\","
            + "\"large_image\":\"\\/\\/i2.linio.com\\/p\\/e6d12a9e5e8a727b3c6517013ea0b108-product.jpg\","
            + "\"small_image\":\"\\/\\/i2.linio.com\\/p\\/5e5bfc05917af20048436c24887a4b13-product.jpg\","
            + "\"new_user\":2,\"pageType\":\"product\",\"ecommerce\":{\"detail\":"
            + "{\"products\":[{\"name\":\"ASUS 17.3\\\"  "
            + "I7 7700HQ 16GB 1TB 256GB SSD 4GB DDR5 DVD GL753VE-DS74 \",\"id\":"
            + "\"AS282EL01A3LALPE\",\"price\":6499.99,\"brand\":\"Asus\",\"category\":"
            + "\"Laptops\",\"variant\":\"default\"}]}},\"mail_hash\":\"n\\/a\",\"customer\":"
            + "{\"fastLaneEnabled\":false}}";

    Product product = scraper.jsonToObject(jsonData);

    String name = "ASUS 17.3\"  I7 7700HQ 16GB 1TB 256GB SSD 4GB DDR5 DVD GL753VE-DS74 ";
    double webPrice = 6499.99;
    double offerPrice = 5199.0;
    String sku = "AS282EL01A3LALPE";
    String brand = "Asus";

    assertTrue(product.getName().contentEquals(name));
    assertTrue(product.getWebPrice().equals(webPrice));
    assertTrue(product.getOfferPrice().equals(offerPrice));
    assertTrue(product.getSku().contentEquals(sku));
    assertTrue(product.getBrand().contentEquals(brand));
  }

  @Test
  public void getModel() throws Exception {
    LinioScraper scraper = new LinioScraper();

    File htmlFile = new File("src/test/resources/scraper/linioProductPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "https://www.linio.com.pe");

    String model = scraper.getModel(document);
    assertTrue(model.contentEquals("GL753VE-DS74"));
  }

  @Test
  public void getProducts() throws Exception {
  }

}