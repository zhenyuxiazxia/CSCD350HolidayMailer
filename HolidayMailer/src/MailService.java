
public abstract class MailService
{
	   private String serviceName = "";
	   private String hostServerName = "";
	   private int port = 0;
	   
	   public String getServiceName()
	   {
		   return this.serviceName;
	   }
	   
	   public String getHostServerName()
	   { 
		   return this.hostServerName;
	   };
	   
	   public int getPort()
	   {
		   return this.port;
	   }
	   
}
