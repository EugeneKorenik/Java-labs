package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import container.Goods;
import gui.GUI;

/** Поток, записывающий в файл информацию о состоянии склада, порта
 * и очереди вхождения в порт
 * @author Eвгений
 */
public class WriterToFile extends Thread {
	
	/** Получаем списки кораблей в пирсах и в очереди*/
	private GUI gui;
	
	/** Получаем состояние склада*/
	private Warehouse warehouse;
	
	/** Флаг прекращения работы потока*/
	private boolean mustClose;
	
	/** Класс, осуществляющий запись в файл*/
	private FileWriter writer;
	
	/** Преобразует дату в нужный нам текстовый формат*/
	private SimpleDateFormat dateFormat;
	
	/** Текущая дата*/
	private Date date;
	
	/** Создание потока, записывающего каждые 5 секунд информацию о порте
	 * @param gui Получаем списки кораблей в пирсах и в очереди
	 * @param warehouse Получаем состояние склада
	 */
	public WriterToFile(GUI gui, Warehouse warehouse) {
		this.gui = gui;
		this.warehouse = warehouse;
		mustClose = false;		
		dateFormat = new SimpleDateFormat();
		date = new Date();
		try {
			writer = new FileWriter(new File("history.txt"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	/** Устанавливает флаг прекращения работы потока*/
	public void mustClose() {
		mustClose = true;
	}
	
	/** Каждые 5 секунд записывает в файл информацию*/
	@Override
	public void run() {
		while(true) {
			if(mustClose) break;
			ArrayList<String> piersState = gui.getPiersState();
			ArrayList<String> queue = gui.getQueue();
			ArrayList<Goods> goodsInWarehouse = warehouse.getGoodsList();
			
			try {
				writer.write(dateFormat.format(date) + "\n");
				writer.write("piersState\n");
				int size = piersState.size();
				for(int i = 0; i < size; i++)
					writer.write(piersState.get(i) + "\n");
				writer.write("piersStateEnd\n");
				
				writer.write("queue\n");
				size = queue.size();
				for(int i = 0; i < size; i++)
					writer.write(queue.get(i) + "\n");
				writer.write("queueEnd\n");
				
				writer.write("warehouseState\n");
				size = goodsInWarehouse.size();
				for(int i = 0; i < size; i++) {
					Goods goods = goodsInWarehouse.get(i);
					writer.write(goods.getName() + " " + goods.getQuantity() + "\n");
				}
				writer.write("warehouseStateEnd\n\n");
				
				Thread.sleep(5000);
			} catch (IOException e) {
				e.printStackTrace();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
