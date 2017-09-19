package scraper;

import domain.Product;
import domain.Shop;
import java.util.List;

public interface Scraper {

  public List<Product> parseProduct();

  public void saveProduct(List<Product> product);

  public List<Product> getProduct();

  public Shop parseShop();

  public void saveShop(Shop shop);

  public Shop getShop();

}
