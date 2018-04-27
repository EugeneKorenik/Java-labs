package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import client.Account;
import client.AccountList;
import client.Person;
import client.PersonList;
import serverGUI.ServerGUI;

/** A single server which communicate with client
 * @author Eugene */
public class SingleServer extends Thread {
	
	/** Client socket */
	private Socket client;
	
	/** List of person in archive */
	private PersonList personList;
	
	/** List of accounts */
	private AccountList accountList;
	
	/** Stream which send messages to client*/
	private ObjectOutputStream output;
	
	/** Stream whitch get messages from client */
	private ObjectInputStream input;
	
	/** Acess level of client with which this client communicate */
	private int right;
	
	/** Login of client that connect to this thread*/
	private String accountLogin;
	
	private final Logger logger = Logger.getLogger(SingleServer.class);
	
	/** Default constructor 
	 * @param client Client socket
	 * @param accountList List of accounts
	 * @param personList List of persons */
	public SingleServer(Socket client, AccountList accountList, PersonList personList) {
		this.client = client;
		this.personList = personList;
		this.accountList = accountList;
	}
	
	/** In this method thread listen requests from client and give answer */
	@Override
	public void run() {
		try {
		    ArrayList<String> pair = getAccountInfo();
			boolean answer = clientExist(pair);
			if(!answer) output.writeBoolean(false);
	        else output.writeBoolean(true);
			output.flush();
	
			out: while(true) {
				String comand = input.readUTF();	
				switch(comand) {
				case "connect":
					if(answer) connect(pair);
					else {
						ServerGUI.showMessage("Client " + pair.get(0) + " try connect to server (Worng login or password)\n");
						close();
						return;
					}
					break;
				case "add":
					add();
					break;
				case "remove":
					remove();
					break;
				case "save":
					save();					
					break;
				case "registration":
			     	accountList.add(new Account(pair.get(0), pair.get(1), 0));	
			     	Server.rewriteAccountList();
			     	ServerGUI.showMessage("Account " + pair.get(0) + " successfully registrated\n");
					break;
				case "right":
					int right = (Integer) input.readObject();
					String login = input.readUTF();	
					Server.setRights(login, right);
					break;
				case "close":
					close();
					break out;	
				}				
			}							
		} catch(IOException e) {			
			logger.error(e);
		} catch(ClassNotFoundException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
	/** Server get pair login-password from client */
	private ArrayList<String> getAccountInfo() throws IOException, ClassNotFoundException {
		output = new ObjectOutputStream(client.getOutputStream()); 	
		input = new ObjectInputStream(client.getInputStream());		   		    
		ArrayList<String> pair = (ArrayList<String>) input.readObject();					
		return pair;
	}

	/** Server give to client a list of persons and accounts */
	private void connect(ArrayList<String> pair) throws IOException {
		accountLogin = pair.get(0);
		ServerGUI.showMessage("Client " + pair.get(0) + " try connect to server (Succesfully)\n");
		output.writeObject(personList);
		output.flush();
		output.writeObject(accountList);
		output.flush();
		output.writeObject(right);
		output.flush();
	}
	
    /** Server remove person from acrhive */
	private void remove() throws ClassNotFoundException, IOException {
		int index = (Integer) input.readObject();
		String name = personList.remove(index);
		Server.rewritePersonList();
		ServerGUI.showMessage("Person " + name + " removed from database\n");
	}
	
	/** Server add new person to archaive */
	private void add() throws ClassNotFoundException, IOException {
		Person person = (Person) input.readObject();
		personList.add(person);
		Server.rewritePersonList();
		ServerGUI.showMessage("Person " + person.getName() + " was added to database\n");
	}
	
    /** Server save changes of concrete person */
	private void save() throws ClassNotFoundException, IOException {
		Person person = (Person) input.readObject();
		personList.edit(person);
		Server.rewritePersonList();
		ServerGUI.showMessage("Information about person " + person.getName() + " was succesfully changed\n");
	}
	
	/** Reset connection with client */
	public void close() throws IOException {
		Server.rewriteAccountList();
		ServerGUI.showMessage("Account " + accountLogin + " disconected\n");
		output.writeUTF("close");
		output.flush();			
		output.close();
		input.close();
		client.close();		
		Server.removeSingleServer(this);
	}
	
    /** Get login of client with which this thread communicate
     * @return Clients login */
	public String getLogin() {
		return accountLogin;
	}
	
	/** Send client request to set new list of person */
	public void setNewPersonList() {
		try {
			output.reset();
			output.writeUTF("update");
			output.flush();
			output.writeObject(personList);
			output.flush();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
    /** Send client request to set new level acess
     * @param rights New level acess */
	public void setNewRights(int rights) {
		try {
			output.writeUTF("setRights");
			output.flush();
			output.writeObject(rights);
			output.flush();
		} catch (IOException e) {
			logger.error(e);
			e.printStackTrace();
		}
	}
	
    /** Search account with the pair login-password
     * @param pair Pair of login-password
     * @return Does it exist */
	public boolean clientExist(ArrayList<String> pair) {
		int size = accountList.size();
			
		for(int i = 0; i < size; i++) {
			Account client = accountList.get(i);
			if(client.getLogin().equals(pair.get(0)) 
					&& client.getPassword().equals(pair.get(1))) {
				right = client.getAccessRight();
				return true;
			}
		}
		return false;
	}
}
