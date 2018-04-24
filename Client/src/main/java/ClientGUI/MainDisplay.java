package ClientGUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import client.AccountList;
import client.ListenerServerRequest;
import client.Person;
import client.PersonList;

/** Main window of application in a client part
 * @author Eugene */
public class MainDisplay {
	
	private Socket client;
	private Shell shell;
	
	private Display display;
	private Group info; 
	private List personListGUI;
	public Button addPerson, removePerson, saveChanges, adminButton;
	private Label labelAboveList , personNameLabel, birthdayLabel;
	private Label postLabel, educationLabel, characteristicLabel;
	private Label searchLabel, messageLabel;
	
	private Text personNameField, postField, educationField, characteristicField;
	private Text birthdayField, searchField;
	public PersonList personList;
	public AccountList accountList;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private ListenerServerRequest listenerServerRequest;
	private int right;
	
	/** Constructor in which build all GUI 
	 * @param client client socket 
	 * @param input get requests from server
	 * @param output send requests to a server */
	public MainDisplay(ObjectInputStream input, ObjectOutputStream output) {
		this.input = input;
		this.output = output;
		
		display = Display.getCurrent();
		shell = new Shell(display);
		
		try {
			personList = (PersonList) input.readObject();	
			accountList = (AccountList) input.readObject();
			right = (Integer) input.readObject();
			Person.setStaticIndex(personList.size() + 1);
			listenerServerRequest = new ListenerServerRequest(input, output, client, this);
			listenerServerRequest.start();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
			
		createObjects();
		setText();
		setBounds();		
		setFont();
		setNamesInList();
		setRights(right);
		
		ButtonHandler handler = new ButtonHandler();
		addPerson.addSelectionListener(handler);
		removePerson.addSelectionListener(handler);
		saveChanges.addSelectionListener(handler);
		adminButton.addSelectionListener(handler);
		personListGUI.addSelectionListener(handler);
		searchField.addKeyListener(new KeyAdapter());
		
		shell.setSize(600, 390);
		shell.open();
		
		while(!shell.isDisposed()) {
			if(!display.readAndDispatch())
				display.sleep();
		}
				
		try {
			output.writeUTF("close");
			output.flush();					
		} catch(SocketException e) {	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Set access level
	 * @param right new access level */
	public void setRights(int right) {
		this.right = right;
		switch(right) {
		case 0:
			addPerson.setVisible(false);
			removePerson.setVisible(false);
			adminButton.setVisible(false);
			setFieldEnable(false);
			break;
		case 1:
			setFieldEnable(true);
			addPerson.setVisible(false);
			removePerson.setVisible(false);
			adminButton.setVisible(false);
			break;
		case 2:
			setFieldEnable(true);
			addPerson.setVisible(true);
			removePerson.setVisible(true);
			adminButton.setVisible(true);
			adminButton.setVisible(false);
			break;
		case 3:
			setFieldEnable(true);
			addPerson.setVisible(true);
			removePerson.setVisible(true);
			adminButton.setVisible(true);
			adminButton.setVisible(true);
			break;
		}
	}
	
	/** Lock\Unlock fields of editing information of persons
	 * @param value Lock\Unlock	*/
	private void setFieldEnable(boolean value) {
		personNameField.setEnabled(value);
		postField.setEnabled(value);
		educationField.setEnabled(value);
		birthdayField.setEnabled(value);
		characteristicField.setEnabled(value);
		saveChanges.setVisible(value);
	}
	
	/** Show list of person on GUI */
	public void setNamesInList() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				personListGUI.removeAll();
				int size = personList.size();
				for(int i = 0; i < size; i++) {
					Person person = personList.get(i);
					personListGUI.add(Integer.toString(person.getIndex()) + "."+ person.getName());
				}				
			}
		});
	}
		
	/** Creting all GUI objects */
	private void createObjects() {
		info = new Group(shell, SWT.BORDER);
		addPerson = new Button(shell, SWT.PUSH);
		removePerson = new Button(shell, SWT.PUSH);
		adminButton = new Button(shell, SWT.PUSH);
		saveChanges = new Button(info, SWT.PUSH);
		personListGUI = new List(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);		
		
		labelAboveList = new Label(shell, 0);		
		personNameLabel = new Label(info, 0);
		birthdayLabel = new Label(info, 0);
		postLabel = new Label(info, 0);
		educationLabel = new Label(info, 0);
		postLabel = new Label(info, 0);
		characteristicLabel = new Label(info, 0);
		educationLabel = new Label(info, 0);
		searchLabel = new Label(shell, 0);
		messageLabel = new Label(shell, 0);
		
		personNameField = new Text(info, SWT.BORDER);
		postField = new Text(info, SWT.BORDER);		
		educationField = new Text(info, SWT.BORDER);
		characteristicField = new Text(info, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		birthdayField = new Text(info, SWT.BORDER | SWT.SINGLE);
		searchField = new Text(shell, SWT.BORDER | SWT.SINGLE);
	}
	
	/** Set text of labels and buttons */
	private void setText() {
		info.setText("Infromation");		
		addPerson.setText("Add person");
		removePerson.setText("Remove person");
		labelAboveList.setText("Full Names");
		personNameLabel.setText("Name: ");
		birthdayLabel.setText("Birthday: ");
		postLabel.setText("Post: ");
		educationLabel.setText("Education: ");
		characteristicLabel.setText("Characteristic:");
		saveChanges.setText("Save changes");
		searchLabel.setText("Search");
		adminButton.setText("Admin button");
	}
	
	/** Set bounds of all GUI objects */
	private void setBounds() {
		info.setBounds(150, 40, 400, 300);
		removePerson.setBounds(250, 5, 100, 30);
		addPerson.setBounds(150, 5, 100, 30);
		adminButton.setBounds(350, 5, 100, 30);
		messageLabel.setBounds(460, 15, 100, 30);
		
		searchLabel.setBounds(10, 20, 120, 20);
		searchField.setBounds(10, 40, 120, 25);
		
		personListGUI.setBounds(10, 90, 120, 250);
		labelAboveList.setBounds(10, 70, 130, 30);
			
		personNameLabel.setBounds(10, 30, 60, 20);
		personNameField.setBounds(80, 30, 290, 20);
	
		birthdayLabel.setBounds(10, 55, 60, 20);
		birthdayField.setBounds(80, 55, 290, 20);
		
		postLabel.setBounds(10, 80, 60, 20);
		postField.setBounds(80, 80, 290, 20);
		
		educationLabel.setBounds(10, 105, 60, 20);
		educationField.setBounds(80, 105, 290, 20);
		 
		characteristicLabel.setBounds(10, 140, 120, 20);
		characteristicField.setBounds(10, 160, 360, 80);
		
		saveChanges.setBounds(10, 250, 120, 30);
	}
	
	/** Set fonts of some objects on GUI */
	private void setFont() {
		Font font = new Font(display, "Arial", 10, 0);
		Font smallFont = new Font(display, "Arial", 8, 0);
	
		messageLabel.setFont(smallFont);
		messageLabel.setForeground(new Color(Display.getCurrent(), 255, 0, 0));
		searchLabel.setFont(font);
		labelAboveList.setFont(font);
		personListGUI.setFont(smallFont);
		info.setFont(font);
		personNameLabel.setFont(font);
		birthdayLabel.setFont(font);
		postLabel.setFont(font);
		educationLabel.setFont(font);
		characteristicLabel.setFont(font);
	}

	/** Return selected person in a list on GUI
	 * @return selected person in a list on GUI */
	private Person getPerson() {
		Person person = null;
		String selected = personListGUI.getSelection()[0].split("\\.")[0];
		int index = Integer.parseInt(selected);
		
		int i = 0;
		for(i = 0; i < personList.size(); i++) {
			Person tempPerson = personList.get(i);				
			if(index == tempPerson.getIndex()) {
				person = tempPerson;							
			}
		}	
		Person.setStaticIndex(i);
		return person;
	}
	
	/** Show on GUI list of persons in archive
	 * @param personList list of persons in archive */ 
	 public void setPersonList(PersonList personList) {
		this.personList = personList;
		setNamesInList();
	}
	
	/** Button handler. Handle events of adding, removing, editing person,
	 * opening admin window (if account have this access level) */
	class ButtonHandler implements SelectionListener {

		@Override
		public void widgetDefaultSelected(SelectionEvent arg0) {}

		@Override
		public void widgetSelected(SelectionEvent event) {
			if(event.getSource() == addPerson) addPerson();
			else if(event.getSource() ==  removePerson) removePerson();
			else if(event.getSource() ==  saveChanges) saveChanges();	
			else if(event.getSource() ==  adminButton) new AdminDisplay(output, accountList);
			else selectPerson();		
		}
		
		/** Send request to add new person */
		public void addPerson() {
			AddPersonDialog dialog = new AddPersonDialog(shell);
			Person person = dialog.open();
			if(person != null) {
			    try {
			    	output.writeUTF("add");
					output.flush();						
					output.writeObject(person);
					output.flush();
					messageLabel.setText("");
				} catch (IOException e) {
					messageLabel.setText("Server closed");
				}
			}
		}
		
		/** Send request to delete person */
		public void removePerson() {
			int index = personListGUI.getSelectionIndex();
			if(index == -1) return;				
			Person selectedPerson = getPerson();				
			try {
				output.writeUTF("remove");
				output.flush();
				output.writeObject(selectedPerson.getIndex());	
				output.flush();
				personNameField.setText("");
				educationField.setText("");
				postField.setText("");
				birthdayField.setText("");
				characteristicField.setText("");
				messageLabel.setText("");
			} catch (IOException e) {
				messageLabel.setText("Server closed");
			}		
		}
		
		/** Send request to save changes of one person */
		public void saveChanges() {
			int index = personListGUI.getSelectionIndex();
			if(index == -1) return;
			Person selectedPerson = getPerson();			
			selectedPerson.setName(personNameField.getText());
			selectedPerson.setEducation(educationField.getText());
			selectedPerson.setCharacteristic(characteristicField.getText());
			selectedPerson.setBirthday(birthdayField.getText());
			selectedPerson.setPost(postField.getText());
			
			try {
				output.writeUTF("save");
				output.flush();
				output.writeObject(selectedPerson);
				output.flush();
				messageLabel.setText("");
			} catch(IOException e) {
				messageLabel.setText("Server closed");	
			}		
			setNamesInList();
			personListGUI.showSelection();
		}
		
		/** Show information of selected person */
		public void selectPerson() {
			try {
				int index = personListGUI.getSelectionIndex();
				Person selectedPerson = getPerson();
				personNameField.setText(selectedPerson.getName());
				educationField.setText(selectedPerson.getEduation());
				postField.setText(selectedPerson.getPost());
				birthdayField.setText(selectedPerson.getBirthday());
				characteristicField.setText(selectedPerson.getCharacteristic());
			}catch(Exception e) {}
		}	
	}	
	
	/** This class use in a search of person */
	class KeyAdapter implements KeyListener {

		@Override
		public void keyPressed(KeyEvent enter) {}

		@Override
		public void keyReleased(KeyEvent arg0) {
			String searchRequest = searchField.getText().toLowerCase();
			personListGUI.removeAll();
			
			for(int i = 0; i < personList.size(); i++) {
				Person person = personList.get(i);
				if(person.getNameInLowerCase().contains((searchRequest)))
					personListGUI.add(Integer.toString(person.getIndex()) + "."+ person.getName());
			}
		}		
	}
}
