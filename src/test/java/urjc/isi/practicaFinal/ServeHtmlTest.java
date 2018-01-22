package urjc.isi.practicaFinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

public class ServeHtmlTest {

	String toInsert;
	String l1;
	String l2;
	ArrayList<String> testString;
	
	@Mock FileReader FR;
	
	@Mock BufferedReader BR;
	
	@Before
	public void setUp() {
		toInsert = "Pepe";
		l1 = "Prueba";
		l2 = "<!-- insertarqui -->";
		
		testString = new ArrayList<String>();
		testString.add(l1);
		testString.add(l2);
		testString.add(toInsert);
	}
	
	@Test 
	public void testFileNotExist(){
		String nameFile = "NotExist";
		String expectedReturn = "File: " + "/htmlCss/" + nameFile + " NotFound";
		assertEquals(expectedReturn, ServeHtml.serveHtml(ServeHtml.makeFile(nameFile), toInsert));
	}
	
	@Test
	public void testLoop(){
		File fileTest = mock(File.class);
		String expectedReturn = l1 + l2 + toInsert;
		
		try {
			when(BR.readLine()).thenReturn(l1).thenReturn(l2);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		assertEquals(expectedReturn, ServeHtml.serveHtml(fileTest, expectedReturn));	
	}
		
	@Test
	public void testServeActor() {
		String expectedReturn = "<ul>\n" + "<li>" + l1 + "</li>\n<li>" + l2 + "</li>\n<li>"
								+ toInsert + "</li>\n</ul>\n";
		assertEquals(expectedReturn, ServeHtml.parseActorHtml(testString));
	}

}