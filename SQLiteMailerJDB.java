import java.sql.*;
import java.util.*;
import java.lang.*;

public class SQLiteMailerJDB 
{
	public static void main(String args[]) throws SQLException
	{
		Connection c = null;
		
		try
		{
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:HolidayMailer.db");
		}catch (Exception e)
			{
				System.err.println( e.getClass().getName() + ": " + e.getMessage() );
				System.exit(0);
			}
		System.out.println("Opened database successfully");
		/*
		try
		{
			Statement stmt = c.createStatement();
			String sql = "CREATE TABLE USERS " +
				  "(FNAME TEXT NOT NULL, " + 
				  " LNAME TEXT PRIMARY KEY NOT NULL, " +
				  " EMAIL TEXT NOT NULL, " +
				  " PREVEMAIL INT NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
		}catch (Exception e)
		{
			System.out.println( e.getClass().getName() + ": " + e.getMessage() );
			//System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			//System.exit(0);
		}*/
		
		menu(c);
		
		System.out.println("Exitting program");
	}
	
	public static void menu(Connection c) throws SQLException
	{
		System.out.println("Please select a valid option:");
		System.out.println("1: Add user to Holiday Mailer DB");
		System.out.println("2: Remove user from Holiday Mailer DB");
		System.out.println("3: Print the Holiday Mailer DB");
		System.out.println("0: Quit program");
		
		int choice;
		Scanner kb = new Scanner(System.in);
		choice = kb.nextInt();
		
		if(choice < 0 || choice > 3)
		{
			System.out.println("Selection not valid");
			menu(c);
		}
		else
		{
			if(choice == 1)
			{
				addUser(c, kb);
				menu(c);
			}
			if(choice == 2)
			{
				removeUser(c, kb);
				menu(c);
			}
			if(choice == 3)
			{
				printUsers(c);
				menu(c);
			}
			else
			{	
				c.close();
				kb.close();
				
			}
		}
		
	}
	
	public static void addUser(Connection c, Scanner kb) throws SQLException
	{
		String fName, lName, email;
		int sentPrev;
		kb.nextLine();
		System.out.println("Please enter the following to add to the database");
		
		System.out.println("first name");
		fName = kb.nextLine();
		System.out.println("last name");
		lName = kb.nextLine();
		System.out.println("email");
		email = kb.nextLine();
		System.out.println("1 for have recieved email in the past, 0 for have not");
		sentPrev = kb.nextInt();
		
		Statement stmt = null;
		String sql;
		stmt = c.createStatement();
	    sql = "INSERT INTO USERS (FNAME, LNAME, EMAIL, PREVEMAIL) " +
	                   "VALUES (" + fName + ", " + lName + ", " + email + ", " + sentPrev + ");"; 
	    stmt.close();
	    //c.close();
		
	}
	
	public static void removeUser(Connection c, Scanner kb) throws SQLException
	{
		String fName, lName;
		System.out.println("Please enter the follow to add to the database");
		System.out.println("First name\n Last name\n");
		
		fName = kb.nextLine();
		lName = kb.nextLine();
		
				
		Statement stmt = null;
		String sql;
		stmt = c.createStatement();
	    sql = "DELETE from USERS where FNAME = " + fName + " AND LNAME = " + lName + " ;";
	    stmt.executeUpdate(sql);
	    c.commit();
	    
	    
	    stmt.close();
	    //c.close();
	}
	
	public static void printUsers(Connection c) throws SQLException
	{
		Statement stmt = c.createStatement();
		c.setAutoCommit(false);
		ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS;" );
	      while ( rs.next() ) 
	      {
	    	  String fName = rs.getString("FNAME");
	    	  String lName = rs.getString("LNAME");
	    	  String email = rs.getString("EMAIL");
	    	  int sentPrev = rs.getInt("PREVEMAIL");
	    	  boolean prev = (sentPrev == 1);
	    	  System.out.println("First name: "+ fName);
	    	  System.out.println("Last name: "+ lName);
	    	  System.out.println("email: "+ email);
	    	  System.out.println("Recieved email from: "+ prev);
	      }
	      stmt.close();
	      rs.close();
	      
	}
}

	
	
	