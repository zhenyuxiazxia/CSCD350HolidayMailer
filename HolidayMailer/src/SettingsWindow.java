import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class SettingsWindow 
{

	protected Shell shlSuperUltraAlpha;
	private Text emailTextBox;
	private DisposeListener ds;
	private Text text;
	private Text txtMerryChristmasFrom;
	


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
	 * 
	 */
	protected void createContents() 
	{
		shlSuperUltraAlpha = new Shell();
		shlSuperUltraAlpha.setSize(450, 300);
		shlSuperUltraAlpha.setText("Super Ultra Alpha Holiday Mailer Deluxe Plus EX Game of the Year edition");
		shlSuperUltraAlpha.addDisposeListener(this.ds);
		
		Combo providerCombo = new Combo(shlSuperUltraAlpha, SWT.READ_ONLY);
		providerCombo.setItems(new String[] {"Gmail", "Yahoo", "Hotmail"});
		providerCombo.setBounds(10, 42, 91, 23);
		
		Label lblEmailProvider = new Label(shlSuperUltraAlpha, SWT.NONE);
		lblEmailProvider.setBounds(10, 21, 91, 15);
		lblEmailProvider.setText("Email provider");
		
		Label lblEmailAddress = new Label(shlSuperUltraAlpha, SWT.NONE);
		lblEmailAddress.setBounds(10, 83, 91, 15);
		lblEmailAddress.setText("Email Address");
		
		emailTextBox = new Text(shlSuperUltraAlpha, SWT.BORDER);
		emailTextBox.setBounds(10, 104, 179, 21);
		
		Button enterButton = new Button(shlSuperUltraAlpha, SWT.NONE);
		enterButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				String emailRegex="(?!\\..*|.*\\.@|.*\\.\\..*)([\\w\\!#\\$%&'\\*\\+\\-/\\=\\?\\^_`\\{\\|\\}~\\.])+@([\\w\\-\\.]+)(\\.\\w{2,})+";
				String userEmail=emailTextBox.getText();
				if (!userEmail.matches(emailRegex))
				{
					MessageBox mb=new MessageBox(shlSuperUltraAlpha, SWT.ICON_ERROR | SWT.OK);
					mb.setMessage("Not a valid email");
					mb.open();
				}
			}
		});
		enterButton.setToolTipText("Enter email address");
		enterButton.setBounds(10, 192, 75, 25);
		enterButton.setText("Enter");
		
		text = new Text(shlSuperUltraAlpha, SWT.BORDER | SWT.PASSWORD);
		text.setBounds(10, 165, 179, 21);
		
		Label lblPassword = new Label(shlSuperUltraAlpha, SWT.NONE);
		lblPassword.setBounds(10, 144, 55, 15);
		lblPassword.setText("Password");
		
		txtMerryChristmasFrom = new Text(shlSuperUltraAlpha, SWT.BORDER);
		txtMerryChristmasFrom.setToolTipText("Write the text that will appear for your cover emails");
		txtMerryChristmasFrom.setText("Merry Christmas from Bits Please!");
		txtMerryChristmasFrom.setBounds(222, 104, 202, 147);
		
		Label lblCoverEmailText = new Label(shlSuperUltraAlpha, SWT.NONE);
		lblCoverEmailText.setBounds(222, 83, 91, 15);
		lblCoverEmailText.setText("Cover Email Text");
		
			
		

	}

	public void setDisposeListener(DisposeListener ds3) 
	{
		ds=ds3;	
	}
}
