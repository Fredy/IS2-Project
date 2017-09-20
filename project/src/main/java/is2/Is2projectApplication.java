package is2;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;



@SpringBootApplication
@EntityScan("domain")
public class Is2projectApplication {

  public static void main(String[] args) {
    SpringApplication.run(Is2projectApplication.class, args);
  }
}
