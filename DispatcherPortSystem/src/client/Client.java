package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;

import gui.GUI;

/** Поток, подключающийся к клиенту через определенный промежуток времени
 * и взаимодействующий с ним
 * @author Eвгений
 */
public class Client extends Thread {

	/** Периодичность подключения ([3-6] секунд)*/
	private int arrivalPeriod;
	
	/** Информация о корабле, с которым ассоциируется данный поток*/
	private Ship ship;
	
	/** Объект, необходимый для доступа к консоли порта*/
	private GUI gui;
	
	/**
	 * @param gui Объект, необходимый для доступа к консоли порта
	 * @param ship Корабль, с которым ассоциируется данный поток
	 */
	public Client(GUI gui, Ship ship) {
		this.gui = gui;
		this.ship = ship;
		arrivalPeriod = ship.getArrivalPeriod();
	}		
	
	/** Клиент подклюается к серверу, отправляет к нему имя корабля. 
	 * Сервер в ответ отправляет информацию о том, не закрыт ли порт. 
	 * В зависимоти от этого клиент либо перестанет обращаться к серверу,
	 * либо отправит на него информацию о корабле и запросит вход в склад,
	 * после этого опять получает сообщение не был ли закрыт порт, пока 
	 * корбаль разгружался/загружался на складе
	 */
	@Override
	public void run() {
		boolean portIsOpen;
		while(true) {
			try(Socket client = new Socket("localhost", 3345);
					DataOutputStream output = new DataOutputStream(client.getOutputStream());
					DataInputStream input = new DataInputStream(client.getInputStream());
					ObjectOutputStream objectOutput = new ObjectOutputStream(client.getOutputStream())) {
				
				gui.addToQueue(ship.getName());
				output.writeUTF(ship.getName());
				
				output.flush();				
				portIsOpen = input.readBoolean();
				
				if(!portIsOpen) {
					input.close();
					output.close();
					client.close();
					return;
				}
				
				objectOutput.writeObject(ship);
				objectOutput.flush();
				
				portIsOpen = input.readBoolean();
				input.close();
				output.close();
				client.close();
				
				if(!portIsOpen) return;
				
				Thread.sleep(1000 * arrivalPeriod);
					
			}catch(ConnectException e) {				
			}catch(SocketException e) {
				e.printStackTrace();	
			}catch(IOException e) {
				e.printStackTrace();
			}catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
