package server;

import java.util.ArrayList;

import client.Ship;
import container.Goods;
import gui.GUI;

/** �����, ������� ����������� ����� �����
 * @author E������
 */
public class Warehouse {
	
	/** ����������� �����, ������� ������ ������ �������� � ������*/
	private final int PAUSE = 1000;
	
	/** ����, ������������, ������ �� ������*/
    private boolean portIsClose;
    
    /** ������ ��� ��� ������ - ���������� ������, ������������� �� ������*/
	private ArrayList<Goods> goods;
	
	/** ���, ��������� ��� ������� � �������*/
	private GUI gui;	
	
	/** ������� ����� �����
	 * @param gui ���, ��������� ��� ������� � �������
	 *  */
	public Warehouse(GUI gui) {
		this.gui = gui;
		goods = new ArrayList<Goods>();
	}
	
	/** ������������ ���� �������� �������
	 * @param value ������� �� ����
	 * */
	public void setPortIsClose(boolean value) {
		portIsClose = value;
	}
	
	/** ������������������ �����, � ������� ������� ������������, 
	 * ���� ����������� �� ������
	 * @param ship �������, ������� ����� � �����
	 * */
	synchronized public void enterToWarehouse(Ship ship) {
		if(portIsClose) return;
		int additionalPause = ship.getCapacity();
		String shipName = ship.getName();
		
		gui.enterToWarehouse(shipName);
		if(ship.isProvider()) unloadShip(ship);
		else downloadShip(ship);
		
		try {
			Thread.sleep(PAUSE + additionalPause * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		gui.refreshWarehouseTable(goods);		
		gui.leaveWarehouse(shipName);
	}
	
	/** �������� �������
	 * @param ship �������, � ������� ���������� �������� ������
	 */
	private void downloadShip(Ship ship) {
		int size = goods.size();
		String goodsName = ship.getGoodsName();
		for(int i = 0; i < size; i++)
			if(goodsName.equalsIgnoreCase(goods.get(i).getName())) {
				Goods temp = goods.get(i);
				int goodsQuantity = temp.getQuantity();
				goodsQuantity -= ship.getCapacity();
				temp.setQuantity(goodsQuantity);
				if(goodsQuantity < 0) goodsQuantity = 0;
				break;
			}				
	}
	
	/** ��������� �������
	 * @param ship �������, �� �������� ���������� ��������� ������
	 */
	private void unloadShip(Ship ship) {
		int size = goods.size();
		String goodsName = ship.getGoodsName();
		for(int i = 0; i < size; i++)
			if(goodsName.equals(goods.get(i).getName())) {
				Goods temp = goods.get(i);
				int quantity = temp.getQuantity();
				temp.setQuantity(quantity + ship.getCapacity()); 
				return;
			}
		goods.add(new Goods(ship.getGoodsName(), ship.getCapacity()));
	}
	
	/** ��������� ������ ��� ��� ������ - ���������� ������ �� ������ 
	 * @return ������ ��� ��� ������ - ���������� ������
	 */
	public ArrayList<Goods> getGoodsList() {
		return goods;
	}
}
