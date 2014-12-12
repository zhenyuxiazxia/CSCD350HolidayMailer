import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class AddWindowDialog extends Dialog 
{

	protected String[] newContact;
	protected Shell shlAddContact;
	private Text firstNameText;
	private Text lastNameText;
	private Text emailText;
	private String nameRegex="^([\\w'-]+)$";
	private String emailRegex="(?!\\..*|.*\\.@|.*\\.\\..*)([\\w\\!#\\$%&'\\*\\+\\-/\\=\\?\\^_`\\{\\|\\}~\\.])+@([\\w\\-\\.]+)(\\.\\w{2,})+";
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public AddWindowDialog(Shell parent, int style) 
	{
		super(parent, style);
		setText("SWT Dialog");
		newContact=null;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public String[] open()
	{
		createContents();
		shlAddContact.open();
		shlAddContact.layout();
		Display display = getParent().getDisplay();
		while (!shlAddContact.isDisposed()) 
		{
			if (!display.readAndDispatch()) 
			{
				display.sleep();
			}
		}
		return newContact;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() 
	{
		shlAddContact = new Shell(getParent(), getStyle());
		shlAddContact.setSize(300, 272);
		shlAddContact.setText("Add Contact");
		
		Label lblFirstName = new Label(shlAddContact, SWT.NONE);
		lblFirstName.setBounds(10, 10, 55, 15);
		lblFirstName.setText("First Name");
		
		firstNameText = new Text(shlAddContact, SWT.BORDER);
		firstNameText.setToolTipText("Enter a contact's first name");
		firstNameText.setBounds(10, 31, 120, 21);
		
		Label lblLastName = new Label(shlAddContact, SWT.NONE);
		lblLastName.setBounds(10, 58, 55, 15);
		lblLastName.setText("Last Name");
		
		lastNameText = new Text(shlAddContact, SWT.BORDER);
		lastNameText.setToolTipText("Enter the contact's last name");
		lastNameText.setBounds(10, 79, 120, 21);
		
		Label lblEmail = new Label(shlAddContact, SWT.NONE);
		lblEmail.setBounds(10, 106, 55, 15);
		lblEmail.setText("Email");
		
		emailText = new Text(shlAddContact, SWT.BORDER);
		emailText.setToolTipText("Enter the contact's email");
		emailText.setBounds(10, 127, 182, 21);
		
		Label lblPreviousEmail = new Label(shlAddContact, SWT.NONE);
		lblPreviousEmail.setBounds(10, 154, 104, 15);
		lblPreviousEmail.setText("Previous Email?");
		
		final Button btnYes = new Button(shlAddContact, SWT.RADIO);
		btnYes.setBounds(10, 175, 43, 16);
		btnYes.setText("Yes");
		
		Button btnNo = new Button(shlAddContact, SWT.RADIO);
		btnNo.setSelection(true);
		btnNo.setBounds(65, 175, 90, 16);
		btnNo.setText("No");

		Button btnOk = new Button(shlAddContact, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				MessageBox errorMsg=new MessageBox(shlAddContact);
				if(firstNameText.getText()=="")
				{
					errorMsg.setMessage("Must enter a first name");
					errorMsg.open();
				}
				else if(!firstNameText.getText().matches(nameRegex))
				{
					errorMsg.setMessage("Not a valid first name");
					errorMsg.open();
				}
				else if(lastNameText.getText()=="")
				{
					errorMsg.setMessage("Must enter a last name");
					errorMsg.open();
				}
				else if(!lastNameText.getText().matches(nameRegex))
				{
					errorMsg.setMessage("Not a valid last name");
					errorMsg.open();
				}
				else if(emailText.getText()=="")
				{
					errorMsg.setMessage("Must enter an email");
					errorMsg.open();
				}
				else if(!emailText.getText().matches(emailRegex))
				{
					errorMsg.setMessage("Not a valid email");
					errorMsg.open();
				}
				else
				{
					newContact=new String[4];
					newContact[0]=firstNameText.getText();
					newContact[1]=lastNameText.getText();
					newContact[2]=emailText.getText();
					newContact[3]=(btnYes.getSelection()) ? "1" : "0";
					shlAddContact.close();
				}
					
			}
		});
		btnOk.setBounds(10, 208, 75, 25);
		btnOk.setText("Ok");
		
		Button btnCancel = new Button(shlAddContact, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e)
			{
				shlAddContact.close();
			}
		});
		btnCancel.setBounds(209, 208, 75, 25);
		btnCancel.setText("Cancel");
		
		

	}
}
