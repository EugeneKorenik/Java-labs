package gui;

import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.ClientPool;
import server.MultiThreadServer;
import server.WriterToFile;

/** ����� ������������ ����������� ������� �� ������ ���
 * @author E������
 */
public class Handlers {
	
	/** ���������� ���� ��� �������� ������ �������. ���������� ��� �����������
	 * ������� �� ��������������� ������*/
	private AddNewShipDialog dialog;
	
	/** ����, ������ ����� ��� ��������� � ���� ��� ������� �� ������ 
	 * ��������/�������� �����*/
	private MultiThreadServer server;
	
	/** ��� ��������, ������ ��������� ��� ��������� � ���� ����� ��������
	 * ��� ������� �� ��������������� ������ */
	private ClientPool pool;
	
	/** ���, ������� ���������� ��������� � ����, ����� �� ��� ���������� �
	 * �������*/
	private GUI gui;	
	
	/** ���������� ������, ��������� � ����� �� ������ ������*/
	private int piersQuantity;
	
	/** ������� ����� ��������� ������������ ������� ������
	 * @param gui ���, ������� ���������� ��������� � ����, ����� �� ��� 
	 * ���������� � �������
	*/
	public Handlers(GUI gui) {
		piersQuantity = 1;
		pool = new ClientPool(gui);
		dialog = new AddNewShipDialog(pool);
		dialog.setModal(true);
		this.gui = gui;		
	}
	
	/** ��������� ���������� ������� ���������� �������
	 * @return ���������� ������� ���������� ��������
	 */
	public ActionListener getAddShipHandler() {
		return event->{
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		};
	}	
	
	/** ���������� ���������� ������� �������� �����
	 * @return ���������� ������� �������� �����
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
	
	/**������� �������� �����. �������, ���� ��� ������� ������������*/
	private void closeServer() {
		server.close();
		try {
			server.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		gui.blockButtons(true);
	}
	
	/** ���������� ���������� �������� �����
	 * @return ���������� �������� �����
	 */
	public ActionListener getClosePortHandler() {
		return event->closeServer();
	}
	
	/** ���������� ���������� �������� ����������. �������� �������� �����
	 * � ���� ���� ��� ���������, ����� ����� ��������� ����������.
	 * @return ���������� �������� �����
	 */
	public ActionListener getCloseApplicationHandler() {
		return event->{
			gui.setVisible(false);
			if(server != null)
				closeServer();
			System.exit(0);
		};
	}
	
	/** ��������� ���������� ������ ��� �����. �������� �� ������ GUI,
	 * ��� ��������������� ���������� ������ ���������� ������ ���, � ���������
	 * ��� ����� ���������� � ���, �� ������ GUI ������������� ����� ��������
	 * @param quantity ����� ����� ������ � �����
	 */
	public void setPiersQuantity(int quantity) {
		piersQuantity = quantity;
	}
	
	/** ��������� ���������� ������� ��������� ���������� ������ � �����
	 * @return ���������� ������� ��������� ���������� ������ � �����
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
