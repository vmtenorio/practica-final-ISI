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
import java.io.IOException;
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
    	String toInsert = "";
    	response.type("text/html");
    	
    	return ServeHtml.serveHtml(ServeHtml.makeFile(fileName),toInsert);
    }     
    
    public static String serveCss(Request req, Response res) {
		res.type("text/css");
    	return ServeHtml.serveHtml(ServeHtml.makeFile("css.css"),"");
    }
    
    public static byte[] serveImage(Request req, Response res) {
		res.type("image/jpeg");
    	try {
			return ServeHtml.imageToBytes("header.jpg");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return "Fallo al cargar Imagen".getBytes();
    }
    
    public static byte[] serveFavicon(Request req, Response res) {
		res.type("image/png");
    	try {
			return ServeHtml.imageToBytes("favicon.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return "Fallo al cargar Imagen".getBytes();
    }
    
    public static String serveFilm(Request request, Response response) {
    	String toInsert = "<br><h1>Su pelicula contiene los siguientes actores: </h1></br>";
    	String fileName = "pelicula.html";
    	Iterable<String> toParse;
    	
    	response.type("text/html");
		String pelicula = request.queryParams("nombre");
    	try {
    		toParse = Queries.filmQuery(db, graph, pelicula);
    		toInsert += ServeHtml.parseIterable(toParse);
    	}catch(NoSuchFieldException e){
    		toInsert = "Pelicula no encontrada";
    		e.printStackTrace();
		}catch(IllegalArgumentException e) {
			graph = new Graph("resources/movies.txt", "/");
			try{
				toParse = Queries.filmQuery(db, graph, pelicula);
				toInsert += ServeHtml.parseIterable(toParse);
			}catch(Exception e2){
	    		toInsert = "Pelicula no encontrada";
	    		e.printStackTrace();
			}
		}
    	return ServeHtml.serveHtml(ServeHtml.makeFile(fileName),toInsert);
    }
    
    public static String serveYear(Request request, Response response) {
    	String toInsert;
    	String fileName = "pelicula.html";
    	String year = "";
    	Iterable<String> toParse;
    	
    	year = request.queryParams("fecha");    	
    	response.type("text/html");
    	toInsert = "<h1>Las peliculas del año " + year + " son:</h1>";
    	try {
    		toParse = Queries.yearQuery(db, Integer.valueOf(year));
    		toInsert += ServeHtml.parseIterable(toParse);
    	}catch(IllegalArgumentException e){
    		toInsert = "No existen peliculas para ese año";
		}
    	return ServeHtml.serveHtml(ServeHtml.makeFile(fileName),toInsert);
    }
    
    public static String serveActor(Request request, Response response) {
     	String toInsert;
    	String fileName = "actor.html";
    	String name, surname;
    	Iterable<String> toParse;
    	
    	name = request.queryParams("nombre");
		surname = request.queryParams("apellidos");
		response.type("text/html");
    	toInsert = "<h1>El actor/actriz: " + name + " " + surname + " sale en:</h1>";
    	try {
    		toParse = Queries.actorQuery(db, graph, name, surname);
    		toInsert += ServeHtml.parseIterable(toParse);
    	}catch(NoSuchFieldException e){
    		toInsert = "Actor no encontrado";
    		e.printStackTrace();
		}catch(IllegalArgumentException e) {
			graph = new Graph("resources/movies.txt", "/");
			try{
				toParse = Queries.actorQuery(db, graph, name, surname);
				toInsert += ServeHtml.parseIterable(toParse);
			}catch(Exception e2){
	    		toInsert = "Actor no encontrado";
	    		e.printStackTrace();
			}
		}
    	
    	return ServeHtml.serveHtml(ServeHtml.makeFile(fileName),toInsert);
    }
    
    public static String serveDistance(Request request, Response response) {
    	String toInsert;
    	String fileName = "distancia.html";
    	String at1, at2;
    	Iterable<String> toParse;
    	
    	at1 = request.queryParams("at1");
		at2 = request.queryParams("at2");
		response.type("text/html");
		toInsert = "<h1>La distancia entre " + at1 + " y " + at2 + " es de: ";
		try {
			PathFinder pf = Queries.distanceQuery(db, graph, at1, at2);
			if (pf.distanceTo(at2) == Integer.MAX_VALUE) {
				toInsert += "No existe camino entre los actores especificados";
			} else {
				toInsert += pf.distanceTo(at2) + "</h1></br>";
    			toInsert += ServeHtml.parseIterable(pf.pathTo(at2));
			}
    	}catch(NoSuchFieldException e){
    		toInsert = "No existen esos nodos, o no ha usado el formato adecuado";
    		e.printStackTrace();
		}catch(IllegalArgumentException e) {
			graph = new Graph("resources/movies.txt", "/");
			try{
				PathFinder pf = Queries.distanceQuery(db, graph, at1, at2);
	    		toInsert += pf.distanceTo(at2) + "</h1></br>";
	    		toInsert += ServeHtml.parseIterable(pf.pathTo(at2));
			}catch(Exception e2){
				toInsert = "No existen esos nodos, o no ha usado el formato adecuado";
	    		e.printStackTrace();
			}	
		}
		
    	return ServeHtml.serveHtml(ServeHtml.makeFile(fileName),toInsert);
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
    
 	// Create data structures
 	graph = new Graph();
 	db = new Database(connection);
    
    // Retrieves the file uploaded through the /upload_films HTML form
 	// Creates table and stores uploaded file in a two-columns table
    
 	post("/upload", (req, res) -> {
 		String toInsert = "";
 		req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
 		String result = "File uploaded!";
 		try (InputStream input = req.raw().getPart("uploaded_films_file").getInputStream()) {
 			// getPart needs to use the same name "uploaded_films_file" used in the form
 			
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
			    	
			    	////
			    	if (!graph.hasVertex(actor)) {
			    		db.insertActor(actor);
			    	}
			    	////
			    	graph.addEdge(film, actor);
			    }
			    ////
			    // Commit only once, after all the inserts are done
			    // If done after each statement performance degrades
			    connection.commit();
			    ////
			}
 			System.out.println("File Uploaded!");
 			toInsert = "<h1>File succesfully updated</h1>";
 		}catch (Exception e) {
 			toInsert = "Error al subir, consultar terminal para más información";
 			e.printStackTrace();
 		}
 		
		return ServeHtml.serveHtml(ServeHtml.makeFile("form.html"), toInsert);
	    });
 		
 		
 		get("/", (req,res) -> serve(req, res, "index.html")); 
 		get("/buscar_pelicula", (req,res) -> serve(req, res, "pelicula.html"));
 		get("/buscar_actor", (req,res) -> serve(req, res, "actor.html")); 
 		get("/medir_distancia", (req,res) -> serve(req, res, "distancia.html")); 
 		get("/queries", (req,res) -> serve(req, res, "queries.html")); 
 		get("/upload_films", (req,res) -> serve(req, res, "form.html"));
 		
 		post("/buscar_pelicula", Main::serveFilm);
 		post("/pelicula_fecha", Main::serveYear);
 		post("/buscar_actor", Main::serveActor); 
 		post("/medir_distancia", Main::serveDistance); 
		
 		get("/css.css", Main::serveCss);
 		get("/header.jpg", Main::serveImage);
 		get("/favicon.png", Main::serveFavicon);
 		
 		
 	
    }

	static int getHerokuAssignedPort() {
	    ProcessBuilder processBuilder = new ProcessBuilder();
	    if (processBuilder.environment().get("PORT") != null) {
	    	return Integer.parseInt(processBuilder.environment().get("PORT"));
	    }
	    return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
	    }
}
