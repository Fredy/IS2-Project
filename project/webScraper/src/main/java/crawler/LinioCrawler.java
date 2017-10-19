package crawler;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;
import java.util.List;

public class LinioCrawler extends Crawler {

  private List<Category> categories;
  private List<SubCategory> subCategories;
  private List<SubSubCategory> subSubCategories;

  public LinioCrawler() {
    this.url = "https://www.linio.com.pe/";
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
}
