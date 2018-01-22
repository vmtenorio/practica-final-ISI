package urjc.isi.practicaFinal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ServeHtmlTest {

	String toInsert;
	String l1;
	String l2;
	
	@Before
	public void setUp() {
		toInsert = "Pepe";
		l1 = "Prueba";
		l2 = "<!-- insertarqui -->";
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
		FileReader FR = mock(FileReader.class);
		BufferedReader BR = mock(BufferedReader.class);
		String expectedReturn = l1 + l2 + toInsert;
		
		try {
			whenNew(FileReader.class).withArguments(fileTest).thenReturn(FR);
			whenNew(BufferedReader.class).withArguments(FR).thenReturn(BR);
			when(BR.readLine()).thenReturn(l1).thenReturn(l2);
		} catch (Exception e) {
			Assert.fail();
		}
		
		assertEquals(expectedReturn, ServeHtml.serveHtml(fileTest, expectedReturn));	
	}
		
	
}