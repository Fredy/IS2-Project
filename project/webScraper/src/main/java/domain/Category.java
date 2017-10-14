package domain;

import java.util.List;
import java.util.Vector;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class Category {

  private Long id;
  private String name;
  private String url;
  private List<SubCategory> subCategories;

  public Category() {
    subCategories = new Vector<>();
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

  @OneToMany
  @JoinColumn(name = "category_id")
  public List<SubCategory> getSubCategories() {
    return subCategories;
  }

  public void setSubCategories(List<SubCategory> subCategories) {
    this.subCategories = subCategories;
  }

}
