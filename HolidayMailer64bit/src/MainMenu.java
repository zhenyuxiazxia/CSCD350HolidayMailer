

import java.io.IOException;
import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class MainMenu 
{

	protected Shell shlHolidayMailer;
	protected Display display;
	protected SQLiteMailerJDB database;
	protected MusicPlayer mp;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		SQLiteMailerJDB database;
		try 
		{
			MusicPlayer mp=new MusicPlayer();
			mp.start();
			MainMenu window = new MainMenu();
			database=new SQLiteMailerJDB();
			window.setDatabase(database);
			window.setPlayer(mp);

			window.open();
			database.closeConn();
			mp.kill();
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}


	private void setPlayer(MusicPlayer mp2) 
	{
		this.mp=mp2;
	}

	private void setDatabase(SQLiteMailerJDB database2) 
	{
		this.database=database2;
	}

	/**
	 * Open the window.
	 */
	public void open() 
	{
		display = Display.getDefault();
		createContents();
		shlHolidayMailer.open();
		shlHolidayMailer.layout();
		while (!shlHolidayMailer.isDisposed()) 
		{
			if (!display.readAndDispatch()) 
			{
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() 
	{
		shlHolidayMailer = new Shell();
		shlHolidayMailer.setSize(450, 300);
		shlHolidayMailer.setText("Bits Please Holiday Mailer");
		shlHolidayMailer.setLayout(null);
		
		Button mailButton = new Button(shlHolidayMailer, SWT.NONE);
		mailButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				try
				{
					UserInfo user=UserInfo.load();
					MailWindow mailWindow=new MailWindow();
					shlHolidayMailer.setEnabled(false);
					DisposeListener ds=new DisposeListener(){
	
						@Override
						public void widgetDisposed(DisposeEvent e) 
						{
							shlHolidayMailer.setEnabled(true);
							
						}};
					mailWindow.setDisposeListener(ds);
					mailWindow.setDatabase(database);
					mailWindow.setUserInfo(user);
					mailWindow.open();
				}
				catch (IOException ex)
				{
					MessageBox error=new MessageBox(shlHolidayMailer);
					error.setMessage("Please enter an email address in settings before sending an email");
					error.open();
				}
				
			}
		});
		mailButton.setToolTipText("Write a new Holiday email");
		mailButton.setBounds(138, 37, 149, 67);
		mailButton.setText("New Message");
		mailButton.setImage(new Image(display, "Mail.png"));
		
		Button contactsButton = new Button(shlHolidayMailer, SWT.NONE);
		contactsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				ContactsWindow cw=new ContactsWindow();
				shlHolidayMailer.setEnabled(false);
				DisposeListener ds2=new DisposeListener()
				{
					@Override
					public void widgetDisposed(DisposeEvent e) 
					{
						shlHolidayMailer.setEnabled(true);
						
					}
				};
				cw.setDisposeListener(ds2);
				cw.setDatabase(database);
				cw.open();
				
			}
		});
		contactsButton.setToolTipText("Manage your contacts");
		contactsButton.setBounds(41, 150, 149, 67);
		contactsButton.setText("Contacts");
		contactsButton.setImage(new Image(display, "Person.png"));
		
		Button settingsButton = new Button(shlHolidayMailer, SWT.NONE);
		settingsButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				SettingsWindow sw=new SettingsWindow();
				DisposeListener ds3=new DisposeListener()
				{
					@Override
					public void widgetDisposed(DisposeEvent e) 
					{
						shlHolidayMailer.setEnabled(true);
					}};
				shlHolidayMailer.setEnabled(false);
				sw.setDisposeListener(ds3);
				sw.setPlayer(mp);
				sw.open();
			}
		});
		settingsButton.setToolTipText("Manage Settings");
		settingsButton.setBounds(260, 150, 149, 67);
		settingsButton.setText("Settings");
		settingsButton.setImage(new Image(display, "Wrench.png"));

	}
}
