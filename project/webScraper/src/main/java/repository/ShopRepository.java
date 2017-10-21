package repository;

import domain.Category;
import domain.Shop;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ShopRepository extends CrudRepository<Shop, Long> {

  /**
   * @return All Shops with name shopName
   */
  @Query("SELECT s FROM Shop s WHERE s.name = ?1")
  Collection<Shop> getShopByName(String shopName);

  /**
   * @return All Categories of a Shop
   */
  @Query("SELECT s FROM Shop s INNER JOIN s.categories WHERE s.name = ?1")
  Collection<Category> getCategories(String shopName);

}
