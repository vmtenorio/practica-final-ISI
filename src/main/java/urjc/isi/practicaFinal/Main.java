package urjc.isi.practicaFinal;

import static spark.Spark.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.StringTokenizer;

import javax.servlet.MultipartConfigElement;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

// This code is quite dirty. Use it just as a hello world example 
// to learn how to use JDBC and SparkJava to upload a file, store 
// it in a DB, and do a SQL SELECT query
public class Main {
    
	private static Route upload = (Request request, Response response) -> {
        return "/index.html";
    };
	
	public static void main(String[] args) throws 
	ClassNotFoundException {
    	
    port(getHerokuAssignedPort());
    
    //connection = DriverManager.getConnection("jdbc:sqlite:sample_graph.db");
    //connection.setAutoCommit(false);
    staticFiles.location("/htmlCss");
    get("/upload_films", upload);	
//    
//    get("/upload_films", (req, res) -> 
//	"<form action='/upload' method='post' enctype='multipart/form-data'>" 
//	+ "    <input type='file' name='uploaded_films_file' accept='.txt'>"
//	+ "    <button>Upload file</button>" + "</form>");

    // Retrieves the file uploaded through the /upload_films HTML form
 	// Creates table and stores uploaded file in a two-columns table
    Graph graph = new Graph();
 	post("/upload", (req, res) -> {
 		req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
 		String result = "File uploaded!";
 		try (InputStream input = req.raw().getPart("uploaded_films_file").getInputStream()) { 
 			// getPart needs to use the same name "uploaded_films_file" used in the form

 			
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
			    }
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
 		
 		get("/film/:name", (req,res) -> Queries.filmQuery(graph, req.params(":name"))); 
 		get("/actor/:name", (req,res) -> Queries.actorQuery(graph, req.params(":name")));

    }
	
	static int getHerokuAssignedPort() {
	    ProcessBuilder processBuilder = new ProcessBuilder();
	    if (processBuilder.environment().get("PORT") != null) {
	    	return Integer.parseInt(processBuilder.environment().get("PORT"));
	    }
	    return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
	    }
}
