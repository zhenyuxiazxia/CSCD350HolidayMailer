import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;


public class ContactsWindow {

	protected Shell shell;
	private Table contactTable;
	private DisposeListener ds;
	protected SQLiteMailerJDB database;
	private Text letterText;
	

	/**
	 * Open the window.
	 * @wbp.parser.entryPoint
	 */
	public void open() 
	{
		Display display = Display.getDefault();
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
	protected void createContents() {
		shell = new Shell();
		shell.setSize(470, 300);
		shell.setText("SWT Application");
		shell.addDisposeListener(this.ds);
		
		Button addButton = new Button(shell, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				AddWindowDialog ad=new AddWindowDialog(shell, SWT.NONE);
				String[] newContact=ad.open();
				if(newContact!=null)
				{
					Recipient r=new Recipient(newContact[0], newContact[1], newContact[2], Integer.parseInt(newContact[3]));
					try
					{
						database.addUser(r);
						deleteAllContactsFromTable();
						addContactsToTable();
					} catch (SQLException e1) 
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("Recipient "+r.getFirstName()+" "+r.getLastName()+" received from add dialoge!");
				}
			}
		});
		addButton.setToolTipText("Add a new contact");
		addButton.setBounds(10, 145, 75, 25);
		addButton.setText("Add");
		
		Button deleteButton = new Button(shell, SWT.NONE);
		deleteButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				MessageBox deleteMessage;
				if(noneSelected())
				{
					deleteMessage=new MessageBox(shell);
					deleteMessage.setMessage("Must Select a contact first");
					deleteMessage.open();
				}
				else
				{
					deleteMessage=new MessageBox(shell, SWT.YES|SWT.NO);
					deleteMessage.setMessage("Are you sure you want to delete the selected contacts? They will be permanently deleted from the database.");
					if (deleteMessage.open()==SWT.YES)
					{
						System.out.println("ok I'm deleting your contacts");
						//delete items from table
						TableItem[] items=contactTable.getItems();
						for (int i=0; i<items.length; i++)
						{
							if(items[i].getChecked())
							{
								try
								{
									database.removeUser(new Recipient(items[i].getText(0), items[i].getText(1), items[i].getText(2), (items[i].getText(3)=="yes") ? 1 : 0));
								} catch (SQLException e1) 
								{
									e1.printStackTrace();
								}
							}
						}
						deleteAllContactsFromTable();
						addContactsToTable();
						//delete selected items from database
						//query database
						//add items back into table
					}
				}
			}
		});
		deleteButton.setToolTipText("Delete a contact");
		deleteButton.setBounds(10, 176, 75, 25);
		deleteButton.setText("Delete");
		
		Button sendEmailButton = new Button(shell, SWT.NONE);
		sendEmailButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				ArrayList<String> emails=new ArrayList<String>();
				TableItem [] cur=contactTable.getItems();
				for (int i=0; i<cur.length; i++)
				{
					if (cur[i].getChecked())
						emails.add(cur[i].getText(2));
				}
				MailWindow newwin=new MailWindow();
				newwin.setTo(emails);
				newwin.open();
			}
