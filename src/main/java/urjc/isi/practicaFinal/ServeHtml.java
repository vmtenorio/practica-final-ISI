package urjc.isi.practicaFinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ServeHtml {
	
	public static String serveHtml(String nameFile, String toInsert){
		File file = new File("htmlCss/" + nameFile);
		String toReturn = "";
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        toReturn  += line + "\n";
		        if (line == "<!-- insertarqui -->") {
		        	toReturn += toInsert;
		        }
		    }
		}catch (FileNotFoundException e){
			toReturn += "File: " + "/htmlCss/" + nameFile + " NotFound";
			System.out.println(toReturn);
		}catch (IOException e){
			System.out.println("IO");
			toReturn += "FileNotFound";
		}
		
		return toReturn;
	}
}
