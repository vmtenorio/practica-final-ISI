package urjc.isi.practicaFinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class ServeHtml {
	
	static File makeFile(String fileName) {
    	return new File("htmlCss/" + fileName);
    }
	
	public static String readInsert(BufferedReader br, String toInsert) {
		String toReturn = "";
		try{
		    for(String line; (line = br.readLine()) != null; ) {
		    	toReturn  += line + "\n";
		        if (line == "<!-- insertarqui -->") {
		        	toReturn += toInsert;
		        }
		    }
		}catch (IOException e){
			System.out.println("IOException");
			toReturn += "IOException";
		}
		return toReturn;
	}
	
	public static String serveHtml(File file, String toInsert){
		
		String toReturn = "";
		try{
			toReturn += readInsert(new BufferedReader(new FileReader(file)),toInsert);
		}catch (FileNotFoundException e){
			toReturn += "File: " + "/htmlCss/" + file.getName() + " NotFound";
			System.out.println(toReturn);
		}
		
		return toReturn;
	}

	public static String parseActorHtml(Iterable<String> stringIterator) {
		String toReturn = "<ul>\n";
		for(String string : stringIterator) {
			toReturn += "<li>" + string + "</li>\n"; 
		}
		toReturn += "</ul>\n";
		return toReturn;
	}
}
