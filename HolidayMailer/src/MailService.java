
public abstract class MailService
{


		   public String serviceName = "";
		   public String hostServerName = "";
		   public int port = 0;
		   
		   public String getServiceName(){
			   return this.serviceName;
		   }
		   
		   public String getHostServerName(){ 
			   return this.hostServerName;
		   };
		   
		   public int getPort(){
			   return this.port;
		   }
		   
}
