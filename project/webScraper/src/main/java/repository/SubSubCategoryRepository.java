package repository;

import domain.Product;
import domain.SubSubCategory;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SubSubCategoryRepository extends CrudRepository<SubSubCategory, Long> {

  /**
   * @return All SubSubCategories with name {@code subSubCategoryName}
   */
  @Query("SELECT ssc FROM SubSubCategory ssc WHERE ssc.name = ?1")
  Collection<SubSubCategory> getSubSubCategoriesByName(String subSubCategoryName);

  /**
   * @return All Product of a SubSubCategory with name {@code subSubCategoryName}
   */
  @Query("SELECT ssc FROM SubSubCategory ssc INNER JOIN ssc.products WHERE ssc.name = ?1")
  Collection<Product> getProducts(String subSubCategoryName);
}
