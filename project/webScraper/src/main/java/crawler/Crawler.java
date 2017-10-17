package crawler;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;
import java.util.List;

public abstract class Crawler {

  protected String url;
  // You should set the url value in implementation classes.

  public abstract List<Category> getCategories();

  public abstract List<SubCategory> getSubCategories();

  public abstract List<SubSubCategory> getSubSubCategories();

}
