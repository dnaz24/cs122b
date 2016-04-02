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

			System.out.println("\nMovie ID: " + result.getInt(1));
			System.out.println("Movie Title: " + result.getString(2));
			System.out.println("Movie Year: " + result.getInt(3));
			System.out.println("Movie Director: " + result.getString(4)); 
			System.out.println("Banner URL: " + result.getString(5));
			System.out.println("Trailer URL: " + result.getString(6));
			
		}
		
		select.close(); 
		result.close();
		
	}

	public static void insert_star(Connection connection, String Query) throws Exception {

		ArrayList<String> parsedQuery = parseQuery(Query); 
		
		Statement select = connection.createStatement();
		ResultSet result; 
		
		if (parsedQuery.size() == 4)
			result = select.executeQuery("INSERT INTO stars VALUES(" + parsedQuery.get(0) + ", \"" + "" + "\", \"" + parsedQuery.get(1) + "\", " + parsedQuery.get(2) + ", \"" + parsedQuery.get(3) + "\")"); 
		else if (parsedQuery.size() == 5)
			result = select.executeQuery("INSERT INTO stars VALUES(" + parsedQuery.get(0) + ", \"" + parsedQuery.get(1) + "\", \"" + parsedQuery.get(2) + "\", " + parsedQuery.get(3) + ", \"" + parsedQuery.get(4) + "\")");
		else
			return; 
		
		select.close(); 
		result.close();
		
	}

	public static void insert_customer(Connection connection, String Query) throws Exception {

		ArrayList<String> parsedQuery = parseQuery(Query); 
	
		Statement select = connection.createStatement();
		ResultSet result; 
	
		result = select.executeQuery("INSERT INTO customers(id, first_name, last_name, cc_id, address, email, password) SELECT " + parsedQuery.get(0) + ", \"" + parsedQuery.get(1) + "\", \"" + parsedQuery.get(2) + "\", \"" + parsedQuery.get(3) + "\", \"" + parsedQuery.get(4) + "\", \"" + parsedQuery.get(5) + "\", " + parsedQuery.get(6) + " FROM dual WHERE EXISTS (SELECT cc.id FROM creditcards cc, customers c WHERE cc.id = c.cc_id AND cc.id = \"" + parsedQuery.get(3) + "\")"); 
	
		select.close();
		result.close(); 

	}

	public static void delete_customer(Connection connection, String Query) throws Exception {

		ArrayList<String> parsedQuery = parseQuery(Query); 
		
		Statement select = connection.createStatement();
		ResultSet result; 
		
		result = select.executeQuery("DELETE FROM customers WHERE customers.id = " + parsedQuery.get(0)); 
		
		select.close();
		result.close(); 

	}

	public static void print_metadata(Connection connection) throws SQLException {
		
		ResultSet rs = connection.getMetaData().getTables(null, null, "%", null); 
		while (rs.next()){
			
			System.out.println("Table: " + rs.getString(3));
	        Statement select = connection.createStatement();
	        ResultSet result = select.executeQuery("Select * from " + rs.getString(3));
	        ResultSetMetaData metadata = result.getMetaData();
	        for (int i = 1; i <= metadata.getColumnCount(); i++)
	        	System.out.println("Type of column "+ metadata.getColumnName(i) + ": " + metadata.getColumnTypeName(i));
	        System.out.println();
	        
	        select.close();
	        result.close(); 
			
		}
		
	}

	public static void sql_command(Connection connection, String query) {
		try{
			String split[] = query.split(" ");
			Statement statement = connection.createStatement();

			if(split[0].equalsIgnoreCase("select")){
				ResultSet result; 
				result = statement.executeQuery(query);
				int columns = result.getMetaData().getColumnCount();
				String attributes = "";

				for (int i = 1; i <= columns; i ++){
					attributes += (result.getMetaData().getColumnName(i)) + " ";
				}
				System.out.println("\nAttributes: " + attributes);

				while(result.next()) {
					String line = "";
					for (int j = 1; j <= columns; j++){
						line = line + result.getString(j) + " ";
					}
					System.out.println();
					System.out.println(line);
				}
			}
			else if(split[0].equalsIgnoreCase("update")){
				System.out.println("Doesn't do anything yet. need to add more here");
			}
			else if(split[0].equalsIgnoreCase("insert")){
				statement.executeUpdate(query);
				System.out.println("\nSuccessfully inserted into database table");
			}
			else if(split[0].equalsIgnoreCase("delete")){
				statement.executeUpdate(query);
				System.out.println("\nSuccessfully deleted from database table");				
			}
			else{
				System.out.println("You have an error in your SQL syntax");
			}
		} 
		catch(Exception e){
			System.out.println("You have an error in your SQL syntax");
		}
	}

	public static void exit_menu(Connection connection) throws Exception {
		connection.close();
		System.out.println("\nSuccessfully disconnected from your database");						
	}

	public static boolean exit_menu_and_program(Connection connection) throws Exception {
		exit_menu(connection);
		System.out.println("Successfully exit the program");							
		return true;
	}

	public static boolean exit_program() {
		Scanner scan = new Scanner(System.in);
		String decision;
		do{
			System.out.println("Do you want to exit the program? [Y/N]");
			decision = scan.nextLine();
		}while((!decision.equalsIgnoreCase("Y")) && (!decision.equalsIgnoreCase("N")));
		if(decision.equalsIgnoreCase("Y"))
			System.out.println("\nSuccessfully exit the program");
		return decision.equalsIgnoreCase("Y");
	}
	
	public static void main(String[] arg) throws Exception {

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		boolean exit_program = false;
		String db;
		String un;
		String pw;
		String error;
		String star;
		String sql;
		String command = "";

		while(!exit_program){

			Scanner scan = new Scanner(System.in);
			System.out.println("\nEnter a database:");
			db = scan.nextLine();
			System.out.println("Enter a user name:");
			un = scan.nextLine();
			System.out.println("Enter a user password:");
			pw = scan.nextLine();

			try{
				// Connection connection = DriverManager.getConnection("jdbc:mysql:///moviedb", "root", "calmdude6994");
				Connection connection = DriverManager.getConnection(db, un, pw);
				System.out.println("\nSuccessfully connected to your database");

				while(true){
					System.out.println("\nEnter a command [Print, Insert Star, Insert Customer, Delete Customer, Print Metadata, SQL command, Exit Menu, Exit Program]:");
					command = scan.nextLine();

					// print(connection, "james"); 
					if(command.equalsIgnoreCase("Print")){
						System.out.println("\nType the name of the star:");
						star = scan.nextLine();
						print(connection, star);
					}
					else if(command.equalsIgnoreCase("Insert Star")){
						
						System.out.println("Enter the information of the star: ");
						star = scan.nextLine();
						insert_star(connection, star);
						
					}
					else if(command.equalsIgnoreCase("Insert Customer")){

						String customer; 
						System.out.println("Insert the information of the customer: "); 
						customer = scan.nextLine(); 
						insert_customer(connection, customer); 

					}
					else if(command.equalsIgnoreCase("Delete Customer")){
						
						String customer; 
						System.out.println("Insert the information of the customer: ");
						customer = scan.nextLine(); 
						delete_customer(connection, customer); 

					}
					else if(command.equalsIgnoreCase("Print Metadata")){
						print_metadata(connection);
					}
					else if(command.equalsIgnoreCase("SQL Command")){
						System.out.println("\nType a valid SQL command:");
						sql = scan.nextLine();
						sql_command(connection, sql);
					}
					else if(command.equalsIgnoreCase("Exit Menu")){
						exit_menu(connection);
						break;
					}
					else if(command.equalsIgnoreCase("Exit Program")){
						exit_program = exit_menu_and_program(connection);
						break;
					}
				}
			}
			catch(SQLException e){
				error = e.getClass().getCanonicalName();
				if(error.equals("java.sql.SQLException")){
					System.out.println();
					System.out.println(e.getMessage());
				}
				else if(error.equals("com.mysql.jdbc.exceptions.MySQLSyntaxErrorException")){
					System.out.println("\nDatabase does not exist");
				}
				exit_program = exit_program();
			}
		}
	}
}
