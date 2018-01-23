package urjc.isi.practicaFinal;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;

public class ServeHtml {
	
	static File makeFile(String fileName) {
    	return new File("htmlCss/" + fileName);
    }
	
	public static String readInsert(BufferedReader br, String toInsert) {
		String toReturn = "";
		try{
		    for(String line; (line = br.readLine()) != null; ) {
		    	toReturn  += line + "\n";
		    	System.out.println(line);
		        if (line.contains("<!-- insertarqui -->")) {
		        	System.out.println("Entra if insertar aqui");
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
	
	public static byte[] imageToBytes(String ImageName) throws IOException{
		BufferedImage image = ImageIO.read(makeFile(ImageName)); 
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    ImageIO.write(image, "jpg", baos);
	    
	    return baos.toByteArray();	
	}

	public static String parseIterable(Iterable<String> stringIterator) {
		String toReturn = "<ul>\n";
		for(String string : stringIterator) {
			toReturn += "<li>" + string + "</li>\n"; 
		}
		toReturn += "</ul>\n";
		return toReturn;
	}
}
