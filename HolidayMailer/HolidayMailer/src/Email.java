import java.util.Scanner;

public class Email {
	private String subject = "";
	private String textBody = "";
	
	public Email(){ }
	
	public void setSubject(String s){
		this.subject = s;
	}
	
	public void setTextBody(String s){
		this.textBody = s;
	}
	
	public String getSubject(){
		return this.subject;
	}
	
	public String getTextBody(){
		return this.textBody;
	}
	
	public void enterSubject(){
		System.out.println("Please enter the Subject: ");
		Scanner sc = new Scanner(System.in);
		this.subject = sc.nextLine();
	}
	
	public void enterTextBody(){
		System.out.println("Please enter the Email Body: ");
		Scanner sc = new Scanner(System.in);
		this.textBody = sc.nextLine();
	}
	
}
