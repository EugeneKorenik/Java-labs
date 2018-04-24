package client;

import java.io.Serializable;

public class Ship implements Serializable {
	
	private String name;
	private String goodsName;
	private int capacity;
	private int arrivalPeriod;
	private boolean provider;
	
	public Ship(String name, int capacity, String goodsName, int arrivalPeriod, boolean provider) {
		this.name = name;
		this.capacity = capacity;
		this.goodsName = goodsName;
		this.provider = provider;
		this.arrivalPeriod = arrivalPeriod;
	}
	/** Метод, возвращающий имя корабля
	 * @return Имя корабля
	 */
	public String getName() {
		return name;
	}
	
	/** Метод, возвращающий имя товара, который он доставляет
	 * @return Имя товара
	 */
	public String getGoodsName() {
		return goodsName;
	}
	
	/** Метод, возвращающий вместимость корабля 
	 * @return Количество груза, которое корабль может загрузить/разгрузить
	 */
	public int getCapacity() {
		return capacity;
	}
	
	/** Метод, возращающий периодиность обращения корабля к порту
	 * @return Период обращения
	 */
	public int getArrivalPeriod() {
		return arrivalPeriod;
	}
	
	/** Метод, показывающий является корабль импортером или экспортером 
	 * @return Является ли корабль импортером
	 */
	public boolean isProvider() {
		return provider;
	}
}
