package urjc.isi.practicaFinal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Database {
	
	static Connection conn;

	public Database(Connection conn) {
		this.conn = conn;
	}
	
	//Devuelve las peliculas de un año en forma de iterable	
	public Iterable<String> selectFilmYear (int year) {
		String sql = "SELECT * FROM films WHERE year=?";
		Stack<String> films = new Stack<String>();
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setInt(1, year);
			ResultSet rs = pstmt.executeQuery();
	                // Commit after query is executed
			conn.commit();
			while(rs.next()) {
				films.push(rs.getString("title"));
			}
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
		return films;
	}
	
	public String selectFilmTitle(String film) {
		String sql = "SELECT * FROM films WHERE title=?";

		String result = new String();
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, film);
			ResultSet rs = pstmt.executeQuery();
	                // Commit after query is executed
			conn.commit();

			while (rs.next()) {
			    // read the result set
			    result += "film = " + rs.getString("title") + "year: " + rs.getInt("year");
			}
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
		return result;
	}
	
	public String selectActor(String param, String actor) {
		String sql = "SELECT * FROM films WHERE " + param + "=?";
		
		String result = new String();
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, actor);
			ResultSet rs = pstmt.executeQuery();
	                // Commit after query is executed
			conn.commit();

			while (rs.next()) {
			    // read the result set
			    result += "name = " + rs.getString("name") + "surname: " + rs.getInt("surname");
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

		Pattern p = Pattern.compile("(.*) \\((\\d{4}).*\\)");
		Matcher m = p.matcher(film);
		 		
		if (m.find()) {
			title = m.group(1);
			year = Integer.parseInt(m.group(2));
		}else {
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
		String sql = "INSERT INTO films(name, surname) VALUES(?,?)";
	
		String name = actor.split(",")[1];
		String surname = actor.split(",")[0];
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, name);
			pstmt.setString(2, surname);
			pstmt.executeUpdate();
		} catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
    }

}
