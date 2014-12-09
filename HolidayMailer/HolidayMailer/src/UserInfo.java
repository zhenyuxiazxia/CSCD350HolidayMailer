import java.util.*;
import java.io.*;
/**
 * 
 */

/**
 * @author costco
 *
 */
public class UserInfo implements Serializable 
{
	private String userEmail;
	private String service;
	private String coverEmailText;
	
	public UserInfo()
	{
		this.userEmail="";
		this.service=null;
		this.coverEmailText="";
	}
	public UserInfo(String email, String provider, String coverText)
	{
		this.userEmail=email;
		this.service=provider;
		this.coverEmailText=coverText;
	}

	public String toString()
	{
		String str = "";

		str += userEmail + "\n";
		str += service + "\n";
		str += coverEmailText + "\n";
		return str;
	}
	
	public void save()
	{
	  try
      {
         FileOutputStream fileOut = new FileOutputStream("users.ser");
         ObjectOutputStream out   = new ObjectOutputStream(fileOut);
         out.writeObject(this);
         out.close();
         fileOut.close();
         System.out.printf("Serialized data is saved in users.ser");
      }catch(IOException i)
      {
          i.printStackTrace();
      }
	}

	public static UserInfo load()
	{
	  UserInfo read = null;
      try
      {
         FileInputStream fileIn = new FileInputStream("users.ser");
         ObjectInputStream in = new ObjectInputStream(fileIn);
         read = (UserInfo) in.readObject();
         in.close();
         fileIn.close();
      }catch(IOException i)
      {
         i.printStackTrace();
         //return;
      }catch(ClassNotFoundException c)
      {
         System.out.println("UserInfo class not found");
         c.printStackTrace();
         //return;
      }
      return read;
	}
}
