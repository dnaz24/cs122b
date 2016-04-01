package jdbc; 

import java.sql.*;
import java.util.*;

public class JDBC
{
	
	private static ArrayList<String> parseQuery(String Query){
		
		ArrayList<String> parsedQuery = new ArrayList<String>(); 
		
		Scanner in = new Scanner(Query);
		in.useDelimiter(" "); 
		
		while(in.hasNext())
			parsedQuery.add(in.next()); 
		
		in.close(); 
		return parsedQuery; 
		
	}

	public static void print(Connection connection, String Query) throws Exception{

		ArrayList<String> parsedQuery = parseQuery(Query); 
		
		Statement select = connection.createStatement();
		ResultSet result; 
		
		if (parsedQuery.size() == 1)
			result = select.executeQuery("SELECT m.id, m.title, m.year, m.director, IF (m.banner_url IS NULL, \"NULL\", m.banner_url) AS BannerURLExists, IF (m.trailer_url IS NULL, \"NULL\", m.trailer_url) AS TrailerURLExists FROM stars_in_movies sim, movies m, stars s WHERE s.id = sim.star_id AND m.id = sim.movie_id AND (s.first_name = \"" + parsedQuery.get(0) + "\" OR s.last_name = \"" + parsedQuery.get(0) + "\" OR s.id = \"" + parsedQuery.get(0) + "\")");
		else if (parsedQuery.size() == 2)
			result = select.executeQuery("SELECT m.id, m.title, m.year, m.director, IF (m.banner_url IS NULL, \"NULL\", m.banner_url) AS BannerURLExists, IF (m.trailer_url IS NULL, \"NULL\", m.trailer_url) AS TrailerURLExists FROM stars_in_movies sim, movies m, stars s WHERE s.id = sim.star_id AND m.id = sim.movie_id AND ((s.first_name = \"1"  + parsedQuery.get(0) + "\"AND s.last_name = \"" + parsedQuery.get(1) + "\") OR (s.first_name =  \"" + parsedQuery.get(0) + "\" OR s.last_name = \"" + parsedQuery.get(1) + "\"))");
		else
			return; 
		
		while(result.next()){
			
			System.out.println("Movie ID: " + result.getInt(1));
			System.out.println("Movie Title: " + result.getString(2));
			System.out.println("Movie Year: " + result.getInt(3));
			System.out.println("Movie Director: " + result.getString(4)); 
			System.out.println("Banner URL: " + result.getString(5));
			System.out.println("Trailer URL: " + result.getString(6));
			System.out.println();
			
		}
		
	}

	public static void insert_star() {

	}

	public static void insert_customer() {

	}

	public static void delete_customer() {

	}

	public static void print_metadata() {
		System.out.println("print metadata");
	}

	public static void sql_command() {

	}

	public static void exit_menu() {

	}

	public static void exit_program() {

	}
	
	public static void main(String[] arg) throws Exception {

		Class.forName("com.mysql.jdbc.Driver").newInstance();

		Connection connection = DriverManager.getConnection("jdbc:mysql:///moviedb", "root", "calmdude6994");
		
		print(connection, "james caan"); 

	}
}
