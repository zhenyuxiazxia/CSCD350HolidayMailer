import java.util.Scanner;

	
public class MyLoginAuthenticator {


	   private String userName = "";
	   private String password = "";
	   
	   public MyLoginAuthenticator(){
	   }
	   
	   public MyLoginAuthenticator(String userN, String passW){
	      this.userName = userN;
	      this.password = passW;
	   }
	   
	   public String getUserName(){
	      return this.userName;
	   }
	   
	   public String getPassword(){
	      return this.password;
	   }
	   
	   public void setUserName(String s){
	      this.userName = s;
	   }
	   
	   public void setPassword(String s){
	      this.password = s;
	   }
	   
	   public void enterUserInfo(){
	      String userN;
	      String passW;
	      Scanner sc = new Scanner(System.in);
	      
	      System.out.println("Please enter your username: ");
	      userN = sc.next();
	      setUserName(userN);
	      System.out.println("Please enter your password: ");
	      passW = sc.next();
	      setPassword(passW);
	   }
	   
}
