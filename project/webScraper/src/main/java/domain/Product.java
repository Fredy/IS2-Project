package domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

  private Long id;

  private String name;
  private Double normalPrice;
  private Double webPrice;
  private Double offerPrice;
  private Long sku;
  private String model;

  private Shop shop;

  public Product() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @ManyToOne
  @JoinColumn(name = "shop")
  public Shop getShop() {
    return shop;
  }

  public void setShop(Shop shop) {
    this.shop = shop;
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
