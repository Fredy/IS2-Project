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

public class RipleyScraper implements Scraper {

  private String MainUrl;
  private String BaseUrl;

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
    ArrayList<String> links = getAllRealLinks(MainUrl);
    ArrayList<ArrayList<String>> dataOfPage = new ArrayList<>();
    for (String link : links) {
      dataOfPage.add(getInformationOfOneProduct(link));
    }
    return dataOfPage;
  }

  private ArrayList<String> getInformationOfOneProduct(String URL) throws IOException {
    Document page = getHtmlFromURL(URL);
    ArrayList<String> dataCollected = new ArrayList<>(); // Structure is name, price normal, price web, offerprice, SKU, Brand, Model;
    Elements productheader = page.select("[class^=product-header hidden-xs]");
    Elements productPrices = page.select("[class^=product-info]");
    Elements productDetails = page.select("[class^=table table-striped]").select("tbody")
        .select("[data-reactid]");
    String Name, Sku, NormalPrice, WebPrice, OfferPrice = "", Brand, Model;
    Name = productheader.select("[itemprop=name]").text();
    Sku = productheader.select("[class=sku]").text();

    NormalPrice = productPrices.select("[class=product-normal-price]")
        .select("[class=product-price]").text();
    NormalPrice = formatStringToDouble(NormalPrice);

    WebPrice = productPrices.select("[class=product-internet-price]")
        .select("[class=product-price]").text();
    WebPrice = formatStringToDouble(WebPrice);

    int i = 0;
    String before = "";
    Brand = "";
    Model = "";
    for (Element detail : productDetails) {
      if (i >= 2) {
        break;
      }
      String curr = detail.ownText();
      if (before.equals("Marca")) {
        Brand = curr;
        ++i;
      } else if (before.equals("Modelo")) {
        ++i;
        Model = curr;
      }
      before = curr;
    }
    dataCollected.add(Name);
    dataCollected.add(NormalPrice);
    dataCollected.add(WebPrice);
    dataCollected.add(OfferPrice);
    dataCollected.add(Sku);
    dataCollected.add(Brand);
    dataCollected.add(Model);
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

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla/5.0").get();
  }

  private ArrayList<String> getAllRealLinks(String Url) throws IOException {
    Document page;
    ArrayList<String> links = new ArrayList<>();
    for (int i = 1; i < 100; i++) {
      try {
        page = getHtmlFromURL(Url + "?page=" + String.valueOf(i));
        links.addAll(getAllLinks(page));
      } catch (Exception e) {
        break;
      }
    }
    return links;
  }

  private ArrayList<String> getAllLinks(Document page) {
    ArrayList<String> ans = new ArrayList<>();
    Elements Container = page.select("[class^=catalog-container]")
        .select("[class^=catalog-product catalog-item]");
    for (Element element : Container) {
      ans.add(BaseUrl + element.attr("href"));
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
    MainUrl = subSubCategory.getUrl();
    BaseUrl = "http://simple.ripley.com.pe";
    ArrayList<Product> products = new ArrayList<>();
    try {
      products = this.getProductFromPage();
    } catch (IOException e) {
      e.printStackTrace();
    }
    subSubCategory.setProducts(products);
    return products;
  }

  @Override
  public Shop parseShop() {
    try {
      return this.getShopObject();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
