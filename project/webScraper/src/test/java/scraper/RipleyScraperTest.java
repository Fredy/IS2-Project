package scraper;

import static org.junit.Assert.assertEquals;

import domain.Product;
import domain.Shop;
import domain.SubSubCategory;
import java.util.ArrayList;
import org.junit.Test;

public class RipleyScraperTest {

  @Test
  public void getProductFromPage() throws Exception {
    RipleyScraper ripleyScraper = new RipleyScraper();
    ripleyScraper.setMainUrl("http://simple.ripley.com.pe/moda/moda-hombre/casacas");
    ArrayList<Product> products = ripleyScraper.getProductFromPage();
    assertEquals(products.size(), 1);
    Product product = products.get(0);
    assertEquals(product.getName(), "CLOUDBREAK CASACA HOODRAGLAN PARA HOMBRE");
    assertEquals(product.getNormalPrice(), 189, 0.0);
    assertEquals(product.getWebPrice(), 132.30, 0.0);
    assertEquals(product.getOfferPrice(), null);
    assertEquals(product.getSku(), "2016182582218");
    assertEquals(product.getBrand(), "Cloudbreak");
    assertEquals(product.getModel(), "Hoodraglan");

  }

  @Test
  public void getProductUtil() throws Exception {
    RipleyScraper ripleyScraper = new RipleyScraper();
    ArrayList<String> dataRaw = new ArrayList<>();
    dataRaw.add("CLOUDBREAK CASACA HOOLDRAGAN PARA HOMBRE");
    dataRaw.add("189");
    dataRaw.add("13230");
    dataRaw.add("");
    dataRaw.add("2016182582218");
    dataRaw.add("Cloudbreak");
    dataRaw.add("Hoodraglan");
    Product product = ripleyScraper.getProductUtil(dataRaw);
    assertEquals(product.getName(), "CLOUDBREAK CASACA HOOLDRAGLAN PARA HOMBRE");
    assertEquals(product.getNormalPrice(), 189, 0.0);
    assertEquals(product.getWebPrice(), 132.30, 0.0);
    assertEquals(product.getOfferPrice(), null);
    assertEquals(product.getSku(), "2016182582218");
    assertEquals(product.getBrand(), "Cloudbreak");
    assertEquals(product.getModel(), "Hoodraglan");
  }

  @Test
  public void processPage() throws Exception {
    RipleyScraper ripleyScraper = new RipleyScraper();
    ripleyScraper.setMainUrl("http://simple.ripley.com.pe/moda/moda-hombre/casacas");
    ArrayList<ArrayList<String>> dataRaw = ripleyScraper.processPage();
    assertEquals(dataRaw.size(), 1);
    assertEquals(dataRaw.get(0).get(0), "CLOUDBREAK CASACA HOODRAGLAN PARA HOMBRE");
    assertEquals(dataRaw.get(0).get(1), "189");
    assertEquals(dataRaw.get(0).get(2), "132.30");
    assertEquals(dataRaw.get(0).get(3), "");
    assertEquals(dataRaw.get(0).get(4), "2016182582218");
    assertEquals(dataRaw.get(0).get(5), "Cloudbreak");
    assertEquals(dataRaw.get(0).get(6), "Hoodraglan");

  }

  @Test
  public void getInformationOfOneProduct() throws Exception {
    RipleyScraper ripleyScraper = new RipleyScraper();
    ArrayList<String> infoProduct = ripleyScraper.getInformationOfOneProduct(
        "http://simple.ripley.com.pe/index-casaca-flecos-para-mujer-2015196542683");
    assertEquals(infoProduct.get(0), "INDEX CASACA FLECOS PARA MUJER");
    assertEquals(infoProduct.get(1), "169");
    assertEquals(infoProduct.get(2), "84.50");
    assertEquals(infoProduct.get(3), "");
    assertEquals(infoProduct.get(4), "2015196542683");

    assertEquals(infoProduct.get(5), "Index");
    assertEquals(infoProduct.get(6), "Flecos");

  }

  @Test
  public void getAllRealLinks() throws Exception {
    RipleyScraper ripleyScraper = new RipleyScraper();
    ArrayList<String> realLinks = ripleyScraper
        .getAllRealLinks("http://simple.ripley.com.pe/moda/moda-mujer/casacas-y-abrigos");
    assertEquals(realLinks.get(0),
        "http://simple.ripley.com.pe/index-casaca-deco-para-mujer-2015190299712");
    assertEquals(realLinks.get(1),
        "http://simple.ripley.com.pe/only-casaca-souvenir-para-mujer-2015187773553");
    assertEquals(realLinks.get(2),
        "http://simple.ripley.com.pe/index-casaca-duma-para-mujer-2015190292324");
    assertEquals(realLinks.get(3),
        "http://simple.ripley.com.pe/index-casaca-flores-para-mujer-2015192883391");
    assertEquals(realLinks.get(4),
        "http://simple.ripley.com.pe/index-casaca-flecos-para-mujer-2015196542683");
    assertEquals(realLinks.get(5),
        "http://simple.ripley.com.pe/index-casaca-klum-para-mujer-2015184168482");
    assertEquals(realLinks.get(6),
        "http://simple.ripley.com.pe/index-casaca-britos-para-mujer-2015193098206");
    assertEquals(realLinks.get(7),
        "http://simple.ripley.com.pe/index-casaca-street-para-mujer-2015193098152");
    assertEquals(realLinks.get(8),
        "http://simple.ripley.com.pe/index-casaca-oversized-para-mujer-2015193107199");
    assertEquals(realLinks.get(9),
        "http://simple.ripley.com.pe/index-casaca-david-para-mujer-2015188073102");
    assertEquals(realLinks.get(10),
        "http://simple.ripley.com.pe/index-casaca-dario-para-mujer-2015188073058");
    assertEquals(realLinks.get(11),
        "http://simple.ripley.com.pe/index-casaca-doran-para-mujer-2015189417752");
  }

  @Test
  public void getShopObject() throws Exception {
    RipleyScraper ripleyScraper = new RipleyScraper();
    Shop shop = ripleyScraper.getShopObject();
    assertEquals(shop.getName(), "Ripley Peru");
    assertEquals(shop.getUrl(), "http://simple.ripley.com.pe");
    assertEquals(shop.getAddress(), "Av. Porongoche 500, Arequipa 04001");
  }

  @Test
  public void parseProducts() throws Exception {
    SubSubCategory subSubCategory = new SubSubCategory();
    subSubCategory.setUrl("http://simple.ripley.com.pe/moda/moda-hombre/casacas");
    RipleyScraper ripleyScraper = new RipleyScraper();
    ripleyScraper.parseProducts(subSubCategory);
    Product product = subSubCategory.getProducts().get(0);
    assertEquals(product.getName(), "CLOUDBREAK CASACA HOODRAGLAN PARA HOMBRE");
    assertEquals(product.getNormalPrice(), 189, 0.0);
    assertEquals(product.getWebPrice(), 132.30, 0.0);
    assertEquals(product.getOfferPrice(), null);
    assertEquals(product.getSku(), "2016182582218");
    assertEquals(product.getBrand(), "Cloudbreak");
    assertEquals(product.getModel(), "Hoodraglan");
  }

  @Test
  public void parseShop() throws Exception {
    RipleyScraper ripleyScraper = new RipleyScraper();
    Shop shop = ripleyScraper.parseShop();
    assertEquals(shop.getName(), "Ripley Peru");
    assertEquals(shop.getUrl(), "http://simple.ripley.com.pe");
    assertEquals(shop.getAddress(), "Av. Porongoche 500, Arequipa 04001");
  }

}