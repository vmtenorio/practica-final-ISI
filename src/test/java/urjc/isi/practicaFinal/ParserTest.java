package urjc.isi.practicaFinal;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {
	
	
	// Camino [1, 2, 3]
	@Test (expected=NullPointerException.class)
	public void testNullParam() {
		String film = null;
		Parser.getFilmTitle(film);
	}
	
	// Camino [1, 2, 4, 5]
	@Test (expected=IllegalArgumentException.class)
	public void testParamIlegalArgument() {
		String film = "101 Dalmatians";
		Parser.getFilmTitle(film);
	}
	
	// GRAFO getFilmTitle
	// Camino [1, 2, 4, 6, 8]
	@Test 
	public void testHappyPathGetFilm() {
		String film = "101 Dalmatians (2005)";
		assertEquals(Parser.getFilmTitle(film), "101 Dalmatians");
	}
	
	// GRAFO getFilmYear
	// Camino [1, 2, 4, 6, 8]
	@Test 
	public void testHappyPathGetYear() {
		String film = "101 Dalmatians (2005)";
		assertEquals(Parser.getFilmYear(film), 2005);
	}
}
