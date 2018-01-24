package urjc.isi.practicaFinal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {
	
	static Connection conn;

	public Database(Connection conn) {
		this.conn = conn;
	}
	
	public Statement getStatement() throws SQLException{
		return conn.createStatement();
	}
	
	public static boolean filmIsInDB(String film) {
		
		String title;
		try {
			title = Parser.getFilmTitle(film);
		} catch (IllegalArgumentException e) {
			title = film;
		}
		String sql = "SELECT * FROM films WHERE title=?";

		String result = new String();
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, title);
			ResultSet rs = pstmt.executeQuery();
	                // Commit after query is executed
			//conn.commit();
			while(rs.next()) {
				result += rs.getString("title") + " (" + rs.getInt("year") + ")";
			}
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
		return !result.equals("");
	}
	
	public static boolean actorIsInDB(String actor) {
		
		String name, surname;
		try {
			name = Parser.getActorName(actor);
			surname = Parser.getActorSurname(actor);
		}catch (IllegalArgumentException e) {
			name = actor;
			surname = "";
		}
		String sql = "SELECT * FROM actors WHERE name=? AND surname=?";
		
		String result = new String();
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, surname);
			ResultSet rs = pstmt.executeQuery();
	                // Commit after query is executed
			//conn.commit();

			while(rs.next()) {
				result += rs.getString("surname") + ", " + rs.getString("name");
			}
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
		return !result.equals("");
	}
	
	//Devuelve las peliculas de un año en forma de iterable	
	public static Iterable<String> selectFilmYear (int year) {
		String sql = "SELECT * FROM films WHERE year=?";
		Stack<String> films = new Stack<String>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, year);
			ResultSet rs = pstmt.executeQuery();
	                // Commit after query is executed
			//conn.commit();
			while(rs.next()) {
				films.push(rs.getString("title"));
			}
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
		return films;
	}
	
	public static String selectFilmTitle(String film) {
		String sql = "SELECT * FROM films WHERE title=?";

		String result = new String();
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, film);
			ResultSet rs = pstmt.executeQuery();
	                // Commit after query is executed
			//conn.commit();
			while(rs.next()) {
				result += rs.getString("title") + " (" + rs.getInt("year") + ")";
			}
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
		return result;
	}
	
	public static String selectActor(String name, String surname) {
		String sql = "SELECT * FROM actors WHERE name=? AND surname=?";
		
		String result = new String();
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, surname);
			ResultSet rs = pstmt.executeQuery();
	                // Commit after query is executed
			//conn.commit();

			while(rs.next()) {
				String surnameDb = rs.getString("surname");
				String nameDb = rs.getString("name");
				if(surname.equals("")) {
					result += nameDb;
				}else {
					result += surnameDb + ", " + nameDb;
				}
			}
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
		return result;
	}
	
	public void insertFilm(String film) {
		String title;
		int year;
		
		String sql = "INSERT INTO films(title, year) VALUES(?,?)";
		 		
		try {
			title = Parser.getFilmTitle(film);
			year = Parser.getFilmYear(film);
		} catch (IllegalArgumentException e) {
			title = film;
			year = 0;
		}
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, title);
			pstmt.setInt(2, year);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
    }
	
	public void insertActor(String actor) {
		String name, surname;
		String sql = "INSERT INTO actors(name, surname) VALUES(?,?)";
		try {
			name = Parser.getActorName(actor);
			surname = Parser.getActorSurname(actor);
		}catch (IllegalArgumentException e) {
			name = actor;
			surname = "";
		}
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, surname);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
    }

}