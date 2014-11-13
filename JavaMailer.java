//import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
//import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class JavaMailer {

    public static void main(String[] args) {

        final String username = "350bitsplease@gmail.com";
        final String password = "cscd350bitsplease";
        String[] recpientList = new String[4];
        recpientList[0] = "billbinmei@hotmail.com";
        recpientList[1] = "sjtyree@eagles.ewu.edu";
        recpientList[2] = "zxia@eagles.ewu.edu";
        recpientList[3] = "johndcoppinger@gmail.com";
        
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {
        	for(int i = 0; i < 4; i++){
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress("350bitsplease@gmail.com"));
	            message.setRecipients(Message.RecipientType.TO,
	                InternetAddress.parse(recpientList[i]));
	            message.setSubject("CSCD 350 Test eMail");
	            message.setText("This is a Test eMail for CSCD350 Holiday Mailer Project");
	
	            Transport.send(message);
	
	            System.out.println("The eMail has been successfully sent to: " + recpientList[i]);
        	}
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}