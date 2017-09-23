package scraper;

import domain.Product;
import domain.Shop;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.json.*;

public class TottusScraper implements Scraper {

  private String baseURL = "http://www.tottus.com.pe/tottus/browse/Electrohogar-Tecnolog%C3%ADa-Laptops/_/N-82nnyu";

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  public String urlToJsonArray(String baseURL) {
    String result = "";
    try {
      Document doc = this.getHtmlFromURL(baseURL);

      Elements scriptElements = doc.getElementsByTag("script");

      for (Element element : scriptElements) {

        String jsonData = element.data();

        if (!jsonData.contains("dataLayer")) { //To discard other scripts
          jsonData = "";
        }

        if (!jsonData.contains("brand")) { // To discard empty functions
          jsonData = "";
        }
        jsonData = jsonData.split("]}}}'")[0];

        jsonData = jsonData.substring(jsonData.indexOf("[") + 1, jsonData.length());

        if (jsonData != "") {
          result += jsonData;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return result;
  }

  public Vector<String> oneToVector(String inputJson) {
    Vector<String> outputVector = new Vector<String>();
    while (inputJson.indexOf("},{") > 0) {
      outputVector.add(inputJson.substring(inputJson.indexOf("{"), inputJson.indexOf("},{") + 1));
      inputJson = inputJson.substring(inputJson.indexOf("},{") + 2, inputJson.length());
    }
    outputVector
        .add(inputJson.substring(inputJson.indexOf("{"), inputJson.length())); //last register
    return outputVector;
  }

  public List<Product> vectorStringsToProducts(Vector<String> vectorStringIn)
      throws JSONException {
    List<Product> res = new List<Product>() {
      @Override
      public int size() {
        return 0;
      }

      @Override
      public boolean isEmpty() {
        return false;
      }

      @Override
      public boolean contains(Object o) {
        return false;
      }

      @Override
      public Iterator<Product> iterator() {
        return null;
      }

      @Override
      public Object[] toArray() {
        return new Object[0];
      }

      @Override
      public <T> T[] toArray(T[] ts) {
        return null;
      }

      @Override
      public boolean add(Product product) {
        return false;
      }

      @Override
      public boolean remove(Object o) {
        return false;
      }

      @Override
      public boolean containsAll(Collection<?> collection) {
        return false;
      }

      @Override
      public boolean addAll(Collection<? extends Product> collection) {
        return false;
      }

      @Override
      public boolean addAll(int i, Collection<? extends Product> collection) {
        return false;
      }

      @Override
      public boolean removeAll(Collection<?> collection) {
        return false;
      }

      @Override
      public boolean retainAll(Collection<?> collection) {
        return false;
      }

      @Override
      public void clear() {

      }

      @Override
      public Product get(int i) {
        return null;
      }

      @Override
      public Product set(int i, Product product) {
        return null;
      }

      @Override
      public void add(int i, Product product) {

      }

      @Override
      public Product remove(int i) {
        return null;
      }

      @Override
      public int indexOf(Object o) {
        return 0;
      }

      @Override
      public int lastIndexOf(Object o) {
        return 0;
      }

      @Override
      public ListIterator<Product> listIterator() {
        return null;
      }

      @Override
      public ListIterator<Product> listIterator(int i) {
        return null;
      }

      @Override
      public List<Product> subList(int i, int i1) {
        return null;
      }
    } ;

    for (int i = 0; i < vectorStringIn.size(); i++) {
      JSONObject jsonObject = new JSONObject(vectorStringIn.elementAt(i));
      String name = jsonObject.getString("name");
      double webPrice = jsonObject.getDouble("price");
      double offerPrice = 0;// Not found
      String sku = jsonObject.getString("id");
      String model = "";// Not found
      String brand = jsonObject.getString("brand");
      /*System.out.print(name);
      System.out.print(" + ");
      System.out.print(webPrice);
      System.out.print(" + ");
      System.out.print(offerPrice);
      System.out.print(" + ");
      System.out.print(sku);
      System.out.print(" + ");
      System.out.print(model);
      System.out.print(" + ");
      System.out.print(brand);
      System.out.println();*/
      Product tmp = new Product();
      tmp.setName(name);
      tmp.setNormalPrice(webPrice);
      tmp.setSku(sku);
      tmp.setBrand(brand);
      res.add(tmp);
    }
    return res;
  }

  private Shop getShopData() {
    Shop shop = new Shop();
    shop.setName("Tottus Perú");
    shop.setUrl("http://www.tottus.com.pe/tottus/");
    shop.setAddress("Av. Parra 220 Arequipa, Arequipa, PE ");
    return shop;
  }

  @Override
  public List<Product> parseProducts() {
    String res1 = this.urlToJsonArray(this.baseURL);
    Vector<String> res2 = this.oneToVector(res1);
    return this.vectorStringsToProducts(res2);
  }

  @Override
  public Shop parseShop() {
    return this.getShopData();
  }

}
