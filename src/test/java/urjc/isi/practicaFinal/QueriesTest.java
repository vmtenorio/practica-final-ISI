package urjc.isi.practicaFinal;

import org.junit.Test;

public class QueriesTest {
	
	Graph g;
	String actor;
	
	@Test (expected=IllegalArgumentException.class)
	public void testObjetoNoEncontrado () {
		g.adjacentTo(actor);
	}

}
