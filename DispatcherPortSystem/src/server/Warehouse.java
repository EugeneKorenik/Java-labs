package server;

import java.util.ArrayList;

import client.Ship;
import container.Goods;
import gui.GUI;

/** Класс, который предствляет собой склад
 * @author Eвгений
 */
public class Warehouse {
	
	/** Минимальное время, которое клиент должен провести в складе*/
	private final int PAUSE = 1000;
	
	/** Флаг, определяющий, закрыт ли сервер*/
    private boolean portIsClose;
    
    /** Список пар имя товара - количество товара, содержащегося на складе*/
	private ArrayList<Goods> goods;
	
	/** ГПИ, необходим для доступа к консоли*/
	private GUI gui;	
	
	/** Создает новый склад
	 * @param gui ГПИ, необходим для доступа к консоли
	 *  */
	public Warehouse(GUI gui) {
		this.gui = gui;
		goods = new ArrayList<Goods>();
	}
	
	/** Устанвливает флаг закрытия сервера
	 * @param value Закрыть ли порт
	 * */
	public void setPortIsClose(boolean value) {
		portIsClose = value;
	}
	
	/** Синхронизированный метод, в котором корабль разгружается, 
	 * либо загружается со склада
	 * @param ship Корабль, который вошел в склад
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
	
	/** Загрузка корабля
	 * @param ship Корабль, в который происходит загрузка товара
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
	
	/** Разгрузка корабля
	 * @param ship Корабль, из которого происходит разгрузка товара
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
	
	/** Возаращет список пар имя товара - количество товара на складе 
	 * @return Список пар имя товара - количество товара
	 */
	public ArrayList<Goods> getGoodsList() {
		return goods;
	}
}
