package gui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**Класс обесечивающий перетаскивание мышкой окна приложения*/
public class MouseListener extends MouseAdapter {
	
	private GUI gui;
	private int xScene, yScene;
	
	/** Сохраняет в классе ГПИ, перетаскивание которого нужно обеспечить
	 * @param gui Графический пользовательский интерфейс
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
