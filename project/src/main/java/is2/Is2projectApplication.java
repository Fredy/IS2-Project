package is2;

import domain.Product;
import domain.Shop;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repository.ProductRepository;
import repository.ShopRepository;
import scraper.LinioScraper;
import scraper.OechsleScraper;
import scraper.RipleyScraper;
import scraper.SagaScraper;
import scraper.Scraper;
import scraper.TottusScraper;

@SpringBootApplication
@EntityScan("domain")
@EnableJpaRepositories("repository")
public class Is2projectApplication {


  @Autowired
  ProductRepository productRepository;
  @Autowired
  ShopRepository shopRepository;

  public static void main(String[] args) {
    SpringApplication.run(Is2projectApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext applicationContext) {
    return args -> {
      if (args.length < 1) {
        System.out.println("No shop");
      } else {
        System.out.println(args[0]);
        processArgs(args[0]);
      }
    };
  }

  private void processArgs(String shopName) {
    System.out.println("Nombre de la tienda + " + shopName);
    Scraper scraper;
    if (shopName.compareTo("linio") == 0 || shopName.compareTo("Linio") == 0) {
      System.out.println("linio scrapping");
      scraper = new LinioScraper();
    } else if (shopName.compareTo("oechsle") == 0 || shopName.compareTo("Oechsle") == 0) {
      System.out.println("oh scrapping");
      scraper = new OechsleScraper();
    } else if (shopName.compareTo("ripley") == 0 || shopName.compareTo("Ripley") == 0) {
      System.out.println("ripley scrapping");
      scraper = new RipleyScraper();
    } else if (shopName.compareTo("saga") == 0 || shopName.compareTo("Saga") == 0) {
      System.out.println("saga scrapping");
      scraper = new SagaScraper();
    } else if (shopName.compareTo("tottus") == 0 || shopName.compareTo("Tottus") == 0) {
      System.out.println("tottus scrapping");
      scraper = new TottusScraper();
    } else {
      System.out.println("La tienda ingresada no es manejada");
      return;
    }
    List<Product> products = scraper.parseProducts();
    System.out.println(products.size());
    Shop shop = scraper.parseShop();
    shop.setProducts(products);
    productRepository.save(products);
    shopRepository.save(shop);
  }
}
