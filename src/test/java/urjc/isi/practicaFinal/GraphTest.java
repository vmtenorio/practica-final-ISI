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
		g = new Graph();
		g.addEdge("A", "B");
		g.addEdge("B", "C");
		g.addEdge("B", "D");
	}
	
	@Test
	public void testVertices() {
		assertEquals("Llamada a vertices", g.vertices().toString(), "[A, B, C, D]");
		assertEquals("Comprobamos numero de vertices", g.V(), 4);
	}

}
