public class Yahoo extends MailService{
   
   final String serviceName = "Yahoo"; 
   final private String hostServerName = "smtp.mail.yahoo.com";
   final private int port = 587;
   
   public Yahoo(){}
   
   public String getServiceName()
   {
	   return this.serviceName;
   }
   
   public String getHostServerName()
   {
      return this.hostServerName;
   }
   
   public int getPort()
   {
      return this.port;
   }
   
   public String toString()
   {
	      return serviceName + ": "+ hostServerName + " Port: " + port;
   }
}