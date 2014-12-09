import java.io.File;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class MailWindow 
{

	protected Shell shlSuperUltraAlpha;
	protected SQLiteMailerJDB database;
	private Text subjectTextBox;
	private Text toTextBox;
	private Text messageTextBox;
	private DisposeListener ds=null;
	private ArrayList<String> toList=null;
	private boolean canOpen=true;

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() 
	{
		Display display = Display.getDefault();
		createContents();
		shlSuperUltraAlpha.open();
		shlSuperUltraAlpha.layout();
		while (!shlSuperUltraAlpha.isDisposed()) 
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
		shlSuperUltraAlpha = new Shell();
		shlSuperUltraAlpha.setText("Holiday Mailer");
		shlSuperUltraAlpha.setSize(575, 360);
		if(ds!=null)
			shlSuperUltraAlpha.addDisposeListener(this.ds);
		subjectTextBox = new Text(shlSuperUltraAlpha, SWT.BORDER);
		subjectTextBox.setBounds(10, 76, 401, 21);
		
		Label lblSubject = new Label(shlSuperUltraAlpha, SWT.NONE);
		lblSubject.setBounds(10, 55, 55, 15);
		lblSubject.setText("Subject:");
		
		toTextBox = new Text(shlSuperUltraAlpha, SWT.BORDER);
		toTextBox.setBounds(10, 28, 401, 21);
		if (toList!=null)
		{
			addAddresses();
		}
		Label lblTo = new Label(shlSuperUltraAlpha, SWT.NONE);
		lblTo.setBounds(10, 7, 55, 15);
		lblTo.setText("To:");
		
		Label lblMessage = new Label(shlSuperUltraAlpha, SWT.NONE);
		lblMessage.setBounds(10, 104, 55, 15);
		lblMessage.setText("Message:");
		
		final Label attachmentLabel = new Label(shlSuperUltraAlpha, SWT.NONE);
		attachmentLabel.setBounds(10, 268, 103, 15);
		
		messageTextBox = new Text(shlSuperUltraAlpha, SWT.BORDER);
		messageTextBox.setBounds(10, 125, 401, 126);
		
		Button btnAttach = new Button(shlSuperUltraAlpha, SWT.NONE);
		btnAttach.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				FileDialog filedong=new FileDialog(shlSuperUltraAlpha, SWT.OPEN);
				String file=filedong.open();
				if (file!=null)
				{
					
					File f=new File(file);
					String filename=f.getName();
					//add attachment to file
					attachmentLabel.setText(filename);
				}
				
			}
		});
		btnAttach.setToolTipText("Add an attachment");
		btnAttach.setBounds(10, 289, 75, 25);
		btnAttach.setText("Attach");
		
		SelectionAdapter sa=new SelectionAdapter()
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				if (canOpen)
				{
					canOpen=false;
					CoverEmailDialog d=new CoverEmailDialog(shlSuperUltraAlpha, SWT.OK|SWT.CANCEL, database);
					toList=d.open();
					if(toList!=null)
					{
						addAddresses();
					}
					canOpen=true;
				}
			}
		};
		final Button btnNormalEmail = new Button(shlSuperUltraAlpha, SWT.RADIO);
		btnNormalEmail.setBounds(119, 271, 90, 16);
		btnNormalEmail.setText("Normal email");
		btnNormalEmail.addSelectionListener(sa);
		final Button btnCoverEmail = new Button(shlSuperUltraAlpha, SWT.RADIO);
		btnCoverEmail.addSelectionListener(sa);
		btnCoverEmail.setBounds(119, 293, 90, 16);
		btnCoverEmail.setText("Cover email");
		
		Button sendButton = new Button(shlSuperUltraAlpha, SWT.NONE);
		sendButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				MessageBox m=new MessageBox(shlSuperUltraAlpha);
				if (toTextBox.getText().compareTo("")==0)
				{
					m.setMessage("Must enter a recipiant");
					m.open();
				}
				else if (messageTextBox.getText().compareTo("")==0)
				{
					m.setMessage("Message is empty!");
					m.open();
				}
				else if (!btnCoverEmail.getSelection() && !btnNormalEmail.getSelection())
				{
					m.setMessage("Select either normal email or cover email");
					m.open();
				}
			}
		});
		sendButton.setToolTipText("Send the email");
		sendButton.setBounds(282, 272, 129, 42);
		sendButton.setText("Send");
		
	}

	/**
	 * 
	 */
	private void addAddresses() 
	{
		String text="";
		for (int i=0; i<toList.size();i++)
		{
			text+=toList.get(i)+";";
		}
		toTextBox.setText(text);
	}

	public void setDisposeListener(DisposeListener ds) 
	{
		this.ds=ds;
	}

	public void setTo(ArrayList<String> emails) 
	{
		this.toList=emails;		
	}

	public void setDatabase(SQLiteMailerJDB database) 
	{
		this.database=database;
	}
}
