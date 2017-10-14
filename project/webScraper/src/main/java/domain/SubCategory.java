package domain;

import java.util.List;
import java.util.Vector;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class SubCategory {

  private Long id;
  private String name;
  private String url;
  private List<SubSubCategory> subSubCategories;
  private Category category;

  public SubCategory() {
    subSubCategories = new Vector<>();
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @OneToMany
  @JoinColumn(name = "sub_sub_category_id")
  public List<SubSubCategory> getSubSubCategories() {
    return subSubCategories;
  }

  public void setSubSubCategories(List<SubSubCategory> subSubCategories) {
    this.subSubCategories = subSubCategories;
  }

  @ManyToOne
  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
}
