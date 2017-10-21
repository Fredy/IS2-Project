package repository;

import domain.Product;
import java.util.Collection;
import java.util.Date;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

  @Query("SELECT p FROM Product p WHERE p.brand = ?1")
  Collection<Product> getProductsByBrand(String brandName);

  @Query("SELECT p FROM Product p WHERE p.model = ?1")
  Collection<Product> getProductsByModel(String modelName);

  @Query("SELECT p FROM Product p WHERE p.date = ?1")
  Collection<Product> getProductsByDate(Date date);

  @Query(value = "SELECT p FROM Product p WHERE p.date BETWEEN ?1 AND ?2 ")
  Collection<Product> getProductsBetweenDates(Date first, Date last);

  @Query("SELECT p FROM Product p WHERE p.date <= ?1")
  Collection<Product> getProductsBeforeDate(Date date);

  @Query("SELECT p FROM Product p WHERE p.date >= ?1")
  Collection<Product> getProductsAfterDate(Date date);

}
