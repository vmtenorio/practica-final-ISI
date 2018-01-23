package urjc.isi.practicaFinal;

import static org.junit.Assert.*;
import java.sql.Connection;
import java.sql.DriverManager;

import org.junit.Test;
import org.junit.Before;
import org.junit.*;
import java.util.*;
import java.lang.Iterable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class DatabaseTest {
	
	private static Database db;
	private static Statement statement;
	private static Connection connection;
	private static Iterable<String> result;
	private static String film_complete;
	
	
	@Before
	public void SetUp() {
		
		connection = null;
		result = null;
		
	    try
	    {
	      connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
	      statement = connection.createStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
	      db = new Database(connection);
	      statement.executeUpdate("drop table if exists films");
		  statement.executeUpdate("create table films (title text, year int)");
			
		  statement.executeUpdate("drop table if exists actors");
		  statement.executeUpdate("create table actors (name text, surname text)");
	    }
	    catch(SQLException e)
	    {
	      // if the error message is "out of memory", 
	      // it probably means no database file is found
	      System.err.println(e.getMessage());
	    }
	}
	
	@After 
	public void tearDown()
	{
		try
        {
			if(connection != null)
				connection.close();
        }
        catch(SQLException e)
        {
            // connection close failed.
            System.err.println(e);
        }
	}
	
	
	
	//Test para insertFilm
	@Test
	public void test1() throws SQLException {
		
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1965)");
		db.insertFilm("Doll's House, A (1973 I)");
		
		ResultSet rs = statement.executeQuery("select * from films");
	    /*while(rs.next())
	    {
	      // read the result set
	      System.out.println("title = " + rs.getString("title"));
	      System.out.println("year = " + rs.getInt("year") + "\n");
	    }*/
	    rs.next();
	    assertEquals("Disney's Mouseworks Spaceship", rs.getString("title"));
	    assertEquals("1999", rs.getString("year"));
	    rs.next();
	    assertEquals("Dr. Goldfoot and the Bikini Machine", rs.getString("title"));
	    assertEquals("1965", rs.getString("year"));
	}
	
	@Test
	public void test2() throws SQLException {
		
		db.insertFilm("Disney's Mouseworks Spaceship");
		
		ResultSet rs = statement.executeQuery("select * from films");
	    assertEquals("Disney's Mouseworks Spaceship", rs.getString("title"));
	    assertEquals("0", rs.getString("year"));
	}
	
	
	@Test
	public void test3() throws SQLException {
		db.insertFilm("");
		
		ResultSet rs = statement.executeQuery("select * from films");
	    assertEquals("", rs.getString("title"));
	    assertEquals("0", rs.getString("year"));
	}
	
	
	@Test
	public void test4() throws SQLException {
		db.insertFilm("(1999) Disney's Mouseworks Spaceship");
		
		ResultSet rs = statement.executeQuery("select * from films");
	    assertEquals("(1999) Disney's Mouseworks Spaceship", rs.getString("title"));
	    assertEquals("0", rs.getString("year"));
	}
	
	@Test
	public void test5() throws SQLException {
		
	    try {
	    	db.insertFilm(null);
			ResultSet rs = statement.executeQuery("select * from films");
	    } catch (NullPointerException e) {
	       return;
	    }
	    fail ("NullPointerException expected");
	}
	
	//Test para insertActor
	@Test
	public void test6() throws SQLException {
			
		db.insertActor("Feldman, Corey");
		db.insertActor("Celis, Fernando (I)");
		db.insertActor("Eggar, Samantha");
			
		ResultSet rs = statement.executeQuery("select * from actors");

		rs.next();
		assertEquals("Corey", rs.getString("name"));
		assertEquals("Feldman", rs.getString("surname"));
		rs.next();
		assertEquals("Fernando (I)", rs.getString("name"));
		assertEquals("Celis", rs.getString("surname"));
	}
	
	
	@Test
	public void test7() throws SQLException {
		try {
			db.insertActor("");
			ResultSet rs = statement.executeQuery("select * from actors");
	    } catch (ArrayIndexOutOfBoundsException e) {
	       return;
	    }
	    fail ("ArrayIndexOutOfBoundsException expected");
	}
	
	@Test
	public void test8() throws SQLException {
		try {
			db.insertActor(null);
			ResultSet rs = statement.executeQuery("select * from actors");
	    } catch (NullPointerException e) {
	       return;
	    }
	    fail ("NullPointerException expected");
	}
	
	
	//Test para selectFilmYear
	@Test
	public void test9() throws SQLException {
		
		
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1999)");
		result = Database.selectFilmYear(1999);
		
		ResultSet rs = statement.executeQuery("select * from films");
	    /*while(rs.next())
	    {
	      // read the result set
	      System.out.println("title = " + rs.getString("title"));
	      System.out.println("year = |" + rs.getInt("year") + "|" + "\n");
	    }*/
		
		
		/*System.out.println("PRUEBAv1:");
		for(String s: result){
			System.out.println("PRUEBAv2:");
			System.out.println(s);
		}*/
		
		Iterator<String> iterable = result.iterator();

		assertEquals("Dr. Goldfoot and the Bikini Machine", iterable.next());
		assertEquals("Disney's Mouseworks Spaceship", iterable.next());
	}
	
	@Test
	public void test10() throws SQLException {
		try {
			db.insertFilm("");
			result = Database.selectFilmYear(1999);
		
			ResultSet rs = statement.executeQuery("select * from films");
			Iterator<String> iterable = result.iterator();

			assertEquals("", iterable.next());
	    } catch (NoSuchElementException e) {
	        return;
	    }
	    fail ("NoSuchElementException expected");
	}
	
	@Test
	public void test11() throws SQLException {
		try {
			db.insertFilm(null);
			result = Database.selectFilmYear(1999);
			ResultSet rs = statement.executeQuery("select * from actors");
	    } catch (NullPointerException e) {
	        return;
	    }
	    fail ("NullPointerException expected");
	}
	
	
	//Test para selectFilmTitle
	@Test
	public void test12() throws SQLException {
			
			
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1965)");
		db.insertFilm("Doll's House, A (1973 I)");
		film_complete = Database.selectFilmTitle("Dr. Goldfoot and the Bikini Machine");
			
		//ResultSet rs = statement.executeQuery("select * from films");
	    /*while(rs.next())
	    {
	      // read the result set
	      System.out.println("title = " + rs.getString("title"));
	      System.out.println("year = |" + rs.getInt("year") + "|" + "\n");
	    }*/
		

		assertEquals("film = Dr. Goldfoot and the Bikini Machine  year = 1965\n", film_complete);
	}
	
	
	@Test
	public void test13() throws SQLException {
		
		db.insertFilm("");
		film_complete = Database.selectFilmTitle("Dr. Goldfoot and the Bikini Machine");
	
		assertEquals("", film_complete);
	}
	
	@Test
	public void test14() throws SQLException {
		try {
			db.insertFilm(null);
			film_complete = Database.selectFilmTitle("Dr. Goldfoot and the Bikini Machine");
		
			assertEquals("", film_complete);
		} catch (NullPointerException e) {
	        return;
	    }
	    fail ("NullPointerException expected");
	}
	
	
	//Test para selectActor

}
