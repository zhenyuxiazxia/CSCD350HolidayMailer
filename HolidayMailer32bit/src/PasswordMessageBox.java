import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class PasswordMessageBox extends Dialog 
{

	protected String password;
	protected Shell shlEnterPassword=null;
	private Text text;
	private String userEmail;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PasswordMessageBox(Shell parent, int style, String userEmail) 
	{
		super(parent, style);
		setText("SWT Dialog");
		this.userEmail=userEmail;
	}

	/**
	 * Open the dialog.
	 * @return the user's password for their email address
	 */
	public String open() 
	{
		createContents();
		shlEnterPassword.open();
		shlEnterPassword.layout();
		Display display = getParent().getDisplay();
		while (!shlEnterPassword.isDisposed())
		{
			if (!display.readAndDispatch()) 
			{
				display.sleep();
			}
		}
		return password;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() 
	{
		shlEnterPassword = new Shell(getParent(), getStyle());
		shlEnterPassword.setSize(257, 201);
		shlEnterPassword.setText("Enter Password");
		
		Label passwordLabel = new Label(shlEnterPassword, SWT.NONE);
		passwordLabel.setBounds(20, 47, 223, 15);
		passwordLabel.setText("Enter your password for "+this.userEmail+":");
		
		text = new Text(shlEnterPassword, SWT.BORDER | SWT.PASSWORD);
		text.setBounds(20, 86, 223, 21);
		
		Button btnOk = new Button(shlEnterPassword, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				password=text.getText();
				shlEnterPassword.close();
			}
		});
		btnOk.setBounds(20, 125, 75, 25);
		btnOk.setText("Ok");
		
		Button btnCancel = new Button(shlEnterPassword, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				shlEnterPassword.close();
			}
		});
		btnCancel.setBounds(169, 125, 75, 25);
		btnCancel.setText("Cancel");

	}
}
