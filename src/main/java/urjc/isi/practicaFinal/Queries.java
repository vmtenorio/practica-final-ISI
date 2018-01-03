package urjc.isi.practicaFinal;

public class Queries {
	
	public static String filmQuery (Graph g, String film) {
		String result = new String();
		if (!g.hasVertex(film)) {
			result = "Film not found!";
		} else {
			for(String s: g.adjacentTo(film)) {
				result += s;
			}
		}
		System.out.println(result);
		return result;
	}
	
	public static String actorQuery (Graph g, String actor) {
		String result = new String();
		if (!g.hasVertex(actor)) {
			result = "Actor not found!";
		} else {
			for(String s: g.adjacentTo(actor)) {
				result += s;
			}
		}
		return result;
	}
}
