package urjc.isi.practicaFinal;

public class Queries {
	
	public static Iterable<String> query (Database db, Graph g, String table, String object) {
		
		if(db.select(table, object) == "") {
			throw new IllegalArgumentException(object + " not found!");
		} else {
			return g.adjacentTo(object);
		}
	}
	
	public static Iterable<String> distanceQuery (Graph g, String object1, String object2) {

		PathFinder pf = new PathFinder(g, object1);
		return pf.pathTo(object2);
	}
	
	public static Iterable<String> yearQuery (Database db, int year) {
		return db.selectFilmYear(year);
	}
}
