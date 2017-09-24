package scraper;

import domain.Product;
import domain.Shop;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class RipleyScraper implements Scraper {

  private ArrayList<Product> getProductFromPage() throws IOException {
    ArrayList<ArrayList<String>> dataRaw = processPage();
    ArrayList<Product> products = new ArrayList<>();
    for (ArrayList<String> raw : dataRaw) {
      products.add(getProductU(raw));
    }
    return products;
  }

  private Product getProductU(ArrayList<String> raw) {
    Product product = new Product();
    if (raw.get(0).compareTo("N/A") != 0) {
      product.setName(raw.get(0));
    }
    if (raw.get(1).compareTo("-1") != 0) {
      product.setNormalPrice(Double.parseDouble(raw.get(1)));
    }
    if (raw.get(2).compareTo("-1") != 0) {
      product.setWebPrice(Double.parseDouble(raw.get(2)));
    }
    if (raw.get(3).compareTo("-1") != 0) {
      product.setOfferPrice(Double.parseDouble(raw.get(3)));
    }
    if (raw.get(4).compareTo("N/A") != 0) {
      product.setSku(raw.get(4));
    }
    if (raw.get(5).compareTo("N/A") != 0) {
      product.setBrand(raw.get(5));
    }
    if (raw.get(6).compareTo("N/A") != 0) {
      product.setModel(raw.get(6));
    }
    return product;
  }

  private ArrayList<ArrayList<String>> processPage() throws IOException {
    String URL = "http://simple.ripley.com.pe/computo/laptops/todas-las-laptops";
    String URLTitle = "http://simple.ripley.com.pe";
    ArrayList<String> links = getAllRealLinks(URL, URLTitle);
    ArrayList<ArrayList<String>> dataOfPage = new ArrayList<>();
    for (String link : links) {
      dataOfPage.add(getInformationOfOneProduct(link));
    }
    return dataOfPage;
  }

  private ArrayList<String> getInformationOfOneProduct(String URL) throws IOException {
    Document page = getHtmlFromURL(URL);
    ArrayList<String> dataCollected = new ArrayList<>(); // Structure is name, price normal, price web, offerprice, SKU, Brand, Model;
    Elements names = page.select("[itemprop=name]");
    Elements normalPrice = page.getElementsByClass("product-normal-price")
        .select("[class=product-price]");
    Elements internetPrice = page.getElementsByClass("product-internet-price")
        .select("[class=product-price]");
    if (internetPrice.isEmpty()) {
      internetPrice = page.getElementsByClass("product-internet-price-not-best")
          .select("[class=product-price]");
    }
    Elements sku = page.getElementsByClass("sku");
    if (names.isEmpty()) {
      dataCollected.add("N/A");
    } else {
      dataCollected.add(names.first().text());
    }

    if (normalPrice.isEmpty()) {
      dataCollected.add("-1");
    } else {
      dataCollected.add(formatStringToDouble(normalPrice.first().text()));
    }

    if (internetPrice.isEmpty()) {
      dataCollected.add("-1");
    } else {
      dataCollected.add(formatStringToDouble(internetPrice.first().text()));
    }

    dataCollected.add("-1");

    if (sku.isEmpty()) {
      dataCollected.add("N/A");
    } else {
      dataCollected.add(sku.first().text());
    }

    Elements anotherData;
    Elements nextData;
    int found = 0;
    for (int i = 50; i < 300; i++) {
      if (found >= 2) {
        break;
      }
      anotherData = page.select("[data-reactid=" + String.valueOf(i) + "]");
      if (!anotherData.isEmpty()) {
        if (anotherData.first().text().compareTo("Marca") == 0
            || anotherData.first().text().compareTo("Modelo") == 0) {
          found++;
          nextData = page.select("[data-reactid=" + String.valueOf(i + 1) + "]");
          dataCollected.add(nextData.first().text());
        }
      }
    }
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

  private ArrayList<String> getAllRealLinks(String URL, String URLTitle) throws IOException {
    Document page;
    ArrayList<String> links = new ArrayList<>();
    for (int i = 1; i < 6; i++) {
      page = getHtmlFromURL(URL + "?page=" + String.valueOf(i));
      links.addAll(getAllLinks(page, URLTitle));
    }
    return links;
  }

  private ArrayList<String> getAllLinks(Document page, String URL) {
    ArrayList<String> ans = new ArrayList<>();
    Elements links = page.select("a[href]");
    String strlink;
    for (Element link : links) {
      strlink = link.attr("href");
      if (strlink.contains("-laptop-") || strlink.contains("-notebook-")) {
        ans.add(URL + strlink);
      }
    }
    return ans;
  }

  private Shop getShopObject() throws IOException {
    Shop shop = new Shop();
    shop.setName("Ripley Peru");
    shop.setUrl("http://simple.ripley.com.pe/computo/laptops/todas-las-laptops");
    shop.setAddress("Av. Porongoche 500, Arequipa 04001");
    return shop;
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

  @Override
  public List<Product> parseProducts() {
    try {
      return this.getProductFromPage();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

}
