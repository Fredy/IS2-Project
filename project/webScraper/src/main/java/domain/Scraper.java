package domain;

import domain.Product;
import java.util.Collection;

public interface Scraper {

  public Collection<Product> parseData();

  public void saveData(Collection<Product> product);

  public Collection<Product> getData();

}
