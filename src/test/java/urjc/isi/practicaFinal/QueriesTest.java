package urjc.isi.practicaFinal;

import org.junit.Before;
import org.junit.Test;

public class QueriesTest {
	
	private static Queries query;
	Graph g;
	String actor;
	
	@Before
	public void setUp() {
		query = new Queries();
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testObjetoNoEncontrado () {
		query.actorQuery(g, actor);
	}

}
