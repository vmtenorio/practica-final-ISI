package urjc.isi.practicaFinal;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import org.junit.Test;

public class PathFinderTest {
	
	Graph g;
	PathFinder pf;
	
	@Before
	public void setUp() {
		g = new Graph("resources/routes.txt", " ");
		pf = new PathFinder(g, "ATL");
	}

	@Test
	public void testHappyPath() {
		assertEquals("Calcula mal la distancia", pf.distanceTo("PHX"), 2);
		assertEquals("Calcula mal el camino", pf.pathTo("PHX").toString(), "ATL ORD PHX ");
	}
	
	@Test
	public void testObjetoNoEncontrado() {
		assertEquals("Distancia a un objeto que no esta en el grafo", pf.distanceTo("ASDF"), Integer.MAX_VALUE);
		assertEquals("Camino a un obejeto que no esta en el grafo", pf.pathTo("ASDF").toString(), "");
	}
	
	//CAMINOS GRAFO: PATHFINDER
	
	// Camino: [1,2]
	@Test (expected=NullPointerException.class)
	public void testPathFinderNullString () {
		pf = new PathFinder(g, null);
	}
	
	// Camino: [1,2]
	@Test (expected=NullPointerException.class)
	public void testPathFinderNullGraph () {
		pf = new PathFinder(null, "ATL");
	}

	// Camino: [9,10,11,13,15,9]
	@Test 
	public void testPathFinderFor () {
		pf = new PathFinder(g, "JFK");
		assertEquals("Calcula mal el camino", pf.pathTo("DFW").toString(), "JFK ORD DFW ");
	}
	
	// Camino: [9,10,9]
	@Test 
	public void testPathFinderNotIf() {
		pf = new PathFinder(g, "JFK");
		assertEquals("Calcula mal el camino", pf.pathTo("ORD").toString(), "JFK ORD ");
	}	
}
