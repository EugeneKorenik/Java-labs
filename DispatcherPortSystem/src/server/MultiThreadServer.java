package server;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import gui.GUI;


public class MultiThreadServer extends Thread {
	
	/** ���, � ������� ����������� ������ ��������� �������� �� ��������*/
    private ThreadPoolExecutor executor;
    
    /** �������, ����������� ��� �������� ����*/
    private BlockingQueue<Runnable> serverQueue;
    
    /** ������, � ������� ������������ ��������� ������*/
    private boolean[] piersIsOpen;
    
    /** ��������� �����*/
    private ServerSocket serverSocket;
    
    /** ���� �������� ����� */
    private boolean portIsOpen;
    
    /** ����� ������ � �����*/
    private int piersQuantity;
    
    /** ����� �����*/
    private Warehouse warehouse;
    
    /** �����, ������������� ������ ���������� � ����*/
    private WriterToFile writer;
    
    /** ��������� ��� ������� � �������, ����� �������� ���������� � ������ � 
     * ��������� �������� ����� */
    private GUI gui;
    
    /** ������������ ����� ������*/
    private final int MAX_PIERS_QUANTITY = 5000;
    
    /** ������� ����, � ������������ ������ ������
     * @param gui  ��������� ��� ������� � �������, ����� �������� ���������� � ������ � 
     * ��������� �������� �����
     * @param piersQuantity ���������� ������ � �����
     */
	public MultiThreadServer(GUI gui, int piersQuantity) {
		this.gui = gui;
		this.piersQuantity = piersQuantity;
		
		portIsOpen = true;
		serverQueue = new LinkedBlockingQueue<Runnable>();
		piersIsOpen = new boolean[MAX_PIERS_QUANTITY];
		warehouse = new Warehouse(gui);
		writer = new WriterToFile(gui, warehouse);
		
		for(int i = 0; i < MAX_PIERS_QUANTITY; i++)
			piersIsOpen[i] = true;
		
		executor = new ThreadPoolExecutor(piersQuantity, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, serverQueue);
	
		try {
			serverSocket = new ServerSocket(3345);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** ������� ���������� �� ������, ����� ����� ���������� ��� �� ���������*/
	@Override
	public void run() {		
		try {
			serverSocket.setSoTimeout(6000);
			writer.start();
			while(!serverSocket.isClosed()) {
				try {
					Socket client = serverSocket.accept();	
					handle(client);
				} catch(SocketTimeoutException e) {
					if(portIsOpen) continue;
					serverSocket.close();
					executor.shutdown();
					gui.portCloseMessage();
					interrupt();
				}
			}
		} catch(BindException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/** ���� ������������ ������-���� �����*/
	private void handle(Socket client) {
		out:while(true) {
			if(!portIsOpen) break;
			for(int i = 0; i < piersQuantity; i++) {
				if(serverSocket.isClosed()) break out;
				if(piersIsOpen[i]) {			
					piersIsOpen[i] = false;
					executor.execute(new Server(this, client, warehouse, gui, i));		
					break out;
				} 
			}
		}
	}
	
	/** ��������� ���������� ������
	 * @param quantity ����� ���������� ������
	 * */
	public void setPiersQuantity(int quantity) {
		if(quantity > 5000) return;
		executor.setCorePoolSize(quantity);
		piersQuantity = quantity;
	}
	
	/** ��������, ������ �� ������ (������������ � ��������� ������-�������) 
	 * @return ������ �� ������*/
	public boolean isOpen() {
		return portIsOpen;
	}
	
	/** �������� �����*/
	public void close() {
		gui.startClosePortMessage();
		portIsOpen = false;
		warehouse.setPortIsClose(true);
		writer.mustClose();
	}
	
	/** ������������ �����
	 * @param index ����� �����, ������� ��� ����������*/
	synchronized public void remove(int index) {
		piersIsOpen[index] = true;
	}
}
