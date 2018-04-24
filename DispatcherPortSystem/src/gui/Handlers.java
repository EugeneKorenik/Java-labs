package gui;

import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.ClientPool;
import server.MultiThreadServer;
import server.WriterToFile;

/** Класс поставляющий обработчики событий на кнопки ГПИ
 * @author Eвгений
 */
public class Handlers {
	
	/** Диалоговое окно для создания нового корабля. Необходимо для обработчика
	 * нажатия на соответствующую кнопку*/
	private AddNewShipDialog dialog;
	
	/** Порт, объект нужен для обращения к нему при нажатии на кнопки 
	 * открытия/закрытия порта*/
	private MultiThreadServer server;
	
	/** Пул клиентов, коорый необходим для доваления в него новых кораблей
	 * при нажатии на соответствующую строку */
	private ClientPool pool;
	
	/** ГПИ, который необходимо поставить в порт, чтобы он мог обращаться к
	 * консоли*/
	private GUI gui;	
	
	/** Количество пирсов, имеющееся в порту на данный момент*/
	private int piersQuantity;
	
	/** Создает новый поставщик обработчиков нажатия кнопок
	 * @param gui ГПИ, который необходимо поставить в порт, чтобы он мог 
	 * обращаться к консоли
	*/
	public Handlers(GUI gui) {
		piersQuantity = 1;
		pool = new ClientPool(gui);
		dialog = new AddNewShipDialog(pool);
		dialog.setModal(true);
		this.gui = gui;		
	}
	
	/** Посталяет обработчик события добавления корабля
	 * @return Обработчик события добавления кораблся
	 */
	public ActionListener getAddShipHandler() {
		return event->{
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		};
	}	
	
	/** Поставляет обработчик события открытия порта
	 * @return Обработчик события открытия порта
	 */
	public ActionListener getOpenPortHandler() {
		return event->{
			server = new MultiThreadServer(gui, piersQuantity);
			server.start();
			gui.setPiersQuantity(piersQuantity);
			gui.blockButtons(false);
			pool.runAllClients();
		};
	}
	
	/**Фукнция закрытия порта. Ожидает, пока все клиенты отсоединятся*/
	private void closeServer() {
		server.close();
		try {
			server.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		gui.blockButtons(true);
	}
	
	/** Поставляет обработчик закрытия порта
	 * @return Обработчик закрытия порта
	 */
	public ActionListener getClosePortHandler() {
		return event->closeServer();
	}
	
	/** Поставляет обработчик закрытия приложения. Вызывает закрытия порта
	 * и ждет пока тот закроется, после этого закрывает приложение.
	 * @return Обработчик закрытия порта
	 */
	public ActionListener getCloseApplicationHandler() {
		return event->{
			gui.setVisible(false);
			if(server != null)
				closeServer();
			System.exit(0);
		};
	}
	
	/** Установка количества пирсов для порта. Вызывает из класса GUI,
	 * ибо соответствующий обработчик меняет количество пирсов там, а поскольку
	 * это число необходимо и тут, то объект GUI устанавливает новое значение
	 * @param quantity Новое число пирсов у порта
	 */
	public void setPiersQuantity(int quantity) {
		piersQuantity = quantity;
	}
	
	/** Возаращет обработчик события установки количества пирсов у порта
	 * @return Обработчик события установки количества пирсов у порта
	 */
	public ActionListener getPiersQuantityHandler() {
		return event->{
			while(true){
				String quantityStr = JOptionPane.showInputDialog("Enter piersQuantity");
				if(quantityStr == null) return;
				
				try {
					int quantity = Integer.parseInt(quantityStr);
					if(quantity < 1 || quantity > 100) throw new NumberFormatException();
					server.setPiersQuantity(quantity);
					gui.setPiersQuantity(quantity);
					break;
				}catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "Enter digit in [1, 100]");
				}
			}
		};
	}
}
