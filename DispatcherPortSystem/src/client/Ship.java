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
	/** �����, ������������ ��� �������
	 * @return ��� �������
	 */
	public String getName() {
		return name;
	}
	
	/** �����, ������������ ��� ������, ������� �� ����������
	 * @return ��� ������
	 */
	public String getGoodsName() {
		return goodsName;
	}
	
	/** �����, ������������ ����������� ������� 
	 * @return ���������� �����, ������� ������� ����� ���������/����������
	 */
	public int getCapacity() {
		return capacity;
	}
	
	/** �����, ����������� ������������ ��������� ������� � �����
	 * @return ������ ���������
	 */
	public int getArrivalPeriod() {
		return arrivalPeriod;
	}
	
	/** �����, ������������ �������� ������� ���������� ��� ����������� 
	 * @return �������� �� ������� ����������
	 */
	public boolean isProvider() {
		return provider;
	}
}
