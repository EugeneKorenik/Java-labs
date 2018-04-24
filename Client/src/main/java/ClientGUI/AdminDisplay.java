package ClientGUI;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import client.Account;
import client.AccountList;

/** Admin window which allow set access level of all accounts
 * @author Eugene */
public class AdminDisplay {
	
	private Display display;
	private Shell shell;
	private AccountList accountList;
	private ArrayList<CCombo> comboList;
	private Label adminWindowLabel, messageLabel;
	private final String[] titles = {"login", "Right"};
	private Table table;
	private static boolean isOpened;
	private ObjectOutputStream output;
	
	/** Constructor in which build GUI of this window
	 * @param output send requests to a server
	 * @param accountList list of accounts */
	public AdminDisplay(ObjectOutputStream output, AccountList accountList) {
		if(isOpened) return;
		isOpened = true;
		
		this.accountList = accountList;
		this.output = output;
		display = Display.getCurrent();
		shell = new Shell(display);
		comboList = new ArrayList<CCombo>();		

		adminWindowLabel = new Label(shell, 0);
		messageLabel = new Label(shell, 0);
		adminWindowLabel.setText("Administrator window");
		adminWindowLabel.setBounds(10, 15, 240, 20);
		messageLabel.setBounds(250, 17, 120, 20);
		
		adminWindowLabel.setFont(new Font(display, "Arial", 12, 0));
		messageLabel.setForeground(new Color(display, 255, 0, 0));
		messageLabel.setFont(new Font(display, "Arial", 10, 0));		
		
		buildTable();								
		shell.setSize(370, 250);
		shell.open();		
		while(!shell.isDisposed()) {
			if(display.readAndDispatch())
				display.sleep();
		}
		isOpened = false;
	}
	
	/** In this method build table */
	private void buildTable() {
		table = new Table(shell, SWT.FULL_SELECTION | SWT.MULTI | SWT.BORDER);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		
		TableColumn login = new TableColumn(table, SWT.NONE);
		TableColumn right = new TableColumn(table, SWT.NONE);
		
		ComboHandler handler = new ComboHandler();
		
		for(int i = 0; i < accountList.size(); i++) {
			TableItem item = new TableItem(table, SWT.BORDER);
			Account account = accountList.get(i);
			item.setText(0, account.getLogin());			
			TableEditor editor = new TableEditor(table);
			CCombo combo = new CCombo(table, SWT.NONE);
			combo.setItems(new String[] {"Read", "Edit", "Add/Remove", "Admin"});
			combo.setEditable(false);
			combo.addSelectionListener(handler);
			combo.select(account.getAccessRight());			
			comboList.add(combo);
			editor.grabHorizontal = true;			
			editor.setEditor(combo, item, 1);
		}
								
		login.setWidth(105);
		right.setWidth(200);
		login.setResizable(false);
		right.setResizable(false);		
		login.setText(titles[0]);
		right.setText(titles[1]);
			
		table.setBounds(10, 40, 330, 150);
	}
	
	/** Handler which send server request about editing access level 
	 * of concrere account
	 * @author Eвгений */
	class ComboHandler implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent event) {}

		@Override
		public void widgetSelected(SelectionEvent event) {
			CCombo source = (CCombo) event.getSource();
			int rights = source.getSelectionIndex();
			try {
			    int index = comboList.indexOf(source);
			    String login = table.getItem(index).getText();	
			    accountList.get(login).setRight(rights);
			    output.writeUTF("right");
			    output.flush();
			    output.writeObject(rights);
			    output.flush();
			    output.writeUTF(login);
			    output.flush();			
			    messageLabel.setText(null);
			} catch(IOException e) {
				messageLabel.setText("Server closed");
			}
		}		
	}
}
