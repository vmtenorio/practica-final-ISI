package urjc.isi.practicaFinal;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {
	
	
	// GRAFO getFilmTitle
	// Camino [1, 2, 3]
	@Test (expected=NullPointerException.class)
	public void testNullTitle() {
		String film = null;
		Parser.getFilmTitle(film);
	}

	// Camino [1, 2, 4, 5]
	@Test (expected=IllegalArgumentException.class)
	public void testTitleIlegalArgument() {
		String film = "101 Dalmatians";
		Parser.getFilmTitle(film);
	}
	
	// Camino [1, 2, 4, 6, 8]
	@Test 
	public void testHappyPathGetFilm() {
		String film = "101 Dalmatians (2005)";
		assertEquals(Parser.getFilmTitle(film), "101 Dalmatians");
	}
	
	// GRAFO getFilmYear
	// Camino [1, 2, 3]
	@Test (expected=NullPointerException.class)
	public void testNullYear() {
		String film = null;
		Parser.getFilmYear(film);
	}

	// Camino [1, 2, 4, 5]
	@Test (expected=IllegalArgumentException.class)
	public void testYearIlegalArgument() {
		String film = "101 Dalmatians";
		Parser.getFilmYear(film);
	}
	
	// Camino [1, 2, 4, 6, 8]
	@Test 
	public void testHappyPathGetYear() {
		String film = "101 Dalmatians (2005)";
		assertEquals(Parser.getFilmYear(film), 2005);
	}
	
	// GRAFO getActorName
	// Camino [1, 2, 3]
	@Test (expected=NullPointerException.class)
	public void testNullActorName() {
		String actor = null;
		Parser.getActorName(actor);
	}
	
	// Camino [1, 2, 4, 5]
	@Test (expected=IllegalArgumentException.class)
	public void testActorNameIlegalArgument() {
		String actor = "Braid";
		Parser.getActorName(actor);
	}
	
	// Camino [1, 2, 4, 6, 8]
	@Test 
	public void testHappyPathGetActorName() {
		String actor = "Braid, Hilda";
		assertEquals(Parser.getActorName(actor), "Hilda");
	}
	
	// GRAFO getActorSurname
	// Camino [1, 2, 3]
	@Test (expected=NullPointerException.class)
	public void testNullActorSurname() {
		String actor = null;
		Parser.getActorSurname(actor);
	}
	
	// Camino [1, 2, 4, 5]
		@Test (expected=IllegalArgumentException.class)
		public void testActorSurnameIlegalArgument() {
			String actor = "Braid";
			Parser.getActorSurname(actor);
		}
	
	// Camino [1, 2, 4, 6, 8]
	@Test 
	public void testHappyPathGetActorSurname() {
		String actor = "Braid, Hilda";
		assertEquals(Parser.getActorSurname(actor), "Braid");
	}
}
