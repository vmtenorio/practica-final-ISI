package urjc.isi.practicaFinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ServeHtml {
	
	static File makeFile(String fileName) {
    	return new File("htmlCss/" + fileName);
    }
	
	public static String serveHtml(File file, String toInsert){
		
		String toReturn = "";
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        toReturn  += line + "\n";
		        if (line == "<!-- insertarqui -->") {
		        	toReturn += toInsert;
		        }
		    }
		}catch (FileNotFoundException e){
			toReturn += "File: " + "/htmlCss/" + file.getName() + " NotFound";
			System.out.println(toReturn);
		}catch (IOException e){
			System.out.println("IO");
			toReturn += "FileNotFound";
		}
		
		return toReturn;
	}
}
