package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import client.Client;
import client.Ship;
import gui.GUI;

/** Поток для обработки запросов клиента
 * @author Eвгений
*/

public class Server extends Thread {
	
	/**Сокет, чьи запросы будут обрабатываться */
	private Socket client;
	
	/**Номер пирса, который представляет данный поток*/
	private int piersId;
	
	/**Объект, через который осуществяляется доступ к складу*/
	private Warehouse warehouse;
	
	/**Многопоточный сервер, которому нам поток говорит о занятости 
	 * или освобождении порта с определенным номером*/
	private MultiThreadServer server;
	
	/**Графический интерфей, необходим для доступа к консоли порта */
	private GUI gui;
	
	/** Создает новый поток-пир, присваивает ему номер и связывает со всеми необходимыми объектами
	 * @param server Многопоточный сервер
	 * @param client Сокет клиента
	 * @param warehouse Объект для доступа к складу
	 * @param gui Объект для доступа к консоли порта на графическом интерфейсе
	 * @param piersId Номер пирса, который представляет данный поток
	 */
	public Server(MultiThreadServer server, Socket client, Warehouse warehouse, GUI gui, int piersId) {
		super(Integer.toString(piersId));
		this.server = server;
		this.warehouse = warehouse;
		this.piersId = piersId;
		this.client = client;
		this.gui = gui;
	}
		
	/**Выполняется вся логика потока */
	@Override
	public void run() {
		String name = "";
		try(DataOutputStream output = new DataOutputStream(client.getOutputStream());
				DataInputStream input = new DataInputStream(client.getInputStream());
				ObjectInputStream objectInput = new ObjectInputStream(client.getInputStream())) {
			
			name = input.readUTF();
			
			if(server.isOpen()) output.writeBoolean(true);
			else {
			   	output.writeBoolean(false);
				output.close();
				objectInput.close();
				input.close();
	
				gui.shipReject(name, piersId);
				client.close();
				server.remove(piersId);
				return;
			}			
			output.flush();
			
			gui.shipEnter(name, piersId);
            Thread.sleep(3000);
                        
			Ship ship = (Ship) objectInput.readObject();
			warehouse.enterToWarehouse(ship);
			                  
            if(server.isOpen()) output.writeBoolean(true);
            else output.writeBoolean(false);
               
		    input.close();
			output.close();
			objectInput.close();
			client.close();		
				
		}catch(IOException e) {
			e.printStackTrace();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
			
		gui.shipLeave(name, piersId);
		server.remove(piersId);		
	}
}
