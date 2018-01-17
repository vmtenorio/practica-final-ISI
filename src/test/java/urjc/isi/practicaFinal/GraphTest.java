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
	
	@Test
	public void testEdges() {
		assertEquals("Comprobamos numero de edges", g.E(), 3);
		assertEquals("Grado del vertice", g.degree("B"), 3);
		assertEquals("Comprobamos que devuelve bien los nexos del grafo", g.adjacentTo("B").toString(), "{ A, C, D }");
		assertTrue("hasEdge True", g.hasEdge("A", "B"));
		assertFalse("hasEdge False", g.hasEdge("A", "C"));
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testObjetoNoEncontrado () {
		g.adjacentTo("E");
	}
	
	@Test
	public void testGraphFromFile () {
		g = new Graph("resources/routes.txt", " ");
		assertEquals("Número de vértices del grafo", g.V(), 10);
		assertEquals("Numero de edges del grafo", g.E(), 16);
	}

}

