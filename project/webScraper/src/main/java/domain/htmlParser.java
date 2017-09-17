package domain;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class htmlParser {

  public static void main(String args[]) {
    Document document;
    try {
      document = Jsoup.connect(
          "http://www.tottus.com.pe/tottus/browse/Electrohogar-Tecnolog%C3%ADa-Laptops/_/N-82nnyu")
          .get();
      String title = document.title();
      System.out.println("Title: " + title);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
