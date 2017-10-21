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

public class RipleyCrawler extends Crawler {

  private Document getHtmlFromUrl(String PageUrl) throws IOException {
    return Jsoup.connect(PageUrl).userAgent("Mozilla/5.0").get();
  }

  private String getUrl() {
    return url;
  }

  private void setUrl(String url) {
    this.url = url;
  }

  @Override
  protected List<Category> buildCategories() {
    this.setUrl("http://simple.ripley.com.pe");
    ArrayList<Category> RCategories = new ArrayList<>();
    Document Page;
    try {
      Page = getHtmlFromUrl(url);
    } catch (IOException e) {
      e.printStackTrace();
      return RCategories;
    }
    try {
      Page = getHtmlFromUrl(url);
    } catch (IOException e) {
      e.printStackTrace();
    }
    Elements elements = Page.getElementsByClass("main-categories")
        .select("[class^=main-category first-column]");
    for (Element element : elements) {
      String Name = element.select("[class^=main-category-name]")
          .select("[class^=main-category-active-text]").text();
      Category category = new Category();
      category.setName(Name);
      category.setUrl(this.getUrl() + element.select("[class^=main-category-name]").attr("href"));
      buildSubCategories(element, category);
      RCategories.add(category);
    }
    return RCategories;
  }

  private ArrayList<SubCategory> buildSubCategories(Element Category, Category category) {
    ArrayList<SubCategory> RSubCategories = new ArrayList<>();
    Elements elements = Category.select("[class^=child-categories second-column]")
        .select("[class=child-category]");
    for (Element element : elements) {
      Elements realCategory = element.select("[href~=^/[\\-a-z]+/[\\-a-z]+$]");
      if (!realCategory.isEmpty()) {
        String Name = realCategory.first().text();
        if(Name.equals("Marcas"))continue;
        //System.out.println("\t" + Name);
        SubCategory subCategory = new SubCategory();
        subCategory.setName(Name);
        subCategory.setUrl(this.getUrl() + realCategory.attr("href"));
        buildSubSubCategories(element, subCategory);
        RSubCategories.add(subCategory);
      }
    }
    category.setSubCategories(RSubCategories);
    return RSubCategories;
  }

  private ArrayList<SubSubCategory> buildSubSubCategories(Element SubCategory,
      SubCategory subCategory) {
    ArrayList<SubSubCategory> RSubSubCategories = new ArrayList<>();
    Elements elements = SubCategory.select("[href~=^/[\\-a-z]+/[\\-a-z]+/[\\-a-z]+$]");
    for (Element element : elements) {
      String Name = element.text();
      //System.out.println("\t\t"+Name);
      SubSubCategory subSubCategory = new SubSubCategory();
      subSubCategory.setName(Name);
      subSubCategory.setUrl(this.getUrl() + element.attr("href"));
      RSubSubCategories.add(subSubCategory);
    }
    subCategory.setSubSubCategories(RSubSubCategories);
    return RSubSubCategories;
  }

}
