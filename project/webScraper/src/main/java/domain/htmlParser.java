package domain;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class htmlParser {
  public static void main(String args[]){
    Document document;
    try 
    {
  	  document = Jsoup.connect("http://tutorialspointexamples.com/").get();
  	  String title = document.title();
	    System.out.println("Title: " + title);
    }
    catch (IOException e)
    {
	    e.printStackTrace();
    }		
  }
}
