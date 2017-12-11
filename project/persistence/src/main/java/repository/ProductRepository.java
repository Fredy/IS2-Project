package repository;

import domain.Product;
import java.util.Collection;
import java.util.Date;
import javax.persistence.TemporalType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {

  @Query("SELECT p FROM Product p")
  Collection<Product> getAllProducts();

  /**
   * @return All Products with name {@code brandName}
   */
  @Query("SELECT p FROM Product p WHERE p.brand = ?1")
  Collection<Product> getProductsByBrand(String brandName);

  /**
   * @return All products with model {@code modelName}
   */
  @Query("SELECT p FROM Product p WHERE p.model = ?1")
  Collection<Product> getProductsByModel(String modelName);

  /**
   * @return All Products stored in date {@code date}
   */
  @Query("SELECT p FROM Product p WHERE p.date = ?1")
  Collection<Product> getProductsByDate(@Temporal(TemporalType.TIMESTAMP) Date date);

  /**
   * @param first Lower date limit
   * @param last Lpper date limit
   * @return All products stored between {@code first} and {@code last} dates
   */
  @Query(value = "SELECT p FROM Product p WHERE p.date BETWEEN ?1 AND ?2 ")
  Collection<Product> getProductsBetweenDates(@Temporal(TemporalType.TIMESTAMP) Date first,
      @Temporal(TemporalType.TIMESTAMP) Date last);

  /**
   * @return All Products before than date {@code date}
   */
  @Query("SELECT p FROM Product p WHERE p.date <= ?1")
  Collection<Product> getProductsBeforeDate(@Temporal(TemporalType.TIMESTAMP) Date date);

  /**
   * @return All Products after than date {@code date}
   */
  @Query("SELECT p FROM Product p WHERE p.date >= ?1")
  Collection<Product> getProductsAfterDate(@Temporal(TemporalType.TIMESTAMP) Date date);

}
