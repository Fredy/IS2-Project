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

public abstract class SagaCrawler extends Crawler {

  private List<Category> categories;
  private List<SubCategory> subCategories;
  private List<SubSubCategory> subSubCategories;

  public SagaCrawler() {
    this.url = "http://www.falabella.com.pe/";
    categories();
  }

  @Override
  public List<Category> getCategories() {
    // TODO: process categories.
    return this.categories;
  }

  @Override
  public List<SubCategory> getSubCategories() {
    // TODO: process subCategories
    return this.subCategories;
  }

  @Override
  public List<SubSubCategory> getSubSubCategories() {
    // TODO: process subSubCategories
    return this.subSubCategories;
  }

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  public List<Category> categories() {

    List<Category> categoryUrls = new ArrayList<Category>();

    Document doc = null;
    try {
      doc = getHtmlFromURL(url);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String htmlString = doc.html();
    Document document = Jsoup.parse(htmlString);
    Elements elements = document
        .select(
            ".site-wrapper #header > .fb-masthead__nav .fb-masthead__primary-wrapper .fb-masthead__primary-links__item");

    for (Element element : elements) {

      List<SubCategory> subCategoryUrls = new ArrayList<SubCategory>();

      Category category = new Category();
      category.setName(element.select("h3").text());
      category.setUrl(url);

      Elements elsc = element.select(
          ".fb-masthead__child-wrapper > .fb-masthead__child-wrapper-content li.fb-masthead__child-links__item ");

      for (Element sc : elsc) {
        List<SubSubCategory> subSubCategoryUrls = new ArrayList<SubSubCategory>();

        SubCategory subCategory = new SubCategory();
        subCategory.setName(sc.select("a.fb-masthead__child-links__item__link h4").text());
        subCategory.setUrl(url + sc.select("a.fb-masthead__child-links__item__link ").attr("href"));

        Elements elssc = sc.select(".fb-masthead__grandchild-links li a");

        for (Element ssc : elssc) {
          SubSubCategory subSubCategory = new SubSubCategory();
          String cadena = "ver todo";
          int resultado = ssc.text().toLowerCase().indexOf(cadena);

          if (resultado == -1) {
            subSubCategory.setName(ssc.text());
            subSubCategory.setUrl(url + ssc.attr("href"));
            subSubCategoryUrls.add(subSubCategory);
            this.subSubCategories.add(subSubCategory);
          }

        }

        subCategory.setSubSubCategories(subSubCategoryUrls);
        subCategoryUrls.add(subCategory);
        this.subCategories.add(subCategory);
      }

      category.setSubCategories(subCategoryUrls);
      categoryUrls.add(category);
      this.categories.add(category);
    }

    return categoryUrls;

  }

}