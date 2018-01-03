package urjc.isi.practicaFinal;

public class Queries {
	
	public String filmQuery (Graph g, String film) {
		String result = new String();
		if (!g.hasVertex(film)) {
			result = "Film not found!";
		} else {
			Iterable<String> actor_list;
			actor_list = g.adjacentTo(film);
			for(String s: actor_list) {
				result += s;
			}
		}
		return result;
	}
	
	public String actorQuery (Graph g, String actor) {
		String result = new String();
		if (!g.hasVertex(actor)) {
			result = "Actor not found!";
		} else {
			Iterable<String> film_list;
			film_list = g.adjacentTo(actor);
			for(String s: film_list) {
				result += s;
			}
		}
		return result;
	}
}
