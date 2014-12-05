public class MyLoginAuthenticatorTester{
   
   public static void main(String[] args){
	  MyLoginAuthenticator auth = new MyLoginAuthenticator();
      auth.enterUserInfo();
      System.out.println("The username entered is: " + auth.getUserName());
      System.out.println("The password entered is: " + auth.getPassword());
   }
}