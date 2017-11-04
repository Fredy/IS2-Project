package crawler;

import static org.junit.Assert.assertTrue;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

// TODO: this is just for test that tests works xd, do the proper tests
public class LinioCrawlerTest {

  @Test
  public void crawlCategories() throws Exception {
    LinioCrawler crawler = new LinioCrawler();

    File htmlFile = new File("src/test/resources/crawler/linioPage.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "https://www.linio.com.pe");

    List<Integer> tmp = new ArrayList<>();
    List<Category> categories = crawler.crawlCategories(document, tmp);

    assertTrue(categories.get(0).getName()
        .contentEquals("celulares y tablets"));
    assertTrue(categories.get(0).getUrl()
        .contentEquals("https://www.linio.com.pe/c/celulares-y-tablets"));

    assertTrue(categories.get(1).getName()
        .contentEquals("computación"));
    assertTrue(categories.get(1).getUrl()
        .contentEquals("https://www.linio.com.pe/c/computacion"));

    assertTrue(categories.get(2).getName()
        .contentEquals("hogar"));
    assertTrue(categories.get(2).getUrl()
        .contentEquals("https://www.linio.com.pe/c/hogar"));

  }

  @Test
  public void crawlSubsAndSubSubs() throws Exception {
    LinioCrawler crawler = new LinioCrawler();

    File htmlFile = new File("src/test/resources/crawler/linioSubCategories.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "https://www.linio.com.pe");

    List<SubCategory> subCategories = crawler.crawlSubsAndSubSubs(document, 4);

    assertTrue(subCategories.get(0).getName().contentEquals("consolas y videojuegos"));
    assertTrue(subCategories.get(0).getUrl()
        .contentEquals("https://www.linio.com.pe/c/consolas-y-videojuegos"));

    assertTrue(subCategories.get(1).getName().contentEquals("libros"));
    assertTrue(subCategories.get(1).getUrl()
        .contentEquals("https://www.linio.com.pe/c/libros-y-peliculas/libros"));

    assertTrue(subCategories.get(2).getName().contentEquals("películas"));
    assertTrue(subCategories.get(2).getUrl()
        .contentEquals("https://www.linio.com.pe/c/libros-y-peliculas/peliculas"));

    assertTrue(subCategories.get(3).getName().contentEquals("música"));
    assertTrue(subCategories.get(3).getUrl()
        .contentEquals("https://www.linio.com.pe/c/libros-y-peliculas/musica"));

    List<SubSubCategory> subSubCategory1 = subCategories.get(1).getSubSubCategories();

    assertTrue(subSubCategory1.get(0).getName().contentEquals("literatura y novelas"));
    assertTrue(subSubCategory1.get(0).getUrl()
        .contentEquals("https://www.linio.com.pe/c/libros/literatura-y-novelas"));

    assertTrue(subSubCategory1.get(1).getName().contentEquals("libros infantiles"));
    assertTrue(subSubCategory1.get(1).getUrl()
        .contentEquals("https://www.linio.com.pe/c/libros/libros-infantiles"));

    List<SubSubCategory> subSubCategories2 = subCategories.get(3).getSubSubCategories();

    assertTrue(subSubCategories2.get(0).getName().contentEquals("música"));
    assertTrue(subSubCategories2.get(0).getUrl()
        .contentEquals("https://www.linio.com.pe/c/libros-y-peliculas/musica"));
  }
}