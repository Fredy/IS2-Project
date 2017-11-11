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

public class RipleyCrawler extends Crawler {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public RipleyCrawler() {
    this.setUrl("http://simple.ripley.com.pe");
  }

  private Document getHtmlFromUrl(String pageUrl) throws IOException {
    return Jsoup.connect(pageUrl).userAgent("Mozilla/5.0").get();
  }

  private String getUrl() {
    return url;
  }

  private void setUrl(String url) {
    this.url = url;
  }

  @Override
  protected List<Category> buildCategories() {
    ArrayList<Category> rCategories = new ArrayList<>();
    Document page;
    try {
      page = getHtmlFromUrl(url);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
      return rCategories;
    }
    Elements elements = page.getElementsByClass("main-categories")
        .select("[class^=main-category first-column]");
    for (Element element : elements) {
      String name = element.select("[class^=main-category-name]")
          .select("[class^=main-category-active-text]").text();
      name = name.toLowerCase();
      Category category = new Category();
      category.setName(name);
      category.setUrl(element.select("[class^=main-category-name]").attr("abs:href"));
      buildSubCategories(element, category);
      rCategories.add(category);
    }
    return rCategories;
  }

  ArrayList<SubCategory> buildSubCategories(Element categoryEl, Category category) {
    ArrayList<SubCategory> rSubCategories = new ArrayList<>();
    Elements elements = categoryEl.select("[class^=child-categories second-column]")
        .select("[class=child-category]");
    for (Element element : elements) {
      Elements realCategory = element.select("[href~=^/[\\-a-z]+/[\\-a-z]+$]");
      if (!realCategory.isEmpty()) {
        String name = realCategory.first().text();
        name = name.toLowerCase();
        if (name.equals("marcas")) {
          continue;
        }
        logger.debug("\t" + name);
        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setUrl(realCategory.attr("abs:href"));
        buildSubSubCategories(element, subCategory);
        rSubCategories.add(subCategory);
      }
    }
    category.setSubCategories(rSubCategories);
    return rSubCategories;
  }

  ArrayList<SubSubCategory> buildSubSubCategories(Element subCategoryEl,
      SubCategory subCategory) {
    ArrayList<SubSubCategory> rSubSubCategories = new ArrayList<>();
    Elements elements = subCategoryEl.select("[href~=^/[\\-a-z]+/[\\-a-z]+/[\\-a-z]+$]");
    for (Element element : elements) {
      String name = element.text();
      name = name.toLowerCase();
      logger.debug("\t\t" + name);
      SubSubCategory subSubCategory = new SubSubCategory();
      subSubCategory.setName(name);
      subSubCategory.setUrl(element.attr("abs:href"));
      rSubSubCategories.add(subSubCategory);
    }
    subCategory.setSubSubCategories(rSubSubCategories);
    return rSubSubCategories;
  }

}
