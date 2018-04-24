package server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import container.Goods;
import gui.GUI;

/** �����, ������������ � ���� ���������� � ��������� ������, �����
 * � ������� ��������� � ����
 * @author E������
 */
public class WriterToFile extends Thread {
	
	/** �������� ������ �������� � ������ � � �������*/
	private GUI gui;
	
	/** �������� ��������� ������*/
	private Warehouse warehouse;
	
	/** ���� ����������� ������ ������*/
	private boolean mustClose;
	
	/** �����, �������������� ������ � ����*/
	private FileWriter writer;
	
	/** ����������� ���� � ������ ��� ��������� ������*/
	private SimpleDateFormat dateFormat;
	
	/** ������� ����*/
	private Date date;
	
	/** �������� ������, ������������� ������ 5 ������ ���������� � �����
	 * @param gui �������� ������ �������� � ������ � � �������
	 * @param warehouse �������� ��������� ������
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
	
	/** ������������� ���� ����������� ������ ������*/
	public void mustClose() {
		mustClose = true;
	}
	
	/** ������ 5 ������ ���������� � ���� ����������*/
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
