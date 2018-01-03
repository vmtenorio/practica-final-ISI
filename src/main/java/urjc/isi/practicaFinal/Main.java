package urjc.isi.practicaFinal;

import static spark.Spark.*;
import spark.Request;
import spark.Response;

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

// This code is quite dirty. Use it just as a hello world example 
// to learn how to use JDBC and SparkJava to upload a file, store 
// it in a DB, and do a SQL SELECT query
public class Main {
    
	public static void main(String[] args) throws 
	ClassNotFoundException, SQLException {
    	
    port(getHerokuAssignedPort());
    
    //connection = DriverManager.getConnection("jdbc:sqlite:sample_graph.db");
    //connection.setAutoCommit(false);
    
    get("/:table/:film", Graph::doSelect);
    get("/upload_films", (req, res) -> 
    	"<form action='/upload' method='post' enctype='multipart/form-data'>" 
    	+ "    <input type='file' name='uploaded_films_file' accept='.txt'>"
    	+ "    <button>Upload file</button>" + "</form>");

    // Retrieves the file uploaded through the /upload_films HTML form
 	// Creates table and stores uploaded file in a two-columns table
 	post("/upload", (req, res) -> {
 		req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/tmp"));
 		String result = "File uploaded!";
 		try (InputStream input = req.raw().getPart("uploaded_films_file").getInputStream()) { 
 			// getPart needs to use the same name "uploaded_films_file" used in the form

 			/*// Prepare SQL to create table
 			Statement statement = connection.createStatement();
 			statement.setQueryTimeout(30); // set timeout to 30 sec.
 			statement.executeUpdate("drop table if exists films");
 			statement.executeUpdate("create table films (v string, w string)");
			*/

 			
 			// Read contents of input stream that holds the uploaded file
 			InputStreamReader isr = new InputStreamReader(input);
 			BufferedReader br = new BufferedReader(isr);
 			//String s;
 			Scanner s = new Scanner(br);
 			
 			// create graph
 	        Graph graph = new Graph();
 	        /*while (!StdIn.isEmpty()) {
 	            String v = StdIn.readString();
 	            String w = StdIn.readString();
 	            graph.addEdge(v, w);
 	        }*/
 	       while (s.hasNext()) {
	        	
	            String v = s.next();
	            System.out.println("1: |" + v +"|\n");
	            if (s.hasNext()) {
		            String w = s.next();
		            System.out.println("2: |" + w +"|\n");
		            graph.addEdge(v, w);
		            
		            //insert(connection, v, w);
			    
				    // Commit only once, after all the inserts are done
				    // If done after each statement performance degrades
				    //connection.commit();
	            }else {
	            	break;
	            }
	            
	        }
 	        s.close();

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
 			
 			
 			
	        /*// create graph
	        Graph graph = new Graph();
	        while (s.hasNext()) {
	        	
	            String v = s.next();
	            System.out.println("1: |" + v +"|\n");
	            if (s.hasNext()) {
		            String w = s.next();
		            System.out.println("2: |" + w +"|\n");
		            //graph.addEdge(v, w);
		            
		            insert(connection, v, w);
			    
				    // Commit only once, after all the inserts are done
				    // If done after each statement performance degrades
				    connection.commit();
	            }else {
	            	break;
	            }
	            
	        }*/
 			/*while (s.hasNext())
 			    System.out.println(s.next());*/
	        input.close();
 		}
		return result;
	    }); 
        
        
        /*
        // print out graph
        StdOut.println(graph);

        // print out graph again by iterating over vertices and edges
        for (String v : graph.vertices()) {
            StdOut.print(v + ": ");
            for (String w : graph.adjacentTo(v)) {
                StdOut.print(w + " ");
            }
            StdOut.println();
        }*/

    }
}
