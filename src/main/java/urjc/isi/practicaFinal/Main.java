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
    
    
    public static void insert(Connection conn, String film, String actor) {
	String sql = "INSERT INTO films(film, actor) VALUES(?,?)";

	try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
		pstmt.setString(1, film);
		pstmt.setString(2, actor);
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
    staticFiles.location("htmlCss");
    //get("/upload_films2", upload);
    get("/:table/:film", Main::doSelect);
    
    get("/upload_films", (req, res) -> 
	"<form action='/upload' method='post' enctype='multipart/form-data'>" 
	+ "    <input type='file' name='uploaded_films_file' accept='.txt'>"
	+ "    <button>Upload file</button>" + "</form>");

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
 			statement.executeUpdate("create table films (film text, actor text)");
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


			    // Now get actors and insert them
			    while (tokenizer.hasMoreTokens()) {
			    	graph.addEdge(film, tokenizer.nextToken());
			    	////
			    	insert(connection, film, tokenizer.nextToken());
			    	////
			    }
			    ////
			    // Commit only once, after all the inserts are done
			    // If done after each statement performance degrades
			    connection.commit();
			    ////
			}

 	        // print out graph
 	        StdOut.println(graph);

 	        // print out graph again by iterating over vertices and edges
 	        for (String v : graph.vertices()) {
 	            StdOut.print(v + ": ");
 	            for (String w : graph.adjacentTo(v)) {
 	                StdOut.print(w + " ");
 	            }
 	            StdOut.println();
 	        }
	        input.close();
 		}
		return result;
	    });
 		
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
