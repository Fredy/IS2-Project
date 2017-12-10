package domain;

import domain.features.Feature;
import domain.productDistance.NGram;
import java.util.ArrayList;
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

  /**
   * Compare two Products using their atributes, This method calculate the distance between the same
   * atributes in both Products.
   *
   * @param other Product to compare
   * @return distance normalizated [ 0.0 - 1.0 ]
   */
  double compare(Product other) {
    double distance = 0d;
    ArrayList<String> ef = this.ExtraFeatures.getExtraFeatures();
    ArrayList<String> efOther = other.ExtraFeatures.getExtraFeatures();

    NGram ngram = new NGram();
    distance += ngram.distance(this.getName(), other.getName());
    distance += ngram.distance(this.getBrand(), other.getBrand());
    distance += ngram.distance(this.getModel(), other.getModel());
    distance += ngram.distance(this.getNormalPrice().toString(), other.getNormalPrice().toString());
    distance += ngram.distance(this.getWebPrice().toString(), other.getWebPrice().toString());
    distance += ngram.distance(this.getOfferPrice().toString(), other.getOfferPrice().toString());

    for (int i = 0; i < ef.size(); ++i) {
      distance = distance + ngram.distance(ef.get(i), efOther.get(i));
    }

    return distance / (6 + ef.size());
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