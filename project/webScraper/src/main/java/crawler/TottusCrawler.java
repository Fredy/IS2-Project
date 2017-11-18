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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TottusCrawler extends Crawler {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private List<Category> categories;

  public TottusCrawler() {
    this.url = "http://www.tottus.com.pe/tottus/";
  }

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  protected List<Category> buildCategories() {
    Document doc;
    try {
      doc = this.getHtmlFromURL(url);
      this.categories = crawlingCategory(doc);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    return this.categories;
  }

  List<Category> crawlingCategory(Document doc) {
    List<Category> listCatTmp = new ArrayList<>();
    Elements categoryElements = doc.body()
        .getElementsByClass("small-nav-menu");
    logger.info("SIZE Categories: " + categoryElements.size());
    for (Element element : categoryElements) {

      Elements categoryName = element.getElementsByTag("p");
      String product = categoryName.text();
      String relUrl = element.getElementsByTag("a")
          .attr("abs:href");
      Category catTmp = new Category();
      catTmp.setName(product);
      logger.debug("CATEGORY:[" + product + "]");

      catTmp.setUrl(relUrl);
      catTmp.setSubCategories(crawlingSubCategory(element));
      listCatTmp.add(catTmp);
    }
    return listCatTmp;
  }

  List<SubCategory> crawlingSubCategory(Element category) {
    List<SubCategory> listSubTmp = new ArrayList<>();
    Elements subCategoryName = category.getElementsByClass("col-md-2-4");
    logger.debug("SIZE subCategories: " + subCategoryName.size());
    for (Element el : subCategoryName) {
      String nameSub = el.getElementsByTag("h4").text();
      String urlSub = el.getElementsByTag("a")
          .attr("abs:href");
      logger.debug("CSub:[" + nameSub + "]={" + urlSub + "}");

      SubCategory subTmp = new SubCategory();
      subTmp.setName(nameSub.toLowerCase());
      subTmp.setUrl(urlSub);
      subTmp.setSubSubCategories(crawlingSubSubCategory(el));
      listSubTmp.add(subTmp);
    }
    return listSubTmp;
  }

  List<SubSubCategory> crawlingSubSubCategory(Element subcategory) {
    int cont = 0;
    List<SubSubCategory> listSubSubTmp = new ArrayList<>();
    Elements aaa = subcategory.getElementsByTag("li");
    for (Element ela : aaa) {
      String nameSubSub = ela.text();
      String urlSubSub = ela.getElementsByTag("a").attr("abs:href");
      logger.debug("SUBSUB_{" + nameSubSub + "}=[" + urlSubSub);
      if (cont > 1) {
        SubSubCategory subSubTmp = new SubSubCategory();
        subSubTmp.setName(nameSubSub.toLowerCase());
        subSubTmp.setUrl(urlSubSub);
        listSubSubTmp.add(subSubTmp);
      }
      cont++;
    }
    return listSubSubTmp;
  }

  @Override
  public List<Category> getCategories() {
    this.categories = buildCategories();
    return this.categories;
  }
}
