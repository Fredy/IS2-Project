package is2;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;


public interface Scraper{
	@Autowired
	private String url;
	//link
	

	public Collection<String> ParseData();
		//maybe List<Product>
	
	public Collection<String> SaveData();
	public Collection<String> GetData();
	
	
	
	

}
