package urjc.isi.practicaFinal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

// Grafo del test:
// A--B--C
//    \-D

public class GraphTest {
	
	Graph g;

	@Before
	public void setUp() {
		g = new Graph("resources/routes.txt", " ");
		g.addEdge("A", "B");
		g.addEdge("B", "C");
		g.addEdge("B", "D");
	}
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
