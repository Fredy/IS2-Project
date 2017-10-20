package crawler;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;
import java.util.List;
import org.jsoup.nodes.Document;

public abstract class Crawler {

  protected String url;
  // You should set the url value in implementation classes.

  protected List<Category> categories;

  /**
   * This method builds the Categories Tree in class member categories.
   * The intention of this method is to don't make multiple requests to the web page.
   * @param homePage
   * @return Builded tree in {@code List<Category>}
   */
  protected abstract List<Category> buildCategories(Document homePage);

  /** Returns the builded tree of the current page**/
  public abstract List<Category> getCategories();

  /** Returns all the subCategories of the current page **/
  public abstract List<SubCategory> getSubCategories();

  /** Returns all the subSubCategories of the corrent page**/
  public abstract List<SubSubCategory> getSubSubCategories();

}
