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

public class RipleyScraper implements Scraper {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private String mainUrl;

  private ArrayList<Product> getProductFromPage() throws IOException {
    ArrayList<ArrayList<String>> dataRaw = processPage();
    ArrayList<Product> products = new ArrayList<>();
    for (ArrayList<String> raw : dataRaw) {
      products.add(getProductUtil(raw));
    }
    return products;
  }

  private Product getProductUtil(ArrayList<String> raw) {
    Product product = new Product();
    if (!raw.get(0).isEmpty()) {
      product.setName(raw.get(0));
    }
    if (!raw.get(1).isEmpty()) {
      product.setNormalPrice(Double.parseDouble(raw.get(1)));
    }
    if (!raw.get(2).isEmpty()) {
      product.setWebPrice(Double.parseDouble(raw.get(2)));
    }
    if (!raw.get(3).isEmpty()) {
      product.setOfferPrice(Double.parseDouble(raw.get(3)));
    }
    if (!raw.get(4).isEmpty()) {
      product.setSku(raw.get(4));
    }
    if (!raw.get(5).isEmpty()) {
      product.setBrand(raw.get(5));
    }
    if (!raw.get(6).isEmpty()) {
      product.setModel(raw.get(6));
    }
    return product;
  }

  private ArrayList<ArrayList<String>> processPage() throws IOException {
    ArrayList<String> links = getAllRealLinks(mainUrl);
    ArrayList<ArrayList<String>> dataOfPage = new ArrayList<>();
    for (String link : links) {
      dataOfPage.add(getInformationOfOneProduct(link));
    }
    return dataOfPage;
  }

  private ArrayList<String> getInformationOfOneProduct(String url) throws IOException {
    Document page = getHtmlFromURL(url);
    ArrayList<String> dataCollected = new ArrayList<>(); // Structure is name, price normal, price web, offerprice, SKU, Brand, Model;
    Elements productheader = page.select("[class^=product-header hidden-xs]");
    Elements productPrices = page.select("[class^=product-info]");
    Elements productDetails = page.select("[class^=table table-striped]").select("tbody")
        .select("[data-reactid]");
    String name, sku, normalPrice, webPrice, offerPrice = "", brand, model;
    name = productheader.select("[itemprop=name]").text();
    sku = productheader.select("[class=sku]").text();

    normalPrice = productPrices.select("[class=product-normal-price]")
        .select("[class=product-price]").text();
    normalPrice = formatStringToDouble(normalPrice);

    webPrice = productPrices.select("[class=product-internet-price]")
        .select("[class=product-price]").text();
    webPrice = formatStringToDouble(webPrice);

    int i = 0;
    String before = "";
    brand = "";
    model = "";
    for (Element detail : productDetails) {
      if (i >= 2) {
        break;
      }
      String curr = detail.ownText();
      if (before.equals("Marca")) {
        brand = curr;
        ++i;
      } else if (before.equals("Modelo")) {
        ++i;
        model = curr;
      }
      before = curr;
    }
    dataCollected.add(name);
    dataCollected.add(normalPrice);
    dataCollected.add(webPrice);
    dataCollected.add(offerPrice);
    dataCollected.add(sku);
    dataCollected.add(brand);
    dataCollected.add(model);
    return dataCollected;
  }

  private String formatStringToDouble(String raw) {
    StringBuilder ans = new StringBuilder();
    for (int i = 0; i < raw.length(); i++) {
      if (digit(raw.charAt(i))) {
        ans.append(raw.charAt(i));
      }
    }
    return ans.toString();
  }

  private Boolean digit(Character character) {
    return character == '0' || character == '1' || character == '2' || character == '3'
        || character == '4' || character == '5' || character == '6' || character == '7'
        || character == '8' || character == '9';
  }

  private Document getHtmlFromURL(String pageURL) throws IOException {
    return Jsoup.connect(pageURL).userAgent("Mozilla/5.0").get();
  }

  private ArrayList<String> getAllRealLinks(String url) throws IOException {
    Document page;
    ArrayList<String> links = new ArrayList<>();
    for (int i = 1; i < 100; i++) {
      try {
        page = getHtmlFromURL(url + "?page=" + String.valueOf(i));
        links.addAll(getAllLinks(page));
      } catch (Exception e) {
        //I use the try catch tool for another goals
        break;
      }
    }
    return links;
  }

  private ArrayList<String> getAllLinks(Document page) {
    ArrayList<String> ans = new ArrayList<>();
    Elements container = page.select("[class^=catalog-container]")
        .select("[class^=catalog-product catalog-item]");
    for (Element element : container) {
      ans.add(element.attr("abs:href"));
    }
    return ans;
  }

  private Shop getShopObject() throws IOException {
    Shop shop = new Shop();
    shop.setName("Ripley Peru");
    shop.setUrl("http://simple.ripley.com.pe");
    shop.setAddress("Av. Porongoche 500, Arequipa 04001");
    return shop;
  }

  @Override
  public List<Product> parseProducts(SubSubCategory subSubCategory) {
    mainUrl = subSubCategory.getUrl();
    ArrayList<Product> products = new ArrayList<>();
    try {
      products = this.getProductFromPage();
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    logger.debug("Productos scrapeados: " + Integer.toString(products.size()));
    subSubCategory.setProducts(products);
    return products;
  }

  @Override
  public Shop parseShop() {
    try {
      return this.getShopObject();
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return null;
  }

}
