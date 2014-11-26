import java.sql.*;
import java.util.*;
import java.lang.*;
/*
 * Coded by John Coppinger
 * 
 * This program manages a databas of people defined as follows:
 * FNAME     = first name 
 * LNAME     = last  name
 * EMAIL     = email address
 * PREVEMAIL = have we recieved an email from them previously 
 *System.out.println("fName   |   lName   |   email   |   recieved");
 *System.out.println(fName + " | " + lName + " | " +  email + " | " +  prev);
 */
public class SQLiteMailerJDB 
{
	public static void main(String args[]) throws SQLException
	{		
		int choice = 100;
		Connection c = openConn();	
		if(!doesUsersExist(c) )
		{
			generateUSERS(c);
		}
		do{
			choice = menu(c);
		}while(choice != 0);
		System.out.println("Exitting program");
	}
	public static Recipient makeRecipient(String mode, Scanner kb)
	{
		String fName, lName, email;
		int sentPrev;
		
		System.out.println("Please enter the following to " + mode + " to the database");
		System.out.println("first name");
		fName = kb.nextLine();
		System.out.println("last name");
		lName = kb.nextLine();
		System.out.println("email");
		email = kb.nextLine();
		System.out.println("1 for have recieved email in the past, 0 for have not");
		sentPrev = kb.nextInt();
		Recipient temp = new Recipient(fName, lName, email, sentPrev);
		return temp;
	}
	/*
	 * adds a user into the database 
	 *
	 */	
	
	public static void addUser(Connection c, Scanner kb) throws SQLException
	{
		Recipient cur = makeRecipient("add", kb);
		PreparedStatement pStmt = null;
		pStmt = c.prepareStatement("INSERT INTO USERS VALUES(?, ?, ?, ?)");
		pStmt.setString(1, cur.getFirstName() );
		pStmt.setString(2, cur.getLastName() );
		pStmt.setString(3, cur.getAddress() );
		pStmt.setInt(4, cur.getMailedBefore() );	
		try
		{
				pStmt.execute();	
		}catch (Exception e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}  
		pStmt.close();
	}
	/*
	 * removes a user from the database
	 */
	public static void removeUser(Connection c, Scanner kb) throws SQLException
	{
		PreparedStatement pStmt = null;
		Recipient cur = makeRecipient("delete", kb);
		try
		{
				pStmt = c.prepareStatement(
				    "DELETE FROM USERS WHERE FNAME = ? AND LNAME = ? AND EMAIL = ?");
				pStmt.setString(1, cur.getFirstName() );
				pStmt.setString(2, cur.getLastName() );
				pStmt.setString(3, cur.getAddress() );	
				pStmt.execute();
		}catch (Exception e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}  
	    pStmt.close();

	}
	/*
	 * A simple print method for the whole db users table
	 * it simply prints based on the order the users were input
	 * 
	 */
	public static ArrayList<Recipient> printUsers(Connection c) throws SQLException
	{
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS;" );
		ArrayList<Recipient> arr = generateResultArr(rs);
		stmt.close();
		return arr;   
	}
	/*
	 * prints based on first name ascending
	 */
	public static ArrayList<Recipient> printSortedFName(Connection c) throws SQLException
	{
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS ORDER BY FNAME ASC;" );
		ArrayList<Recipient> arr = generateResultArr(rs);
		stmt.close();
		return arr;   
	}
	/*
	 *prints based on last name ascending 
	 */
	public static ArrayList<Recipient> printSortedLName(Connection c) throws SQLException
	{
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS ORDER BY LNAME ASC;" );
		ArrayList<Recipient> arr = generateResultArr(rs);
		stmt.close();
		return arr;   
	}
	/*
	 *Prints based on first name descending 
	 *
	 */
	public static ArrayList<Recipient> printSortedFNameReverse(Connection c) throws SQLException
	{	
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS ORDER BY FNAME DESC;" );
		ArrayList<Recipient> arr = generateResultArr(rs);
		stmt.close();
		return arr;   
	}
	/*
	 * Prints based on last name descending
	 * 
	 */
	public static ArrayList<Recipient> printSortedLNameReverse(Connection c) throws SQLException
	{
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS ORDER BY LNAME DESC;" );
		ArrayList<Recipient> arr = generateResultArr(rs);
		stmt.close();
		return arr;    
	}
	/*
	 * prints based on email ascending
	 * 
	 */
	public static ArrayList<Recipient> printSortedEmail(Connection c)throws SQLException
	{
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS ORDER BY EMAIL ASC;" );
		ArrayList<Recipient> arr = generateResultArr(rs);
		stmt.close();
		return arr;
	}
	/*
	 * 
	 */
	public static ArrayList<Recipient> printSortedEmailReverse(Connection c)throws SQLException
	{
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS ORDER BY EMAIL DESC;" );
		ArrayList<Recipient> arr = generateResultArr(rs);
		stmt.close();
		return arr;
	}
	
