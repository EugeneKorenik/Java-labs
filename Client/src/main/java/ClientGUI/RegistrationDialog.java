package ClientGUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/** Dialog to registrate new account
 * @author Eugene */
public class RegistrationDialog extends Dialog {
	
	private Label loginLabel, passwordLabel, passwordAgainLabel;
	private Text loginField, passwordField, passwordAgainField;
    private Button enterButton, cancelButton;
    private Shell dialog;
	
    /** Constructor in which build all GUI
	 * @param parent shell from which this dialog will be opened */
	public RegistrationDialog(Shell parent) {
		super(parent);
		
		dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		
		loginLabel = new Label(dialog, 0);
		passwordLabel = new Label(dialog, 0);
		passwordAgainLabel = new Label(dialog, 0);
		loginLabel.setText("Login: ");
		passwordLabel.setText("Password");
		passwordAgainLabel.setText("Password again:");
		
		enterButton = new Button(dialog, SWT.PUSH);
		cancelButton = new Button(dialog, SWT.PUSH);
		loginField = new Text(dialog, SWT.BORDER | SWT.SINGLE);
		passwordField = new Text(dialog, SWT.BORDER | SWT.SINGLE | SWT.PASSWORD);
		passwordAgainField = new Text(dialog, SWT.BORDER | SWT.SINGLE | SWT.PASSWORD);
		
		Handler handler = new Handler();
		enterButton.addSelectionListener(handler);
		cancelButton.addSelectionListener(handler);
		
		setBounds();
		setFont();
		
		enterButton.setText("Enter");
		cancelButton.setText("Cancel");
		
		dialog.setSize(300, 200);		
	}
	
	/** Set bounds of all GUI objects */
	private void setBounds() {
		loginField.setBounds(105, 10, 150, 30);
		loginLabel.setBounds(10, 20, 70, 30);
		
		passwordField.setBounds(105, 50, 150, 30);
		passwordLabel.setBounds(10, 60, 70, 30);
		
		passwordAgainField.setBounds(105, 90, 150, 30);
		passwordAgainLabel.setBounds(10, 100, 85, 30);
		
		enterButton.setBounds(105, 130, 70, 30);
		cancelButton.setBounds(185, 130, 70, 30);
	}
	
	/** Set font in text fields */
	private void setFont() {
		Font font = new Font(Display.getCurrent(), "Arial", 12, 0);
		loginField.setFont(font);
		passwordField.setFont(font);
		passwordAgainField.setFont(font);
	}
	
	/** Handler of button. Send request to add new account or close this dialog
	 * @author Eugene */
	class Handler implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent event) {
			if(event.getSource() == cancelButton)
				dialog.dispose();
			else {
				String login = loginField.getText();
				String password = passwordField.getText();
				String passwordAgain = passwordAgainField.getText();
				
				if(login.replaceAll(" ", "").equals("") ||
						password.replaceAll(" ", "").equals("") || 
						!password.equals(passwordAgain))
					return;
						
				try {
					Socket client = new Socket("localhost", 3345);
					ObjectInputStream input = new ObjectInputStream(client.getInputStream());
					ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
					
					ArrayList<String> pair = new ArrayList<String>();		
					pair.add(login);
					pair.add(password);
					
					output.writeObject(pair);
					output.flush();
					
					boolean answer = input.readBoolean();
					if(!answer) {
						output.writeUTF("registration");
						output.flush();						
					}					
					output.writeUTF("close");
					output.flush();			
					
					output.writeUTF(login);
					output.flush();
					
					client.close();
					input.close();
					output.close();
					
					loginField.setText("");
					passwordField.setText("");
					passwordAgainField.setText("");					
					dialog.dispose();					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}			
		}
		
	}
	
	/** Show dialog */
	public void open() {			
		Shell parent = getParent();
		
		if(dialog.isDisposed())
			dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.open();
		
		Display display = parent.getDisplay();	
		
		while(!dialog.isDisposed()) {
			if(!display.readAndDispatch())
				display.sleep();
		}
	}
}
