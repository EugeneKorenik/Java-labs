package client;

import java.util.ArrayList;

import gui.GUI;

/**
 * Класс, который добавляет новых клиентов и хранит о них информацию после
 * закрытия порта, чтобы после возобновить их обращение
 * @author Eвгений
 */
public class ClientPool {
	
	/**Контейнер, содержащий все корабли, которые обращаются к серверу */
	private ArrayList<Ship> ships;
	
	/**Объект ГПИ, который необходимо передавать клиента, чтобы они могли
	 * обращаться к конслоли */
	private GUI gui;
	
	/**
	 * @param gui Объект ГПИ, который необходимо передавать клиента, чтобы они могли
	 * обращаться к конслоли */
	public ClientPool(GUI gui) {
		ships = new ArrayList<Ship>();
		this.gui = gui;
	}
	
	/**
	 * Добавляет в список нового клиента и запускает его на выполнение
	 * @param ship Корабль, который будет представлять новый созданный поток-клиент
	 */
	public void addClient(Ship ship) {
		Client client = new Client(gui, ship);
		ships.add(ship);		
		client.start();
	}
	
	/** Запускает все клиенты, обращавшиеся к порту до того, как он был закрыт*/
	public void runAllClients() {
		for(int i = 0; i < ships.size(); i++) {
			Ship ship = ships.get(i);
			Client client = new Client(gui, ship);
			client.start();
		}		
	}
}
