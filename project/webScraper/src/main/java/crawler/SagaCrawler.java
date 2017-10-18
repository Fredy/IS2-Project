package crawler;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class SagaCrawler extends Crawler{

  private static String url = "http://www.falabella.com.pe/";

  public Vector<Category> categories() throws IOException {

    Vector<Category> categoryUrls = new Vector<Category>();

    Document doc = Jsoup.connect(url)
        .userAgent("Mozilla")
        .get();

    String htmlString = doc.html();
    Document document = Jsoup.parse(htmlString);
    Elements elements = document
        .select(
            ".site-wrapper #header > .fb-masthead__nav .fb-masthead__primary-wrapper .fb-masthead__primary-links__item");

    for (Element element : elements) {

      Vector<SubCategory> subCategoryUrls = new Vector<SubCategory>();

      Category category = new Category();
      category.setName(element.select("h3").text());
      category.setUrl(url);

      Elements elsc = element.select(
          ".fb-masthead__child-wrapper > .fb-masthead__child-wrapper-content li.fb-masthead__child-links__item ");

      for (Element sc : elsc) {
        Vector<SubSubCategory> subSubCategoryUrls = new Vector<SubSubCategory>();

        SubCategory subCategory = new SubCategory();
        subCategory.setName(sc.select("a.fb-masthead__child-links__item__link h4").text());

        subCategory.setUrl(url + sc.select("a.fb-masthead__child-links__item__link ").attr("href"));

        Elements elssc = sc.select(".fb-masthead__grandchild-links li a");

        for (Element ssc : elssc) {
          SubSubCategory subSubCategory = new SubSubCategory();
          subSubCategory.setName(ssc.text());
          subSubCategory.setUrl(url + ssc.attr("href"));
          subSubCategoryUrls.add(subSubCategory);
        }

        subCategory.setSubSubCategories(subSubCategoryUrls);
        subCategoryUrls.add(subCategory);
      }

      category.setSubCategories(subCategoryUrls);
      categoryUrls.add(category);
    }

    return categoryUrls;

  }


  public List<Category> getCategories() {
    return null;
  }

  public List<SubCategory> getSubCategories() {
    return null;
  }

  public List<SubSubCategory> getSubSubCategories() {
    return null;
  }

}