package client;

import java.util.ArrayList;

import gui.GUI;

/**
 * �����, ������� ��������� ����� �������� � ������ � ��� ���������� �����
 * �������� �����, ����� ����� ����������� �� ���������
 * @author E������
 */
public class ClientPool {
	
	/**���������, ���������� ��� �������, ������� ���������� � ������� */
	private ArrayList<Ship> ships;
	
	/**������ ���, ������� ���������� ���������� �������, ����� ��� �����
	 * ���������� � �������� */
	private GUI gui;
	
	/**
	 * @param gui ������ ���, ������� ���������� ���������� �������, ����� ��� �����
	 * ���������� � �������� */
	public ClientPool(GUI gui) {
		ships = new ArrayList<Ship>();
		this.gui = gui;
	}
	
	/**
	 * ��������� � ������ ������ ������� � ��������� ��� �� ����������
	 * @param ship �������, ������� ����� ������������ ����� ��������� �����-������
	 */
	public void addClient(Ship ship) {
		Client client = new Client(gui, ship);
		ships.add(ship);		
		client.start();
	}
	
	/** ��������� ��� �������, ������������ � ����� �� ����, ��� �� ��� ������*/
	public void runAllClients() {
		for(int i = 0; i < ships.size(); i++) {
			Ship ship = ships.get(i);
			Client client = new Client(gui, ship);
			client.start();
		}		
	}
}
