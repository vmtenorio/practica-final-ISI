package urjc.isi.practicaFinal;

import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.StringTokenizer;

import javax.servlet.MultipartConfigElement;

import java.io.BufferedReader;
import java.io.File;
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
    private static Database db;
    private static Graph graph;
    
    static Connection getConnection () throws URISyntaxException, SQLException {
    	URI dbUri = new URI(System.getenv("DATABASE_URL"));
     	String username = dbUri.getUserInfo().split(":")[0];
     	String password = dbUri.getUserInfo().split(":")[1];
     	String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + dbUri.getPath();
     	return DriverManager.getConnection(dbUrl, username, password);
    }
    
    public static String serve(Request request, Response response, String fileName) {
    	response.type("text/html");
    	return ServeHtml.serveHtml(ServeHtml.makeFile(fileName),"");
    }     
    
    public static String serveCss(Request req, Response res) {
		res.type("text/css");
    	return ServeHtml.serveHtml(ServeHtml.makeFile("css.css"),"");
    }
    
    
	public static void main(String[] args) throws 
	ClassNotFoundException, SQLException, URISyntaxException {
    	
    port(getHerokuAssignedPort());
    
    // This code only works for PostgreSQL in Heroku
 	// Connect to PostgreSQL in Heroku
    try {
    	connection = getConnection();
    } catch(URISyntaxException e) {
    	System.err.println("Problem getting dbURI");
    } catch(SQLException e) {
    	System.err.println("Problem getting sql connection");
    }
    
 	
 	// PostgreSQL default is to auto-commit (1 transaction / statement execution)
         // Set it to false to improve performance
 	connection.setAutoCommit(false);
 	
 	
    //connection = DriverManager.getConnection("jdbc:sqlite:sample_graph.db");
    //connection.setAutoCommit(false);
    //get("/upload_films2", upload);
    
 	// Create data structures
 	graph = new Graph();
 	db = new Database(connection);
    
    // Retrieves the file uploaded through the /upload_films HTML form
 	// Creates table and stores uploaded file in a two-columns table
    
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
			    if(!graph.hasVertex(film)) {
			    	db.insertFilm(film);
			    }

			    // Now get actors and insert them
			    while (tokenizer.hasMoreTokens()) {
			    	String actor = tokenizer.nextToken();
			    	graph.addEdge(film, actor);
			    	////
			    	if (!graph.hasVertex(actor)) {
			    		db.insertActor(actor);
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
 		
 		
 		get("/", (req,res) -> serve(req, res, "index.html")); 
 		get("/buscar_pelicula", (req,res) -> serve(req, res, "pelicula.html"));
 		get("/buscar_actor", (req,res) -> serve(req, res, "actor.html")); 
 		get("/medir_distancia", (req,res) -> serve(req, res, "distancia.html")); 
 		get("/queries", (req,res) -> serve(req, res, "queries.html")); 
 		get("/upload_films", (req,res) -> serve(req, res, "form.html")); 
 		
 		post("/buscar_pelicula", (req,res) -> serve(req, res, "pelicula.html"));
 		post("/pelicula_fecha", (req,res) -> serve(req, res, "pelicula.html"));
 		post("/buscar_actor", (req,res) -> serve(req, res, "actor.html")); 
 		post("/medir_distancia", (req,res) -> serve(req, res, "distancia.html")); 
 		post("/queries", (req,res) -> serve(req, res, "queries.html")); 
 		post("/upload_films", (req,res) -> serve(req, res, "form.html")); 
 		
 		get("/css.css", Main::serveCss);
    	get("/upload_films", (req, res) -> ServeHtml.serveHtml(ServeHtml.makeFile("form.html"), ""));
    	
 		//get("/film/:name", (req,res) -> Queries.filmQuery(graph, req.params(":name"))); 
 		//get("/actor/:name", (req,res) -> Queries.actorQuery(graph, req.params(":name")));
 	
    }

	static int getHerokuAssignedPort() {
	    ProcessBuilder processBuilder = new ProcessBuilder();
	    if (processBuilder.environment().get("PORT") != null) {
	    	return Integer.parseInt(processBuilder.environment().get("PORT"));
	    }
	    return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
	    }
}
