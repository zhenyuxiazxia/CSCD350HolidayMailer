import java.io.Serializable;

/**
 * 
 */

/**
 * @author costco
 *
 */
public class UserInfo implements Serializable 
{
	private String userEmail;
	private MailService service;
	private String coverEmailText;
	
	public UserInfo()
	{
		this.userEmail="";
		this.service=null;
		this.coverEmailText="";
	}
	public UserInfo(String email, MailService provider, String coverText)
	{
		this.userEmail=email;
		this.service=provider;
		this.coverEmailText=coverText;
	}
	
	public void save()
	{
		//this.
	}
}
