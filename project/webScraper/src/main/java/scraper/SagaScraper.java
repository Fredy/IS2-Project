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


public class SagaScraper implements Scraper {

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  private List<Product> parse(String url) throws IOException {

    List<Product> productsUrls = new ArrayList<>();
    Document document = getHtmlFromURL(url);
    Elements elements = document
        .select(
            ".site-wrapper #main > div#fbra_browseProductList .fb-filters .fb-pod-group > div.fb-pod-group__item .fb-pod__item > .fb-pod__header > a[href]");

    for (Element element : elements) {
      Product product = new Product();
      String relUrl = element.attr("abs:href");

      /*Access the other page*/
       /*Begin*/
      Document documentIn = getHtmlFromURL(relUrl);

      /*Price*/
      try {
        product.setNormalPrice(getPrice(documentIn));
      } catch (IOException e) {
        e.printStackTrace();
        product.setNormalPrice(null);
      }

      Element elementIn;
      try {

        elementIn = documentIn
            .select(
                ".site-wrapper #main > .fb-module-wrapper .fb-accordion-tabs section.fb-accordion-tabs__content .fb-product-information__product-information-tab .fb-product-information-tab__copy ul")
            .get(0);

      } catch (IndexOutOfBoundsException excepcion) {
        return null;
      }

       /*Brand*/
      String relUrlIn = elementIn.text();
      try {
        product.setBrand(getBrand(relUrlIn));
      } catch (IOException e) {
        e.printStackTrace();
        product.setBrand(null);
      }

      /*Model*/
      try {
        product.setModel(getModel(relUrlIn));
      } catch (IOException e) {
        e.printStackTrace();
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
      //System.out.println("sku " + stringProd[0]);
      product.setSku(stringProd[0]);

      /*Name*/
      String name = "";
      for (int j = 1; j < stringProd.length; j++) {
        name += stringProd[j] + " ";
      }
      //System.out.println("name:  " + name);
      product.setName(name);

      product.setWebPrice(null);
      product.setOfferPrice(null);

      /*End*/

      productsUrls.add(product);
    }
    return productsUrls;
  }

  private String getModel(String relUrlIn) throws IOException {
    try {
      String stringModelP[] = relUrlIn.split("Modelo:");
      String stringModel[] = stringModelP[1].split(" ");
      //System.out.println("model  " + stringModel[1]);
      return stringModel[1];
    } catch (ArrayIndexOutOfBoundsException excepcion) {
      return null;
    }

  }

  private String getBrand(String relUrlIn) throws IOException {
    try {
      String stringBandP[] = relUrlIn.split("Marca:");
      String stringBand[] = stringBandP[1].split(" ");
      //System.out.println("band  " + stringBand[1]);
      return stringBand[1];
    } catch (ArrayIndexOutOfBoundsException excepcion) {
      return null;
    }
  }

  private Double getPrice(Document docIn) throws IOException {
    Element content = docIn.body().getElementsByAttributeValue("type", "text/javascript").get(0);
    String price = content.data();
    String stringPrice[] = price.split("originalPrice");
    String stringPriceP[] = stringPrice[1].split("," + "\"");
    String stringP = stringPriceP[0];
    stringP = stringP.substring(3, stringP.length() - 1);
    stringP = stringP.replace(",", "");
    //System.out.println("price  " + stringP);
    return Double.parseDouble(stringP);
  }

  @Override
  public List<Product> parseProducts(SubSubCategory subSubCategory) {
    try {
      String url = subSubCategory.getUrl();
      return this.parse(url);
    } catch (IOException e) {
      e.printStackTrace();
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
