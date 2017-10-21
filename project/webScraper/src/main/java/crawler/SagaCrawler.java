package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SagaCrawler extends Crawler {

  public SagaCrawler() {
    this.url = "http://www.falabella.com.pe/";
  }

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  @Override
  protected List<Category> buildCategories() {

    List<Category> categoryUrls = new ArrayList<Category>();

    Document doc = null;
    try {
      doc = getHtmlFromURL(this.url);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String htmlString = doc.html();
    Document document = Jsoup.parse(htmlString);
    Elements elements = document
        .select(
            ".site-wrapper #header > .fb-masthead__nav .fb-masthead__primary-wrapper .fb-masthead__primary-links__item");

    for (int c = 2; c < elements.size(); c++) {
      Element element = elements.get(c);

      List<SubCategory> subCategoryUrls = new ArrayList<SubCategory>();

      Category category = new Category();

      category.setName(element.select("h3").text().toLowerCase().trim());
      category.setUrl(this.url);

      Elements elsc = element.select(
          ".fb-masthead__child-wrapper > .fb-masthead__child-wrapper-content li.fb-masthead__child-links__item ");

      for (Element sc : elsc) {
        List<SubSubCategory> subSubCategoryUrls = new ArrayList<SubSubCategory>();

        SubCategory subCategory = new SubCategory();
        subCategory.setName(sc.select("a.fb-masthead__child-links__item__link h4").text().toLowerCase().trim());
        subCategory.setUrl(sc.select("a.fb-masthead__child-links__item__link ").attr("abs:href"));

        Elements elssc = sc.select(".fb-masthead__grandchild-links li a");

        for (Element ssc : elssc) {
          SubSubCategory subSubCategory = new SubSubCategory();
          String cadena = "ver todo";
          int resultado = ssc.text().toLowerCase().indexOf(cadena);

          if (resultado == -1) {
            subSubCategory.setName(ssc.text().toLowerCase().trim());
            subSubCategory.setUrl(ssc.attr("abs:href"));
            subSubCategoryUrls.add(subSubCategory);
          }

        }

        subCategory.setSubSubCategories(subSubCategoryUrls);
        subCategoryUrls.add(subCategory);
      }

      category.setSubCategories(subCategoryUrls);
      categoryUrls.add(category);
    }

    return categoryUrls;

  }

}