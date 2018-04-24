package application;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

/**
 * Класс, связывающий между собой GUI
 * и Account 
 * 
 * @author Eugene
 * @version 1.0
 * */

public class Controller {
	
	/** Таблица, в которой будет отображаться 
	 * выбранная папка или результат поиска
	 * */
	@FXML public TableView<FileWrap> tableView;
	
	/** Столбцы таблицы */
	@FXML private TableColumn<FileWrap, String> nameColumn, sizeColumn, typeColumn, dateOfModifyingColumn;
	
	/** Древовидная структура, в которой хранится
	 * каталог файлов
	 * */
	@FXML public TreeView<String> treeView;
	
	@FXML private Button addFileButton, addFolderButton, removeFileButton, exitProgramButton;
	@FXML private Button enterInSystemButton, editPasswordButton;
	
	/** Поле поиска файлов */
	@FXML private TextField searchField;
    
	/** Логическое ядро программы*/
	private Account account;
	
	/** Устанавливает обработчики событий 
	 * на все кнопки
	 */
	private void setButtonHandlers() {
		addFileButton.setOnMouseClicked(e->account.addFile());
		addFolderButton.setOnMouseClicked(e->account.addFolder());
		removeFileButton.setOnMouseClicked(e->account.removeElement());
		exitProgramButton.setOnMouseClicked(e->account.exitProgram());
		editPasswordButton.setOnMouseClicked(e->account.editPassword());
		enterInSystemButton.setOnMouseClicked(e->account.enterInSystem());
		
		addFileButton.setDisable(true);
		removeFileButton.setDisable(true);
		addFolderButton.setDisable(true);
		editPasswordButton.setDisable(true);
	}
		
	/** Настраивает параметры считывания
	 * данных из объектов типа FileWrap
	 * */
	private void tuneTableView() {
		nameColumn.setCellValueFactory(new PropertyValueFactory<FileWrap, String>("name"));
		sizeColumn.setCellValueFactory(new PropertyValueFactory<FileWrap, String>("size"));
		typeColumn.setCellValueFactory(new PropertyValueFactory<FileWrap, String>("type"));
		dateOfModifyingColumn.setCellValueFactory(new PropertyValueFactory<FileWrap, String>("dateOfModifying"));
		tableView.setOnMouseClicked(e->{
			if(e.getClickCount() != 2) return;
			FileWrap file = tableView.getSelectionModel().getSelectedItem();
			if(file == null) return;
			account.openFile(file);
		});
	}
	
	/** Метод, вызывающийся при запросе поиска*/
	private void onSearch() {
		searchField.setOnKeyPressed(e->{
			if(e.getCode() != KeyCode.ENTER) return;
			account.search(searchField.getText());
		});
	}
	
	@FXML
	public void initialize() {
		account = new Account(this);
		tuneTableView();
		setButtonHandlers();
		onSearch();
	}
	
	/** Установка считанной из базы данных вершины каталога
	 * в TreeView */
	public void setRoot(FileTreeItem root) {
		treeView.setRoot(root);
	}
		
	/** Установка в таблицу результатов поиска*/
	public void setTableContent(ObservableList<FileWrap> files) {
		tableView.setItems(files);
	}
	
	/** Разблокировка кнопок для работы с файлами
	 * при входе как администратор или пользователь
	 */
	public void unblockButtons() {
		addFileButton.setDisable(false);
		removeFileButton.setDisable(false);
		addFolderButton.setDisable(false);
		editPasswordButton.setDisable(false);
	}
}
