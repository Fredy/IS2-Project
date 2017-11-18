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

public class SagaCrawler extends Crawler {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public SagaCrawler() {
    this.url = "http://www.falabella.com.pe/";
  }

  private Document getHtmlFromURL(String PageURL) throws IOException {
    return Jsoup.connect(PageURL).userAgent("Mozilla").get();
  }

  public List<SubSubCategory> subSubCategory(Elements elssc) {
    String name_, url_;
    List<SubSubCategory> subSubCategoryUrls = new ArrayList<>();

    for (Element ssc : elssc) {
      SubSubCategory subSubCategory = new SubSubCategory();
      String cadena = "ver todo";
      int resultado = ssc.text().toLowerCase().indexOf(cadena);

      if (resultado == -1) {
        name_ = ssc.text().toLowerCase().trim();
        subSubCategory.setName(name_);
        url_ = ssc.attr("abs:href");
        subSubCategory.setUrl(url_);
        logger.debug("SUBSUBCAT{" + name_ + "}=[" + url_);

        subSubCategoryUrls.add(subSubCategory);
      }
    }
    return subSubCategoryUrls;

  }


  public List<SubCategory> subCategory(Elements elsc) {
    List<SubCategory> subCategoryUrls = new ArrayList<>();

    String name_, url_;
    for (Element sc : elsc) {
      List<SubSubCategory> subSubCategoryUrls;

      SubCategory subCategory = new SubCategory();
      name_ = sc.select("a.fb-masthead__child-links__item__link h4").text().toLowerCase()
          .trim();
      subCategory.setName(name_);
      url_ = sc.select("a.fb-masthead__child-links__item__link ").attr("abs:href");
      subCategory.setUrl(url_);
      logger.debug("SUBCAT{" + name_ + "}=[" + url_);

      Elements elssc = sc.select(".fb-masthead__grandchild-links li a");

      subSubCategoryUrls = subSubCategory(elssc);

      subCategory.setSubSubCategories(subSubCategoryUrls);
      subCategoryUrls.add(subCategory);
    }
    return subCategoryUrls;
  }


  public List<Category> category(Document document) {
    String name_, url_;
    List<Category> categoryUrls = new ArrayList<>();

    Elements elements = document != null ? document
        .select(
            ".site-wrapper #header > .fb-masthead__nav .fb-masthead__primary-wrapper .fb-masthead__primary-links__item")
        : null;

    if (elements != null) {
      for (int c = 2; c < elements.size(); c++) {
        Element element = elements.get(c);

        List<SubCategory> subCategoryUrls;
        Category category = new Category();
        name_ = element.select("h3").text().toLowerCase().trim();

        category.setName(name_);
        url_ = this.url;
        category.setUrl(url_);
        logger.debug("CAT{" + name_ + "}=[" + url_);

        Elements elsc = element.select(
            ".fb-masthead__child-wrapper > .fb-masthead__child-wrapper-content li.fb-masthead__child-links__item ");

        subCategoryUrls = subCategory(elsc);

        category.setSubCategories(subCategoryUrls);
        categoryUrls.add(category);
      }
    }
    return categoryUrls;

  }

  @Override
  protected List<Category> buildCategories() {

    List<Category> categoryUrls;

    Document document = null;
    try {
      document = getHtmlFromURL(this.url);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    categoryUrls = category(document);

    return categoryUrls;

  }

}