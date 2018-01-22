package urjc.isi.practicaFinal;

import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.MultipartConfigElement;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.URISyntaxException;
import java.net.URI;

// This code is quite dirty. Use it just as a hello world example 
// to learn how to use JDBC and SparkJava to upload a file, store 
// it in a DB, and do a SQL SELECT query
public class Main {
    
	private static Route upload = (Request request, Response response) -> { 
        response.redirect("index-1.html");
        return("Unread Code");
    };
    
    //////////////////////////////////////////////////////////////////////////////
    
    private static Connection connection;
    
    // Used to illustrate how to route requests to methods instead of
    // using lambda expressions
    public static String doSelect(Request request, Response response) {
	return select (connection, request.params(":table"), 
		       request.params(":film"));
    }

    public static String select(Connection conn, String table, String film) {
	String sql = "SELECT * FROM " + table + " WHERE film=?";

	String result = new String();
	
	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		pstmt.setString(1, film);
		ResultSet rs = pstmt.executeQuery();
                // Commit after query is executed
		connection.commit();

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
    
    
    public static void insertFilm(Connection conn, String film) {
		String sql = "INSERT INTO films(title, year) VALUES(?,?)";
		String title;
		int year;
		
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
    
    public static void insertActor(Connection conn, String actor) {
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
    //////////////////////////////////////////////////////////////////////////////
    
    
    
    
    
	public static void main(String[] args) throws 
	ClassNotFoundException, SQLException, URISyntaxException {
    	
    port(getHerokuAssignedPort());
    
    // This code only works for PostgreSQL in Heroku
 	// Connect to PostgreSQL in Heroku
 	
    URI dbUri = new URI(System.getenv("DATABASE_URL"));
 	String username = dbUri.getUserInfo().split(":")[0];
 	String password = dbUri.getUserInfo().split(":")[1];
 	String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
 	connection = DriverManager.getConnection(dbUrl, username, password);
 	
 	// PostgreSQL default is to auto-commit (1 transaction / statement execution)
         // Set it to false to improve performance
 	connection.setAutoCommit(false);
    
    
    //connection = DriverManager.getConnection("jdbc:sqlite:sample_graph.db");
    //connection.setAutoCommit(false);
    //get("/upload_films2", upload);
    
    
    // Retrieves the file uploaded through the /upload_films HTML form
 	// Creates table and stores uploaded file in a two-columns table
    Graph graph = new Graph();
 	post("/upload", (req, res) -> {
 		req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
 		String result = "File uploaded!";
 		try (InputStream input = req.raw().getPart("uploaded_films_file").getInputStream()) { 
 			// getPart needs to use the same name "uploaded_films_file" used in the form
 			
 			
 			////
 			// Prepare SQL to create table
 			Statement statement = connection.createStatement();

 			// This code only works for PostgreSQL
 			statement.executeUpdate("drop table if exists films");
 			statement.executeUpdate("create table films (title text, year int)");
 			
 			statement.executeUpdate("drop table if exists actors");
 			statement.executeUpdate("create table actors (name text, surname text)");
 			/////
 			
 			// Read contents of input stream that holds the uploaded file
 			InputStreamReader isr = new InputStreamReader(input);
 			BufferedReader br = new BufferedReader(isr);

 			String s;
 			while ((s = br.readLine()) != null) {
 				System.out.println(s);

			    // Tokenize the film name and then the actors, separated by "/"
			    StringTokenizer tokenizer = new StringTokenizer(s, "/");

			    // First token is the film name(year)
			    String film = tokenizer.nextToken();
			    insertFilm(connection, film);


			    // Now get actors and insert them
			    while (tokenizer.hasMoreTokens()) {
			    	String actor = tokenizer.nextToken();
			    	graph.addEdge(film, actor);
			    	////
			    	if (!graph.hasVertex(actor)) {
			    		insertActor(connection, actor);
			    	}
			    	////
			    }
			    ////
			    // Commit only once, after all the inserts are done
			    // If done after each statement performance degrades
			    connection.commit();
			    ////
			}

 	        // print out graph
 	        //StdOut.println(graph);

 	        // print out graph again by iterating over vertices and edges
 	        /*for (String v : graph.vertices()) {
 	            StdOut.print(v + ": ");
 	            for (String w : graph.adjacentTo(v)) {
 	                StdOut.print(w + " ");
 	            }
 	            StdOut.println();
 	        }*/
	        //input.close();
 		}
 		System.out.println("File Uploaded!");
		return result;
	    });

 		get("/", (req, res) -> ServeHtml.serveHtml("index.html",""));
    	get("/css.css", (req, res) -> ServeHtml.serveHtml("css.css",""));
    	get("/upload_films", (req, res) -> ServeHtml.serveHtml("form.html", ""));

  //	get("prueba", (req, resp) ->  {resp.type("text/html");
  // 	ServeHtml.serveHtml("index.html");});	
    	
 		//get("/film/:name", (req,res) -> Queries.filmQuery(graph, req.params(":name"))); 
 		//get("/actor/:name", (req,res) -> Queries.actorQuery(graph, req.params(":name")));
 		get("/:table/:film", Main::doSelect);
 	
    }

	static int getHerokuAssignedPort() {
	    ProcessBuilder processBuilder = new ProcessBuilder();
	    if (processBuilder.environment().get("PORT") != null) {
	    	return Integer.parseInt(processBuilder.environment().get("PORT"));
	    }
	    return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
	    }
}
