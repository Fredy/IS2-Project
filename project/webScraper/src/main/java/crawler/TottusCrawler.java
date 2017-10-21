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
    this.categories = null;
  }

  private Document doc = null;
  private List<Category> categories;

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  protected List<Category> buildCategories() {
    List<Category> res = new ArrayList<>();
    try {
      doc = this.getHtmlFromURL(url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (doc != null) {
      Elements categoryElements = doc.body()
          .getElementsByClass("small-nav-menu");
      //System.out.println("SIZE Categories: " + categoryElements.size());
      for (Element element : categoryElements) {

        Elements categoryName = element.getElementsByTag("p");
        String product = categoryName.text();
        String relUrl = element.getElementsByTag("a")
            .attr("abs:href");
        Category catTmp = new Category();
        catTmp.setName(product);
        //System.out.println("CATEGORY:[" + product + "]");
        catTmp.setUrl(relUrl);
        List<SubCategory> listSubTmp = new ArrayList<>();
        Elements subCategoryName = element.getElementsByClass("col-md-2-4");
        for (Element el : subCategoryName) {//subcategories
          String nameSub = el.getElementsByTag("h4").text();
          String urlSub = el.getElementsByTag("a")
              .attr("abs:href");
          //System.out.println("CSub:[" + nameSub + "]={" + urlSub + "}");
          SubCategory subTmp = new SubCategory();
          subTmp.setName(nameSub.toLowerCase());
          subTmp.setUrl(urlSub);
          int cont = 0;
          List<SubSubCategory> listSubSubtmp = new ArrayList<>();
          Elements aaa = el.getElementsByTag("li");
          for (Element ela : aaa) { // subsubcategories
            String nameSubSub = ela.text();
            String urlSubSub = ela.getElementsByTag("a").attr("abs:href");
            //System.out.println("SUBSUB_{" + nameSubSub + "}=[" + urlSubSub);
            if (cont > 1) {
              SubSubCategory subSubTmp = new SubSubCategory();
              subSubTmp.setName(nameSubSub.toLowerCase());
              subSubTmp.setUrl(urlSubSub);
              listSubSubtmp.add(subSubTmp);
            }
            cont++;
          }
          subTmp.setSubSubCategories(listSubSubtmp);
          listSubTmp.add(subTmp);
        }
        catTmp.setSubCategories(listSubTmp);
        res.add(catTmp);
      }
    }

    return res;
  }

  @Override
  public List<Category> getCategories() {
    this.categories = buildCategories();
    return this.categories;
  }
}
