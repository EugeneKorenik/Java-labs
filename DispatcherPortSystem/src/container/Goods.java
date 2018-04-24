package container;

/** Класс для хранения пары ИМЯ ТОВАРА - КОЛИЧЕСТВО ТОВАРА
 * @author Eвгений
 */
public class Goods {
	
	/** Имя товара */
	private String name;
	
	/** Количество товара*/
	private int quantity;
	
	/**
	 * Создает новую пару ИМЯ ТОВАРА - КОЛИЧЕСТВО ТОВАРА
	 * @param name Имя товара
	 * @param quantity Количество товара
	 */
	public Goods(String name, int quantity) {
		this.name = name;
		this.quantity = quantity;
	}
	
	/** Метод, возвращающий наименование товара
	 * @return Наименование товара
	 */
	public String getName() {
		return name;
	}
	
	/** Метод, возвращающий количество товара*/
	public int getQuantity() {
		return quantity;
	}
	
	/** Метод, устанавливающий имя товара
	 * @param Новое имя товара 
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/** Метод, устанавливающий количнство товара
	 * @param Новое количество товара
	 * */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
