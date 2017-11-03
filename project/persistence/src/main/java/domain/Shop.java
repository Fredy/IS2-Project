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
import javax.persistence.Table;


@Entity
@Table(name = "shop")
public class Shop {

  private Long id;
  private String name;
  private String url;
  private String address;

  private List<Category> categories;

  public Shop() {
    categories = new Vector<>();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "shop_id")
  public List<Category> getCategories() {
    return categories;
  }

  public void setCategories(List<Category> categories) {
    this.categories = categories;
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}