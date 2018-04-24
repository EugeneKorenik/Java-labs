package container;

/** ����� ��� �������� ���� ��� ������ - ���������� ������
 * @author E������
 */
public class Goods {
	
	/** ��� ������ */
	private String name;
	
	/** ���������� ������*/
	private int quantity;
	
	/**
	 * ������� ����� ���� ��� ������ - ���������� ������
	 * @param name ��� ������
	 * @param quantity ���������� ������
	 */
	public Goods(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}
	
	/** �����, ������������ ������������ ������
	 * @return ������������ ������
	 */
	public String getName() {
		return name;
	}
	
	/** �����, ������������ ���������� ������*/
	public int getQuantity() {
		return quantity;
	}
	
	/** �����, ��������������� ��� ������
	 * @param ����� ��� ������ 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/** �����, ��������������� ���������� ������
	 * @param ����� ���������� ������
	 * */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
