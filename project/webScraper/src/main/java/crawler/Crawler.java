package crawler;

import domain.Category;
import java.util.List;

public abstract class Crawler {

  protected String url;
  // You should set the url value in implementation classes.

  protected List<Category> categories;

  /**
   * This method builds the Categories Tree in class member categories. The intention of this method
   * is to don't make multiple requests to the web page.
   *
   * @return Builded tree in {@code List<Category>}
   */
  protected abstract List<Category> buildCategories();

  public List<Category> getCategories() {
    this.categories = buildCategories();
    return this.categories;
  }
}
