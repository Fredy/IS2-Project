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

}
