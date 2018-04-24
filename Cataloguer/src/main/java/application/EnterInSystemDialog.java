package application;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/** Класс диалогового окна для
 * входа в систему как пользователь или
 * же как администратор 
 * @author Eugene
 */
public class EnterInSystemDialog extends TextInputDialog {
	
	private Pane radioButtonPane;
	private RadioButton asUser, asAdmin;
	private Button confirmPasswordButton;
	private AcessType acessType;
	private Account account;
	
	EnterInSystemDialog(Account account) { 
		this.account = account;
		editInterface();
		setButtonHandler();
	}
	
	/** Изменение интерфейса диалогового окна*/
	private void editInterface() {
		setTitle("Вход в систему");
		getEditor().setPromptText("Пароль");
		getEditor().setFocusTraversable(false);
		
		confirmPasswordButton = new Button("Ok");
		ButtonBar bar = (ButtonBar) getDialogPane().getChildren().get(2);
		bar.getButtons().remove(0);
		bar.getButtons().add(confirmPasswordButton);		
		
		getDialogPane().getChildren().remove(0);
		radioButtonPane = new Pane();
		ToggleGroup group = new ToggleGroup();
		asUser = new RadioButton("Как пользователь");
		asAdmin = new RadioButton("Как администратор");
		asUser.setToggleGroup(group);
		asAdmin.setToggleGroup(group);
		asAdmin.setSelected(true);
		asAdmin.setFocusTraversable(true);
		asAdmin.setLayoutY(20);
		
		radioButtonPane.getChildren().addAll(asUser, asAdmin);
		radioButtonPane.setLayoutX(20);
		radioButtonPane.setLayoutY(10);
		getDialogPane().getChildren().add(radioButtonPane);
	}
	
	/** Создание и установка обработчика событий при
	 * нажатии на кнопку OK
	 * */
	public void setButtonHandler() {
		EventHandler<MouseEvent> handler = e->{
			String enterPassword = getEditor().getText();
			String password = account.getAdminPassword();
			if(asUser.isSelected()) password = account.getUserPassword();
			if(!password.equals(enterPassword)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Ошибка");
				alert.setTitle("Ошибка");
				alert.setContentText("Введен неверный пароль");
				alert.showAndWait();
				return;
			}
			
			if(asUser.isSelected()) account.setAcessType(AcessType.user);
			else account.setAcessType(AcessType.admin);
			close();
		};
		confirmPasswordButton.setOnMouseClicked(handler);
	}
}
