import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;


public class ContactsWindow {

	protected Shell shlContacts;
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
		shlContacts.open();
		shlContacts.layout();
		while (!shlContacts.isDisposed()) 
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
		shlContacts = new Shell();
		shlContacts.setSize(470, 300);
		shlContacts.setText("Contacts");
		shlContacts.addDisposeListener(this.ds);
		
		Button addButton = new Button(shlContacts, SWT.NONE);
		addButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				AddWindowDialog ad=new AddWindowDialog(shlContacts, SWT.NONE);
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
						MessageBox error=new MessageBox(shlContacts, SWT.ERROR);
						error.setMessage("That email address already exists in the database");
						error.open();
					}
					System.out.println("Recipient "+r.getFirstName()+" "+r.getLastName()+" received from add dialoge!");
				}
			}
		});
		addButton.setToolTipText("Add a new contact");
		addButton.setBounds(10, 145, 75, 25);
		addButton.setText("Add");
		
		Button deleteButton = new Button(shlContacts, SWT.NONE);
		deleteButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				MessageBox deleteMessage;
				if(noneSelected())
				{
					deleteMessage=new MessageBox(shlContacts);
					deleteMessage.setMessage("Must Select a contact first");
					deleteMessage.open();
				}
				else
				{
					deleteMessage=new MessageBox(shlContacts, SWT.YES|SWT.NO);
					deleteMessage.setMessage("Are you sure you want to delete the selected contacts? They will be permanently deleted from the database.");
					if (deleteMessage.open()==SWT.YES)
					{
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
		
		Button sendEmailButton = new Button(shlContacts, SWT.NONE);
		sendEmailButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				try
				{
					UserInfo user=UserInfo.load();
					ArrayList<String> emails=new ArrayList<String>();
					TableItem [] cur=contactTable.getItems();
					for (int i=0; i<cur.length; i++)
					{
						if (cur[i].getChecked())
							emails.add(cur[i].getText(2));
					}
					MailWindow newwin=new MailWindow();
					newwin.setDatabase(database);
					newwin.setUserInfo(user);
					newwin.setTo(emails);
					DisposeListener ds=new DisposeListener(){
						
						@Override
						public void widgetDisposed(DisposeEvent e) 
						{
							shlContacts.setEnabled(true);
							
						}};
					newwin.setDisposeListener(ds);
					shlContacts.setEnabled(false);
					newwin.open();
				}
				catch (IOException ex)
				{
					MessageBox error=new MessageBox(shlContacts);
					error.setMessage("Please enter an email address in settings before sending an email");
					error.open();
				}
			}
;		});
		sendEmailButton.setToolTipText("Send an email to the selected contacts");
		sendEmailButton.setBounds(10, 207, 149, 44);
		sendEmailButton.setText("Send Email");
		
		contactTable = new Table(shlContacts, SWT.BORDER | SWT.CHECK | SWT.FULL_SELECTION);
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
		
		final Combo sortByCombo = new Combo(shlContacts, SWT.READ_ONLY);
		sortByCombo.setToolTipText("Sort the entries by first or last name");
		sortByCombo.setItems(new String[] {"First Name", "Last Name", "Last Name, starting with"});
		sortByCombo.setBounds(317, 178, 91, 23);
		sortByCombo.setText("Sort By");
		
		letterText = new Text(shlContacts, SWT.BORDER);
		letterText.setEnabled(false);
		letterText.setBounds(317, 230, 38, 21);
		
		final Label enterLetterLabel = new Label(shlContacts, SWT.NONE);
		enterLetterLabel.setEnabled(false);
		enterLetterLabel.setBounds(317, 209, 66, 15);
		enterLetterLabel.setText("Enter letter");
		
		final Button okButton = new Button(shlContacts, SWT.NONE);
		okButton.addSelectionListener(new SelectionAdapter() 
		{
			@Override
			public void widgetSelected(SelectionEvent e) 
			{
				try
				{
					MessageBox ms=new MessageBox(shlContacts, SWT.NONE);
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
		
		Label lblSortBy = new Label(shlContacts, SWT.NONE);
		lblSortBy.setBounds(317, 155, 55, 15);
		lblSortBy.setText("Sort by");
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
