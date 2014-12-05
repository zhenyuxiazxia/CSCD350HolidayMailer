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

	final MyLoginAuthenticator myauth = new MyLoginAuthenticator();
	
	/**
	 * This is the main program that manages sending mail.
	 */
    public void sendMail(){
    	
    	MailService ms = serviceSelector();
    	
  	    myauth.enterUserInfo();  //String username = "cscd350bitsplease"; String password = "TomCapaul350";
        
        Properties props = setProperties(ms);
        Session session = setSession(myauth, props);
        
        String[] recipientList = new String[1];
        recipientList[0] = "billbinmei@hotmail.com";

        send(myauth, session, recipientList);
    }//end of sendMail()
    
    
    /**
     * This method selects one of the three Email Services 
     * 1. Gmail
     * 2. Hotmail
     * 3. Yahoo
     * @return
     */
    public static MailService serviceSelector(){
    	MailService ms = null;
    	int choice = 0;   
    	
		System.out.println("Please choose the following service: ");
		System.out.println("Press 1 to choose Gmail");
		System.out.println("Press 2 to choose Hotmail");
		System.out.println("Press 3 to choose Yahoo");
		
		Scanner sc = new Scanner(System.in);
		choice = sc.nextInt();
		if (choice == 1){
			ms = new Gmail();
		}
		if (choice == 2){
			ms = new Hotmail();
		}
		if (choice == 3){
			ms = new Yahoo();
		}
		
    	return ms;
    }//end of serviceSelector()
    
    /**
     * This method sets connection/authentication properties for email clients
     * @param ms
     * @return
     */
    public static Properties setProperties(MailService ms){
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
    public static Session setSession(final MyLoginAuthenticator myauth, Properties props){
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(myauth.getUserName(), myauth.getPassword());
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
    public static void send(MyLoginAuthenticator myauth, Session session, String[] recipientList){
    	
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