package urjc.isi.practicaFinal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
	
	/**
	 * Devuelve el título de una película
	 * @param film Película en formato "Título (año)"
	 * @return Título de la película
	 */

	public static String getFilmTitle (String film) {
		Pattern p = Pattern.compile("(.*) \\((\\d{4}).*\\)");
		Matcher m = p.matcher(film);
		 		
		if (m.matches()) {
			return m.group(1);
		} else {
			throw new IllegalArgumentException("La película no tiene el formato Título (año). Es " + film);
		}
	}
	
	/**
	 * Devuelve el año de una película
	 * @param film Película en formato "Título (año)"
	 * @return Año de la película
	 */

	public static int getFilmYear (String film) {
		Pattern p = Pattern.compile("(.*) \\((\\d{4}).*\\)");
		Matcher m = p.matcher(film);
		 		
		if (m.matches()) {
			return Integer.parseInt(m.group(2));
		} else {
			throw new IllegalArgumentException("La película no tiene el formato Título (año). Es " + film);
		}
	}
	
	/**
	 * Devuelve el nombre de un actor
	 * @param actor Actor en formato "Apellido, Nombre"
	 * @return Nombre del actor
	 */
	public static String getActorName (String actor) {
		Pattern p = Pattern.compile("(.*), (.*)");
		Matcher m = p.matcher(actor);
 		
		if (m.matches()) {
			return m.group(2);
		} else {
			throw new IllegalArgumentException("El actor no tiene el formato Apellido, Nombre. Es " + actor);
		}
	}
	
	/**
	 * Devuelve el apellido de un actor
	 * @param actor Actor en formato "Apellido, Nombre"
	 * @return Apellido del actor
	 */
	public static String getActorSurname (String actor) {
		Pattern p = Pattern.compile("(.*), (.*)");
		Matcher m = p.matcher(actor);
 		
		if (m.matches()) {
			return m.group(1);
		} else {
			throw new IllegalArgumentException("El actor no tiene el formato Apellido, Nombre. Es " + actor);
		}
	}

}
