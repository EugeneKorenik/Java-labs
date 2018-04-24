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
	
	/** Пул, в котором запускаются потоки обработки запросов от клиентов*/
    private ThreadPoolExecutor executor;
    
    /** Очередь, необходимая для создания пула*/
    private BlockingQueue<Runnable> serverQueue;
    
    /** Массив, в котором показывается занятость портов*/
    private boolean[] piersIsOpen;
    
    /** Серверный сокет*/
    private ServerSocket serverSocket;
    
    /** Флаг закрытия порта */
    private boolean portIsOpen;
    
    /** Число пирсов у порта*/
    private int piersQuantity;
    
    /** Склад порта*/
    private Warehouse warehouse;
    
    /** Поток, осуществяющий запись информации в файл*/
    private WriterToFile writer;
    
    /** Необходим для доступа к консоли, чтобы выводить информацию о старте и 
     * окончании закрытия порта */
    private GUI gui;
    
    /** Максимальное число пирсов*/
    private final int MAX_PIERS_QUANTITY = 5000;
    
    /** Создает порт, с определенным числом пирсов
     * @param gui  Необходим для доступа к консоли, чтобы выводить информацию о старте и 
     * окончании закрытия порта
     * @param piersQuantity Количество пирсов у порта
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
	
	/** Ожидает поключения от клиент, после этого отправляет его на обработку*/
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
	
	/** Ждет освобождения какого-либо пирса*/
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
	
	/** Установка количества портов
	 * @param quantity Новое количество портов
	 * */
	public void setPiersQuantity(int quantity) {
		if(quantity > 5000) return;
		executor.setCorePoolSize(quantity);
		piersQuantity = quantity;
	}
	
	/** Проверка, открыт ли сервер (используется в отдельном потоке-сервере) 
	 * @return Открыт ли сервер*/
	public boolean isOpen() {
		return portIsOpen;
	}
	
	/** Закрытие порта*/
	public void close() {
		gui.startClosePortMessage();
		portIsOpen = false;
		warehouse.setPortIsClose(true);
		writer.mustClose();
	}
	
	/** Освобождение порта
	 * @param index Номер порта, который был освобожден*/
	synchronized public void remove(int index) {
		piersIsOpen[index] = true;
	}
}
