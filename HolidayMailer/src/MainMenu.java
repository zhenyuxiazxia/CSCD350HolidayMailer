

import java.sql.SQLException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class MainMenu 
{

	protected Shell shell;
	protected Display display;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) 
	{
		try 
		{
			MainMenu window = new MainMenu();
			SQLiteMailerJDB database=new SQLiteMailerJDB();
			
			window.open();
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

	/**
	 * Open the window.
	 */
	public void open() 
	{
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) 
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
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		shell.setLayout(null);
		
		Button mailButton = new Button(shell, SWT.NONE);
		mailButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				MailWindow mailWindow=new MailWindow();
				shell.setEnabled(false);
				DisposeListener ds=new DisposeListener(){

					@Override
					public void widgetDisposed(DisposeEvent e) 
					{
						shell.setEnabled(true);
						
					}};
				mailWindow.setDisposeListener(ds);
				mailWindow.open();
				
			}
		});
		mailButton.setToolTipText("Write a new Holiday email");
		mailButton.setBounds(138, 37, 149, 67);
		mailButton.setText("New Message");
		mailButton.setImage(new Image(display, "Mail.png"));
		
		Button contactsButton = new Button(shell, SWT.NONE);
		contactsButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				ContactsWindow cw=new ContactsWindow();
				shell.setEnabled(false);
				DisposeListener ds2=new DisposeListener(){
					@Override
					public void widgetDisposed(DisposeEvent e) 
					{
						shell.setEnabled(true);
						
					}};
				cw.setDisposeListener(ds2);
				cw.open();
				
			}
		});
		contactsButton.setToolTipText("Manage your contacts");
		contactsButton.setBounds(41, 150, 149, 67);
		contactsButton.setText("Contacts");
		contactsButton.setImage(new Image(display, "Person.png"));
		
		Button settingsButton = new Button(shell, SWT.NONE);
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
						shell.setEnabled(true);
					}};
				shell.setEnabled(false);
				sw.setDisposeListener(ds3);
				sw.open();
			}
		});
		settingsButton.setToolTipText("Manage Settings");
		settingsButton.setBounds(260, 150, 149, 67);
		settingsButton.setText("Settings");
		settingsButton.setImage(new Image(display, "Wrench.png"));

	}
}
