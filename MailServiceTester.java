
public class MailServiceTester {

	   public static void main(String[] args){
	      
	      MailService ms1 = new Gmail();
	      System.out.println(ms1.getServiceName());
	      System.out.println(ms1.getHostServerName());
	      System.out.println(ms1.getPort());
	      
	      MailService ms2 = new Hotmail();
	      System.out.println(ms2.getServiceName());
	      System.out.println(ms2.getHostServerName());
	      System.out.println(ms2.getPort());
	      
	      MailService ms3 = new Yahoo();
	      System.out.println(ms3.getServiceName());
	      System.out.println(ms3.getHostServerName());
	      System.out.println(ms3.getPort());
	   }

}
