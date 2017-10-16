package scraper;

import domain.Product;
import domain.Shop;
import domain.SubSubCategory;
import java.util.List;

public interface Scraper {

  List<Product> parseProducts(SubSubCategory subSubCategory);

  Shop parseShop();

}
