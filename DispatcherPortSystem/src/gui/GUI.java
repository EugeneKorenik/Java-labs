package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Panel;
import java.awt.TextArea;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import container.Goods;
import server.WriterToFile;

public class GUI extends JFrame implements Serializable {

	/**��������� ������ ��������� �����, ������ � ������� � ����*/
	private final JLabel QUEUE_LABEL = new JLabel("Queue");
	private final JLabel PIERS_STATE_LABEL = new JLabel("Pierses info"); 
	private final JLabel WAREHOUSE_LABEL = new JLabel("Warehouse");
	
	/**�������, � ������� ��������� ��� �������� �������� � ����� */
	private TextArea console;
	
	/**������ ��� ��������\�������� �����, ���������� ������ ��������,
	 * ��������� ����� ������� � �������� ����������*/
	private JButton openPort;
	private JButton closePort;
	private JButton addShip;
	private JButton setPiersQuantity;
	private JButton close;
	
	/**������� ����������� �����������*/
	private final Border COMPONENT_BORDER = new LineBorder(Color.blue);
	
	/**���������� ���������� � ��������� ����� ������ */
	private JLabel piersQuantityLabel, piersFreeLabel;
	
	/**��������� ������ ������� � ��������� �����*/
	private final String[] header = {"�", "Ship"};
	
	/**��������� ������� ��������� ������ */
	private final String[] warehouseHeader = {"Goods", "Quantity"};
	
	/**������ ������� ��������� ������, �����, � ������� �� ��������� � ����*/
	private DefaultTableModel piersStateModel, queueModel, warehouseModel;
	
	/**������ � ������ ����*/
	private final int FRAME_WIDTH = 630;
	private final int FRAME_HEIGHT = 340;
	
	/**������ � ������ ������� */
	private final int CONSOLE_WIDTH = 400;
	private final int CONSOLE_HEIGHT = 150;	
	
	/**������ ���� ������ */
	private final Dimension BUTTON_SIZE = new Dimension(150, 30);
	
	/**��������� ������������ ������� ������*/
	private Handlers handlers;
	
	/**����� ���� ������ � ����� ������, ������� �������� */
	private int piersQuantity, piersFree;
	
