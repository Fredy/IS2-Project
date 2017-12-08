package domain;

import domain.features.Feature;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
  private String sku;
  private String model;
  private String brand;
  private Date date;
  private Feature ExtraFeatures;

  public Product() {
    date = new Date();
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

  public String getSku() {
    return sku;
  }

  public void setSku(String sku) {
    this.sku = sku;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "extra_feature_id")
  public Feature getExtraFeatures() {
    return ExtraFeatures;
  }

  public void setExtraFeatures(Feature extraFeatures) {
    ExtraFeatures = extraFeatures;
  }
}