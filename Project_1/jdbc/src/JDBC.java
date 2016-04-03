package jdbc; 

import java.sql.*;
import java.util.*;

public class JDBC
{
	
	private static boolean isNumber(String string){
		
		for (int i = 0; i < string.length(); i++){
			
			if (!Character.isDigit(string.charAt(i)))
				return false; 
			
		}
		
		return true; 
		
	}
	
	public static void print(Connection connection, Scanner scan) throws Exception{
		
		System.out.println("Please Enter the First Name, Last Name, or ID: ");
		String firstQuery = scan.nextLine(); 
		System.out.println("Please Enter the Last Name (Click 'Enter' if Blank): ");
		String secondQuery = scan.nextLine(); 
		
		Statement select = connection.createStatement();
		ResultSet result; 
		
		if (isNumber(firstQuery))
			result = select.executeQuery("SELECT m.id, m.title, m.year, m.director, IF (m.banner_url IS NULL, \'NULL\', m.banner_url) AS BannerURLExists, IF (m.trailer_url IS NULL, \'NULL\', m.trailer_url) AS TrailerURLExists FROM stars_in_movies sim, movies m, stars s WHERE s.id = sim.star_id AND m.id = sim.movie_id AND (s.id = " + firstQuery + ")");
		else if (secondQuery.equals(""))
			result = select.executeQuery("SELECT DISTINCT m.id, m.title, m.year, m.director, IF (m.banner_url IS NULL, \'NULL\', m.banner_url) AS BannerURLExists, IF (m.trailer_url IS NULL, \'NULL\', m.trailer_url) AS TrailerURLExists FROM stars_in_movies sim, movies m, stars s WHERE s.id = sim.star_id AND m.id = sim.movie_id AND (s.first_name = \'" + firstQuery + "\' OR s.last_name = \'" + firstQuery + "\')");
		else
			result = select.executeQuery("SELECT DISTINCT m.id, m.title, m.year, m.director, IF (m.banner_url IS NULL, \'NULL\', m.banner_url) AS BannerURLExists, IF (m.trailer_url IS NULL, \'NULL\', m.trailer_url) AS TrailerURLExists FROM stars_in_movies sim, movies m, stars s WHERE s.id = sim.star_id AND m.id = sim.movie_id AND ((s.first_name = \'"  + firstQuery + "\' AND s.last_name = \'" + secondQuery + "\') OR (s.first_name =  \'" + firstQuery + "\' OR s.last_name = \'" + secondQuery + "\'))");
		
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

	public static void insert_star(Connection connection, Scanner scan) throws Exception {
		
		System.out.println("Please Enter the Star ID: ");
		String starID = scan.nextLine(); 
		System.out.println("Please Enter the First Name (Click 'Enter' if Blank): ");
		String firstName = scan.nextLine(); 
		System.out.println("Please Enter the Last Name: ");
		String lastName = scan.nextLine(); 
		System.out.println("Please Enter the Date: ");
		String date = scan.nextLine(); 
		System.out.println("Please Enter the Photo URL: ");
		String photoURL = scan.nextLine(); 
		
		String updateString; 
		
		if (firstName.equals(""))
			updateString = "INSERT INTO stars VALUES(" + starID + ", \'" + "" + "\', \'" + lastName + "\', \'" + date + "\', \'" + photoURL + "\')"; 
		else
			updateString = "INSERT INTO stars VALUES(" + starID + ", \'" + firstName + "\', \'" + lastName + "\', \'" + date + "\', \'" + photoURL + "\')";
		
		PreparedStatement updateStatement = connection.prepareStatement(updateString);
		updateStatement.executeUpdate();
		updateStatement.close(); 
		
	}

	public static void insert_customer(Connection connection, Scanner scan) throws Exception {
		
		System.out.println("Please Enter the Customer ID: ");
		String customerID = scan.nextLine(); 
		System.out.println("Please Enter the First Name: ");
		String firstName = scan.nextLine(); 
		System.out.println("Please Enter the Last Name: ");
		String lastName = scan.nextLine(); 
		System.out.println("Please Enter the Credit Card ID: ");
		String creditCardID = scan.nextLine(); 
		System.out.println("Please Enter the Address: ");
		String address = scan.nextLine(); 
		System.out.println("Please Enter the Email: ");
		String email = scan.nextLine(); 
		System.out.println("Please Enter the Password: ");
		String password = scan.nextLine(); 
	
		String updateString = "INSERT INTO customers(id, first_name, last_name, cc_id, address, email, password) SELECT " + customerID + ", \'" + firstName + "\', \'" + lastName + "\', \'" + creditCardID + "\', \'" + address + "\', \'" + email + "\', " + password + " FROM dual WHERE EXISTS (SELECT cc.id FROM creditcards cc, customers c WHERE cc.id = c.cc_id AND cc.id = \'" + creditCardID + "\')"; 
		
		PreparedStatement updateStatement = connection.prepareStatement(updateString);
		updateStatement.executeUpdate();
		updateStatement.close(); 

	}

	public static void delete_customer(Connection connection, Scanner scan) throws Exception {
		
		System.out.println("Please enter the Customer ID: ");
		String customerID = scan.nextLine(); 
		
		String updateString = "DELETE FROM customers WHERE customers.id = " + customerID; 
		
		PreparedStatement updateStatement = connection.prepareStatement(updateString);
		updateStatement.executeUpdate();
		updateStatement.close(); 

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

			if(split[0].equalsIgnoreCase("select")){
				Statement statement = connection.createStatement();
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
				
				result.close(); 
				statement.close(); 
				
			}
			else if(split[0].equalsIgnoreCase("update")){
				
				PreparedStatement updateStatement = connection.prepareStatement(query);
				updateStatement.executeUpdate();
				updateStatement.close(); 
				
			}
			else if(split[0].equalsIgnoreCase("insert")){
				
				PreparedStatement updateStatement = connection.prepareStatement(query);
				updateStatement.executeUpdate();
				updateStatement.close(); 
				System.out.println("\nSuccessfully inserted into database table");
				
			}
			else if(split[0].equalsIgnoreCase("delete")){
				
				PreparedStatement updateStatement = connection.prepareStatement(query);
				updateStatement.executeUpdate();
				updateStatement.close(); 
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

	public static boolean exit_program(Scanner scan) {
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

					if(command.equalsIgnoreCase("Print")){
			
						print(connection, scan);
						
					}
					else if(command.equalsIgnoreCase("Insert Star")){
						
						insert_star(connection, scan);
						
					}
					else if(command.equalsIgnoreCase("Insert Customer")){

						insert_customer(connection, scan); 

					}
					else if(command.equalsIgnoreCase("Delete Customer")){
						
						delete_customer(connection, scan); 

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
					
					else {
						
						System.out.println("Invalid Command. Please try again."); 
						
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
				exit_program = exit_program(scan);
			}
		}
	}
}