	/*
	 * double prompts the user before deleting the whole USERS table
	 */
	public static void dropDB(Connection c, Scanner kb) throws SQLException
	{
		System.out.println("Are you sure you want to delete the entire USERS table?");
		System.out.println("Y/n");
		String ch = kb.nextLine();
		if(ch.equalsIgnoreCase("y") )
		{
			System.out.println("Are you Really sure?\nY/n");
			ch = kb.nextLine();
			if(ch.equalsIgnoreCase("y") )
			{
				
				try
				{
					
					Statement stmt = c.createStatement();
					String sql = "DROP TABLE IF EXISTS USERS" ;
					stmt.executeUpdate(sql);
					stmt.close();
					generateUSERS(c);
				}catch (Exception e)
				{
					System.err.println( e.getClass().getName() + ": " + e.getMessage() );
					System.exit(0);
				}  
			}
		}
		
		
	}
	/*
	 * Opens the initial connection to the database
	 */
	public static Connection openConn()
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
		System.out.println("Opened database successfully\n\n");
		
		return c;
	}	
	/*
	 * checks to make sure the USERS table exists before skipping its creations
	 */
	public static boolean doesUsersExist(Connection c) throws SQLException
	{
		Statement stmt = c.createStatement();
		
		ResultSet rs = stmt.executeQuery( "SELECT * FROM USERS;" );
		if (rs != null)
		{	
			System.out.println("USERS table found, skipping table generation\n\n");
			rs.close();
			return true;
		}
		
		else
		{
			System.out.println("USERS table not found, generating now\n\n");
			
			return false;
		}
		
	}
	/* 
	 * if the directly above method returns false this method will generate the USERS table within the database.
	 */
	public static void generateUSERS(Connection c)
	{
		
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
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			//System.exit(0);
		}
	}	
	/*
	 * makes an arraylist of recipients based on the passed in resultset
	 */
	public static ArrayList<Recipient> generateResultArr(ResultSet rs) throws SQLException
	{
		ArrayList<Recipient> arr = new ArrayList<Recipient>();
		
		while ( rs.next() ) 
	      {
			  String fName = rs.getString("FNAME");
	    	  String lName = rs.getString("LNAME");
	    	  String email = rs.getString("EMAIL");
	    	  int sentPrev = rs.getInt("PREVEMAIL");
	    	  Recipient cur = new Recipient(fName, lName, email, sentPrev);
	    	  arr.add(cur);
	      }
	      rs.close();
	      return(arr);
	}
	/*
	 * A simple menu
	 */	
	public static int menu(Connection c) throws SQLException
	{
		
		System.out.println("Please select a valid option:");
		System.out.println("1: Add user to Holiday Mailer DB");
		System.out.println("2: Remove user from Holiday Mailer DB");
		System.out.println("3: Print the Holiday Mailer DB");
		System.out.println("4: Delete USERS table.(double prompted to confirm)");
		System.out.println("5: Sorted first name");
		System.out.println("6: sorted last  name");
		System.out.println("7: Sorted first name reverse");
		System.out.println("8: sorted last  name reverse");
		System.out.println("9: sorted email");
		System.out.println("10: sorted email reverse");
		System.out.println("0: Quit program");
		
		int choice;
		ArrayList<Recipient> arr = null;
		Scanner kb = new Scanner(System.in);
		choice = kb.nextInt();
		kb.nextLine();
		try{	
			if(choice < 0 || choice > 10)
			{
				System.out.println("Selection not valid");
			}
			
			else
			{
				if(choice == 1)
				{
					addUser(c, kb);
				}
				else if(choice == 2)
				{
					removeUser(c, kb);
				}
				else if(choice == 3)
				{
					arr = printUsers(c);
				}
				else if(choice == 4)
				{
					dropDB(c, kb);
				}
				else if(choice == 5)
				{
					arr = printSortedFName(c);
				}
				else if(choice == 6)
				{
					arr = printSortedLName(c);
				}
				else if(choice == 7)
				{
					arr = printSortedFNameReverse(c);
				}
				else if(choice == 8)
				{
					arr = printSortedLNameReverse(c);
				}
				else if(choice == 9)
				{
					arr = printSortedEmail(c);
				}
				else if(choice == 10)
				{
					arr = printSortedEmailReverse(c);
				}
				else
				{
					c.close();
				}
			}
		}catch (Exception e)
		{
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		if(arr!=null)
		{
			printArr(arr);
		}
		return choice;  	
	}
	

	public static void printArr(ArrayList<Recipient> arr)
	{
		System.out.println("fName   |   lName   |   email   |   recieved");
		for(int i = 0; i < arr.size(); i++)
		{
			System.out.println(arr.get(i));	
		}
	}
}	
	
	