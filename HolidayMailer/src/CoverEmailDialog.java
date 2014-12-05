import java.util.ArrayList;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class CoverEmailDialog extends Dialog 
{

	protected int result;
	protected ArrayList<String> emails;
	protected Shell shlSelectRecipients;
	private Table contactTable;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public CoverEmailDialog(Shell parent, int style) 
	{
		super(parent, style);
		setText("SWT Dialog");
		emails=null;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public ArrayList<String> open() 
	{
		createContents();
		shlSelectRecipients.open();
		shlSelectRecipients.layout();
		Display display = getParent().getDisplay();
		while (!shlSelectRecipients.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return emails;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shlSelectRecipients = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shlSelectRecipients.setSize(375, 340);
		shlSelectRecipients.setText("Select Recipients");
		
		Label lblSendCoverEmail = new Label(shlSelectRecipients, SWT.NONE);
		lblSendCoverEmail.setBounds(10, 31, 126, 15);
		lblSendCoverEmail.setText("Send Cover Email To:");
		
	
		final Button selectAllRadio = new Button(shlSelectRecipients, SWT.RADIO);
		//selectAllRadio.addSelectionListener(radioEvent);
		selectAllRadio.setBounds(10, 63, 90, 16);
		selectAllRadio.setText("Select All");
		
		final Button previousEmailRadio = new Button(shlSelectRecipients, SWT.RADIO);
		previousEmailRadio.setBounds(10, 94, 222, 16);
		previousEmailRadio.setText("Select those that sent a previous email");
		
		SelectionAdapter radioEvent=new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				//check to see which radio button is selected
				if (selectAllRadio.getSelection())
				{
					for (int x=0; x<contactTable.getItemCount(); x++)
					{
						contactTable.getItem(x).setChecked(true);
					}
				}
				else if(previousEmailRadio.getSelection())
				{
					for (int x=0; x<contactTable.getItemCount(); x++)
					{
						TableItem item=contactTable.getItem(x);
						if(item.getText(3)=="yes")
							item.setChecked(true);
						else
							item.setChecked(false);
					}
				}
			}
		};
		//add listener to radio buttons
		selectAllRadio.addSelectionListener(radioEvent);
		previousEmailRadio.addSelectionListener(radioEvent);
		
		contactTable = new Table(shlSelectRecipients, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		contactTable.setBounds(10, 129, 349, 132);
		contactTable.setHeaderVisible(true);
		contactTable.setLinesVisible(true);
		
		TableColumn tblclmnFirstname = new TableColumn(contactTable, SWT.NONE);
		tblclmnFirstname.setWidth(120);
		tblclmnFirstname.setText("First Name");
		
		TableColumn tblclmnLastName = new TableColumn(contactTable, SWT.NONE);
		tblclmnLastName.setWidth(120);
		tblclmnLastName.setText("Last Name");
		
		TableColumn tblclmnEmail = new TableColumn(contactTable, SWT.NONE);
		tblclmnEmail.setWidth(100);
		tblclmnEmail.setText("Email");
		
		TableColumn tblclmnPreviousEmail = new TableColumn(contactTable, SWT.NONE);
		tblclmnPreviousEmail.setWidth(100);
		tblclmnPreviousEmail.setText("Previous email?");

		TableItem newitem=new TableItem(contactTable, SWT.NONE, 0);
		newitem.setText(0, "Spencer");
		newitem.setText(1, "Tyree");
		newitem.setText(2, "holycowrap@hotmail.com");
		newitem.setText(3, "No");
		
		TableItem newitem2=new TableItem(contactTable, SWT.NONE, 1);
		newitem2.setText(0, "John");
		newitem2.setText(1, "Coppinger");
		newitem2.setText(2, "youknownothingjohncoppinger@hotmail.com");
		newitem2.setText(3, "No");
		
		TableItem newitem3=new TableItem(contactTable, SWT.NONE, 2);
		newitem3.setText(0, "Bin");
		newitem3.setText(1, "Mei");
		newitem3.setText(2, "billbinmei@hotmail.com");
		newitem3.setText(3, "yes");
		
		TableItem newitem4=new TableItem(contactTable, SWT.NONE, 3);
		newitem4.setText(0, "Zhenyu");
		newitem4.setText(1, "Xia");
		newitem4.setText(2, "zxia@eagles.ewu.edu");
		newitem4.setText(3, "yes");
		
		Button okButton = new Button(shlSelectRecipients, SWT.NONE);
		okButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				result=SWT.OK;
				emails=new ArrayList<String>();
				TableItem [] items=contactTable.getItems();
				for (int i=0; i<items.length; i++)
				{
					if (items[i].getChecked())
						emails.add(items[i].getText(2));
				}
				shlSelectRecipients.close();
			}
		});
		okButton.setBounds(10, 276, 75, 25);
		okButton.setText("Ok");
		
		Button cancelButton = new Button(shlSelectRecipients, SWT.NONE);
		cancelButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				result=SWT.CANCEL;
				shlSelectRecipients.close();
			}
		});
		cancelButton.setBounds(279, 276, 75, 25);
		cancelButton.setText("Cancel");
	}
}
