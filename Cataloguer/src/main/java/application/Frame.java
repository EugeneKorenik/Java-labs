package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/** 
 * Загрузочный класс приложения
 * @author Eugene
 */ 
public class Frame extends Application {

	private Pane mainPane;
	private Controller controller;
	public static Stage primaryStage;
	public static double xScenePos, yScenePos;
	
	public static void main(String[] args) {
		launch(args);
	}	
	
	/** Метод, в котором получается графичсекая сцена,
	 * создается и присваивается контроллер*/
	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Frame.fxml"));
		controller = new Controller();		
		loader.setController(controller);		
		mainPane = loader.load();
		
		
		stage.addEventHandler(MouseEvent.MOUSE_PRESSED, e->{
			xScenePos = e.getSceneX();
			yScenePos = e.getSceneY();
		});
		
		stage.addEventHandler(MouseEvent.MOUSE_DRAGGED, e->{
			stage.setX(e.getScreenX() - xScenePos);
			stage.setY(e.getScreenY() - yScenePos);
		});
				
		stage.setScene(new Scene(mainPane));
		stage.setResizable(false);
		stage.initStyle(StageStyle.UNDECORATED);
		primaryStage = stage;
		stage.show();
	}
}