	/**������� ���*/
	public GUI() {
		piersQuantity = 0;
		piersFree = 0;
		handlers = new Handlers(this);
			
		setLayout(null);
		console = new TextArea();
		console.setBounds(0, 0, CONSOLE_WIDTH, CONSOLE_HEIGHT);
		console.setBackground(Color.WHITE);
		console.setEditable(false);
		console.setFocusable(false);
		console.setFont(new Font("Arial", 15, 15));
		
		JScrollPane scroll = new JScrollPane();
		scroll.setBounds(20, 20, CONSOLE_WIDTH, CONSOLE_HEIGHT);
		scroll.setLayout(null);
		scroll.add(console);
		add(scroll);
			
		createButton();
		createPortStatePanel();
		createTabels();
		setHandlers();
		
		MouseListener listener = new MouseListener(this);
		addMouseListener(listener);
		addMouseMotionListener(listener);
		
		getRootPane().setBorder(COMPONENT_BORDER);
		setResizable(false);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setUndecorated(true);
		setVisible(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**��������� ������ ������� ������������� ����� ���� ������ � �����
	 * ������ ������� ��������*/
	public void createPortStatePanel() {
		piersQuantityLabel = new JLabel("SUMMARY: " + piersQuantity);
		piersFreeLabel = new JLabel("FREE: " + piersFree);
		piersQuantityLabel.setBounds(20, 0, 100, 20);
		piersFreeLabel.setBounds(180, 0, 100, 20);
		
		add(piersFreeLabel);
		add(piersQuantityLabel);
	}
	
	/**��������� ����������� �� ������*/
	public void setHandlers() {
		addShip.addActionListener(handlers.getAddShipHandler());
		openPort.addActionListener(handlers.getOpenPortHandler());
		closePort.addActionListener(handlers.getClosePortHandler());
		close.addActionListener(handlers.getCloseApplicationHandler());
		setPiersQuantity.addActionListener(handlers.getPiersQuantityHandler());
	}
	
	/**�������� ������� ��������� ������, ������ � ������� ��������� � ����*/
	public void createTabels() {
		Panel statePanel = new Panel();				
		statePanel.setBounds(20, CONSOLE_HEIGHT + 30, 190, 140);
		statePanel.setLayout(new BorderLayout());
		statePanel.add(PIERS_STATE_LABEL, BorderLayout.NORTH);		
		piersStateModel = new DefaultTableModel(header, 100);
		
		for(int i = 0; i < 100; i++) {
			piersStateModel.setValueAt(i+1, i, 0);
			piersStateModel.setValueAt(" - ", i, 1);
		}
		
		JTable piersState = new JTable(piersStateModel);	
		statePanel.add(new JScrollPane(piersState), BorderLayout.CENTER);
		add(statePanel);
		
		Panel queuePanel = new Panel();
		queuePanel.setBounds(230, CONSOLE_HEIGHT + 30, 190, 140);
		queuePanel.setLayout(new BorderLayout());
		queuePanel.add(QUEUE_LABEL, BorderLayout.NORTH);
		queueModel = new DefaultTableModel(header, 100);
			
		for(int i = 0; i < 100; i++) {
			queueModel.setValueAt(i+1, i, 0);
			queueModel.setValueAt("", i, 1);
		}
		
		JTable queue = new JTable(queueModel);
		queuePanel.add(new JScrollPane(queue), BorderLayout.CENTER);
		add(queuePanel);
		
		Panel warehousePanel = new Panel();
		warehousePanel.setBounds(CONSOLE_WIDTH + 40, CONSOLE_HEIGHT + 30, 150, 140);
		warehousePanel.setLayout(new BorderLayout());
		warehousePanel.add(WAREHOUSE_LABEL, BorderLayout.NORTH);
		warehouseModel = new DefaultTableModel(warehouseHeader, 0);
		JTable warehouseTable = new JTable(warehouseModel);
		warehousePanel.add(new JScrollPane(warehouseTable), BorderLayout.CENTER);
		
		add(warehousePanel);
	}
	
	/**�������� ������*/
	public void createButton() {
		Panel buttonBar = new Panel();
		buttonBar.setLayout(null);
		buttonBar.setBounds(CONSOLE_WIDTH + 40, 20, 150, 150);
		buttonBar.setBackground(Color.blue);
	
		openPort = new JButton("Open port");
		closePort = new JButton("Close port");
		addShip = new JButton("Add ship");
		setPiersQuantity = new JButton("Set piers quantity");
		close = new JButton("Close application");
		
		openPort.setBackground(Color.white);
		closePort.setBackground(Color.white);
		addShip.setBackground(Color.white);
		setPiersQuantity.setBackground(Color.white);
		close.setBackground(Color.white);
		
		openPort.setBorder(COMPONENT_BORDER);
		closePort.setBorder(COMPONENT_BORDER);
		addShip.setBorder(COMPONENT_BORDER);
		setPiersQuantity.setBorder(COMPONENT_BORDER);
		close.setBorder(COMPONENT_BORDER);
		
		close.setSize(BUTTON_SIZE);
		close.setLocation(0, 0);
		
		openPort.setSize(BUTTON_SIZE);
		openPort.setLocation(0, 30);
		
		closePort.setSize(BUTTON_SIZE);
		closePort.setLocation(0, 60);
		
		addShip.setSize(BUTTON_SIZE);
		addShip.setLocation(0, 90);
		
		setPiersQuantity.setSize(BUTTON_SIZE);
		setPiersQuantity.setLocation(0, 120);
		
    	closePort.setEnabled(false);
		addShip.setEnabled(false);
		setPiersQuantity.setEnabled(false);
		
		buttonBar.add(close);
		buttonBar.add(openPort);
		buttonBar.add(closePort);
		buttonBar.add(addShip);
		buttonBar.add(setPiersQuantity);
		
		add(buttonBar);
	}	
	
	/** ��������� ���������� ������. �������� ����������� ������ � �������
	 * @param quantity ����� ���������� ������
	 * */
	public void setPiersQuantity(int quantity) {
    	if(quantity > piersQuantity) piersFree += quantity - piersQuantity;
    	else {
    		piersFree -= piersQuantity - quantity;
    		if(piersFree < 0) piersFree = 0;
    	}
    	
       	piersQuantity = quantity;
       	handlers.setPiersQuantity(piersQuantity);
       	
   		refreshInfo();
		
		int start = 0;
		while(!piersStateModel.getValueAt(start, 1).equals(" - "))
			start++;
		
		for(int i = start; i < quantity; i++)
			piersStateModel.setValueAt("Free", i, 1);
		
		for(int i = quantity; i < 100; i++)
			if(piersStateModel.getValueAt(i, 1).equals("Free"))
				piersStateModel.setValueAt(" - ", i, 1);
	}
	
	/**��������� ������� ������� � ������� ��� �������, ������� ��������� � �����
	 * @param shipName ��� �������, ������� ����� ������ �� ��������� � ����
	 * */
    public void addToQueue(String shipName) {
    	int row = 0;
    	for(row = 0; ; row++)
    		if(queueModel.getValueAt(row, 1).equals(""))
    			break;
    	queueModel.setValueAt(shipName, row, 1);
    }
	
    /**������������� � ������, ��������������� ������ ����� ��� �������,
     * ������� ��� ���������
     * @param shipName ��� ������� � �����
     * @param piersId ����� �����
     */
	synchronized public void shipEnter(String shipName, int piersId) {
		int row = 0;
		for(row = 0; ; row++)
			if(piersId+1 == (Integer) piersStateModel.getValueAt(row, 0))
				break;
		
		piersStateModel.setValueAt(shipName, row, 1);
		console.append("Ship " + shipName + " enter to " + (piersId+1) + " piers\n");	
		
		for(int i = 0; i < 99; i++)
			queueModel.setValueAt(queueModel.getValueAt(i+1, 1), i, 1);
		
		piersFree--;
		refreshInfo();
	}
	
	/** ������� ��� ������� �� ������� � ������� ����� ����� ����, ���
	 * ������ ������� ����
	 * @param shipName ��� �������, ������� ������� ����
	 * @param piersId ����� �����, ������� �����������
	 */
	synchronized public void shipLeave(String shipName, int piersId) {
		int row = 0;
		for(row = 0; ;row++)
			if(piersStateModel.getValueAt(row, 1).equals(shipName))
				break;
		
		if(row < piersQuantity) piersStateModel.setValueAt("Free", row, 1);		
		else piersStateModel.setValueAt(" - ", row, 1);
		console.append("Ship " + shipName + " leave " + (piersId+1) + " piers\n");
		piersFree++;
		refreshInfo();
	}
	
	/**��������� ���������� � ��������� ����� ������ � ����� ��������� ������*/
	public void refreshInfo() {
		piersQuantityLabel.setText("SUMMARY: " + piersQuantity);
		piersFreeLabel.setText("Free: " + piersFree);
	}
	
	/** ������� ��������� � �������, ��� ������� �������� � �������
	 * @param shipName ��� �������, ������� �� ����� ����� � ����
	 * @param piersId ����� �����, � ������� � �������� ���� ��������
	 */
	public void shipReject(String shipName, int piersId) {
		console.append("Reject " + shipName + " enter to " + (piersId+1) + " piers\n");
	}
	
	/** ������� ���������, ��� ������� ����� � �����
	 * @param shipName ��� �������, ��������� � �����
	 */
	public void enterToWarehouse(String shipName) {
		console.append("Ship " + shipName + " enter to warehouse\n");
	}
	
	/** ������� ���������, ��� ������� ������� � �����
	 * @param shipName ��� �������, ����������� � �����
	 */
	public void leaveWarehouse(String shipName) {
		console.append("Ship " + shipName + " leave warehouse\n");
	}
	
	/** ������� ��������� � ���. ��� ���� ����� ��������*/
	public void startClosePortMessage() {
		console.append("PORT START CLOSE\n");
	}
	
	/** ������� ���������, ��� ���� �������� */
	public void portCloseMessage() {
		console.append("PORT CLOSE\n");
	}
	
	/** ���������� ������ ���� �������� � �������
	 * @return C����� ���� �������� � �������
	 * */
	public ArrayList<String> getQueue() {
		synchronized(queueModel) {
			ArrayList<String> queue = new ArrayList<String>();
			int rowsQuantity = queueModel.getRowCount();
			for(int i = 0; i < rowsQuantity; i++) {
				if(queueModel.getValueAt(i, 1).equals("")) break;
				queue.add((String)queueModel.getValueAt(i, 1));
			}
			return queue;
		}
	}
	
	/** ���������� ������ ���� �������� � �����
	 * @return C����� ���� �������� � �����
	 * */
	public ArrayList<String> getPiersState() {
		synchronized(piersStateModel) {
			ArrayList<String> shipsName = new ArrayList<String>();
			int rowsQuantity = piersStateModel.getRowCount();
			for(int i = 0; i < rowsQuantity; i++) {
				if(piersStateModel.getValueAt(i, 1).equals(" - ")) break;
				shipsName.add((String) piersStateModel.getValueAt(i, 1));
			}
			return shipsName;
		}
	}
	
	/** ��������� ������� ��������� ������
	 * @param goods ������ ���������� ���� �������� ������ - ���������� ������ 
	 * �� ������
	 * */
	public void refreshWarehouseTable(ArrayList<Goods> goods) {
		int goodsQuantity = goods.size();
	    int rowsQuantity = warehouseModel.getRowCount();
		
		for(int i = 0; i < rowsQuantity; i++) {
			Goods temp = goods.get(i);
			warehouseModel.setValueAt(temp.getName(), i, 0);
			warehouseModel.setValueAt(temp.getQuantity(), i, 1);
		}
			
		for(int i = rowsQuantity; i < goodsQuantity; i++) {
			Goods temp = goods.get(i);		
			Vector<Object> tempVector = new Vector<Object>();
			tempVector.add(temp.getName());
			tempVector.add(temp.getQuantity());
			warehouseModel.addRow(tempVector);
		}				
	}
	
	/** ��������� ������ ��� �������� ��� �������� �����
	 * @param value ���������� ���� �� ������������� ����� �������� �����
	 * ��� ��� ���������
	 */
	public void blockButtons(boolean value) {
		openPort.setEnabled(value);
		closePort.setEnabled(!value);
		addShip.setEnabled(!value);
		setPiersQuantity.setEnabled(!value);
	}
		
	public static void main(String[] args) throws InvocationTargetException, InterruptedException {
		SwingUtilities.invokeAndWait(new Runnable() {
			public void run() {
				new GUI();
			}
		});	
	}
}
