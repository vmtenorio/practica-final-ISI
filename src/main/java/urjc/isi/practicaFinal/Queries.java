package urjc.isi.practicaFinal;

public class Queries {
	
	public static String filmQuery (Graph g, String film) {
		String result = new String();
		result = "Actors who appeared in the film " + film + ":\n";
		if (!g.hasVertex(film)) {
			result = "Film not found!";
		} else {
			for(String s: g.adjacentTo(film)) {
				result += s + "\n";
			}
		}
		System.out.println(result);
		return result;
	}
	
	public static String actorQuery (Graph g, String actor) {
		String result = new String();
		result = "Films where the actor " + actor + " appeared:\n";
		if (!g.hasVertex(actor)) {
			result = "Actor no encontrado!";
		} else {
			for(String s: g.adjacentTo(actor)) {
				result += s + "\n";
			}
		}
		return result;
	}
}
