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

  public ArrayList<Product> getProductFromPage() throws IOException {
    ArrayList<ArrayList<String> >  dataRaw = processPage();
    Product newProduct = new Product();
    ArrayList<Product> products = new ArrayList<>();
    for (ArrayList<String> raw: dataRaw) {
      products.add(getProductU(raw));
    }
    return products;
  }

  private Product getProductU(ArrayList<String> raw){
    Product product = new Product();
    product.setName(raw.get(0));
    product.setNormalPrice(Double.parseDouble(raw.get(1)));
    product.setWebPrice(Double.parseDouble(raw.get(2)));
    product.setOfferPrice(Double.parseDouble(raw.get(3)));
    product.setSku(raw.get(4));
    product.setBrand(raw.get(5));
    product.setBrand(raw.get(6));
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
    for (ArrayList<String> page : dataOfPage) {
      for (String info : page) {
        System.out.print(info + " ");
      }
      System.out.println("");
    }
    return dataOfPage;
  }

  private ArrayList<String> getInformationOfOneProduct(String URL) throws IOException {
    System.out.println("Parsing : " + URL);
    Document page = getHtmlFromURL(URL);
    ArrayList<String> dataCollected = new ArrayList<>(); // Structure is name, price normal, price web, offerprice, SKU, Brand, Model;
    Elements names = page.select("[itemprop=name]");
    Elements normalPrice = page.select("[itemprop=price]");
    Elements internetPrice = page.select("[itemprop=lowPrice]");
    if (internetPrice.isEmpty() && !normalPrice.isEmpty()) {
      internetPrice = normalPrice;
      normalPrice.empty();
    }
    Elements sku = page.select("[itemprop=sku]");
    if (names.isEmpty()) {
      dataCollected.add("N/A");
    } else {
      dataCollected.add(names.first().text());
    }

    if (normalPrice.isEmpty()) {
      dataCollected.add("N/A");
    } else {
      dataCollected.add(normalPrice.first().text());
    }

    if (internetPrice.isEmpty()) {
      dataCollected.add("N/A");
    } else {
      dataCollected.add(internetPrice.first().text());
    }

    dataCollected.add("N/A");

    if (sku.isEmpty()) {
      dataCollected.add("N/A");
    } else {
      dataCollected.add(sku.first().text());
    }

    //Hardest part because of datarect-id :(
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

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla/5.0").get();
  }

  private ArrayList<String> getAllRealLinks(String URL, String URLTitle) throws IOException {
    Document page;
    ArrayList<String> links = new ArrayList<String>();
    for (int i = 1; i < 6; i++) {
      page = getHtmlFromURL(URL + "?page=" + String.valueOf(i));
      links.addAll(getAllLinks(page, URLTitle));
    }
    return links;
  }

  private ArrayList<String> getAllLinks(Document page, String URL) {
    ArrayList<String> ans = new ArrayList<String>();
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
    return null;
  }
}
