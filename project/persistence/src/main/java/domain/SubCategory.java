package domain;

import java.util.List;
import java.util.Vector;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class SubCategory {

  private Long id;
  private String name;
  private String url;
  private List<SubSubCategory> subSubCategories;

  public SubCategory() {
    subSubCategories = new Vector<>();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
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

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "sub_category_id")
  public List<SubSubCategory> getSubSubCategories() {
    return subSubCategories;
  }

  public void setSubSubCategories(List<SubSubCategory> subSubCategories) {
    this.subSubCategories = subSubCategories;
  }

}
