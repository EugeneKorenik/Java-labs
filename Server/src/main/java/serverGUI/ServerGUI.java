package serverGUI;

import java.io.File;
import java.util.ArrayList;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import client.Account;
import client.AccountList;
import client.Person;
import client.PersonList;
import server.Server;

/** Server console on which show actions of all accounts
 * @author Eugene */
public class ServerGUI {
	
	private static ServerGUI serverGUI;
	private static Server server;
	private static Text textArea;
	private Button stopServer, startServer;
	private Handler buttonHandler;
    
	private final Logger logger = Logger.getLogger(ServerGUI.class);
	
	/** Constructor in which build full GUI */
	private ServerGUI() {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.addShellListener(new ShellListener() {
            public void shellActivated(ShellEvent arg0) {}
			public void shellDeactivated(ShellEvent arg0) {}
			public void shellDeiconified(ShellEvent arg0) {}
			public void shellIconified(ShellEvent arg0) {}	
			public void shellClosed(ShellEvent event) {
				try {
					if(server != null) {
						server.close();
					    server.join();					    
					}
				} catch (InterruptedException e) {
					logger.error(e);
					e.printStackTrace();
				}
				System.exit(0);
			}
		});
		
		textArea = new Text(shell, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		textArea.getVerticalBar().setVisible(true);
		textArea.setEditable(false);
		textArea.setBounds(10, 10, 450, 240);
		
		stopServer = new Button(shell, SWT.PUSH);
		startServer = new Button(shell, SWT.PUSH);
		
		buttonHandler = new Handler();
		startServer.setBounds(460, 10, 100, 30);
		stopServer.setBounds(460, 45, 100, 30);
		
		startServer.addSelectionListener(buttonHandler);
		stopServer.addSelectionListener(buttonHandler);	
		stopServer.setEnabled(false);		
			
		stopServer.setText("Stop");
		startServer.setText("Start");
		
		shell.setSize(580, 300);
		shell.open();
		
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch())
			    display.sleep();			
		}		
	}
	
	/** Handle events of starting and closing server
	 * @author Eugene */
	class Handler implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent event) {
			if(event.getSource() == startServer) {
				startServer.setEnabled(false);				
				server = new Server();
				server.start();						
				stopServer.setEnabled(true);
				textArea.append("Server start work...\n");
				
			} else {
				try {
					stopServer.setEnabled(false);
					server.close();		
					server.join();
					startServer.setEnabled(true);
					textArea.append("Sever stopped\n");
				} catch (InterruptedException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}		
	}
	
	/** Method show about changes in a list of persons or accounts
	 * @param message Message which must be output */
	public static void showMessage(final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
		    	textArea.append(message);
			}
		});
	}
	
	/** Start of work of a server part */
	public static void main(String[] args) {
		new ServerGUI();			
	}
}
