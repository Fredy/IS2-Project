package repository;

import domain.SubCategory;
import domain.SubSubCategory;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SubCategoryRepository extends CrudRepository<SubCategory, Long> {

  /**
   * @return All SubCategories with name {@code subCategoryName}
   */
  @Query("SELECT sc FROM SubCategory sc WHERE sc.name = ?1")
  Collection<SubCategory> getSubCategoriesByName(String subCategoryName);

  /**
   * @return All SubSubCategories from a SubCategory with name {@code subCategoryName}
   */
  @Query("SELECT sc FROM SubCategory sc INNER JOIN sc.subSubCategories WHERE sc.name = ?1")
  Collection<SubSubCategory> getSubSubCategories(String subCategoryName);
}
