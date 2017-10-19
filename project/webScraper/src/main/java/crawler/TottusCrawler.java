package crawler;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TottusCrawler extends Crawler {

  public TottusCrawler() {
    this.url = "http://www.tottus.com.pe/tottus/";
  }

  private Document doc = null;

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  private List<Category> getListCategories() {
    List<Category> res = new ArrayList<Category>();
    if (doc != null) {
      Elements npriceElements = doc.body()
          .getElementsByClass(
              "icon icon-display");

      System.out.println("SIZE Categories: " + npriceElements.size());
      for (Element element : npriceElements) {

        String product = element.text();
        String relUrl = element.getElementsByTag("a")
            .attr("abs:href");
        Category tmp = new Category();
        tmp.setName(product);
        tmp.setUrl(relUrl);
        res.add(tmp);
      }
    }

    return res;
  }

  private List<SubCategory> getListSubCategories() {
    List<SubCategory> res = new ArrayList<SubCategory>();
    if (doc != null) {
      Elements npriceElements = doc.body()
          .getElementsByClass(
              "menu-header-link");

      System.out.println("SIZE subCategories: " + npriceElements.size());
      for (Element element : npriceElements) {

        String product = element.text();
        String relUrl = element.getElementsByTag("a")
            .attr("abs:href");
        if (relUrl.contains("browse/")) {
          SubCategory tmp = new SubCategory();
          tmp.setName(product);
          tmp.setUrl(relUrl);
          res.add(tmp);
        }
      }
    }

    return res;
  }

  @Override
  public List<Category> getCategories() {
    try {
      doc = this.getHtmlFromURL(url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return getListCategories();
  }


  @Override
  public List<SubCategory> getSubCategories() {
    return getListSubCategories();
  }

  @Override
  public List<SubSubCategory> getSubSubCategories() {
    return null;
  }


}
