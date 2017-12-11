package scraper;

import domain.Product;
import domain.Shop;
import domain.SubSubCategory;
import domain.features.LaptopFeature;
import java.util.List;

public interface Scraper {

  List<Product> parseProducts(SubSubCategory subSubCategory);

  Shop parseShop();

  void parseExtraFeatures(Product product);

}
