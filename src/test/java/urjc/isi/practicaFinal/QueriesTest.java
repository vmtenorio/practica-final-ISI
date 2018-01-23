package urjc.isi.practicaFinal;

import static org.junit.Assert.*;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.Iterable;

public class QueriesTest {

	Graph g;
	private static Database db;
	private static Connection con;
	private static Statement statement;

	@Before
	public void setUp() {
		g = new Graph();
		con = null;
		try
        {
          // create a database connection
          con = DriverManager.getConnection("jdbc:sqlite:sample.db");
          Statement statement = con.createStatement();
          db = new Database(con);
          statement.setQueryTimeout(30);  // set timeout to 30 sec.
          statement.executeUpdate("drop table if exists actors");
          statement.executeUpdate("drop table if exists films");
          statement.executeUpdate("create table actors (name string, surname string)");
          statement.executeUpdate("create table films (title string, year int)");
        }
        catch(SQLException e)
        {
          // if the error message is "out of memory",
          // it probably means no database file is found
          System.err.println(e.getMessage());
        }
	}

	@After
	public void tearDown() {
		try
        {
          if(con != null)
            con.close();
        }
        catch(SQLException e)
        {
          // connection close failed.
          System.err.println(e);
        }
	}

	@Test (expected=IllegalArgumentException.class)
	public void testFilmNoEncontrada () throws SQLException {
		String film = "HOLA		(1111)";
		db.insertFilm("101 Dalmatians (1996)");
		Queries.filmQuery(db, g, film);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testActorNoEncontrado () throws SQLException {
		String actor = "HOLA, HOLA";
		String param = "";
		db.insertActor("Sibaldi, Stefano");
		Queries.actorQuery(db, g, param, actor);
	}

	@Test
	public void testFilmHappyPath () throws SQLException {
		db.insertFilm("101 Dalmatians (1996)");
		db.insertFilm("12 Dogs of Christmas, The (2005)");
		g.addEdge("101 Dalmatians (1996)", "Braid, Hilda");
		g.addEdge("101 Dalmatians (1996)", "Laurie, Hugh");
		g.addEdge("12 Dogs of Christmas, The (2005)", "Hicks, Adam");
		String film = "101 Dalmatians (1996)";
		Iterable<String> it = Queries.filmQuery(db, g, film);
		assertEquals(it.toString(), "{ " + "Braid, Hilda" + ", " + "Laurie, Hugh" + " }");
	}

	@Test
	public void testActorHappyPath () throws SQLException {
		db.insertActor("Braid, Hilda");
		db.insertActor("Hicks, Adam");
		g.addEdge("101 Dalmatians (1996)", "Braid, Hilda");
		g.addEdge("101 Dalmatians (1996)", "Laurie, Hugh");
		g.addEdge("12 Dogs of Christmas, The (2005)", "Hicks, Adam");
		String actor = "Braid, Hilda";
		String param = "name";
		Iterable<String> it = Queries.actorQuery(db, g, param, actor);
		assertEquals(it.toString(), "{ " + "101 Dalmatians (1996)" + " }");
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testFilmNoEncontradaEnGrafo () throws SQLException {
		db.insertFilm("101 Dalmatians (1996)");
		db.insertFilm("12 Dogs of Christmas, The (2005)");
		String film = "101 Dalmatians (1996)";
		Queries.filmQuery(db, g, film);
	}
	
	@Test (expected=IllegalArgumentException.class)
	public void testActorNoEncontradoEnGrafo () throws SQLException {
		db.insertActor("Braid, Hilda");
		db.insertActor("Hicks, Adam");
		String actor = "Hilda";
		String param = "name";
		Queries.actorQuery(db, g, param, actor);
	}
}
