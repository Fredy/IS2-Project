package service;

import domain.Product;
import domain.productDistance.NGram;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.ProductRepository;

@Service
public class ProductService {

  @Autowired
  ProductRepository productRepository;

  /**
   * Compare two Products using their atributes, This method calculate the distance between the same
   * atributes in both Products.
   *
   * @param p1 First product for comparison
   * @param p2 Second product for comparison
   * @return distance normalizated [ 0.0 - 1.0 ]
   */
  double compare(Product p1, Product p2) {
    double distance = 0d;
    ArrayList<String> efp1 = p1.getExtraFeatures().moreFeatures();
    ArrayList<String> efp2 = p2.getExtraFeatures().moreFeatures();

    NGram ngram = new NGram();
    distance += ngram.distance(p1.getName(), p2.getName());
    distance += ngram.distance(p1.getBrand(), p2.getBrand());
    distance += ngram.distance(p1.getModel(), p2.getModel());
    distance += ngram.distance(p1.getNormalPrice().toString(), p2.getNormalPrice().toString());
    distance += ngram.distance(p1.getWebPrice().toString(), p2.getWebPrice().toString());
    distance += ngram.distance(p1.getOfferPrice().toString(), p2.getOfferPrice().toString());

    for (int i = 0; i < efp1.size(); ++i) {
      distance = distance + ngram.distance(efp1.get(i), efp2.get(i));
    }

    //if( p1.getName().compareTo())

    return distance / (6 + efp1.size());
  }


  /**
   * @param p1 Product base
   * @param n Number of product to search
   * @return n-Products more similar to Product p1
   */
  ArrayList<Product> getSimilarProducts(Product p1, int n) {
    ArrayList<Product> similars = new ArrayList<>();
    ArrayList<Product> allProducts = new ArrayList<>();
    Map<Double, Product> mapProducts = new TreeMap<>();
    allProducts.addAll(productRepository.getAllProducts());

    allProducts.remove(p1);

    for (Product p2 : allProducts) {
      double distance = compare(p1, p2);

      if (mapProducts.containsValue(new Double(distance))) {
        distance += Double.MIN_VALUE;
      }

      mapProducts.put(new Double(distance), p2);
    }

    similars.addAll(mapProducts.values());
    return new ArrayList<>(similars.subList(0, n));
  }

}
