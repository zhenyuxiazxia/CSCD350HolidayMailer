import java.sql.*;
import java.util.*;
import java.lang.*;
/*
 * Coded by John Coppinger
 * 
 * This program manages a database of people defined as follows:
 * FNAME     = first name 
 * LNAME     = last  name
 * EMAIL     = email address
 * PREVEMAIL = have we recieved an email from them previously 
 *System.out.println("fName   |   lName   |   email   |   recieved");
 *System.out.println(fName + " | " + lName + " | " +  email + " | " +  prev);
 */
public class SQLiteMailerJDB 
{
	private Connection c;
	
	public SQLiteMailerJDB() throws SQLException
	{
		
		this.c = openConn();	
		
		DatabaseMetaData md = c.getMetaData();
		ResultSet rs = md.getTables(null, null, "USERS", null);
		if(rs.next())
		{
			//we gucci, don't make users table
		}
		else
		{
			generateUSERS(c);
		}
	}
	
	public Recipient makeRecipient(String mode, Scanner kb)
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
	
	public void addUser(Recipient cur) throws SQLException
	{
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
	public void removeUser(Recipient cur) throws SQLException
	{
		PreparedStatement pStmt = null;
		
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
	public ArrayList<Recipient> printUsers() throws SQLException
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
	public ArrayList<Recipient> printSortedFName() throws SQLException
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
	public ArrayList<Recipient> printSortedLName() throws SQLException
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
	public ArrayList<Recipient> printSortedFNameReverse() throws SQLException
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
	public ArrayList<Recipient> printSortedLNameReverse() throws SQLException
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
	public ArrayList<Recipient> printSortedEmail()throws SQLException
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
	 public ArrayList<Recipient> printSortedEmailReverse()throws SQLException
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
	public void dropDB() throws SQLException
	{
		System.out.println("Are you sure you want to delete the entire USERS table?");
		System.out.println("Y/n");
		
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
	/*
	 * closes the connection safely when processing is finished
	 */
	public void closeConn() throws SQLException
	{
		this.c.close();
	}
	/*
	 * Opens the initial connection to the database
	 */
	private Connection openConn()
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
		
		return c;
	}	
	/*
	 * checks to make sure the USERS table exists before skipping its creations
	 */
	private boolean doesUsersExist() throws SQLException
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
	private void generateUSERS(Connection c)
	{		
		try
		{
			Statement stmt = c.createStatement();
			String sql = "CREATE TABLE USERS " +
				  "(FNAME TEXT NOT NULL, " + 
				  " LNAME TEXT NOT NULL, " +
				  " EMAIL TEXT PRIMARY KEY NOT NULL, " +
				  " PREVEMAIL  INT NOT NULL)";
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
	private static ArrayList<Recipient> generateResultArr(ResultSet rs) throws SQLException
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
	
	

	public static void printArr(ArrayList<Recipient> arr)
	{
		System.out.println("fName   |   lName   |   email   |   recieved");
		for(int i = 0; i < arr.size(); i++)
		{
			System.out.println(arr.get(i));	
		}
	}
	
	public ArrayList<Recipient> printUsersByLetter(String s) throws SQLException
	{
		PreparedStatement pStmt = c.prepareStatement("SELECT * FROM USERS WHERE LNAME LIKE ?;");
		s += "%";
		pStmt.setString(1, s);
		ResultSet rs = pStmt.executeQuery();
		ArrayList<Recipient> arr = generateResultArr(rs);
		pStmt.close();
		return arr;   
	}
}	
	
	