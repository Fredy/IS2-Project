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

  @Override
  protected List<Category> buildCategories() {
    List<Category> crawledCategories = null;
    try {
      Document homePageHtml = getHtmlFromURL(this.url);
      List<Integer> dataIndex = new ArrayList<>();
      crawledCategories = crawlCategories(homePageHtml, dataIndex);

      // TODO: make a try catch for this Document ?
      Document subCategoriesHtml = this.getHtmlFromURL("https://www.linio.com.pe/ng/main-menu");
      for (int i = 0; i < crawledCategories.size(); i++) {
        List<SubCategory> subCategories = this
            .crawlSubsAndSubSubs(subCategoriesHtml, dataIndex.get(i));
        crawledCategories.get(i).setSubCategories(subCategories);

      }

    } catch (IOException e) {
      e.printStackTrace();
    }
    return crawledCategories;
  }

  private List<Category> crawlCategories(Document homePage, List<Integer> outDataIndex) {
    List<Category> crawledCategories = new ArrayList<>();

    Elements categories = homePage.body().getElementById("navbar")
        .getElementById("main-menu")
        .getElementsByClass("main-menu")
        .first().getElementsByClass("nav-item");

    for (Element category : categories) {
      Element data = category.getElementsByTag("a").first();
      String name = data.attr("title").toLowerCase().trim();

      // "solo Hoy", "tiendas oficiales" and "supermercado" repeat products from others categories,
      // so we don't consider them.
      if (name.contentEquals("solo hoy") ||
          name.contentEquals("tiendas oficiales") ||
          name.contentEquals("supermercado")) {
        continue;
      }

      String href = data.attr("abs:href");
      Integer index = Integer.parseInt(data.attr("data-toggle-menu-content"));

      Category tmpCategory = new Category();
      tmpCategory.setName(name);
      tmpCategory.setUrl(href);
      crawledCategories.add(tmpCategory);
      outDataIndex.add(index);
    }

    return crawledCategories;
  }

  private List<SubCategory> crawlSubsAndSubSubs(Document pageHtml, Integer categoryIndex) {
    // This function should receive the Document from https://www.linio.com.pe/ng/main-menu

    String selector = String.format("[data-menu='%s']", categoryIndex.toString());
    Elements subCategoryRawData = pageHtml.body().children().select(selector)
        .first().getElementsByClass("col-lg-6 col-xl-5")
        .first().getElementsByClass("col-xs-6");

    Elements allSubAndSS = new Elements();
    for (Element sCatData : subCategoryRawData) {
      Elements tmpSubAndSS = sCatData.children();
      allSubAndSS.addAll(tmpSubAndSS);
    }

    List<SubCategory> subCategories = new ArrayList<>();
    SubCategory subCategory = null;
    for (int i = 0; i < allSubAndSS.size(); i++) {
      String name = allSubAndSS.get(i).attr("title").toLowerCase().trim();
      String url = allSubAndSS.get(i).attr("abs:href");
      String className = allSubAndSS.get(i).attr("class");

      if (className.contentEquals("subcategory-title")) {
        if (name.contains("ofertas") || name.contains("tienda") || name.contains("más") ||
            name.contains("liquidación")) {
          // Increment i until it's element is a new sub Category, or there is no more elements.
          do {
            i++;
          } while (i < allSubAndSS.size() && !allSubAndSS.get(i).attr("class")
              .contentEquals("subcategory-title"));
          i--;
          continue;
        }

        subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setUrl(url);
        subCategories.add(subCategory);
      } else {
        SubSubCategory tmpSSC = new SubSubCategory();
        tmpSSC.setName(name);
        tmpSSC.setUrl(url);
        subCategory.getSubSubCategories().add(tmpSSC);
      }
    }

    return subCategories;
  }
}



