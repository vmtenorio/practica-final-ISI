package urjc.isi.practicaFinal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class QueriesTest {
	
	private static Database db;
	Graph g;
	
	
	@Before
	public void setUp() {
		db = new Database(null);
		g = new Graph();
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testObjetoNoEncontrado () {
		String actor = null;
		String actors;
		query(db, g, actors, actor);
	}
	
}
