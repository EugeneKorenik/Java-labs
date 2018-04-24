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

/** �����, �������������� � ������� ����� ������������ ���������� �������
 * � ����������������� � ���
 * @author E������
 */
public class Client extends Thread {

	/** ������������� ����������� ([3-6] ������)*/
	private int arrivalPeriod;
	
	/** ���������� � �������, � ������� ������������� ������ �����*/
	private Ship ship;
	
	/** ������, ����������� ��� ������� � ������� �����*/
	private GUI gui;
	
	/**
	 * @param gui ������, ����������� ��� ������� � ������� �����
	 * @param ship �������, � ������� ������������� ������ �����
	 */
	public Client(GUI gui, Ship ship) {
		this.gui = gui;
		this.ship = ship;
		arrivalPeriod = ship.getArrivalPeriod();
	}		
	
	/** ������ ����������� � �������, ���������� � ���� ��� �������. 
	 * ������ � ����� ���������� ���������� � ���, �� ������ �� ����. 
	 * � ���������� �� ����� ������ ���� ���������� ���������� � �������,
	 * ���� �������� �� ���� ���������� � ������� � �������� ���� � �����,
	 * ����� ����� ����� �������� ��������� �� ��� �� ������ ����, ���� 
	 * ������� �����������/���������� �� ������
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
