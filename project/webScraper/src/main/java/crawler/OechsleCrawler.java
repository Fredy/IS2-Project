package crawler;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OechsleCrawler extends Crawler {

  public OechsleCrawler() {
    url = "http://www.oechsle.pe";
    categories = new Vector<>();
  }

  @Override
  protected List<Category> buildCategories() {
    try {
      Document homePage = getHtmlFromUrl(this.url);

      crawlingCategories(homePage);
      crawlingSubCategories(homePage);
      crawlingSubSubCategories(homePage);

    } catch (IOException e) {
      System.out.println("We can't access to web page");
      //e.printStackTrace();
    }
    return this.categories;
  }

  private void crawlingCategories(Document doc) {
    Vector<Category> categoriesAux = new Vector<>();
    Elements cats = doc.getElementsByAttributeValue("class", "wrap-hover");
    for (Element catElement : cats) {
      Category category = new Category();
      String nameCategory = catElement.text().toLowerCase();

      Element aux = catElement.getElementsByTag("a").first();
      String urlCategory = this.url + aux.attr("href");

      category.setName(nameCategory);
      category.setUrl(urlCategory);
      categoriesAux.add(category);
    }

    this.categories = categoriesAux.subList(0, categoriesAux.size() - 2);

  }

  private void crawlingSubCategories(Document doc) {

    Elements subCats = doc.getElementsByAttributeValue("class", "menu-preview wrap-hover");
    int cont = 0;

    for (Element catElementItems : subCats) {
      Vector<SubCategory> subCategoriesAux = new Vector<>();
      String nameSubCategory;
      String urlSubCategory;
      Elements catsElements = catElementItems.select("div[class^=item]");

      for (Element catItem : catsElements) {
        SubCategory subCategory = new SubCategory();

        Elements nameSubCats = catItem.getElementsByAttributeValue("class", "tit");

        nameSubCategory = nameSubCats.text().toLowerCase();
        urlSubCategory = this.url + nameSubCats.select("a").first().attr("href");

        subCategory.setName(nameSubCategory);
        subCategory.setUrl(urlSubCategory);

        subCategoriesAux.add(subCategory);
      }

      categories.get(cont).setSubCategories(subCategoriesAux);
      cont++;
    }

  }

  private void crawlingSubSubCategories(Document doc) {
    Elements cats = doc.getElementsByAttributeValue("class", "menu-preview wrap-hover");
    int contC = 0;
    int indexCatRemove = 0;
    int indexSubCatRemove = 0;
    boolean remove = false;
    for (Element catItem : cats) {
      int contS = 0;
      Elements catsElements = catItem.getElementsByAttributeValue("class", "info"); // categories

      for (Element catElement : catsElements) {
        Vector<SubSubCategory> subSubCatAux = new Vector<>();
        Element subCats = catElement.getElementsByAttributeValue("class", "tit")
            .first(); // subcategories
        Elements subSubCats = catElement.select("a[class^=sub]"); // subsubcategories
        for (Element subSubCatElement : subSubCats) {
          SubSubCategory subSubCategory = new SubSubCategory();
          String nameSubSubCat = subSubCatElement.text().toLowerCase();
          String urlSubSubCat = this.url + subSubCatElement.attr("href");

          subSubCategory.setName(nameSubSubCat);
          subSubCategory.setUrl(urlSubSubCat);

          subSubCatAux.add(subSubCategory);
        }

        if (subSubCatAux.size() == 0) {
          SubSubCategory subSubCategory = new SubSubCategory();
          subSubCategory.setName(categories.get(contC).getSubCategories().get(contS).getName());
          subSubCategory.setUrl(categories.get(contC).getSubCategories().get(contS).getUrl());
          subSubCatAux.add(subSubCategory);
        }

        if (subCats.text().equals("-")) {
          categories.get(contC).getSubCategories().get(contS - 1).getSubSubCategories()
              .addAll(subSubCatAux);
          indexCatRemove = contC;
          indexSubCatRemove = contS;
          remove = true;
        } else {
          categories.get(contC).getSubCategories().get(contS).setSubSubCategories(subSubCatAux);
        }

        contS++;
      }
      contC++;
    }

    if (remove) {
      categories.get(indexCatRemove).getSubCategories().remove(indexSubCatRemove);
    }

  }

  private Document getHtmlFromUrl(String url) throws IOException, HttpStatusException {
    return Jsoup.connect(url).get();
  }

}
