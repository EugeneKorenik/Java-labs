package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;

/** Перечисление, определяющие уровень доступа к приложения*/
enum AcessType {guest, user, admin}

/** Содержит в себе всю логику приложения
 * @author Eugene
 * @version 1.0
 */

public class Account {
	/** Пароли для входа как пользователь и как администратор*/
	private String userPassword, adminPassword;
	
	/** Дерево, в котором хранятся все файлы*/
  	private TreeView<String> cataloguerTree;
  	
  	/** Таблица, которая отображает файлы в выбранно папке или же результат поиска*/
  	private TableView<FileWrap> tableView;
  	
  	/** Текущий уровень доступа. Бывает три вида: администратор, пользователь, гость*/
   	private AcessType acessType;
   	
   	/** Лимит на добавление фалов при уровне доступа пользователя*/
   	private double limit;
   	
   	/** Контроллер в котором нам нужен доступ к кнопкам и таблице*/
   	private Controller controller;
  	
   	/**
   	 * @param contoller Контроллер в котором нам нужен доступ к кнопкам и таблице
   	 */
	Account(Controller controller) {
		Timer timer = new Timer(this);
		this.controller = controller;
		this.cataloguerTree = controller.treeView;
		this.tableView = controller.tableView;
		cataloguerTree.setOnMouseClicked(e->{
			FileTreeItem selectedItem = (FileTreeItem) cataloguerTree.getSelectionModel().getSelectedItem();
			if(selectedItem == null) return;
			if(e.getClickCount() == 2 && selectedItem.getType() == CataloguerItemType.file) 
				openFile(selectedItem.getFiles().get(0));
			else {
				if(selectedItem.getType() == CataloguerItemType.file) 
					selectedItem = (FileTreeItem) selectedItem.getParent();
				tableView.setItems(selectedItem.getFiles());
			}
		});
		
		DatabaseHandler handler = new DatabaseHandler(this);
		cataloguerTree.setRoot(handler.parseInformation());
		userPassword = handler.getUserPassword();
		adminPassword = handler.getAdminPassword();
		System.out.println(adminPassword + " " + userPassword); 
		limit = handler.getLimit();
	}
	
	/** Метод добавление нового файла в каталог*/
	public void addFile() {
		FileTreeItem selectedItem = (FileTreeItem) cataloguerTree.getSelectionModel().getSelectedItem();
		if(selectedItem == null || selectedItem.getType() == CataloguerItemType.file) return;
		FileChooser chooser = new FileChooser();
		File selectedFile = chooser.showOpenDialog(Frame.primaryStage);
		if(selectedFile == null) return;
		FileWrap newFile = new FileWrap(selectedFile);
		if(acessType == AcessType.user && newFile.length() /(1024 * 1024) > limit) {
			Alert limitExceed = new Alert(AlertType.ERROR);
			limitExceed.setTitle("Ошибка");
			limitExceed.setHeaderText("Превышен лимит лимит добавления в сутки");
			limitExceed.showAndWait();
			return;
		}
		if(selectedItem.fileIsExist(newFile)) {
			Alert fileIsExistAlert = new Alert(AlertType.ERROR);
			fileIsExistAlert.setTitle("Ошибка");
			fileIsExistAlert.setHeaderText("Такой файл уже есть в данной папке");
			fileIsExistAlert.show();
			return;
		}
		selectedItem.add(newFile);
		selectedItem.getChildren().add(new FileTreeItem(newFile));
		if(acessType == AcessType.user)
			limit -= (double) newFile.length() / (1024*1024);
		System.out.println(limit);
	}
	
	/** Метод добавления новой папки в каталог*/
	public void addFolder() {
		FileTreeItem selectedItem = (FileTreeItem) cataloguerTree.getSelectionModel().getSelectedItem();
		if(selectedItem == null || selectedItem.getType() == CataloguerItemType.file) return;
		FolderNameInputDialog folderNameInputDialog = new FolderNameInputDialog(selectedItem);
		folderNameInputDialog.showAndWait();		
	}
	
	
	/** Метод удаления из каталога папки или файла*/
	public void removeElement() {
		FileTreeItem selectedItem = (FileTreeItem) cataloguerTree.getSelectionModel().getSelectedItem();
		if(selectedItem == cataloguerTree.getRoot()) return;
		FileTreeItem parent = (FileTreeItem) selectedItem.getParent();
		if(selectedItem.getType() == CataloguerItemType.file) 
			parent.getFiles().remove(selectedItem.getFiles().get(0));
		else
			selectedItem.getFiles().clear();
		parent.getChildren().remove(selectedItem);
	}
	
