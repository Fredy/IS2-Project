package is2;


import domain.Product;
import domain.Shop;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repository.ProductRepository;
import repository.ShopRepository;
import scraper.TottusScraper;


@SpringBootApplication
@EntityScan("domain")
@EnableJpaRepositories("repository")
public class Is2projectApplication {

  @Autowired
  ProductRepository productRepository;
  @Autowired
  ShopRepository shopRepository;

  @PostConstruct
  void init() {
    TottusScraper tottusScraper = new TottusScraper();
    Shop shop = tottusScraper.parseShop();
    System.out.println(shop.getName());
    System.out.println(shop.getAddress());
    shopRepository.save(shop);
  }

  public static void main(String[] args) {
    SpringApplication.run(Is2projectApplication.class, args);
  }
}
