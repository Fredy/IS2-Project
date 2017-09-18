package domain;

import domain.Product;
import java.util.List;

public interface Scraper {

  public List<Product> parseProduct();

  public void saveProduct(List<Product> product);

  public List<Product> getProduct();

  public List<Shop> parseShop();

  public void saveShop(List<Shop> shop);

  public List<Shop> getShop();

}
