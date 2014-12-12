import java.util.ArrayList;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class JavaMailer 
{

	private static Session session=null;
	
	/**
	 * This is the main program that manages sending mail.
	 */
    public void authenticate(String service, String email, String password)
    {
    	MailService ms = serviceSelector(service);
        Properties props = setProperties(ms);
        session = setSession(props, email, password);
        System.out.println("Authenticated");
    }
    
    
    /**
     * This method selects one of the three Email Services 
     * 1. Gmail
     * 2. Hotmail
     * 3. Yahoo
     * @return
     */
    private static MailService serviceSelector(String service)
    {
    	MailService ms = null;
    	
		if (service.compareTo("Gmail")==0)
		{
			ms = new Gmail();
		}
		if (service.compareTo("Hotmail")==0)
		{
			ms = new Hotmail();
		}
		if (service.compareTo("Yahoo")==0)
		{
			ms = new Yahoo();
		}
		
    	return ms;
    }//end of serviceSelector()
    
    /**
     * This method sets connection/authentication properties for email clients
     * @param ms
     * @return
     */
    private static Properties setProperties(MailService ms)
    {
        Properties tempprops = new Properties();
        tempprops.put("mail.smtp.starttls.enable", "true");
        tempprops.put("mail.smtp.auth", "true");
        tempprops.put("mail.smtp.host", ms.getHostServerName()); 
        tempprops.put("mail.smtp.port", ms.getPort());
        return tempprops;
    }//end of setProperties

    /**
     * This method set up the connection session for email clients. 
     * @param myauth
     * @param props
     * @return
     */
    private static Session setSession(Properties props, final String email, final String password)
    {
        Session session = Session.getInstance(props, new javax.mail.Authenticator() 
        {
            protected PasswordAuthentication getPasswordAuthentication() 
            {
                return new PasswordAuthentication(email, password);
            }
        });
        return session;
    }//end of setSession
    
    /**
     * This method sends the email. 
     * @param myauth
     * @param session
     * @param recipientList
     */
    public static void send(String subject, String body, String from, ArrayList<String> recipientList, String attachment) throws MessagingException
    {
		for(String rec : recipientList)
		{
	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress(from));
	        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
	        message.setSubject(subject);
	        message.setText(body);
	    	 if(attachment!=null)
	    	 {
	    		 BodyPart messageBodyPart = new MimeBodyPart();
	    		 message = attach(messageBodyPart, message,subject,body, attachment);
	    	 }
	    	 Transport.send(message);

            }
        }
        
    	/**
    	 * This method attaches the file to email
    	 * @param messageBodyPart
    	 * @param message
    	 * @return
    	 */
        public static Message attach(BodyPart messageBodyPart, Message message, String text, String body, String filename){
        	try
        	{
        		messageBodyPart.setText(body);
            	Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(messageBodyPart);
                messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(filename);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(filename);
                multipart.addBodyPart(messageBodyPart);
                message.setContent(multipart);
        	} catch (MessagingException e) 
        	{
        		throw new RuntimeException(e);
        	}
            
    	   return message;
        } //end of attach 
    
    /**
     *  TEMPORARILY UNDER CONSTRUCTION
     */
    
}//end of JavaMailer Class