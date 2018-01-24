package urjc.isi.practicaFinal;

import static org.junit.Assert.*;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.Iterable;

public class QueriesTest {

	Graph g;
	private static Database db;
	private static Connection con;

	@Before
	public void setUp() {
		g = new Graph();
		con = null;
		try
        {
          // create a database connection
          con = DriverManager.getConnection("jdbc:sqlite:sample.db");
          db = new Database(con);
          Statement statement = db.getStatement();
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

	@Test (expected=NoSuchFieldException.class)
	public void testFilmNull () throws SQLException, NoSuchFieldException {
		db.insertFilm("101 Dalmatians (1996)");
		db.insertFilm("12 Dogs of Christmas, The (2005)");
		String film = null;
		Queries.filmQuery(db, g, film);
	}

	@Test (expected=NoSuchFieldException.class)
	public void testNameActorNull() throws SQLException, NoSuchFieldException {
		db.insertActor("Braid, Hilda");
		db.insertActor("Hicks, Adam");
		String name = null;
		String surname = "Braid";
		Queries.actorQuery(db, g, name, surname);
	}

	@Test (expected=NoSuchFieldException.class)
	public void testSurnameActorNull() throws SQLException, NoSuchFieldException {
		db.insertActor("Braid, Hilda");
		db.insertActor("Hicks, Adam");
		String name = "Hilda";
		String surname = null;
		Queries.actorQuery(db, g, name, surname);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testFilmNoEncontradaEnGrafo () throws SQLException, NoSuchFieldException {
		db.insertFilm("101 Dalmatians (1996)");
		db.insertFilm("12 Dogs of Christmas, The (2005)");
		String film = "101 Dalmatians";
		Queries.filmQuery(db, g, film);
	}

	@Test (expected=IllegalArgumentException.class)
	public void testActorNoEncontradoEnGrafo () throws SQLException, NoSuchFieldException {
		db.insertActor("Braid, Hilda");
		db.insertActor("Hicks, Adam");
		String name = "Hilda";
		String surname = "Braid";
		Queries.actorQuery(db, g, name, surname);
	}

	@Test (expected=NoSuchFieldException.class)
	public void testActorChangeName () throws SQLException, NoSuchFieldException {
		db.insertActor("Braid, Hilda");
		g.addEdge("101 Dalmatians (1996)", "Braid, Hilda");
		String name = "Braid";
		String surname = "Hilda";
		Queries.actorQuery(db, g, name, surname);
	}

	@Test (expected=NoSuchFieldException.class)
	public void testFilmWithYear () throws SQLException, NoSuchFieldException {
		db.insertFilm("12 Dogs of Christmas, The (2005)");
		g.addEdge("12 Dogs of Christmas, The (2005)", "Hicks, Adam");
		String film = "12 Dogs of Christmas, The (2005)";
		Queries.filmQuery(db, g, film);
	}
	
	
	
	//Caminos: Grafo Queries
	
	//filmQuery
	//Camino [1, 2]
	@Test (expected=SQLException.class)
	public void testFilmSQLException () throws SQLException, NoSuchFieldException {
		con = DriverManager.getConnection(null);
        db = new Database(con);
        Statement statement = db.getStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        statement.executeUpdate("drop table if exists actors");
		db.insertFilm("12 Dogs of Christmas, The (2005)");
		g.addEdge("12 Dogs of Christmas, The (2005)", "Hicks, Adam");
		String film = "12 Dogs of Christmas, The (2005)";
		Queries.filmQuery(db, g, film);
	}
	
	//Camino [1, 3, 4]
	@Test (expected=NoSuchFieldException.class)
	public void testFilmNoEncontrada () throws SQLException, NoSuchFieldException {
		String film = "HOLA		(1111)";
		db.insertFilm("101 Dalmatians");
		Queries.filmQuery(db, g, film);
	}
	
	//Camino [1, 3, 5, 6, 8]
	@Test
	public void testFilmHappyPath () throws SQLException, NoSuchFieldException {
		db.insertFilm("101 Dalmatians (1996)");
		db.insertFilm("12 Dogs of Christmas, The (2005)");
		g.addEdge("101 Dalmatians (1996)", "Braid, Hilda");
		g.addEdge("101 Dalmatians (1996)", "Laurie, Hugh");
		g.addEdge("12 Dogs of Christmas, The (2005)", "Hicks, Adam");
		String film = "101 Dalmatians";
		Iterable<String> it = Queries.filmQuery(db, g, film);
		assertEquals(it.toString(), "{ " + "Braid, Hilda" + ", " + "Laurie, Hugh" + " }");
	}
	
	
	//actorQuery
	//Camino [1, 2]
	@Test (expected=SQLException.class)
	public void testActorSQLException () throws SQLException, NoSuchFieldException {
		con = DriverManager.getConnection(null);
        db = new Database(con);
        Statement statement = db.getStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        statement.executeUpdate("drop table if exists actors");
        g.addEdge("101 Dalmatians (1996)", "Braid, Hilda");
        String name = "Hilda";
		String surname = "Braid";
		db.insertActor("Braid, Hilda");
		Queries.actorQuery(db, g, name, surname);
	}
	
	//Camino [1, 3, 4]
	@Test (expected=NoSuchFieldException.class)
	public void testActorNoEncontrado () throws SQLException, NoSuchFieldException {
		String name = "Hilda";
		String surname = "Braid";
		db.insertActor("Sibaldi, Stefano");
		Queries.actorQuery(db, g, name, surname);
	}
	
	//Camino [1, 3, 5, 6, 8]
	@Test
	public void testActorHappyPath () throws SQLException, NoSuchFieldException {
		db.insertActor("Braid, Hilda");
		db.insertActor("Hicks, Adam");
		g.addEdge("101 Dalmatians (1996)", "Braid, Hilda");
		g.addEdge("101 Dalmatians (1996)", "Laurie, Hugh");
		g.addEdge("12 Dogs of Christmas, The (2005)", "Hicks, Adam");
		String name = "Hilda";
		String surname = "Braid";
		Iterable<String> it = Queries.actorQuery(db, g, name, surname);
		assertEquals(it.toString(), "{ " + "101 Dalmatians (1996)" + " }");
	}
	
	
	//distanceQuery
	//Camino [1, 2, 3]
	@Test (expected=SQLException.class)
	public void testDistanceSQLException () throws SQLException, NoSuchFieldException {
		con = DriverManager.getConnection(null);
        db = new Database(con);
        Statement statement = db.getStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        statement.executeUpdate("drop table if exists actors");
        db.insertActor("Braid, Hilda");
		db.insertActor("Hicks, Adam");
		db.insertActor("Laurie, Hugh");
		g.addEdge("101 Dalmatians (1996)", "Braid, Hilda");
		g.addEdge("101 Dalmatians (1996)", "Laurie, Hugh");
		g.addEdge("12 Dogs of Christmas, The (2005)", "Hicks, Adam");
		g.addEdge("12 Dogs of Christmas, The (2005)", "Braid, Hilda");
		String object1 = "Laurie, Hugh";
		String object2 = "Hicks, Adam";
		assertEquals(Queries.distanceQuery (db, g, object1, object2).distanceTo(object2), 4);
	}
	
	//Camino [1, 2, 4, 6]
	@Test
	public void testDistanceHappyPath () throws SQLException, NoSuchFieldException {
		db.insertActor("Braid, Hilda");
		db.insertActor("Hicks, Adam");
		db.insertActor("Laurie, Hugh");
		g.addEdge("101 Dalmatians (1996)", "Braid, Hilda");
		g.addEdge("101 Dalmatians (1996)", "Laurie, Hugh");
		g.addEdge("12 Dogs of Christmas, The (2005)", "Hicks, Adam");
		g.addEdge("12 Dogs of Christmas, The (2005)", "Braid, Hilda");
		String object1 = "Laurie, Hugh";
		String object2 = "Hicks, Adam";
		assertEquals(Queries.distanceQuery (db, g, object1, object2).distanceTo(object2), 4);
	}
	
	//Camino [1, 2, 4, 5]
	@Test (expected=NullPointerException.class)
	public void testDistanceNullGraph () throws SQLException, NoSuchFieldException {
		db.insertActor("Braid, Hilda");
		db.insertActor("Hicks, Adam");
		db.insertActor("Laurie, Hugh");
		g=null;
		String object1 = "Laurie, Hugh";
		String object2 = "Hicks, Adam";
		assertEquals(Queries.distanceQuery (db, g, object1, object2).distanceTo(object2), 4);
	}		
	
	//Camino [1, 7, 8]
	@Test (expected=NoSuchFieldException.class)
	public void testDistanceNoSuchField () throws SQLException, NoSuchFieldException {
		db.insertActor("Laurie, Hugh");
		String object1 = "Laurie, Hugh";
		String object2 = "Hicks, Adam";
		Queries.distanceQuery (db, g, object1, object2);
	}
	
	
	//yearQuery
	//Camino [1, 2]
	@Test (expected=SQLException.class)
	public void testYearSQLException () throws SQLException, NoSuchFieldException {
		con = DriverManager.getConnection(null);
        db = new Database(con);
        Statement statement = db.getStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.
        statement.executeUpdate("drop table if exists actors");
		db.insertFilm("12 Dogs of Christmas, The (2005)");
		//g.addEdge("12 Dogs of Christmas, The (2005)", "Hicks, Adam");
		int year = 2005;
		Queries.yearQuery(db, year);
	}
	
	//Camino [1, 3]
	@Test 
	public void testYearHappyPath () throws SQLException, NoSuchFieldException {
		db.insertFilm("12 Dogs of Christmas, The (2005)");
		int year = 2005;
		assertEquals(Queries.yearQuery(db, year).toString(), "12 Dogs of Christmas, The ");
	}	
	
	
	
}
