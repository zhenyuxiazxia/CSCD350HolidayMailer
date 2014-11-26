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

	protected Object result;
	protected Shell shell;
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
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() 
	{
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setSize(375, 300);
		shell.setText(getText());
		
		Label lblSendCoverEmail = new Label(shell, SWT.NONE);
		lblSendCoverEmail.setBounds(10, 31, 126, 15);
		lblSendCoverEmail.setText("Send Cover Email To:");
		
		Button selectAllRadio = new Button(shell, SWT.RADIO);
		selectAllRadio.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				for (int x=0; x<contactTable.getItemCount(); x++)
				{
					contactTable.getItem(x).setChecked(true);
				}
			}
		});
		selectAllRadio.setBounds(10, 63, 90, 16);
		selectAllRadio.setText("Select All");
		
		Button previousEmailRadio = new Button(shell, SWT.RADIO);
		previousEmailRadio.setBounds(10, 94, 222, 16);
		previousEmailRadio.setText("Select those that sent a previous email");
		
		contactTable = new Table(shell, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		contactTable.setBounds(10, 129, 344, 132);
		contactTable.setHeaderVisible(true);
		contactTable.setLinesVisible(true);
		
		TableColumn tblclmnFirstname = new TableColumn(contactTable, SWT.NONE);
		tblclmnFirstname.setWidth(120);
		tblclmnFirstname.setText("First Name");
		
		TableColumn tblclmnLastName = new TableColumn(contactTable, SWT.NONE);
		tblclmnLastName.setWidth(120);
		tblclmnLastName.setText("Last Name");
		
		TableColumn tblclmnPreviousEmail = new TableColumn(contactTable, SWT.NONE);
		tblclmnPreviousEmail.setWidth(100);
		tblclmnPreviousEmail.setText("Previous email?");

		TableItem newitem=new TableItem(contactTable, SWT.NONE, 0);
		newitem.setText(0, "Spencer");
		newitem.setText(1, "Tyree");
		newitem.setText(2, "No");
		
		TableItem newitem2=new TableItem(contactTable, SWT.NONE, 1);
		newitem2.setText(0, "John");
		newitem2.setText(1, "Coppinger");
		newitem2.setText(2, "No");
		
		TableItem newitem3=new TableItem(contactTable, SWT.NONE, 2);
		newitem3.setText(0, "Bin");
		newitem3.setText(1, "Mei");
		newitem3.setText(2, "yes");
		
		TableItem newitem4=new TableItem(contactTable, SWT.NONE, 3);
		newitem4.setText(0, "Zhenyu");
		newitem4.setText(1, "Xia");
		newitem4.setText(2, "yes");
	}
}
