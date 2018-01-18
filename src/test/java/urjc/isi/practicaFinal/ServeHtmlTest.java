package urjc.isi.practicaFinal;

import java.io.File;
import java.io.FileNotFoundException;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ServeHtmlTest {

	String toInsert;

	@Before
	public void setUp() {
		String toInsert = "Pepe";
	}
	
	@Test 
	public void testFileNotExist(){
		String nameFile = "NotExist";
		String expectedReturn = "File: " + "/htmlCss/" + nameFile + " NotFound";
		assertEquals(expectedReturn, ServeHtml.serveHtml(ServeHtml.makeFile(nameFile), toInsert));
	}
	
	@Test
	public void testLoop() {
		
	}
	
	
}