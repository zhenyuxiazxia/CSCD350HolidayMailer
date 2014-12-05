public class Hotmail extends MailService{

   final private String serviceName = "Hotmail";
   final private String hostServerName = "smtp.live.com";
   final private int port = 587;
   
   public Hotmail(){}
   
   public String getServiceName(){
	   return this.serviceName;
   }
   
   public String getHostServerName(){
      return this.hostServerName;
   }
   
   public int getPort(){
      return this.port;
   }
   
   public String toString(){
	      return serviceName + ": "+ hostServerName + " Port: " + port;
   }
}