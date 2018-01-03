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

import java.util.Scanner;

import java.util.StringTokenizer;

import javax.servlet.MultipartConfigElement;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/******************************************************************************
 *  Compilation:  javac Graph.java
 *  Execution:    java Graph < input.txt
 *  Dependencies: ST.java SET.java In.java StdOut.java
 *  Data files:   https://introcs.cs.princeton.edu/45graph/tinyGraph.txt
 *  
 *  Undirected graph data type implemented using a symbol table
 *  whose keys are vertices (String) and whose values are sets
 *  of neighbors (SET of Strings).
 *
 *  Remarks
 *  -------
 *   - Parallel edges are not allowed
 *   - Self-loop are allowed
 *   - Adjacency lists store many different copies of the same
 *     String. You can use less memory by interning the strings.
 *
 *  % java Graph < tinyGraph.txt
 *  A: B C G H 
 *  B: A C H 
 *  C: A B G 
 *  G: A C 
 *  H: A B 
 *
 *  A: B C G H 
 *  B: A C H 
 *  C: A B G 
 *  G: A C 
 *  H: A B 
 *
 ******************************************************************************/

/**
 *  The {@code Graph} class represents an undirected graph of vertices
 *  with string names.
 *  It supports the following operations: add an edge, add a vertex,
 *  get all of the vertices, iterate over all of the neighbors adjacent
 *  to a vertex, is there a vertex, is there an edge between two vertices.
 *  Self-loops are permitted; parallel edges are discarded.
 *  <p>
 *  For additional documentation, see <a href="https://introcs.cs.princeton.edu/45graph">Section 4.5</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i> by Robert Sedgewick and Kevin Wayne.
 */
public class Graph {

    // symbol table: key = string vertex, value = set of neighboring vertices
    private ST<String, SET<String>> st;

    // number of edges
    private int E;
    
    
    
    
    
    
    
    
    
    
    private static Connection connection;
    
    public static String doSelect(Request request, Response response) {
    return select (connection, request.params(":table"), 
                                       request.params(":film"));
    }
    
    
    public static String select(Connection conn, String table, String v) {
    String sql = "SELECT * FROM " + table + " WHERE film=?";

    String result = new String();
    	
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    	pstmt.setString(1, v);
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
    
    public static void insert(Connection conn, String v, String w) {
    String sql = "INSERT INTO films(v, w) VALUES(?,?)";

    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    	pstmt.setString(1, v);
    	pstmt.setString(2, w);
    	pstmt.executeUpdate();
    	} catch (SQLException e) {
    	System.out.println(e.getMessage());
    }
    
    /*sql = "INSERT INTO films(w, v) VALUES(?,?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
    	pstmt.setString(1, w);
    	pstmt.setString(2, v);
    	pstmt.executeUpdate();
    	} catch (SQLException e) {
    	System.out.println(e.getMessage());
    }*/
    }
    
    
    
    
    
    
    
    

   /**
     * Initializes an empty graph with no vertices or edges.
     */
    public Graph() {
        st = new ST<String, SET<String>>();
    }

   /**
     * Initializes a graph from the specified file, using the specified delimiter.
     *
     * @param filename the name of the file
     * @param delimiter the delimiter
     */
    public Graph(String filename, String delimiter) {
        st = new ST<String, SET<String>>();
        In in = new In(filename);
        while (in.hasNextLine()) {
            String line = in.readLine();
            String[] names = line.split(delimiter);
            for (int i = 1; i < names.length; i++) {
                addEdge(names[0], names[i]);
            }
        }
    }

   /**
     * Returns the number of vertices in this graph.
     *
     * @return the number of vertices in this graph
     */
    public int V() {
        return st.size();
    }

   /**
     * Returns the number of edges in this graph.
     *
     * @return the number of edges in this graph
     */
    public int E() {
        return E;
    }

    // throw an exception if v is not a vertex
    private void validateVertex(String v) {
        if (!hasVertex(v)) throw new IllegalArgumentException(v + " is not a vertex");
    }

   /**
     * Returns the degree of vertex v in this graph.
     *
     * @param  v the vertex
     * @return the degree of {@code v} in this graph
     * @throws IllegalArgumentException if {@code v} is not a vertex in this graph
     */
    public int degree(String v) {
        validateVertex(v);
        return st.get(v).size();
    }

   /**
     * Adds the edge v-w to this graph (if it is not already an edge).
     *
     * @param  v one vertex in the edge
     * @param  w the other vertex in the edge
     */
    public void addEdge(String v, String w) {
        if (!hasVertex(v)) addVertex(v);
        if (!hasVertex(w)) addVertex(w);
        if (!hasEdge(v, w)) E++;
        st.get(v).add(w);
        st.get(w).add(v);
    }

   /**
     * Adds vertex v to this graph (if it is not already a vertex).
     *
     * @param  v the vertex
     */
    public void addVertex(String v) {
        if (!hasVertex(v)) st.put(v, new SET<String>());
    }


   /**
     * Returns the vertices in this graph.
     *
     * @return the set of vertices in this graph
     */
    public Iterable<String> vertices() {
        return st.keys();
    }

   /**
     * Returns the set of vertices adjacent to v in this graph.
     *
     * @param  v the vertex
     * @return the set of vertices adjacent to vertex {@code v} in this graph
     * @throws IllegalArgumentException if {@code v} is not a vertex in this graph
     */
    public Iterable<String> adjacentTo(String v) {
        validateVertex(v);
        return st.get(v);
    }

   /**
     * Returns true if v is a vertex in this graph.
     *
     * @param  v the vertex
     * @return {@code true} if {@code v} is a vertex in this graph,
     *         {@code false} otherwise
     */
    public boolean hasVertex(String v) {
        return st.contains(v);
    }

   /**
     * Returns true if v-w is an edge in this graph.
     *
     * @param  v one vertex in the edge
     * @param  w the other vertex in the edge
     * @return {@code true} if {@code v-w} is a vertex in this graph,
     *         {@code false} otherwise
     * @throws IllegalArgumentException if either {@code v} or {@code w}
     *         is not a vertex in this graph
     */
    public boolean hasEdge(String v, String w) {
        validateVertex(v);
        validateVertex(w);
        return st.get(v).contains(w);
    }

   /**
     * Returns a string representation of this graph.
     *
     * @return string representation of this graph
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (String v : st) {
            s.append(v + ": ");
            for (String w : st.get(v)) {
                s.append(w + " ");
            }
            s.append('\n');
        }
        return s.toString();
    }

   /**
     * Unit tests the {@code Graph} data type.
     */
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
    
    
    static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
    	return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
    }
    
}
