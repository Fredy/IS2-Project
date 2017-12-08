package scraper;

import domain.Product;
import domain.Shop;
import domain.SubSubCategory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SagaScraper implements Scraper {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  public Product getAttr(Document documentIn) throws IOException {
    Product product = new Product();

      /*Price*/
    try {
      product.setNormalPrice(getPrice(documentIn));
    } catch (NullPointerException e) {
      logger.error(e.getMessage(), e);
      product.setNormalPrice(null);
    }

    Element elementIn = null;
    try {
      elementIn = documentIn
          .select(
              ".site-wrapper #main > .fb-module-wrapper .fb-accordion-tabs section.fb-accordion-tabs__content .fb-product-information__product-information-tab .fb-product-information-tab__copy ul")
          .get(0);

       /*Brand*/
      String relUrlIn = null;
      try {
        relUrlIn = elementIn.text();
        product.setBrand(getBrand(relUrlIn));
      } catch (NullPointerException e) {
        logger.error(e.getMessage(), e);
        product.setBrand(null);
      }

      /*Model*/
      try {
        product.setModel(getModel(relUrlIn));
      } catch (IOException e) {
        logger.error(e.getMessage(), e);
        product.setModel(null);
      }

       /*Sku*/
      elementIn = documentIn
          .select(
              ".site-wrapper #main > #fbra_browseMainProduct .fb-product__form .fb-product-cta .fb-product-cta__container .fb-product-cta--desktop")
          .get(0);

      relUrlIn = elementIn.text();
      String stringArray[] = relUrlIn.split(":");
      String stringProd[] = stringArray[1].split(" ");
      logger.debug("SKU{" + stringProd[0] + "}");

      product.setSku(stringProd[0]);

      /*Name*/
      String name = "";
      for (int j = 1; j < stringProd.length; j++) {
        name += stringProd[j] + " ";
      }

      logger.debug("NAME{" + name + "}");

      product.setName(name);
    } catch (IndexOutOfBoundsException excepcion) {
      product.setModel(null);
      product.setName(null);
    }
    product.setWebPrice(null);
    product.setOfferPrice(null);

      /*End*/
    return product;
  }

  public List<Product> parse(Document document) throws IOException {

    List<Product> productsUrls = new ArrayList<>();

    Elements elements = document
        .select(
            ".site-wrapper #main > div#fbra_browseProductList .fb-filters .fb-pod-group > div.fb-pod-group__item .fb-pod__item > .fb-pod__header > a[href]");

    for (Element element : elements) {
      Product product;
      String relUrl = element.attr("abs:href");

      /*Access the other page*/
       /*Begin*/
      Document documentIn = getHtmlFromURL(relUrl);
      product = getAttr(documentIn);
      productsUrls.add(product);
    }
    return productsUrls;
  }

  public String getModel(String relUrlIn) throws IOException {
    try {
      String stringModelP[] = relUrlIn.split("Modelo:");
      String stringModel[] = stringModelP[1].split(" ");
      logger.debug("MODEL{" + stringModel[1] + "}");

      return stringModel[1];
    } catch (ArrayIndexOutOfBoundsException excepcion) {
      return null;
    }

  }

  public String getBrand(String relUrlIn) throws IOException {
    try {

      String stringBandP[] = relUrlIn.split("Marca:");
      String stringBand[] = stringBandP[1].split(" ");
      logger.debug("BRAND{" + stringBand[1] + "}");

      return stringBand[1];
    } catch (ArrayIndexOutOfBoundsException excepcion) {
      return null;
    }
  }

  public Double getPrice(Document docIn) throws IOException {
    String stringPriceP[] = new String[0];
    String stringP;
    try {

      Element content = docIn.body().getElementsByAttributeValue("type", "text/javascript")
          .get(0);
      String price = content.data();
      String stringPrice[] = price.split("originalPrice");
      stringPriceP = stringPrice[1].split("," + "\"");

      stringP = stringPriceP[0];
      stringP = stringP.substring(3, stringP.length() - 1);
      stringP = stringP.replace(",", "");
      logger.debug("PRICE{" + stringP + "}");
      return Double.parseDouble(stringP);

    } catch (ArrayIndexOutOfBoundsException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
  }

  @Override
  public List<Product> parseProducts(SubSubCategory subSubCategory) {
    try {
      String url = subSubCategory.getUrl();
      Document document = getHtmlFromURL(url);
      return this.parse(document);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);

    }
    return null;
  }

  @Override
  public Shop parseShop() {
    return this.getShop();
  }

  private Shop getShop() {
    Shop shop = new Shop();
    shop.setName("Saga Falabella Perú");
    shop.setUrl("http://www.falabella.com.pe");
    shop.setAddress("Av. Paseo de la República 3220 - San Isidro - Lima");
    return shop;
  }
}