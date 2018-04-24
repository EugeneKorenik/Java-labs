package ClientGUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.apache.log4j.*;

/** From this class start work client part of application */
public class AuthorizationDisplay {
	
	private Label message;
	private Text loginField, passwordField;
    private Button enterButton, registrationButton;
    private Shell shell;
    
    final private Logger logger = Logger.getLogger(AuthorizationDisplay.class);
	   
    /** Constructor. Here building all GUI of this window */
	public AuthorizationDisplay() {
		Display display = new Display();
		Font font = new Font(display, "Arial", 12, 0);
		shell = new Shell(display);
		
		Label authorizationLabel = new Label(shell, 0);		
		loginField = new Text(shell, SWT.BORDER);
		passwordField = new Text(shell, SWT.BORDER | SWT.SINGLE | SWT.PASSWORD);	
		enterButton = new Button(shell, SWT.PUSH);
		registrationButton = new Button(shell, SWT.PUSH);
		message = new Label(shell, 0);
		
		authorizationLabel.setText("Entrance");
		authorizationLabel.setFont(font);
		loginField.setFont(font);
		passwordField.setFont(font);
		
		Color color = new Color(display, 255, 0, 0);
		message.setForeground(color);
		message.setBounds(90, 22, 150, 20);
		
		enterButton.setText("Enter");
		registrationButton.setText("Registration");				
		
		authorizationLabel.setBounds(20, 20, 65, 20);
		loginField.setBounds(20, 40, 200, 30);
		passwordField.setBounds(20, 80, 200, 30);	
		enterButton.setBounds(20, 120, 90, 30);
		registrationButton.setBounds(130, 120, 90, 30);
		
		ButtonHandler handler = new ButtonHandler();
		enterButton.addSelectionListener(handler);
		registrationButton.addSelectionListener(handler);
		
		shell.setSize(300, 200);
		shell.open();
		
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch())
			    display.sleep();				
		}		
	}
	
	/** Button handler. Handle events of authorization and closing window
	 * @author Eugene */
	class ButtonHandler implements SelectionListener {

		public void widgetDefaultSelected(SelectionEvent event) {}

		public void widgetSelected(SelectionEvent event) {		
			if(enterButton == event.getSource()) {
				try {
					String login = loginField.getText();
					String password = passwordField.getText();
					
					if(login.replaceAll(" ", "").equals("") || password.replaceAll(" ", "").equals(""))
						return;
					
					Socket client = new Socket("localhost", 3345);
					ObjectInputStream input = new ObjectInputStream(client.getInputStream());
					ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
					ArrayList<String> list = new ArrayList<String>();
					
					list.add(loginField.getText());
					list.add(passwordField.getText());		
					
					output.writeObject(list);
					output.flush();
					
					boolean answer = input.readBoolean();	
					output.writeUTF("connect");
					output.flush();
					
					if(answer) {
						shell.dispose();
						new MainDisplay(input, output);
					} else {
						message.setText("Wrong login or password");
					}
					
				} catch(IOException e) {
					message.setText("Server doesn't work");
					logger.warn(e);
				}				
			} else {
				new RegistrationDialog(shell).open();
			}
		}		
	}
	
	/** Start of work with a client part */
	public static void main(String[] args) {
		new AuthorizationDisplay();
	}
}
