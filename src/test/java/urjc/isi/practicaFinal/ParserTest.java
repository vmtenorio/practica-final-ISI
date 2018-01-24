package urjc.isi.practicaFinal;

import static org.junit.Assert.*;

import org.junit.Test;

public class ParserTest {

	
	// Camino [1, 2, 3]
	@Test (expected=NullPointerException.class)
	public void testNullFilm() {
		String film = null;
		Parser.getFilmTitle(film);
	}

}
