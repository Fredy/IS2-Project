package scraper;

import domain.Product;
import domain.Shop;
import java.util.List;

public interface Scraper {

  List<Product> parseProducts(/*SubSubCategory subSubCategory*/);
  // TODO: uncomment and include SubSubCategory when implemented

  Shop parseShop();

}
