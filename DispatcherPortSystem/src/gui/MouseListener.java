package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**����� ������������� �������������� ������ ���� ����������*/
public class MouseListener extends MouseAdapter {
	
	private GUI gui;
	private int xScene, yScene;
	
	/** ��������� � ������ ���, �������������� �������� ����� ����������
	 * @param gui ����������� ���������������� ���������
	 */
	public MouseListener(GUI gui) {
		this.gui = gui;
	}
	
	@Override
	public void mouseDragged(MouseEvent me) {
		gui.setLocation(me.getXOnScreen() - xScene, me.getYOnScreen() - yScene);
	}
	
	@Override
	public void mousePressed(MouseEvent me) {
		xScene = me.getX();
		yScene = me.getY();
	}

}
