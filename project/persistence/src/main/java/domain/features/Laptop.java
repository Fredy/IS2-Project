package domain.features;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;


@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class Laptop extends Feature {

  private String color;
  private Long storageCapacity;
  private String cpuModel;
  private Long displaySize;

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Long getStorageCapacity() {
    return storageCapacity;
  }

  public void setStorageCapacity(Long storageCapacity) {
    this.storageCapacity = storageCapacity;
  }

  public String getCpuModel() {
    return cpuModel;
  }

  public void setCpuModel(String cpuModel) {
    this.cpuModel = cpuModel;
  }

  public Long getDisplaySize() {
    return displaySize;
  }

  public void setDisplaySize(Long displaySize) {
    this.displaySize = displaySize;
  }

}
