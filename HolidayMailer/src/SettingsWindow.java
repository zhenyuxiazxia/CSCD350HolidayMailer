import java.io.IOException;

import org.eclipse.swt.events.DisposeListener;
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
	private Text coverEmailText;
	protected MusicPlayer mp;

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
		shlSuperUltraAlpha.setText("Settings");
		shlSuperUltraAlpha.addDisposeListener(this.ds);
		
		final Combo providerCombo = new Combo(shlSuperUltraAlpha, SWT.READ_ONLY);
		providerCombo.setItems(new String[] {"Gmail", "Yahoo", "Hotmail"});
		providerCombo.setBounds(10, 104, 91, 23);
		
		Label lblEmailProvider = new Label(shlSuperUltraAlpha, SWT.NONE);
		lblEmailProvider.setBounds(10, 83, 91, 15);
		lblEmailProvider.setText("Email provider");
		
		Label lblEmailAddress = new Label(shlSuperUltraAlpha, SWT.NONE);
		lblEmailAddress.setBounds(10, 143, 91, 15);
		lblEmailAddress.setText("Email Address");
		
		emailTextBox = new Text(shlSuperUltraAlpha, SWT.BORDER);
		emailTextBox.setBounds(10, 165, 179, 21);
		
		coverEmailText = new Text(shlSuperUltraAlpha, SWT.BORDER);
		coverEmailText.setToolTipText("Write the text that will appear for your cover emails");
		coverEmailText.setText("Merry Christmas from Bits Please!");
		coverEmailText.setBounds(222, 104, 202, 147);
		
		try
		{
			UserInfo info=UserInfo.load();
			emailTextBox.setText(info.getEmailAddress());
			coverEmailText.setText(info.getCoverEmailText());
			providerCombo.setText(info.getProvider());
			//providerCombo.set
		}
		catch (IOException ex)
		{
			//no preset settings
		}
		Button enterButton = new Button(shlSuperUltraAlpha, SWT.NONE);
		enterButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				MessageBox mb=new MessageBox(shlSuperUltraAlpha, SWT.ICON_ERROR | SWT.OK);
				String emailRegex="(?!\\..*|.*\\.@|.*\\.\\..*)([\\w\\!#\\$%&'\\*\\+\\-/\\=\\?\\^_`\\{\\|\\}~\\.])+@([\\w\\-\\.]+)(\\.\\w{2,})+";
				String userEmail=emailTextBox.getText(), provider=providerCombo.getText();
				if (!userEmail.matches(emailRegex))
				{
					mb.setMessage("Not a valid email");
					mb.open();
				}
				else if(provider.compareTo("")==0)
				{
					mb.setMessage("Please select an email provider");
					mb.open();
				}
				else if(!userEmail.contains(provider.toLowerCase()) && !userEmail.contains(provider.toUpperCase()))
				{
					mb.setMessage("email address does not match provider");
					mb.open();
				}
				else
				{
					UserInfo newInfo=new UserInfo(userEmail, provider, coverEmailText.getText());
					newInfo.save();
					mb=new MessageBox(shlSuperUltraAlpha, SWT.OK);
					mb.setMessage("Settings successfully saved");
					mb.open();
				}
			}
		});
		enterButton.setToolTipText("Enter email address");
		enterButton.setBounds(10, 202, 75, 25);
		enterButton.setText("Enter");
		
		Label lblCoverEmailText = new Label(shlSuperUltraAlpha, SWT.NONE);
		lblCoverEmailText.setBounds(222, 83, 91, 15);
		lblCoverEmailText.setText("Cover Email Text");
		
		Label lblMusic = new Label(shlSuperUltraAlpha, SWT.NONE);
		lblMusic.setBounds(222, 21, 55, 15);
		lblMusic.setText("Music");
		
		
		
		final Button onRadio = new Button(shlSuperUltraAlpha, SWT.RADIO);
		onRadio.setSelection(true);
		onRadio.setBounds(223, 49, 37, 16);
		onRadio.setText("On");
		
		final Button offRadio = new Button(shlSuperUltraAlpha, SWT.RADIO);
		offRadio.setBounds(276, 49, 37, 16);
		offRadio.setText("Off");
		
		SelectionAdapter musicListener=new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				//check which button is selected
				if (onRadio.getSelection())
				{
					if (!mp.getPlay())
					{
						mp=new MusicPlayer();
						mp.start();
					}
				}
				else if (offRadio.getSelection())
				{
					if (mp.getPlay())
					{
						mp.kill();
					}
				}
			}
		};
		offRadio.addSelectionListener(musicListener);
		onRadio.addSelectionListener(musicListener);
	}

	public void setDisposeListener(DisposeListener ds3) 
	{
		ds=ds3;	
	}

	public void setPlayer(MusicPlayer mp) 
	{
		this.mp=mp;
		
	}


}
