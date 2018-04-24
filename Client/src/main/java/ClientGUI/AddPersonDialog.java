package ClientGUI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.apache.log4j.*;

import client.Person;

/** Dialog in which user can add new person to archive 
 * @author Eugene */
public class AddPersonDialog extends Dialog {
	
	private Label nameLabel, postLabel, educationLabel, birthdayLabel;
	private Label characteristicLabel, addNewPersonLabel, messageLabel;

	private Text nameField, postField, educationField, birthdayField;
	private Text characteristicField;
	private Button enter, cancel;
	
	private Shell dialog;
	private Person person;
	
	private final Logger logger = Logger.getLogger(AddPersonDialog.class);
	
	/** Constructor in which build all GUI
	 * @param parent shell from which this dialog will be opened */
	public AddPersonDialog(Shell parent) {
		super(parent);
		
		dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setSize(350, 280);
		
		createObjects();
		setText();
		setBounds();		
		
		Handler handler = new Handler();
		enter.addSelectionListener(handler);
		cancel.addSelectionListener(handler);
	}
	
	/** Show dialog
	 * @return a person was creating */
	public Person open() {
		person = null;
		Display display = getParent().getDisplay();
		dialog.open();
		
		while(!dialog.isDisposed()) {
			if(!display.readAndDispatch())
				display.sleep();
		}
		return person;
	}
	
	/** Create all GUI objects */
	private void createObjects() {
		addNewPersonLabel = new Label(dialog, 0);
		nameLabel = new Label(dialog, 0);
		postLabel = new Label(dialog, 0);
		educationLabel = new Label(dialog, 0);
		birthdayLabel = new Label(dialog, 0);
		characteristicLabel = new Label(dialog, 0);
		messageLabel = new Label(dialog, 0);
		
		Color redColor = new Color(Display.getCurrent(), 255, 0, 0);
		messageLabel.setForeground(redColor);
		
		nameField = new Text(dialog, SWT.SINGLE | SWT.BORDER);
		postField = new Text(dialog, SWT.SINGLE | SWT.BORDER);
		educationField = new Text(dialog, SWT.SINGLE | SWT.BORDER);
		birthdayField = new Text(dialog, SWT.SINGLE | SWT.BORDER);
		characteristicField = new Text(dialog, SWT.MULTI | SWT.V_SCROLL);
		
		enter = new Button(dialog, 0);
		cancel = new Button(dialog, 0);
	}
	
	/** Set text of all GUI objects */
	private void setText() {
		Font header = new Font(Display.getCurrent(), "Arial", 12, 0);		
		addNewPersonLabel.setText("Add new Person");
		addNewPersonLabel.setFont(header);
		nameLabel.setText("Name:");
		postLabel.setText("Post:");
		educationLabel.setText("Education:");
		birthdayLabel.setText("Birthday:");
		characteristicLabel.setText("Characteristic:");
		
		enter.setText("Enter");
		cancel.setText("Cancel");
	}
	
	/** Set bounds of all objects */
	private void setBounds() {
		addNewPersonLabel.setBounds(10, 10, 120, 20);
		messageLabel.setBounds(140, 15, 200, 20);
		
		nameLabel.setBounds(20, 35, 60, 20);
		nameField.setBounds(85, 35, 240, 20);
		
		postLabel.setBounds(20, 60, 60, 20);
		postField.setBounds(85, 60, 240, 20);
		
		educationLabel.setBounds(20, 85, 60, 20);
		educationField.setBounds(85, 85, 240, 20);
		
		birthdayLabel.setBounds(20, 110, 60, 20);
		birthdayField.setBounds(85, 110, 240, 20);
		
		characteristicLabel.setBounds(20, 135, 120, 20);
		characteristicField.setBounds(20, 155, 305, 50);
		
		enter.setBounds(85, 215, 70, 30);
		cancel.setBounds(165, 215, 70, 30);
	}
	
	/** Handler of button. Send request to add new person or close this dialog
	 * @author Eugene */
	class Handler implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}
        
		@Override
		public void widgetSelected(SelectionEvent event) {
			if(event.getSource() == enter) {
				String name = nameField.getText();
				if(name.replaceAll(" ", "").equals("")) {
					messageLabel.setText("Field NAME should be filled");
			            return;
				}
					
				String education = educationField.getText();
				String post = postField.getText();
				String birthdayStr = birthdayField.getText().replaceAll(",", ".1");
				String characteristic = characteristicField.getText();
					
				SimpleDateFormat format = new SimpleDateFormat("dd.mm.yyyy");
				Date birthday;
				try {
					birthday = format.parse(birthdayStr);
					person = new Person(name, post, education, characteristic, birthdayStr);
				} catch (ParseException e) {
					messageLabel.setText("Date format must be dd.mm.yyyy");
					logger.error(e);
					return;
				}	
			}
			dialog.dispose();
		}		
	}
}
