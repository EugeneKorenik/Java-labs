package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Класс диалогового окна для изменения пароля
 * администратора или пользователя
 * @author Eugene
 */
public class EditPasswordDialog extends Application {
	
	private PasswordField oldPassword, newPassword, newPasswordAgain;
	private Pane passwordFieldsContainer;
	public  Stage stage;
	private Scene scene;
	private Label label;
	private Button confirmButton, cancelButton;
	private Account account;
	
	EditPasswordDialog (Account account) {
		this.account = account;
		try {
			start(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
	/** Метод построения графического интерфейса */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane pane = new Pane();
		stage = primaryStage;
		oldPassword = new PasswordField();
		oldPassword.setPrefWidth(100);
		newPassword = new PasswordField();
		newPasswordAgain = new PasswordField();
		confirmButton = new Button("Confirm");
		cancelButton = new Button("Cancel");
		label = new Label("Изменение пароля");
		label.setFont(new Font(18));
		
		oldPassword.setPrefWidth(160);
		newPassword.setPrefWidth(160);
		newPasswordAgain.setPrefWidth(160);
		
		oldPassword.setPromptText("Старый пароль");
		newPassword.setPromptText("Новый пароль");
		newPasswordAgain.setPromptText("Повторите новый пароль");
		
		oldPassword.setLayoutY(30);
		newPassword.setLayoutY(60);
		newPasswordAgain.setLayoutY(90);
		confirmButton.setLayoutY(120);
		cancelButton.setLayoutY(120);
		cancelButton.setLayoutX(85);
		
		confirmButton.setPrefWidth(75);
		cancelButton.setPrefWidth(75);
		
		cancelButton.setOnMouseClicked(e->stage.close());
		confirmButton.setOnMouseClicked(getHandlerOnConformButton());		
		
		passwordFieldsContainer = new Pane();
		passwordFieldsContainer.setLayoutX(20);
		passwordFieldsContainer.setLayoutY(20);
		passwordFieldsContainer.getChildren().addAll(label, oldPassword, newPassword, newPasswordAgain, confirmButton, cancelButton);
		pane.getChildren().add(passwordFieldsContainer);
		pane.setPadding(new Insets(10,10,0,10));
		scene = new Scene(pane);
		stage = new Stage();
		stage.setOnCloseRequest(e->{
			oldPassword.setText(null);
			newPassword.setText(null);
			newPasswordAgain.setText(null);
		});
		stage.setResizable(false);
		stage.setScene(scene);
	}
	
	/** Метод отображения диалогового окна*/
	public void showAndWait() {
		stage.showAndWait();
	}
	
	public boolean isShowing() {
		if(stage.isShowing()) return true;
		return false;
	}
	
	/** Метод, возвращающий обработчик события при нажатии на
	 * кнопку ОК
	 * @return - обработчик при подтверждении изменения пароляъ
	 */
	private EventHandler<MouseEvent> getHandlerOnConformButton() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Ошибка");
		
		EventHandler<MouseEvent> handler = e->{
			if(account.getAcessType() == AcessType.guest) {
				alert.setHeaderText("Недостаточно прав");
				alert.showAndWait();
				return;
			}
			if(!newPassword.getText().equals(newPasswordAgain.getText())){
				alert.setHeaderText("Пароли не совпадают");
				alert.showAndWait();
			} else {
				String oldPass = account.getUserPassword();
				if(account.getAcessType() == AcessType.admin) oldPass = account.getAdminPassword();
				if(oldPass.equals(oldPassword.getText())) {
					if(account.getAcessType() == AcessType.admin) account.setAdminPassword(newPassword.getText());
					else account.setUserPassword(newPassword.getText());
					oldPassword.setText(null);
					newPassword.setText(null);
					newPasswordAgain.setText(null);
					stage.close();
				}else {
					alert.setHeaderText("Неправильно введен пароль");
					alert.showAndWait();
				}			
			}			
		};
		return handler;
	}
}
