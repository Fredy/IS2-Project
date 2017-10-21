package is2;

import crawler.Crawler;
import crawler.LinioCrawler;
import crawler.OechsleCrawler;
import crawler.RipleyCrawler;
import crawler.SagaCrawler;
import crawler.TottusCrawler;
import domain.Category;
import domain.Product;
import domain.Shop;
import domain.SubCategory;
import domain.SubSubCategory;
import java.util.List;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import repository.CategoryRepository;
import repository.ProductRepository;
import repository.ShopRepository;
import repository.SubCategoryRepository;
import repository.SubSubCategoryRepository;
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
  @Autowired
  CategoryRepository categoryRepository;
  @Autowired
  SubCategoryRepository subCategoryRepository;
  @Autowired
  SubSubCategoryRepository subSubCategoryRepository;

  public static void main(String[] args) {
    SpringApplication.run(Is2projectApplication.class, args);
  }

  @Bean
  public CommandLineRunner commandLineRunner(ApplicationContext applicationContext) {
    return args -> {
      if (args.length < 1) {
        System.out.println("No se ha colocado una tienda");
      } else {
        System.out.println(args[0]);
        processArgs(args[0]);
      }
    };
  }

  private void processArgs(String shopName) {

    Scraper scraper;
    Crawler crawler;
    if (shopName.compareTo("linio") == 0 || shopName.compareTo("Linio") == 0) {
      scraper = new LinioScraper();
      crawler = new LinioCrawler();
    } else if (shopName.compareTo("oechsle") == 0 || shopName.compareTo("Oechsle") == 0) {
      scraper = new OechsleScraper();
      crawler = new OechsleCrawler();
    } else if (shopName.compareTo("ripley") == 0 || shopName.compareTo("Ripley") == 0) {
      scraper = new RipleyScraper();
      crawler = new RipleyCrawler();
    } else if (shopName.compareTo("saga") == 0 || shopName.compareTo("Saga") == 0) {
      scraper = new SagaScraper();
      crawler = new SagaCrawler();
    } else if (shopName.compareTo("tottus") == 0 || shopName.compareTo("Tottus") == 0) {
      scraper = new TottusScraper();
      crawler = new TottusCrawler();
    } else {
      System.out.println("La tienda ingresada no es manejada por el software");
      return;
    }
    //Create shop Register
    Shop shop = scraper.parseShop();
    //Create the categories tree.
    System.out.println("tienda " + shopName);

    List<Category> categories = crawler.getCategories();
    shop.setCategories(categories);
    shopRepository.save(shop);
    Scanner scanner = new Scanner(System.in);
    System.out.println("La tienda presenta las siguientes categorias, elija la deseada: ");
    for (int i = 0; i < categories.size(); ++i) {
      System.out.println(String.valueOf(i) + ") " + categories.get(i).getName());
    }
    Category categoryS = categories.get(scanner.nextInt());

    System.out.println("La categoria presenta las siguientes sub-categorias, elija la deseada: ");
    for (int i = 0; i < categoryS.getSubCategories().size(); ++i) {
      System.out.println(String.valueOf(i) + ") " + categoryS.getSubCategories().get(i).getName());
    }
    SubCategory subCategoryS = categoryS.getSubCategories().get(scanner.nextInt());

    System.out
        .println("La sub-categoria presenta las siguientes sub-sub-categorias, elija la deseada: ");
    for (int i = 0; i < subCategoryS.getSubSubCategories().size(); ++i) {
      System.out
          .println(String.valueOf(i) + ") " + subCategoryS.getSubSubCategories().get(i).getName());
    }
    SubSubCategory subSubCategoryS = subCategoryS.getSubSubCategories().get(scanner.nextInt());
    for (Category category : categories) {
      for (SubCategory subCategory : category.getSubCategories()) {
        for (SubSubCategory subSubCategory : subCategory.getSubSubCategories()) {
          List<Product> products;
          if (subSubCategoryS == subSubCategory) {
            products = scraper.parseProducts(subSubCategory);
            subSubCategory.setProducts(products);
          }
        }
      }
    }
    categoryRepository.save(categories);

  }
}
