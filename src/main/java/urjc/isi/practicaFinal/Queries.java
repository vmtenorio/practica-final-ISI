package urjc.isi.practicaFinal;

public class Queries {
	
	public static Iterable<String> filmQuery (Database db, Graph g, String film) throws NoSuchFieldException {
		
		String dbData = db.selectFilmTitle(film);
		if(dbData == "") {
			throw new NoSuchFieldException("Film " + film + " not found!");
		} else {
			return g.adjacentTo(dbData);
		}
	}
	
	public static Iterable<String> actorQuery (Database db, Graph g, String name, String surname) throws NoSuchFieldException {
		
		String dbData = db.selectActor(name, surname);
		if(dbData == "") {
			throw new NoSuchFieldException("Actor " + name + surname + " not found!");
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
