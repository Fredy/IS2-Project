package scraper;

import domain.Product;
import domain.Shop;
import domain.SubSubCategory;
import domain.features.LaptopFeature;
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

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  Product getAttr(Document documentIn) throws IOException {

    Product product = new Product();

    /*Price*/
    product.setNormalPrice(getPrice(documentIn));

    Element elementIn;
    try {
      elementIn = documentIn
          .select(
              ".site-wrapper #main > .fb-module-wrapper .fb-accordion-tabs section.fb-accordion-tabs__content .fb-product-information__product-information-tab .fb-product-information-tab__copy ul")
          .get(0);

      String relUrlIn = elementIn.text();
      /*Extra Features*/
      //extraFeaturesLaptop(relUrlIn);

      /*Brand*/
      product.setBrand(getBrand(relUrlIn));

      /*Model*/
      product.setModel(getModel(relUrlIn));

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
      StringBuilder name = new StringBuilder();
      for (int j = 1; j < stringProd.length; j++) {
        name.append(stringProd[j]).append(" ");
      }

      logger.debug("NAME{" + name + "}");
      product.setName(name.toString());
    } catch (IndexOutOfBoundsException e) {
      product.setModel(null);
      product.setName(null);
      logger.error(e.getMessage(), e);
    }
    product.setWebPrice(null);
    product.setOfferPrice(null);

      /*End*/
    return product;
  }

  public LaptopFeature extraFeaturesLaptop(String relUrlIn) throws IOException {
    LaptopFeature extraFeatures = new LaptopFeature();

      /*Color*/
    String color = getColor(relUrlIn);
    extraFeatures.setColor(color);

      /*String memoryRAM = getRAM(relUrlIn);
      extraFeatures.setStorageCapacity(memoryRAM);
      */

      /*Storage Capacity*/
    Long stCapacity = getStorageCap(relUrlIn);
    extraFeatures.setStorageCapacity(stCapacity);

      /*Display Capacity*/
    Float displayCap = getDisplaySize(relUrlIn);
    extraFeatures.setDisplaySize(displayCap);

      /*CPU Model*/
    String cpuModel = getCPUModel(relUrlIn);
    extraFeatures.setCpuModel(cpuModel);
    return extraFeatures;
  }

  public List<Product> parse(Document document) throws IOException {

    List<String> productsUrls = getUrl(document);
    List<Product> products = new ArrayList<>();
    for (String string : productsUrls) {
      Product product;

      /*Access the other page*/
       /*Begin*/
      Document documentIn = getHtmlFromURL(string);
      product = getAttr(documentIn);
      products.add(product);
    }
    return products;
  }

  public List<String> getUrl(Document document) {
    List<String> productsUrls = new ArrayList<>();

    Elements elements = document
        .select(
            ".site-wrapper #main > div#fbra_browseProductList .fb-filters .fb-pod-group > div.fb-pod-group__item .fb-pod__item > .fb-pod__header > a[href]");

    for (Element element : elements) {
      String relUrl = element.attr("abs:href");
      productsUrls.add(relUrl);
    }
    return productsUrls;
  }

  String getModel(String relUrlIn) throws IOException {
    try {
      String stringModelP[] = relUrlIn.split("Modelo:");
      String stringModel[] = stringModelP[1].split(" ");
      logger.debug("MODEL{" + stringModel[1] + "}");

      return stringModel[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
  }

  String getBrand(String relUrlIn) throws IOException {
    try {

      String stringBandP[] = relUrlIn.split("Marca:");
      String stringBand[] = stringBandP[1].split(" ");
      logger.debug("BRAND{" + stringBand[1] + "}");

      return stringBand[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
  }

  ////////////////Extra features//////////////////

  String getColor(String relUrlIn) throws IOException {
    try {

      String stringBandP[] = relUrlIn.split("Color:");
      String stringBand[] = stringBandP[1].split(" ");
      logger.debug("COLOR{" + stringBand[1] + "}");

      return stringBand[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
  }


  String getRAM(String relUrlIn) throws IOException {
    try {

      String stringBandP[] = relUrlIn.split("Memoria RAM:");
      String stringBand[] = stringBandP[1].split(" ");
      logger.debug("Memoria RAM:{" + stringBand[1] + "}");

      return stringBand[1];
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
  }


  String getCPUModel(String relUrlIn) throws IOException {
    try {

      String stringBandP[] = relUrlIn.split("Procesador:");
      String stringBand[] = stringBandP[1].split(" ");
      logger
          .debug("Procesador:{" + stringBand[1] + " " + stringBand[2] + " " + stringBand[3] + "}");

      return stringBand[1] + " " + stringBand[2] + " " + stringBand[3];
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
  }

  Long getStorageCap(String relUrlIn) throws IOException {
    try {

      String stringBandP[] = relUrlIn.split("Disco duro:");
      String stringBand[] = stringBandP[1].split(" ");

      int intIndex = stringBand[1].indexOf("TB");
      if (intIndex < 0) {
        stringBand[1] = stringBand[1].replace("GB", "");
      } else {
        stringBand[1] = stringBand[1].replace("TB", "000");
      }

      if (stringBand[2].contains("TB")) {
        stringBand[1] += "000";
      }

      logger.debug("Disco duro:{" + stringBand[1] + "}");

      return Long.parseLong(stringBand[1]);
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
  }

  Float getDisplaySize(String relUrlIn) throws IOException {
    try {

      String stringBandP[] = relUrlIn.split("Tamaño de pantalla:");
      String stringBand[] = stringBandP[1].split(" ");
      String s = stringBand[1];
      s = s.replace("pulgadas", "");
      s = s.replace(",", ".");
      logger.debug("Tamaño de pantalla:{" + s + "}");

      return Float.parseFloat(s);
    } catch (ArrayIndexOutOfBoundsException e) {
      logger.error(e.getMessage(), e);
      return null;
    }
  }

  /////////////////////////////////////

  Double getPrice(Document docIn) throws IOException {
    String stringPriceP[];
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