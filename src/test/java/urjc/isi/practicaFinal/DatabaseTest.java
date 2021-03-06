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
	      db = new Database(connection);
	      statement = db.getStatement();
	      statement.setQueryTimeout(30);  // set timeout to 30 sec.
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

	/*
	 * Test para FilmIsIn
	 */
	
	// Camino [1,2]
	@Test (expected=NullPointerException.class)
	public void testFilmNullIsInDB() throws SQLException {
		db.filmIsInDB(null);
		
	}
	
	// Camino [1,4,5,7,8,9], [8,9,8], [9,8,10]
	@Test
	public void testFilmIsInDB() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1965)");
		String film = "Dr. Goldfoot and the Bikini Machine (1965)";
		assertTrue(db.filmIsInDB(film));
	}
	
	@Test 
	public void testFilmIsNotInDB() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1965)");
		String film = "ASDF (1965)";
		assertFalse(db.filmIsInDB(film));
	}
	
	// Camino [1,3,5,7,8,9]
	@Test
	public void testFilmIncorrectName() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship");
		String film = "Disney's Mouseworks Spaceship";
		assertTrue(db.filmIsInDB(film));
	}
	
	//Camino [9,8,9]
	@Test
	public void testFilmTwoTimes() throws SQLException {
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1965)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1965)");
		String film = "Dr. Goldfoot and the Bikini Machine (1965)";
		assertTrue(db.filmIsInDB(film));
	}
	
	/*
	 * Test para ActorIsIn
	 */
	
	// Camino [1,2]
	@Test (expected=NullPointerException.class)
	public void testActorNullIsInDB() throws SQLException {
		db.actorIsInDB(null);
		
	}
	
	// Camino [1,4,5,7,8,9], [8,9,8], [9,8,10]
	@Test
	public void testActorIsInDB() throws SQLException {
		db.insertActor("Feldman, Corey");
		db.insertActor("Celis, Fernando (I)");
		String actor = "Feldman, Corey";
		assertTrue(db.actorIsInDB(actor));
	}
		

	@Test
	public void testActorIsNotInDB() throws SQLException {
		db.insertActor("Feldman, Corey");
		db.insertActor("Celis, Fernando (I)");
		String actor = "Feldman, Asdf";
		assertFalse(db.actorIsInDB(actor));
	}
	
	// Camino [1,3,5,7,8,9]
	@Test
	public void testActorIncorrectName() throws SQLException {
		db.insertActor("Pepe Perez");
		String actor = "Pepe Perez";
		assertTrue(db.actorIsInDB(actor));
	}
	
	//Camino [9,8,9]
	@Test
	public void testActorTwoTimes() throws SQLException {
		db.insertActor("Perez, Pepe");
		db.insertActor("Perez, Pepe");
		String actor = "Perez, Pepe";
		assertTrue(db.actorIsInDB(actor));
	}
	
	/*
	 * Tests para selectFilmYear
	 */
		
	// Camino [1 2 4 6 8] [6 7 6] [7 6 8]
	@Test
	public void happyPathSelectFilmYear() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1999)");
		result = db.selectFilmYear(1999);
				
		ResultSet rs = statement.executeQuery("select * from films");
		Iterator<String> iterable = result.iterator();
			
		assertEquals("Dr. Goldfoot and the Bikini Machine", iterable.next());
		assertEquals("Disney's Mouseworks Spaceship", iterable.next());
	}
	
	// Camino para [7 6 7]
	@Test
	public void SelectFilmYearTwice() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		result = db.selectFilmYear(1999);
				
		ResultSet rs = statement.executeQuery("select * from films");
		Iterator<String> iterable = result.iterator();
		
		assertEquals("Disney's Mouseworks Spaceship", iterable.next());
		assertEquals("Disney's Mouseworks Spaceship", iterable.next());
	}
		
	// Camino [1 2 4 6]
	@Test(expected=NullPointerException.class)
	public void testForNullElementSelectFilmYear() throws SQLException {
		db.insertFilm(null);
		result = db.selectFilmYear(1999);
		ResultSet rs = statement.executeQuery("select * from actors");
	}
		
	@Test
	public void testForNoElementSelectFilmYear() throws SQLException {
		try {
			db.insertFilm("");
			result = db.selectFilmYear(1999);
				
			ResultSet rs = statement.executeQuery("select * from films");
			Iterator<String> iterable = result.iterator();

			assertEquals("", iterable.next());
		 } catch (NoSuchElementException e) {
		    return;
		 }
		 fail ("NoSuchElementException expected");
	}
	
	/*
	 * Test para SelectFilmTitle 
	 */
	
	// Camino [1 2 4 6 8] [6 7 6] [7 6 8]
	@Test
	public void happyPathSelectFilmTitle() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Dr. Goldfoot and the Bikini Machine (1965)");
		db.insertFilm("Doll's House, A (1973 I)");
		film_complete = db.selectFilmTitle("Dr. Goldfoot and the Bikini Machine");
		
		assertEquals("Dr. Goldfoot and the Bikini Machine (1965)", film_complete);
	}
	
	// Camino para [7 6 7]
	@Test
	public void SelectFilmTitleTwice() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		db.insertFilm("Disney's Mouseworks Spaceship (1999)");
		String resultFilm = db.selectFilmTitle("Disney's Mouseworks Spaceship");
		String expectedReturn = "Disney's Mouseworks Spaceship (1999)";
		
		assertEquals(expectedReturn + expectedReturn, resultFilm);
	}
	
	// Camino [1 2 4 6]		
	@Test
	public void testForNoElementSelectFilmTitle() throws SQLException {
			
		db.insertFilm("");
		film_complete = db.selectFilmTitle("Dr. Goldfoot and the Bikini Machine");
		
		assertEquals("", film_complete);
	}
	
	// Camino [1 5]
	@Test(expected=NullPointerException.class)
	public void testForNullElementSelectFilmTitle() throws SQLException {
		db.insertFilm("Citizen Kane(1941)");
		film_complete = db.selectFilmTitle(null);
	}	
		
	/*
	Test para selectActor
	 */
	
	
	// Camino [1 2 4 6 7 8 10 11 6 12] Este camino no es principal pero engloba varios,
	// Los que pasan por el nodo 10 pero solo dan una vuelta al bucle
	@Test 
	public void happyPathSelectActor() throws SQLException {	
		db.insertActor("Feldman, Corey");
		db.insertActor("Celis, Fernando (I)");
		db.insertActor("Eggar, Samantha");
		name_complete = db.selectActor("Fernando (I)", "Celis");
			
		assertEquals("Celis, Fernando (I)", name_complete);
	}
	
	// Camino [1 2 4 6 7 8 10 11 6 7 8 10 11 6 12] Este camino no es principal 
	// pero engloba varios los que pasan por el nodo 10 y que dan dos vueltas al búcle
	@Test 
	public void twoLoopsSelectActor() throws SQLException {	
		db.insertActor("Celis, Fernando (I)");
		db.insertActor("Celis, Fernando (I)");
		db.insertActor("Eggar, Samantha");
		name_complete = db.selectActor("Fernando (I)", "Celis");
			
		assertEquals("Celis, Fernando (I)"+"Celis, Fernando (I)", name_complete);
	}
	
	// Camino [1 2 4 6 7 8 9 11 6 7 8 9 11 6 12] No es camino principal
	// pero engloba los que pasan por el nodo 9 y dan dos vueltas al bucle
	@Test
	public void twoLoopsWrongNameActor() throws SQLException {
		db.insertActor("Corey Feldman");
		db.insertActor("Corey Feldman");
		name_complete = db.selectActor("Corey Feldman", "");
		
		assertEquals("Corey Feldman" + "Corey Feldman", name_complete);
	}
	
	//Camino Principal [1 2 4 6 7 8 9 11] [6 7 8 9 11 6] [7 8 9 11 6 12]
	@Test
	public void testNoNameFoundSelectActor() throws SQLException {
		db.insertActor("Feldman, Corey");
		name_complete = db.selectActor("surname", "Celis");
		
		assertEquals("", name_complete);
	}
	
	@Test
	public void testWrongParamOrderSelectActor() throws SQLException {	
		db.insertActor("Feldman, Corey");
		db.insertActor("Celis, Fernando (I)");
		name_complete = db.selectActor("Celis","Fernando (I)");
			
		assertEquals("", name_complete);
	}
		
	//Camino [1 5]
	@Test(expected=NullPointerException.class)
	public void testForNullElementSelectActor() throws SQLException {		
		db.insertActor("Feldman, Corey");
		name_complete = db.selectActor(null, null);
	}

	/*
	 * Test para InsertFilm
	 */
	
	// Camino [1 2 5 7]
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
	
	// Camino [1 2 4 5 7]
	@Test
	public void testNoYearInsertFilm() throws SQLException {
		db.insertFilm("Disney's Mouseworks Spaceship");
		
		ResultSet rs = statement.executeQuery("select * from films");
	    assertEquals("Disney's Mouseworks Spaceship", rs.getString("title"));
	    assertEquals("0", rs.getString("year"));
	}
	
	// Caso limite de la entrada
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
	
	// Camino [1 2 3]
	@Test (expected=NullPointerException.class)
	public void testForNullElementInsertFilm() throws SQLException {
	    db.insertFilm(null);
	}
	
	/*
	 * Test para insertActor
	 */
	
	// Camino [1 2 5 7]
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
	
	// Camino [1 2 4 5 7]
	@Test
	public void testForNoElementInsertActor() throws SQLException {
		db.insertActor("Corey Feldman");
		ResultSet rs = statement.executeQuery("select * from actors");
		
		rs.next();
		assertEquals("Corey Feldman", rs.getString("name"));
		assertEquals("", rs.getString("surname"));
	}
	
	// Camino [1 2 3]
	@Test (expected=NullPointerException.class)
	public void testForNullElementInsertActor() throws SQLException {
		db.insertActor(null);
	}
			
	
}
