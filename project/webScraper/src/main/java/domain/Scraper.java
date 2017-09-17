package domain;

import domain.Product;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

public interface Scraper {

//  @Autowired
  //private String url;

  public Collection<Product> parseData();

  public void saveData(Collection<Product> list);

  public Collection<Product> getData();

}
