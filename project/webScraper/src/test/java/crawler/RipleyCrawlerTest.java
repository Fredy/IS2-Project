package crawler;

import static org.junit.Assert.assertEquals;

import domain.Category;
import domain.SubCategory;
import domain.SubSubCategory;
import java.io.File;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

public class RipleyCrawlerTest {

  @Test
  public void buildCategories1() throws Exception {
    RipleyCrawler ripleyCrawler = new RipleyCrawler();
    File htmlFile = new File("src/test/resources/crawler/ripleyCrawler.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://simple.ripley.com");
    ArrayList<Category> categories = (ArrayList<Category>) ripleyCrawler.buildCategories1(document);
    //First we are going to verify the categories
    assertEquals(categories.size(), 13);
    assertEquals(categories.get(0).getName(), "tv y video");
    assertEquals(categories.get(1).getName(), "cómputo");
    assertEquals(categories.get(2).getName(), "muebles");
    assertEquals(categories.get(3).getName(), "dormitorio");
    assertEquals(categories.get(4).getName(), "infantil");
    assertEquals(categories.get(5).getName(), "belleza y accesorios");
    assertEquals(categories.get(6).getName(), "entretenimiento");
    assertEquals(categories.get(7).getName(), "calzado");
    assertEquals(categories.get(8).getName(), "deporte");
    assertEquals(categories.get(9).getName(), "celulares");
    assertEquals(categories.get(10).getName(), "electrohogar");
    assertEquals(categories.get(11).getName(), "moda");
    assertEquals(categories.get(12).getName(), "ripley home");
  }

  @Test
  public void buildSubCategories() throws Exception {
    RipleyCrawler ripleyCrawler = new RipleyCrawler();
    File htmlFile = new File("src/test/resources/crawler/ripleyCrawler.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://simple.ripley.com");
    ArrayList<Category> categories = (ArrayList<Category>) ripleyCrawler.buildCategories1(document);
    Category category = categories.get(0);
    ArrayList<SubCategory> subCategories = (ArrayList<SubCategory>) category.getSubCategories();
    assertEquals(subCategories.size(), 5);
    assertEquals(subCategories.get(0).getName(), "televisores");
    assertEquals(subCategories.get(1).getName(), "video y accesorios");
    assertEquals(subCategories.get(2).getName(), "audio");
    assertEquals(subCategories.get(3).getName(), "equipo de sonido");
    assertEquals(subCategories.get(4).getName(), "car audio");
  }

  @Test
  public void buildSubSubCategories() throws Exception {
    RipleyCrawler ripleyCrawler = new RipleyCrawler();
    File htmlFile = new File("src/test/resources/crawler/ripleyCrawler.html");
    Document document = Jsoup.parse(htmlFile, "UTF-8", "http://simple.ripley.com");
    ArrayList<Category> categories = (ArrayList<Category>) ripleyCrawler.buildCategories1(document);
    Category category = categories.get(0);
    SubCategory subCategory = category.getSubCategories().get(1);
    ArrayList<SubSubCategory> subSubCategories = (ArrayList<SubSubCategory>) subCategory
        .getSubSubCategories();
    assertEquals(subSubCategories.size(), 5);
    assertEquals(subSubCategories.get(0).getName(), "ver todo reproductores");
    assertEquals(subSubCategories.get(1).getName(), "blu-ray");
    assertEquals(subSubCategories.get(2).getName(), "dvd");
    assertEquals(subSubCategories.get(3).getName(), "accesorios de tv");
    assertEquals(subSubCategories.get(4).getName(), "complementos de tv");
  }
}