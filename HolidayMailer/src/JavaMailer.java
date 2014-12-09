import java.util.Properties;
import java.util.Scanner;

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


public class JavaMailer {

	private Session session=null;
	
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
    	
		if (service.compareTo("gmail")==0)
		{
			ms = new Gmail();
		}
		if (service.compareTo("hotmail")==0)
		{
			ms = new Hotmail();
		}
		if (service.compareTo("yahoo")==0)
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
  /*  public static void send(Session session, String[] recipientList){
    	
    	Scanner sc = new Scanner(System.in);
    	String query = "";
    	Email email = new Email();
    	email.enterSubject();
    	email.enterTextBody();
    	
        try {
        	for(String rec : recipientList){ //change to (Recipient rec : recipientList) when database gets implemented
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(myauth.getUserName()));
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(rec));
	            message.setSubject(email.getSubject());
	            message.setText(email.getTextBody());
	            System.out.println("Attach a document? ");
	        	query = sc.next();
	            if(query.equals("yes") || query.equals("y")){
	            	 BodyPart messageBodyPart = new MimeBodyPart();
	            	 message = attach(messageBodyPart, message, email);
	            }
//	    	    } else {
//	    	    	message.setText(email.getTextBody());
//	    	    }
	    	    
	            Transport.send(message);
	
	            System.out.println("The eMail has been successfully sent to: " + rec); //change rec to rec.getEmailAddress(); once Recipient object is implemented
        	}
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    
	/**
	 * This method attaches the file to email
	 * @param messageBodyPart
	 * @param message
	 * @return
	 */
    public static Message attach(BodyPart messageBodyPart, Message message, Email email){
    	try{
    		messageBodyPart.setText(email.getTextBody());
        	Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            String filename = "C:/Users/bmei/MerryChristmas.jpeg"; //location of the attached file
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);
            message.setContent(multipart);
    	} catch (MessagingException e) {
    		throw new RuntimeException(e);
    	}
        
	   return message;
    } //end of attach 
    
    /**
     *  TEMPORARILY UNDER CONSTRUCTION
     */
    
}//end of JavaMailer Class