package domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

  @Id
  private Long id;

  private String name;
  private Double normalPrice;
  private Double webPrice;
  private Double offerPrice;
  private Long sku;
  private String model;

  public Product() {
  }

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

  public Double getNormalPrice() {
    return normalPrice;
  }

  public void setNormalPrice(Double normalPrice) {
    this.normalPrice = normalPrice;
  }

  public Double getWebPrice() {
    return webPrice;
  }

  public void setWebPrice(Double webPrice) {
    this.webPrice = webPrice;
  }

  public Double getOfferPrice() {
    return offerPrice;
  }

  public void setOfferPrice(Double offerPrice) {
    this.offerPrice = offerPrice;
  }

  public Long getSku() {
    return sku;
  }

  public void setSku(Long sku) {
    this.sku = sku;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

}
