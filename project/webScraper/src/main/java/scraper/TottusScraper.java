package scraper;

import domain.Product;
import domain.Shop;
import domain.SubSubCategory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TottusScraper implements Scraper {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  String urlToJsonArray(Document doc) {
    String result = "";
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

      if (!jsonData.contentEquals("")) {
        result = result.concat(jsonData);
      }
    }
    return result;
  }

  List<String> oneToVector(String inputJson) {
    ArrayList<String> outputVector = new ArrayList<>();
    while (inputJson.indexOf("},{") > 0) {
      outputVector.add(inputJson.substring(inputJson.indexOf("{"), inputJson.indexOf("},{") + 1));
      inputJson = inputJson.substring(inputJson.indexOf("},{") + 2, inputJson.length());
    }
    outputVector
        .add(inputJson.substring(inputJson.indexOf("{"), inputJson.length())); //last register
    return outputVector;
  }

  List<Product> vectorStringsToProducts(List<String> vectorStringIn, Document doc)
      throws JSONException {
    List<Product> res = new ArrayList<>();
    //List<List<String>> nulePrices = getPrices(doc);
    for (int i = 0; i < vectorStringIn.size(); i++) {
      JSONObject jsonObject = new JSONObject(vectorStringIn.get(i));
      String fullname = jsonObject.getString("name");
      logger.debug("FulName: " + fullname);

      String model = "";
      Boolean hasModel = false;

      if (fullname.contains("Mod.")) {
        hasModel = true;
        model = fullname.substring(fullname.indexOf("Mod."), fullname.length()); //quitar el Mod.
      }
      String sku = jsonObject.getString("id");
      String brand = jsonObject.getString("brand");
      Double price = jsonObject.getDouble("price");
        /*Double normalPrice = null;
        Double webPrice = null;
        Double offerPrice = null;

        if (nulePrices.get(i).size() == 3) {
          normalPrice = Double.parseDouble(nulePrices.get(i).get(1).replaceAll(",", ""));
          webPrice = Double.parseDouble(nulePrices.get(i).get(0).replaceAll(",", ""));
          offerPrice = Double.parseDouble(nulePrices.get(i).get(2).replaceAll(",", ""));
        }

        if (nulePrices.get(i).size() == 2) {
          normalPrice = Double.parseDouble(nulePrices.get(i).get(1).replaceAll(",", ""));
          webPrice = Double.parseDouble(nulePrices.get(i).get(0).replaceAll(",", ""));
        }
        if (nulePrices.get(i).size() == 1) {
          webPrice = Double.parseDouble(nulePrices.get(i).get(0).replaceAll(",", ""));
        }*/

      Product tmp = new Product();
      tmp.setName(fullname);
      logger.info("Scraping: " + fullname);
      tmp.setWebPrice(price);
        /*if (normalPrice != null) {
          tmp.setNormalPrice(normalPrice);
        }
        if (webPrice != null) {
          tmp.setWebPrice(webPrice);
        }
        if (offerPrice != null) {
          tmp.setOfferPrice(offerPrice);
        }*/

      tmp.setSku(sku);
      if (hasModel) {
        tmp.setModel(model);
      }
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

  List<List<String>> getPrices(Document productDoc) {

    List<List<String>> res = new ArrayList<>();

    if (productDoc == null) {
      return null;
    }

    Elements npriceElements = productDoc.body()
        .getElementsByClass("caption-bottom-wrapper");
    logger.debug("SIZE: " + npriceElements.size());
    for (Element element : npriceElements) {

      String product = element.text();
      Boolean hasCMRPrice = Boolean.FALSE;
      String cmrPrice = null;
      if (product.contains(" Exclusivo con CMR") && !product.contains("%")) {
        hasCMRPrice = Boolean.TRUE;
        cmrPrice = product.substring(product.indexOf("S/") + 3, product.indexOf("Ex"));
      }

      if (product.contains(" S/")) {
        String prices = null;
        String nulePrice = null;
        String activePrice = null;

        if (hasCMRPrice) {
          prices = product.substring(product.indexOf("condiciones S/") + 14, product.indexOf("UN"));
        } else {
          if (product.contains("KG")) { //products sold in KG
            prices = product.substring(product.indexOf("S/") + 3, product.indexOf("KG") - 5);
          } else {//products sold in UN
            prices = product.substring(product.indexOf("S/") + 3, product.indexOf("UN"));
          }

        }
        if (product.contains(" xclusivo con CMR")) { //ERROR Page
          prices = product.substring(product.indexOf("condiciones S/") + 14, product.indexOf("UN"));
        }

        if (prices.contains("S/")) {
          nulePrice = prices.substring(0, prices.indexOf("S/"));
          activePrice = prices.substring(prices.indexOf("S/") + 3, prices.length());
        } else {
          activePrice = prices;
        }
        ArrayList<String> pricesPerProduct = new ArrayList<>();

        pricesPerProduct.add(activePrice);
        if (nulePrice != null) {

          pricesPerProduct.add(nulePrice);
        }
        if (cmrPrice != null) {

          pricesPerProduct.add(cmrPrice);
        }
        res.add(pricesPerProduct);

      }
    }

    return res;
  }

  @Override
  public List<Product> parseProducts(SubSubCategory subSubCategory) {

    List<Product> resList = new ArrayList<>();
    String sscategoryUrl = subSubCategory.getUrl();
    try {
      Document doc = this.getHtmlFromURL(sscategoryUrl);
      String res1 = this.urlToJsonArray(doc);
      List<String> res2 = this.oneToVector(res1);
      resList = this.vectorStringsToProducts(res2, doc);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return resList;

  }

  @Override
  public Shop parseShop() {
    return this.getShopData();
  }

}
