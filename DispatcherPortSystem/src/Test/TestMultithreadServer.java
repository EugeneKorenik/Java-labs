package Test;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import client.ClientPool;
import client.Ship;
import gui.GUI;
import junit.framework.TestCase;
import server.MultiThreadServer;

public class TestMultithreadServer extends TestCase {

	private Assert serverIsOpen;
	
	/** “ест, провер€ющий сервер на закрыти€
	 */
	@Test
	public void testClosing() {
		GUI gui = new GUI();
		ClientPool pool = new ClientPool(gui);
		for(int i = 0; i < 50; i++)
			pool.addClient(new Ship("Name", 2, "SName", 2, true));
		
		MultiThreadServer server = new MultiThreadServer(gui, 5);
		server.start();
		try {
			Thread.sleep(2000);
			server.close();
			Thread.sleep(6000); /* ћаксимальное врем€, которое сервер 
		    может ожидать запрос от клиента */
			
			serverIsOpen.assertFalse(server.interrupted());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}/** 3793 */ 
}
