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
	private static String name_complete;
	
	
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
	
	
	
	
	//Tests para selectFilmYear
	@Test
	public void happyPathSelectFilmYear() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1999)");
		result = Database.selectFilmYear(1999);
			
		ResultSet rs = statement.executeQuery("select * from films");
		Iterator<String> iterable = result.iterator();

		assertEquals("Dr. Goldfoot and the Bikini Machine", iterable.next());
		assertEquals("Disney's Mouseworks Spaceship", iterable.next());
	}
		
	@Test
	public void testForNoElementSelectFilmYear() throws SQLException {
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
	public void testForNullElementSelectFilmYear() throws SQLException {
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
	public void happyPathSelectFilmTitle() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1965)");
		db.insertFilm("Doll's House, A (1973 I)");
		film_complete = Database.selectFilmTitle("Dr. Goldfoot and the Bikini Machine");
		
		assertEquals("Dr. Goldfoot and the Bikini Machine (1965)", film_complete);
	}
		
	@Test
	public void testForNoElementSelectFilmTitle() throws SQLException {
			
		db.insertFilm("");
		film_complete = Database.selectFilmTitle("Dr. Goldfoot and the Bikini Machine");
		
		assertEquals("", film_complete);
	}
		
	@Test
	public void testForNullElementSelectFilmTitle() throws SQLException {
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
	@Test
	public void happyPathSelectActor() throws SQLException {	
		db.insertActor("Feldman, Corey");
		db.insertActor("Celis, Fernando (I)");
		db.insertActor("Eggar, Samantha");
		name_complete = Database.selectActor("Fernando (I)", "Celis");
			
		assertEquals("Celis, Fernando (I)", name_complete);
	}
	
	@Test
	public void testWrongParamOrderSelectActor() throws SQLException {	
		db.insertActor("Feldman, Corey");
		db.insertActor("Celis, Fernando (I)");
		db.insertActor("Eggar, Samantha");
		name_complete = Database.selectActor("Celis","Fernando (I)");
			
		assertEquals("", name_complete);
	}
		
	@Test
	public void testNoNameFoundSelectActor() throws SQLException {
		db.insertActor("Feldman, Corey");
		name_complete = Database.selectActor("surname", "Celis");
		
		assertEquals("", name_complete);
	}
		
	@Test
	public void testForNullElementSelectActor() throws SQLException {
				
		try {
			db.insertActor(null);
			name_complete = Database.selectActor("surname", "Celis");
			assertEquals("", name_complete);
		} catch (NullPointerException e) {
		    return;
		}
		fail ("NullPointerException expected");
	}
	
	
	
	//Test para insertFilm
	@Test
	public void happyPathInsertFilm() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1965)");
		db.insertFilm("Doll's House, A (1973 I)");
		
		ResultSet rs = statement.executeQuery("select * from films");

	    rs.next();
	    assertEquals("Disney's Mouseworks Spaceship", rs.getString("title"));
	    assertEquals("1999", rs.getString("year"));
	    rs.next();
	    assertEquals("Dr. Goldfoot and the Bikini Machine", rs.getString("title"));
	    assertEquals("1965", rs.getString("year"));
	}
	
	@Test
	public void testNoYearInsertFilm() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship");
		
		ResultSet rs = statement.executeQuery("select * from films");
	    assertEquals("Disney's Mouseworks Spaceship", rs.getString("title"));
	    assertEquals("0", rs.getString("year"));
	}
	
	@Test
	public void testForNoElementInsertFilm() throws SQLException {
		db.insertFilm("");
		
		ResultSet rs = statement.executeQuery("select * from films");
	    assertEquals("", rs.getString("title"));
	    assertEquals("0", rs.getString("year"));
	}
	
	@Test
	public void testYearAtBegginingInsertFilm() throws SQLException {
		db.insertFilm("(1999) Disney's Mouseworks Spaceship");
		
		ResultSet rs = statement.executeQuery("select * from films");
	    assertEquals("(1999) Disney's Mouseworks Spaceship", rs.getString("title"));
	    assertEquals("0", rs.getString("year"));
	}
	
	@Test
	public void testForNullElementInsertFilm() throws SQLException {
		
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
	public void happyPathInsertActor() throws SQLException {
			
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
	public void testForNoElementInsertActor() throws SQLException {
		try {
			db.insertActor("");
			ResultSet rs = statement.executeQuery("select * from actors");
	    } catch (IllegalArgumentException e) {
	       return;
	    }
	    fail ("IllegalArgumentException expected");
	}
	
	@Test
	public void testForNullElementInsertActor() throws SQLException {
		try {
			db.insertActor(null);
			ResultSet rs = statement.executeQuery("select * from actors");
	    } catch (NullPointerException e) {
	       return;
	    }
	    fail ("NullPointerException expected");
	}
	
	@Test
	public void testFilmIsInDB() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1965)");
		String film = "Dr. Goldfoot and the Bikini Machine (1965)";
		assertTrue(db.filmIsInDB(film));
	}
	
	@Test
	public void testActorIsInDB() throws SQLException {
		db.insertActor("Feldman, Corey");
		db.insertActor("Celis, Fernando (I)");
		String actor = "Feldman, Corey";
		assertTrue(db.actorIsInDB(actor));
	}
	
	@Test
	public void testFilmIsNotInDB() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1965)");
		String film = "ASDF (1965)";
		assertFalse(db.filmIsInDB(film));
	}
	
	@Test
	public void testActorIsNotInDB() throws SQLException {
		db.insertActor("Feldman, Corey");
		db.insertActor("Celis, Fernando (I)");
		String actor = "Feldman, Asdf";
		assertFalse(db.actorIsInDB(actor));
	}
	

}
