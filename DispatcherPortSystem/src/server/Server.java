package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import client.Client;
import client.Ship;
import gui.GUI;

/** ����� ��� ��������� �������� �������
 * @author E������
*/

public class Server extends Thread {
	
	/**�����, ��� ������� ����� �������������� */
	private Socket client;
	
	/**����� �����, ������� ������������ ������ �����*/
	private int piersId;
	
	/**������, ����� ������� ��������������� ������ � ������*/
	private Warehouse warehouse;
	
	/**������������� ������, �������� ��� ����� ������� � ��������� 
	 * ��� ������������ ����� � ������������ �������*/
	private MultiThreadServer server;
	
	/**����������� ��������, ��������� ��� ������� � ������� ����� */
	private GUI gui;
	
	/** ������� ����� �����-���, ����������� ��� ����� � ��������� �� ����� ������������ ���������
	 * @param server ������������� ������
	 * @param client ����� �������
	 * @param warehouse ������ ��� ������� � ������
	 * @param gui ������ ��� ������� � ������� ����� �� ����������� ����������
	 * @param piersId ����� �����, ������� ������������ ������ �����
	 */
	public Server(MultiThreadServer server, Socket client, Warehouse warehouse, GUI gui, int piersId) {
		super(Integer.toString(piersId));
		this.server = server;
		this.warehouse = warehouse;
		this.piersId = piersId;
		this.client = client;
		this.gui = gui;
	}
		
	/**����������� ��� ������ ������ */
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
