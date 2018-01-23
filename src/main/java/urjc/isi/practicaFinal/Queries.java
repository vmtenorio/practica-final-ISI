package urjc.isi.practicaFinal;

public class Queries {
	
	public static Iterable<String> filmQuery (Database db, Graph g, String film) {
		
		String dbData = db.selectFilmTitle(film);
		if(dbData == "") {
			throw new IllegalArgumentException("Film " + film + " not found!");
		} else {
			return g.adjacentTo(dbData);
		}
	}
	
	public static Iterable<String> actorQuery (Database db, Graph g, String param, String actor) {
		
		String dbData = db.selectActor(param, actor);
		if(dbData == "") {
			throw new IllegalArgumentException("Actor " + actor + " not found!");
		} else {
			return g.adjacentTo(dbData);
		}
	}
	
	public static PathFinder distanceQuery (Graph g, String object1) {

		return new PathFinder(g, object1);
	}
	
	public static Iterable<String> yearQuery (Database db, int year) {
		return db.selectFilmYear(year);
	}
}
