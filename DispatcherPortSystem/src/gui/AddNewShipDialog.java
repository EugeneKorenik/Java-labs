package gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import client.Client;
import client.ClientPool;
import client.Ship;

/**Диалоговое окно для создания нового корабля. Необходимо ввести имя корабля,
 * его вместимость, наименование перевозимого товара, период обращения к порту,
 * тип корабля по отношению к порту (импортер\экспортер)
 * @author Eвгений
  */
public class AddNewShipDialog extends JDialog {
	
	/**Текстовые поля для ввода информации */
	private JTextField shipNameTextField;
	private JTextField capacityTextField;
	private JTextField goodsNameTextField;
	private JTextField arrivalPeriodTextField;
	
	/**Заголовки текстовых полей, чтобы было понятно, что необходимо ввести*/
	private final JLabel NAME_LABEL = new JLabel("Ship name");
	private final JLabel CAPACITY_LABEL = new JLabel("Ship capacity [1-10]");
	private final JLabel GOODS_NAME_LABEL = new JLabel("Goods name");
	private final JLabel ARRIVAL_PERIOD_LABEL = new JLabel("Arrival period [3-6]");
	
	/**Шрифт, который применяется ко всем заголовкам тектовых полей*/
	private final Font LABEL_FONT = new Font("Arial", 14, 14);
	
	/**Кнопки для создания нового клиента или отмене операции*/
	private JButton enter;
	private JButton cancel;
	
	/** Конпка определяющая типа корабля (импортер/экспортер)*/
	private JCheckBox isProvider;
	
	/**Ширина тектовых полей */
	private final int TEXTFIELD_WIDTH = 170;
	
	/**Ширина заголовков тектовых полей */
	private final int LABEL_WIDTH = 170;
	
	/**Ширина кнопок */
	private final int BUTTONS_WIDTH = 80;
	
	/**Высота всех графических компонентов */
	private final int COMPONENTS_HEIGHT = 25;
	
	/**Поол клиентов, куда добавляется новый клиент*/
	private final ClientPool pool;
	
	/**Инициализируется форма для создания нового корабля
	 * @param pool Поол, в котором создается новый клиент
	*/
	public AddNewShipDialog(ClientPool pool) {
		this.pool = pool;
		setLayout(null);
		
		locateComponents();
		addAllOnFrame();
		
		cancel.addActionListener(event->{
			setVisible(false);
			shipNameTextField.setText(null);
			capacityTextField.setText(null);
			goodsNameTextField.setText(null);
			arrivalPeriodTextField.setText(null);
		});
		
		enter.addActionListener(event->{
			String name = "";
			String goodsName = "";
			String capacityStr = "";
			String arrivalPeriodStr = "";
			int capacity = 0;
			int arrivalPeriod = 0;
				
			while(true) {
				name = shipNameTextField.getText();
				capacityStr = capacityTextField.getText();
				goodsName = goodsNameTextField.getText();
				arrivalPeriodStr = arrivalPeriodTextField.getText();
				
				if(name.replaceAll(" ", "").equals("") || capacityStr.replaceAll(" ", "").equals("") 
						|| goodsName.replaceAll(" ", "").equals("") || arrivalPeriodStr.replaceAll("", " ").equals("")) {
				    JOptionPane.showMessageDialog(null, "Invalid input");
					return;
				}
				
				try {
					capacity = Integer.parseInt(capacityStr);
					arrivalPeriod = Integer.parseInt(arrivalPeriodStr);
					if(capacity < 1 || capacity > 10) throw new NumberFormatException("Capacity must be in [1-10]");
					if(arrivalPeriod < 3 || arrivalPeriod > 6) throw new NumberFormatException("Arrival period must be in [3-6]");
					pool.addClient(new Ship(name, capacity, goodsName, arrivalPeriod, isProvider.isSelected()));
					break;
				}catch(NumberFormatException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
					return;
				}
			}
			setVisible(false);
			shipNameTextField.setText(null);
			capacityTextField.setText(null);
			goodsNameTextField.setText(null);
			arrivalPeriodTextField.setText(null);			
		});
		
		setResizable(false);
		setSize(200, 260);
	}	
	
	/**Устанавливает расположение графических компонентов*/
	public void locateComponents() {
		NAME_LABEL.setBounds(10, 0, LABEL_WIDTH, COMPONENTS_HEIGHT);
		NAME_LABEL.setFont(LABEL_FONT);
		
		CAPACITY_LABEL.setBounds(10, 40, LABEL_WIDTH, COMPONENTS_HEIGHT);
		CAPACITY_LABEL.setFont(LABEL_FONT);
		
		GOODS_NAME_LABEL.setBounds(10, 80, LABEL_WIDTH, COMPONENTS_HEIGHT);
		GOODS_NAME_LABEL.setFont(LABEL_FONT);
		
		ARRIVAL_PERIOD_LABEL.setBounds(10, 120, LABEL_WIDTH, COMPONENTS_HEIGHT);
		ARRIVAL_PERIOD_LABEL.setFont(LABEL_FONT);
		
		shipNameTextField = new JTextField();
		capacityTextField = new JTextField();
		goodsNameTextField = new JTextField();
		arrivalPeriodTextField = new JTextField();
		
		shipNameTextField.setBounds(10, 20, TEXTFIELD_WIDTH, COMPONENTS_HEIGHT);
		capacityTextField.setBounds(10, 60, TEXTFIELD_WIDTH, COMPONENTS_HEIGHT);
		goodsNameTextField.setBounds(10, 100, TEXTFIELD_WIDTH, COMPONENTS_HEIGHT);
		arrivalPeriodTextField.setBounds(10, 140, TEXTFIELD_WIDTH, COMPONENTS_HEIGHT);
		
		isProvider = new JCheckBox("Is provider");
		isProvider.setBounds(10, 170, LABEL_WIDTH, COMPONENTS_HEIGHT);
		isProvider.setFont(LABEL_FONT);
		
		enter = new JButton("Enter");
		cancel = new JButton("Cancel");
		
		enter.setBackground(Color.white);
		cancel.setBackground(Color.white);
		
		enter.setBounds(10, 200, BUTTONS_WIDTH, COMPONENTS_HEIGHT);
		cancel.setBounds(100, 200, BUTTONS_WIDTH, COMPONENTS_HEIGHT);
	}
	
	/**Добавление всех комонентов в окно*/
	public void addAllOnFrame() {
		add(NAME_LABEL);
		add(CAPACITY_LABEL);
		add(GOODS_NAME_LABEL);
		add(ARRIVAL_PERIOD_LABEL);
		
		add(shipNameTextField);
		add(capacityTextField);
		add(goodsNameTextField);
		add(arrivalPeriodTextField);
		
		add(isProvider);
		add(enter);
		add(cancel);
	}
}
