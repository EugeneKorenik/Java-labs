package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.eclipse.swt.widgets.Display;

import ClientGUI.MainDisplay;

/** Listen requests from a server and perfrom corresponding operations with GUI */
public class ListenerServerRequest extends Thread {
	
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private MainDisplay mainDisplay;
	private Socket client;
	
	/** Constructor 
	 * @param input get request from a server
	 * @param output not send requests to a server, need for us to clise it here
	 * @param client need for us to close it here
	 * @param mainDisplay main window of GUI */
	public ListenerServerRequest(ObjectInputStream input, ObjectOutputStream output, Socket client, MainDisplay mainDisplay) {
		this.input = input;
		this.output = output;
		this.mainDisplay = mainDisplay;
		this.client = client;
	}
	
	/** Listening requests from server */
	@Override
	public void run() {
		String comand;
		out:while(true) {
			try {
				comand = input.readUTF();
				switch(comand) {
				case "close":
					output.close();
					input.close();
					client.close();
					break out;
				case "update":
					PersonList personList = (PersonList) input.readObject();
					mainDisplay.setPersonList(personList);
					break;
				case "setRights":
					final int rights = (Integer) input.readObject();
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							mainDisplay.setRights(rights);
						}
					});
					break;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				comand = "close";
			} 
		}
	}
}
