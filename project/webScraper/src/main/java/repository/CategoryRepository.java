package repository;

import domain.Category;
import domain.SubCategory;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {

  /**
   * @return All Categories with name {@code categoryName}
   */
  @Query("SELECT c FROM Category c WHERE c.name = ?1")
  Collection<Category> getCategoriesByName(String categoryName);

  /**
   * @return All SubCategories from a Category with name {@code categoryName}
   */
  @Query("SELECT sc FROM Category sc INNER JOIN sc.subCategories WHERE sc.name = ?1")
  Collection<SubCategory> getSubCategories(String categoryName);

}
