
public class Recipient 
{

	private String firstName = "";
	private String lastName = "";
	private String emailAddress = "";
   private int hasMailedBefore = 0;
   
   Recipient(){}
   
   Recipient(String f, String l, String e, int h)
   {
      this.firstName = f;
      this.lastName = l;
      this.emailAddress = e;
      this.hasMailedBefore = h;
   }
   
   public String getFirstName()
   {
      return this.firstName;
   }
   
   public String getLastName()
   {
      return this.lastName;
   }
   
   public String getAddress()
   {  
      return this.emailAddress;
   }
   
   public int getMailedBefore()
   {
      return this.hasMailedBefore;
   }
   
   public String toString()
   {
      return this.firstName + " " + this.lastName + " " + this.emailAddress + " " + this.hasMailedBefore;
   }
}
