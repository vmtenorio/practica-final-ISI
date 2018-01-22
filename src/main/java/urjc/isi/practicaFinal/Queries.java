package urjc.isi.practicaFinal;

public class Queries {
	
	public static Iterable<String> filmQuery (Database db, Graph g, String film) {
		
		if(db.selectFilmTitle(film) == "") {
			throw new IllegalArgumentException("Film " + film + " not found!");
		} else {
			return g.adjacentTo(film);
		}
	}
	
	public static Iterable<String> actorQuery (Database db, Graph g, String param, String actor) {
		
		if(db.selectActor(param, actor) == "") {
			throw new IllegalArgumentException("Actor " + actor + " not found!");
		} else {
			return g.adjacentTo(actor);
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