;		});
		sendEmailButton.setToolTipText("Send an email to the selected contacts");
		sendEmailButton.setBounds(10, 207, 149, 44);
		sendEmailButton.setText("Send Email");
		
		contactTable = new Table(shell, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
		contactTable.setBounds(10, 10, 435, 129);
		contactTable.setHeaderVisible(true);
		contactTable.setLinesVisible(true);
		
		TableColumn nameTableColumn = new TableColumn(contactTable, SWT.NONE);
		nameTableColumn.setWidth(107);
		nameTableColumn.setText("First Name");
		
		TableColumn tblclmnLastname = new TableColumn(contactTable, SWT.NONE);
		tblclmnLastname.setWidth(100);
		tblclmnLastname.setText("LastName");
		
		TableColumn emailTableColumn = new TableColumn(contactTable, SWT.NONE);
		emailTableColumn.setWidth(138);
		emailTableColumn.setText("Email");
		
		TableColumn previousYearTableColumn = new TableColumn(contactTable, SWT.NONE);
		previousYearTableColumn.setWidth(100);
		previousYearTableColumn.setText("Previous Year?");
		
		final Combo sortByCombo = new Combo(shell, SWT.READ_ONLY);
		sortByCombo.setToolTipText("Sort the entries by first or last name");
		sortByCombo.setItems(new String[] {"First Name", "Last Name", "Last Name, starting with"});
		sortByCombo.setBounds(317, 178, 91, 23);
		sortByCombo.setText("Sort By");
		
		letterText = new Text(shell, SWT.BORDER);
		letterText.setEnabled(false);
		letterText.setBounds(317, 230, 38, 21);
		
		final Label enterLetterLabel = new Label(shell, SWT.NONE);
		enterLetterLabel.setEnabled(false);
		enterLetterLabel.setBounds(317, 209, 66, 15);
		enterLetterLabel.setText("Enter letter");
		
		final Button okButton = new Button(shell, SWT.NONE);
		okButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				try
				{
					MessageBox ms=new MessageBox(shell, SWT.NONE);
					String letter=letterText.getText();
					if (letter.length()!=1)
					{
						ms.setMessage("Enter only one letter");
						ms.open();
					}
					else
					{
						char c=letter.charAt(0);
						if ((c>'z' || c<'a') && (c>'Z' || c<'A'))
						{
							ms.setMessage("That's not a letter, dumbshit");
							ms.open();
						}
						else
						{
							ArrayList<Recipient> list=database.printUsersByLetter(c+"");
							deleteAllContactsFromTable();
							addArrayListToTable(list);
						}
					}
				}
				catch (SQLException fuckyou)
				{
					fuckyou.printStackTrace();
				}
			}
		});
		okButton.setEnabled(false);
		okButton.setBounds(361, 230, 27, 25);
		okButton.setText("Ok");
		sortByCombo.addModifyListener(new ModifyListener()
		{
			@Override
			public void modifyText(ModifyEvent e) 
			{
				try
				{
					ArrayList<Recipient> newList=new ArrayList<Recipient>();
					if (sortByCombo.getText().compareTo("First Name")==0)
					{
						disableLetterStuff(enterLetterLabel, okButton);
						newList=database.printSortedFNameReverse();
						deleteAllContactsFromTable();
						addArrayListToTable(newList);
					}
					else if (sortByCombo.getText().compareTo("Last Name")==0)
					{
						disableLetterStuff(enterLetterLabel, okButton);
						newList=database.printSortedLNameReverse();
						deleteAllContactsFromTable();
						addArrayListToTable(newList);
					}
					else if (sortByCombo.getText().compareTo("Last Name, starting with")==0)
					{
						enableLetterStuff(enterLetterLabel, okButton);
						//newList=database.printUsersByLetter("");
					}
				}catch(SQLException turndownfor)
				{
					turndownfor.printStackTrace();
				}
			}

			/**
			 * @param enterLetterLabel
			 * @param okButton
			 */
			private void enableLetterStuff(final Label enterLetterLabel, final Button okButton) 
			{
				okButton.setEnabled(true);
				enterLetterLabel.setEnabled(true);
				letterText.setEnabled(true);
			}

			/**
			 * @param enterLetterLabel
			 * @param okButton
			 */
			private void disableLetterStuff(final Label enterLetterLabel, final Button okButton) 
			{
				okButton.setEnabled(false);
				enterLetterLabel.setEnabled(false);
				letterText.setEnabled(false);
			}
		});
		//adding entries in the database to the contact table
		addContactsToTable();
		/*Hardcoded data to test the table with hardcoded values
		TableItem newitem=new TableItem(contactTable, SWT.NONE, 0);
		newitem.setText(0, "Spencer");
		newitem.setText(1, "Tyree");
		newitem.setText(2, "holycowrap@hotmail.com");
		newitem.setText(3, "No");
		
		TableItem newitem2=new TableItem(contactTable, SWT.NONE, 1);
		newitem2.setText(0, "John");
		newitem2.setText(1, "Coppinger");
		newitem2.setText(2, "johnknowsnothing@gmail.com");
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
		*/
	}

	/**
	 * 
	 */
	private void addContactsToTable() 
	{
		try 
		{
			ArrayList<Recipient> people = database.printUsers();
			addArrayListToTable(people);
		} catch (SQLException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * @param people
	 */
	private void addArrayListToTable(ArrayList<Recipient> people) 
	{
		for (int i=0; i<people.size(); i++)
		{
			TableItem newitem= new TableItem(contactTable, SWT.NONE, 0);
			newitem.setText(0, people.get(i).getFirstName());
			newitem.setText(1, people.get(i).getLastName());
			newitem.setText(2, people.get(i).getAddress());
			newitem.setText(3, (people.get(i).getMailedBefore()==1) ? "yes" : "no");
		}
	}

	private void deleteAllContactsFromTable()
	{
		contactTable.removeAll();
	}
	
	protected boolean noneSelected() 
	{
		TableItem [] items=contactTable.getItems();
		for (int i=0; i<items.length; i++)
			if(items[i].getChecked())
				return false;
		return true;
	}

	public void setDisposeListener(DisposeListener ds2) 
	{
		ds=ds2;
	}

	public void setDatabase(SQLiteMailerJDB database) 
	{
		this.database=database;	
	}
}
