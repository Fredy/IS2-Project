package domain.features;

import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;


@Entity
@PrimaryKeyJoinColumn(referencedColumnName = "id")
public class LaptopFeature extends Feature {

  private String color;
  private Long storageCapacity;
  private String cpuModel;
  private Float displaySize;
  private Integer ramCapacity;

  public ArrayList<String> getExtraFeatures() {
    ArrayList<String> feautures = new ArrayList<>();
    feautures.add(this.getColor());
    feautures.add(this.getStorageCapacity().toString());
    feautures.add(this.getCpuModel());
    feautures.add(this.getDisplaySize().toString());
    feautures.add(this.getRamCapacity().toString());
    return feautures;
  }

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

  public Float getDisplaySize() {
    return displaySize;
  }

  public void setDisplaySize(Float displaySize) {
    this.displaySize = displaySize;
  }

  public Integer getRamCapacity() {
    return ramCapacity;
  }

  public void setRamCapacity(Integer ramCapacity) {
    this.ramCapacity = ramCapacity;
  }
}
