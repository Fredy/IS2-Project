package repository;

import domain.SubCategory;
import domain.SubSubCategory;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SubCategoryRepository extends CrudRepository<SubCategory, Long> {

  /**
   * @return All Categories with name categoryName
   */
  @Query("SELECT s FROM SubCategory sc WHERE sc.name = ?1")
  Collection<SubCategory> getSubCategoriesByName(String subCategoryName);

  /**
   * @return All SubSubCategories from a SubCategory
   */
  @Query("SELECT sc FROM SubCategory sc INNER JOIN sc.subSubCategories WHERE sc.name = ?1")
  Collection<SubSubCategory> getSubSubCategories(String subCategoryName);
}
