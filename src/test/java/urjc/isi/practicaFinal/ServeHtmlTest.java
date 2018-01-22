package urjc.isi.practicaFinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
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
	public void testreadInsert(){
		BufferedReader br = mock(BufferedReader.class);
		String expectedReturn = l1 + "\n" + l2 + "\n" + toInsert;
		try {
			when(br.readLine()).thenReturn(l1).thenReturn(l2).thenReturn(null);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		
		assertEquals(expectedReturn, ServeHtml.readInsert(br, toInsert));	
	}
	
	@Test
	public void testIOExceptionRead() throws IOException {
		String expectedReturn = "IOException";
		BufferedReader br= mock(BufferedReader.class);
		when(br.readLine()).thenThrow(IOException.class);
		assertEquals(expectedReturn, ServeHtml.readInsert(br, ""));	
	}
		
	@Test
	public void testServeActor() {
		String expectedReturn = "<ul>\n" + "<li>" + l1 + "</li>\n<li>" + l2 + "</li>\n<li>"
								+ toInsert + "</li>\n</ul>\n";
		assertEquals(expectedReturn, ServeHtml.parseActorHtml(testString));
	}

}