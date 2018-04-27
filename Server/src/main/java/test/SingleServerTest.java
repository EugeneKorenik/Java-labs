package test;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.junit.Test;

import client.Account;
import client.AccountList;
import client.PersonList;
import junit.framework.TestCase;
import server.SingleServer;

public class SingleServerTest extends TestCase {
	
	private String login = "login";
	private String password = "password";			
	
    @Test
	public void testClientExist() throws UnknownHostException, IOException  {
    	ServerSocket server = new ServerSocket(3345);
    	Socket client = new Socket("localhost", 3345);
	    AccountList accountList = new AccountList();    			
	    PersonList personList = new PersonList();
		SingleServer single = new SingleServer(client, accountList, personList);
		
		ArrayList<String> pair = new ArrayList<String>();
		pair.add("login");
		pair.add("password");
		
	    assertEquals(false, single.clientExist(pair));
	    accountList.add(new Account(login, password, 0));
	    assertEquals(true, single.clientExist(pair));
	    
		client.close();		
		server.close();		
    }
}
