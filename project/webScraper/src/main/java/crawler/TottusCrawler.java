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

  private String url = "http://www.tottus.com.pe/tottus/browse";

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  private List<Category> getListCategories() {
    List<Category> res = new ArrayList<Category>();
    try {
      Document doc = this.getHtmlFromURL(url);
      Elements npriceElements = doc.body()
          .getElementsByClass(
              "icon icon-display");//bg-green white: categories with subcategories//menu-header-link:subcategoriesAll//mp-back//mp-level

      System.out.println("SIZE: " + npriceElements.size());
      for (Element element : npriceElements) {

        String product = element.text();
        System.out.println("product:" + product);
        String relUrl = element.getElementsByTag("a")
            .attr("abs:href");
        System.out.println("rel: " + relUrl);
        Category tmp = new Category();
        tmp.setName(product);
        tmp.setUrl(relUrl);
        res.add(tmp);
      }


    } catch (IOException e) {
      e.printStackTrace();
    }
    return res;
  }


  @Override
  public List<Category> getCategories() {

    return getListCategories();
  }


  @Override
  public List<SubCategory> getSubCategories() {
    return null;
  }

  @Override
  public List<SubSubCategory> getSubSubCategories() {
    return null;
  }


}
