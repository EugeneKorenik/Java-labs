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

/**���������� ���� ��� �������� ������ �������. ���������� ������ ��� �������,
 * ��� �����������, ������������ ������������ ������, ������ ��������� � �����,
 * ��� ������� �� ��������� � ����� (��������\���������)
 * @author E������
  */
public class AddNewShipDialog extends JDialog {
	
	/**��������� ���� ��� ����� ���������� */
	private JTextField shipNameTextField;
	private JTextField capacityTextField;
	private JTextField goodsNameTextField;
	private JTextField arrivalPeriodTextField;
	
	/**��������� ��������� �����, ����� ���� �������, ��� ���������� ������*/
	private final JLabel NAME_LABEL = new JLabel("Ship name");
	private final JLabel CAPACITY_LABEL = new JLabel("Ship capacity [1-10]");
	private final JLabel GOODS_NAME_LABEL = new JLabel("Goods name");
	private final JLabel ARRIVAL_PERIOD_LABEL = new JLabel("Arrival period [3-6]");
	
	/**�����, ������� ����������� �� ���� ���������� �������� �����*/
	private final Font LABEL_FONT = new Font("Arial", 14, 14);
	
	/**������ ��� �������� ������ ������� ��� ������ ��������*/
	private JButton enter;
	private JButton cancel;
	
	/** ������ ������������ ���� ������� (��������/���������)*/
	private JCheckBox isProvider;
	
	/**������ �������� ����� */
	private final int TEXTFIELD_WIDTH = 170;
	
	/**������ ���������� �������� ����� */
	private final int LABEL_WIDTH = 170;
	
	/**������ ������ */
	private final int BUTTONS_WIDTH = 80;
	
	/**������ ���� ����������� ����������� */
	private final int COMPONENTS_HEIGHT = 25;
	
	/**���� ��������, ���� ����������� ����� ������*/
	private final ClientPool pool;
	
	/**���������������� ����� ��� �������� ������ �������
	 * @param pool ����, � ������� ��������� ����� ������
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
	
	/**������������� ������������ ����������� �����������*/
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
	
	/**���������� ���� ���������� � ����*/
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
