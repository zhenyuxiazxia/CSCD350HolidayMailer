public class Gmail extends MailService{

   final private String serviceName = "Gmail";
   final private String hostServerName = "smtp.gmail.com";
   final private int port = 587;
   
   public Gmail(){}
   
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