	/** Метод обработка событий при закрытии*/
	public void exitProgram() {
		DatabaseHandler handler = new DatabaseHandler(this);
		handler.saveInformation();
		Frame.primaryStage.close();
		System.exit(0);
	}
	
	/** Метод изменения пароля*/
	public void editPassword() {
		EditPasswordDialog editPasswordDialog = new EditPasswordDialog(this);
		editPasswordDialog.showAndWait();
	}
	
	/** Вход в систему как пользователь или как администратор*/
	public void enterInSystem() {
		EnterInSystemDialog enterInSystemDialog = new EnterInSystemDialog(this);
		enterInSystemDialog.showAndWait();
	}
	
	/** 
	 * Метод, возвращающий все имеющиеся в узле файлы, содержащие в имени заданную строку
	 * @param list - коллекция, в которую записываются файлы 
	 * @param searchRequest - запрос
	 */
	private void getFileList(FileTreeItem parent, ObservableList<FileWrap> list, String searchRequest) {
		if(parent.getType() == CataloguerItemType.file || parent.isLeaf()) return;
		ObservableList<FileTreeItem> childrenList = parent.getNodes();
		int size = childrenList.size();
		
		ObservableList<FileWrap> files = parent.getFiles();
	
		int filesQuantity = files.size();
		for(int i = 0; i < filesQuantity; i++) {
			FileWrap file = files.get(i);
			if(file.getNameIgnoreCase().contains(searchRequest)) 
				if(!list.contains(file))
					list.add(file);
		}
		
		for(int i = 0; i < size; i++) {
			FileTreeItem node = childrenList.get(i);
			getFileList(node, list, searchRequest);
		}				
	}
	
	/**
	 * Метод устанавливающий в таблицу список файлов, найденных по заданному запросу
	 * @param searchRequest - запрос
	 */
	public void search(String searchRequest) {
		searchRequest = searchRequest.toLowerCase();
		ObservableList<FileWrap> files = FXCollections.observableArrayList();
		getFileList((FileTreeItem) cataloguerTree.getRoot(), files, searchRequest);
		tableView.setItems(files);
	}
	
	/** 
	 * Открывает выбранный файл
	 * @param - файл, выбранный в таблице или TreeView
	 * */
	public void openFile(FileWrap file) {
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(file);
		} catch (IOException e) {
			System.out.println("Операция была отменена пользователем");
		} /*catch(IllegalAccessException e) {
			System.out.println("Файл удален из фаловой системы");			
		}*/
	}

	/**
	 * Устанавливает ограничение на добавление файлов в режиме пользователя
	 * @param limit - устанавливаемое значение предела добавления файлов
	 */	
	public void setLimit(double limit) {
		this.limit = limit;
	}
	
	/**Возвращает значение текущего ограничения на добавление файлов 
	 * @return Значение предела на добавление
	 */	
	public double getLimit() {
		return limit;
	}
	
	/**Возвращает тип уровня доступа к файлам
	 * @return Значение уровня доступа
	 */
	public AcessType getAcessType() {
		return acessType;
	}
	
	/**Устанавлиевает значение уровня доступа к файлам
	 * @param acessType Уровень доступа
	 */
	public void setAcessType(AcessType acessType) {
		this.acessType = acessType;
		if(acessType != AcessType.guest) controller.unblockButtons();
	}
	
	/**Возвращает значение пароля администратора
	 * @return Пароль администратора
	 */
	public String getAdminPassword() {
		return adminPassword;
	}
	
	/**Устанавливает значение пароля администратора 
	 * @param adminPassword Значение устанавливаемого пароля администратора
	 */	
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	
	/**Возвращает значение пароля пользователя
	 * @return Пароль пользователя
	 */
	public String getUserPassword() {
		return userPassword;
	}
	
	/**Устанавливает значение пароля пользователя 
	 * @param userPassword Значение устанавливаемого пароля пользователя
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	/**Возарщает вершину нашего каталога 
	 * @return Вершина каталога
	 */
	public FileTreeItem getTreeRoot() {
		return (FileTreeItem) cataloguerTree.getRoot();
	}	
}

