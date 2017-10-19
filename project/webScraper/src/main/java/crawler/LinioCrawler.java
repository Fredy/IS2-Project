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

public class LinioCrawler extends Crawler {

  private List<Category> categories;

  public LinioCrawler() {
    this.url = "https://www.linio.com.pe/";
    this.categories = null;
  }

  public Document getHtmlFromURL(String pageURL) throws IOException {
    return Jsoup.connect(pageURL).userAgent("Mozilla").get();
  }

  public List<Category> buildCategories(Document homePage) {

    List<Category> crawledCategories = new ArrayList<>();
    List<Integer> dataIndex = new ArrayList<>();

    Elements categories = homePage.body().getElementById("navbar")
        .getElementById("main-menu")
        .getElementsByClass("main-menu")
        .first().getElementsByClass("nav-item");

    for (Element category : categories) {
      Element data = category.getElementsByTag("a").first();
      String name = data.attr("title");

      // "Solo Hoy", "Tiendas Oficiales" and "Supermercado" repeat products from others categories,
      // so we don't consider them.
      if (name.contentEquals("Solo Hoy") ||
          name.contentEquals("Tiendas Oficiales") ||
          name.contentEquals("Supermercado")) {
        continue;
      }

      String href = data.attr("abs:href");
      Integer index = Integer.parseInt(data.attr("data-toggle-menu-content"));

      Category tmpCategory = new Category();
      tmpCategory.setName(name);
      tmpCategory.setUrl(href);
      crawledCategories.add(tmpCategory);
      dataIndex.add(index);
    }

    Document subCategoriesHtml = null;
    try {
      subCategoriesHtml = this.getHtmlFromURL("https://www.linio.com.pe/ng/main-menu");
    } catch (IOException e) {
      System.out.println("Linio Crawler: error loading Sub Categories page");
      //e.printStackTrace();
    }

    if (subCategoriesHtml == null) {
      return null;
    }

    for (int i = 0; i < crawledCategories.size(); i++) {
      List<SubCategory> subCategories = this.buildSubCategory(subCategoriesHtml, dataIndex.get(i));
      crawledCategories.get(i).setSubCategories(subCategories);
    }

    return crawledCategories;
  }

  public List<SubCategory> buildSubCategory(Document pageHtml, Integer categoryIndex) {
    // This function should receive the Document from https://www.linio.com.pe/ng/main-menu
    String selector = String.format("[data-menu='%s']", categoryIndex.toString());
    Elements subCategoryData = pageHtml.body().children().select(selector);

    // TODO: crawl all the categories and subcategories

    return null;

  }

  @Override
  public List<Category> getCategories() {
    // TODO: process categories.
    return this.categories;
  }

  @Override
  public List<SubCategory> getSubCategories() {
    // TODO: process subCategories

    List<SubCategory> subCategories = null;
    return subCategories;
  }

  @Override
  public List<SubSubCategory> getSubSubCategories() {
    // TODO: process subSubCategories
    return null;
  }
}



