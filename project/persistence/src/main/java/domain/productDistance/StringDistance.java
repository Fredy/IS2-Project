package domain.productDistance;

import java.io.Serializable;

public interface StringDistance extends Serializable {

  /**
   * Compute and return a measure of distance. Must be >= 0.
   */
  double distance(String s1, String s2);

}
