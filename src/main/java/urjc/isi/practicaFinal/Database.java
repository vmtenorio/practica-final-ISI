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
	
	public String select(String table, String film) {
		String sql = "SELECT * FROM " + table + " WHERE film=?";

		String result = new String();
		
		try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, film);
			ResultSet rs = pstmt.executeQuery();
	                // Commit after query is executed
			conn.commit();

			while (rs.next()) {
			    // read the result set
			    result += "film = " + rs.getString("film") + "\n";
			    System.out.println("film = "+rs.getString("film") + "\n");

			    result += "actor = " + rs.getString("actor") + "\n";
			    System.out.println("actor = "+rs.getString("actor")+"\n");
			}
		    } catch (SQLException e) {
		    System.out.println(e.getMessage());
		}
		return result;
	}
	
	public static void insertFilm(String film) {
		String title;
		int year;
		
		String sql = "INSERT INTO films(title, year) VALUES(?,?)";

		Pattern p = Pattern.compile(".*(\\d{4})");
		Matcher m = p.matcher(film);
		 		
		if (m.matches()) {
			int yearIndex = film.lastIndexOf(' ');
			title = film.substring(0, yearIndex);
			String yearStr = film.substring(yearIndex+2);						//Nos quitamos el primer paréntesis
			year = Integer.parseInt(yearStr.substring(0, yearStr.length()-1));
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
	
	public static void insertActor(String actor) {
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
