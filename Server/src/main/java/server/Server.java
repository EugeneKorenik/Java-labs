package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import client.AccountList;
import client.Person;
import client.PersonList;

/** This class accept connections from clients and provides threads which 
 * communicate with them
 * @author Eugene */
public class Server extends Thread {

	/** Socket of our server */
	private ServerSocket serverSocket;
	
	/** Pool of signle servers. In need for us to close all single servers
	 * threads at one moment */
	private ExecutorService executor;
	
	/** Collection of single servers. It need for us to close connection. */
	private static ArrayList<SingleServer> singleServers;
	
	/** List of accounts */
	private static AccountList accountList;
	
	/** List of person in archive */
	private static PersonList personList;
	
	/** Objects that provides transformation from object to XML and back */
	private static JAXBContext personContext, accountContext;
	
	/** Objects which write clients and accounts list into the file*/
	private static Marshaller personMarshaller, accountMarshaller;
	
	/** Object whitch read clients and account list from file*/
	private static Unmarshaller personUnmarshaller, accountUnmarshaller;
	
	private static final File ACCOUNT_FILE = new File("accounts.xml");
	private static final File PERSON_FILE = new File("persons.xml");
	private final Logger logger = Logger.getLogger(Server.class);
		
	/** Default constructor */
	public Server() {
		executor = Executors.newCachedThreadPool();
		singleServers = new ArrayList<SingleServer>();
				
		try {
			personContext = JAXBContext.newInstance(PersonList.class);
			accountContext = JAXBContext.newInstance(AccountList.class);
			
			personMarshaller = personContext.createMarshaller();
			accountMarshaller = accountContext.createMarshaller();
			
			personMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			accountMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			
			personUnmarshaller = personContext.createUnmarshaller();
			accountUnmarshaller = accountContext.createUnmarshaller();
		} catch (JAXBException e1) {
			e1.printStackTrace();
			logger.fatal(e1);
		}		
		
		readInfoFromFiles();				
		try {
			serverSocket = new ServerSocket(3345);
			serverSocket.setSoTimeout(1000);
		} catch (IOException e) {
			logger.fatal(e);
			e.printStackTrace();
		}
	}
	
	/** Accept connection from client and provide the thread */
	@Override
	public void run() {
		while(!serverSocket.isClosed()) {
			try {
				Socket client = serverSocket.accept();
				SingleServer singleServer = new SingleServer(client, accountList, personList);
		     	executor.execute(singleServer);	
		     	singleServers.add(singleServer);
			} catch (SocketTimeoutException e) {
			} catch (SocketException e) {		
				logger.error(e);
			} catch (IOException e) {
				logger.error(e);
				e.printStackTrace();
			}
		}		
	}
	
	/** Read information abount existing accounts and person from files */
	private void readInfoFromFiles() {
		try {
			personList = (PersonList) personUnmarshaller.unmarshal(new FileInputStream(PERSON_FILE));
			Person.setStaticIndex(personList.size() + 1);
			accountList = (AccountList) accountUnmarshaller.unmarshal(new FileInputStream(ACCOUNT_FILE));
		} catch (FileNotFoundException | JAXBException e) {
			e.printStackTrace();
			logger.fatal(e);
		}	
	}	
	
	/** Remove single server from ArrayList
	 * @param singleServer Single server whose work was finished */
	public static void removeSingleServer(SingleServer singleServer) {
		singleServers.remove(singleServer);		
	}
	
	/** Rewrite information about accounts in file */
	synchronized public static void rewriteAccountList() {
		try {
			accountMarshaller.marshal(accountList, ACCOUNT_FILE);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	/** Rewrite information about person in file */
	synchronized public static void rewritePersonList() {
		try {
			personMarshaller.marshal(personList, PERSON_FILE);	
			for(int i = 0; i < singleServers.size(); i++) 
				singleServers.get(i).setNewPersonList();			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	/** Send request to client listener to set new access level
	 * @param login Login of account whose access level must be edit
	 * @param right New acess level */
	private static void sendRequestToSetRights(String login, int right) {
		for(int i = 0; i < singleServers.size(); i++) {
			SingleServer single = singleServers.get(i);
			if(single.getLogin().equals(login))
				single.setNewRights(right);			
		}
	}
	
	/** Set new acess rights to account with this login
	 * @param login Login of account whose access level must be edit
	 * @param right New acess level */
	synchronized public static void setRights(String login, int right) {
		accountList.edit(login, right);
		rewriteAccountList();
		sendRequestToSetRights(login, right);
	}
	
	/** Close all single servers and interrupt connection */
	public void close() {
		try {
			for(int i = 0; i < singleServers.size(); i++)
				singleServers.get(i).close();
			singleServers.clear();
			executor.shutdownNow();
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		}		
	}
}
