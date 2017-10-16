package is2;

import domain.Product;
import domain.Shop;
import domain.SubSubCategory;
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
    SpringApplication.run(Is2projectApplication.class, "tottus");
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

    Scraper scraper;
    String urlTest = "";
    String categoryNameTest = "";
    if (shopName.compareTo("linio") == 0 || shopName.compareTo("Linio") == 0) {

      scraper = new LinioScraper();
    } else if (shopName.compareTo("oechsle") == 0 || shopName.compareTo("Oechsle") == 0) {

      scraper = new OechsleScraper();
    } else if (shopName.compareTo("ripley") == 0 || shopName.compareTo("Ripley") == 0) {

      scraper = new RipleyScraper();
    } else if (shopName.compareTo("saga") == 0 || shopName.compareTo("Saga") == 0) {

      scraper = new SagaScraper();
    } else if (shopName.compareTo("tottus") == 0 || shopName.compareTo("Tottus") == 0) {

      scraper = new TottusScraper();
      urlTest = "http://www.tottus.com.pe/tottus/browse/Electrohogar-Tecnolog%C3%ADa-Laptops/_/N-82nnyu";
      categoryNameTest = "Laptops";
    } else {
      System.out.println("La tienda ingresada no es manejada");
      return;
    }
    //Only for test

    SubSubCategory test1 = new SubSubCategory();
    test1.setName(categoryNameTest);
    test1.setUrl(urlTest);

    List<Product> products = scraper.parseProducts(test1);
    System.out.println(">> Productos Scrapeados: " + Integer.toString(products.size()));
    Shop shop = scraper.parseShop();
    shop.setProducts(products);
    productRepository.save(products);
    shopRepository.save(shop);
    System.out.println(">>[OK] Productos almacenados en base de datos");
  }
}
