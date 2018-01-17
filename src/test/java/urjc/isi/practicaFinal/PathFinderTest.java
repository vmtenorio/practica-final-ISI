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
		g = new Graph("routes.txt", " ");
		pf = new PathFinder(g, "ORD");
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
