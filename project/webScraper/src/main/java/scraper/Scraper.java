package scraper;

import domain.Product;
import domain.Shop;
import java.util.List;

public interface Scraper {

  public List<Product> parseProducts();
  public Shop parseShop();

}
