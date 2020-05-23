import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.FileNotFoundException;
import org.apache.commons.io.FileUtils;
import javax.management.openmbean.OpenDataException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

//MxBit2020
//Nothing Fancy
//Bookmarks Extract

public class Main {

	//Experimenting with Maps
	static String data;
	static HashMap<String, String> urlNameHash = new HashMap<String, String>();
	
	static ArrayList<String> urls = new ArrayList<String>();
	static ArrayList<String> names = new ArrayList<String>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String appdata = System.getenv("LOCALAPPDATA");
		String user = System.getProperty("user.home");
		String path_bookmarks;
		
		//Case OS
		
		if(System.getProperty("os.name").contains("Win")) {
			path_bookmarks = appdata + "\\Google\\Chrome\\User Data\\Default\\Bookmarks";
		}
		else if(System.getProperty("os.name").contains("Mac")) {
			path_bookmarks = user + "/Library/Application Support/Google/Chrome/Default/Bookmarks";
		}
		else if(System.getProperty("os.name").contains("Unix") || System.getProperty("os.name").contains("Linux")) {
			path_bookmarks = user + "/.config/google-chrome/Default/Bookmarks";
		}
		
		else {
			System.out.println(System.getProperty("os.name") + "Supported?\nCheck Paths");
			path_bookmarks = "/Library/Application Support/Google/Chrome/Default/Bookmarks";
		}
		
		
		
		
		//Open Bookmark File
		File bmFile= new File(path_bookmarks);
		bmFile.setWritable(false);
		
		
		//Read Data
		Scanner reader;
		try {
			reader = new Scanner(bmFile);
			while (reader.hasNextLine()) {
				data += reader.nextLine()+"\n";
			}
			reader.close();
			
		} catch (FileNotFoundException e) {
			System.out.print("No Such File - adjust Browser Path\n"); 
			e.printStackTrace();
		}
			
		//Create File
		try {
			File txt = new File("yourBookmarks");
				if (txt.createNewFile()) {
					System.out.println("File created: " + txt.getName());
					} else 
						System.out.println("File already exists.");
					
			}	catch (IOException ex) {
					System.out.println("An error occurred.");
					ex.printStackTrace();
			}
		
		//Partioning Information on Useful Cuts
		String[] dataArr = data.split("},|} ]," );
		String[] newlineData = data.split("\n");
		String name = null;
		String url  = null;
			
		
		//Cleaning Data
		for (String block: dataArr ) {
			if(block.contains("\"name\"") && !block.contains("\"folder\"")) {
				
				for (String line: block.split("\n")){
					if(line.contains("name") && block.contains("url")) {
						name = line.trim();
						names.add(line);
					}
					else if(line.contains("url") && !line.contains("\"type\"")) {
						url = line.trim();							
						urls.add(line);
						urlNameHash.put(name, url);
					}
				}
			}
		}
			

		try {
			//Write in File using UTF-8 formating
			OutputStreamWriter txtWriter = new OutputStreamWriter(new FileOutputStream("yourBookmarks", false), StandardCharsets.UTF_8);
			
			//Write Number of Bookmarks
			int count = urlNameHash.size()/2;
			String counter = count + " Bookmarks in your Chrome\n";
			System.out.println(counter);
			txtWriter.write(counter+"\n");
			
			//Output
			Set<String> keys = urlNameHash.keySet();
			for(String key: keys) {
				System.out.println(key+ "\n" + urlNameHash.get(key)+ "\n");
				txtWriter.write(key+ "\n" + urlNameHash.get(key)+ "\n\n");
			}
			txtWriter.close();
		}	catch(IOException exe) {
			exe.printStackTrace();

			}
		
	}
}

