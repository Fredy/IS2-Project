package scraper;

import domain.Product;
import domain.Shop;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SagaScraper implements Scraper {

  private static String url = "http://www.falabella.com.pe/falabella-pe/category/cat1590466/Laptops";

  public static void main(String[] args) throws IOException {
    Vector<Product> parse = parse(url);
  }

  private static void print(String msg, Object... args) {
    System.out.println(String.format(msg, args));
  }

  private static String trim(String s, int width) {
    if (s.length() > width) {
      return s.substring(0, width - 1) + ".";
    } else {
      return s;
    }
  }


  public static Vector<Product> parse(String url) throws IOException {

    Vector<Product> productsUrls = new Vector<Product>();
    Product product = new Product();

    Document doc = Jsoup.connect(url)
        .get();

    String htmlString = doc.html();

    Document document = Jsoup.parse(htmlString);
    Elements elements = document
        .select(
            ".site-wrapper #main > div#fbra_browseProductList .fb-filters .fb-pod-group > div.fb-pod-group__item .fb-pod__item > .fb-pod__header > a[href]");

    for (Element element : elements) {
/*
      Element elementInt = document
          .select(
              ".site-wrapper #main > div#fbra_browseProductList .fb-filters .fb-pod-group > div.fb-pod-group__item .fb-pod__item  .fb-pod__prices-wrapper ")
          .get(1);


      System.out.println("elementInt  "+elementInt);*/

      String relUrl = "http://www.falabella.com.pe" + element.attr("href");

      /*Access the other page*/
       /*Begin*/

      Document docIn = Jsoup.connect(relUrl)
          .get();
      String htmlStringIn = docIn.html();

      Document documentIn = Jsoup.parse(htmlStringIn);

      Element elementIn = documentIn
          .select(
              ".site-wrapper #main > .fb-module-wrapper .fb-accordion-tabs section.fb-accordion-tabs__content .fb-product-information__product-information-tab .fb-product-information-tab__copy ul")
          .get(0);

       /*Brand*/

      String relUrlIn = elementIn.text();
      String stringBandP[] = relUrlIn.split("Marca:");
      String stringBand[] = stringBandP[1].split(" ");
      System.out.println("band  " + stringBand[1]);
      product.setBrand(stringBand[1]);

      /*Model*/

      String stringModelP[] = relUrlIn.split("Modelo:");
      String stringModel[] = stringModelP[1].split(" ");
      System.out.println("model  " + stringModel[1]);
      product.setBrand(stringModel[1]);

       /*Sku*/

      elementIn = documentIn
          .select(
              ".site-wrapper #main > #fbra_browseMainProduct .fb-product__form .fb-product-cta .fb-product-cta__container .fb-product-cta--desktop")
          .get(0);

      relUrlIn = elementIn.text();

      String stringArray[] = relUrlIn.split(":");
      String stringProd[] = stringArray[1].split(" ");
      System.out.println("sku " + stringProd[0]);
      product.setSku(stringProd[0]);

      /*Name*/

      String name = "";
      int j;
      for (j = 1; j < stringProd.length; j++) {
        name += stringProd[j] + " ";
      }
      System.out.println("name:  " + name);
      product.setName(name);


    /*
    //
    product.setNormalPrice();
    product.setOfferPrice();
    product.setWebPrice();*/

      System.out.println("                    ");

      Elements elementIns = documentIn
          .select(
              ".site-wrapper #main > #fbra_browseMainProduct div.fb-product__form div.fb-product-cta > div.fb-product-cta__container > div.fb-product-sets__product-prices  p");
      //elementIns.select("div.fb-product-cta__prices");

      //  System.out.println(elementIns.first().childNodes().get(1));

      for (Element e : elementIns) {

        Elements paragraphs = e.getElementsByTag("p");
        for (Element p : paragraphs) {

          relUrlIn = p.text();
          System.out.println("other Chaactest pem " + relUrlIn);
        }

        relUrlIn = e.text();
        System.out.println("other Chaactest pem " + relUrlIn);
      }

      System.out.println("                    ");

      /*End*/

      productsUrls.add(product);
    }
    return productsUrls;
  }

  public static int findSt(String st1, String st2) {
    int intIndex = st1.indexOf(st2);

    if (intIndex == -1) {
      System.out.println("not found");
      return -1;
    } else {
      System.out.println("Found at index " + intIndex);
      return intIndex;
    }
  }

  //Product model

  @Override
  public List<Product> parseProduct() {
    return null;
  }

  @Override
  public void saveProduct(List<Product> product) {

  }

  @Override
  public List<Product> getProduct() {
    return null;
  }

  @Override
  public Shop parseShop() {
    return null;
  }

  @Override
  public void saveShop(Shop shop) {
  }

  @Override
  public Shop getShop() {
    Shop shop = new Shop();
    shop.setName("Saga Falabella Perú");
    shop.setUrl("http://www.falabella.com.pe");
    shop.setAddress("Av. Paseo de la República 3220 - San Isidro - Lima");
    return shop;
  }
}
