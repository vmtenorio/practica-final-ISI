package urjc.isi.practicaFinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ServeHtml {
	
	public static String serveHtml(String nameFile){
		String toReturn = "Parametro";
		File file = new File("/htmlCss/" + nameFile);
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    for(String line; (line = br.readLine()) != null; ) {
		        // process the line.
		    }
		    // line is not visible here.
		}catch (FileNotFoundException e){
			
		}catch (IOException e){
			
		}
		
		return toReturn;
	}
}
