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

/** ������������, ������������ ������� ������� � ����������*/
enum AcessType {guest, user, admin}

/** �������� � ���� ��� ������ ����������
 * @author Eugene
 * @version 1.0
 */

public class Account {
	/** ������ ��� ����� ��� ������������ � ��� �������������*/
	private String userPassword, adminPassword;
	
	/** ������, � ������� �������� ��� �����*/
  	private TreeView<String> cataloguerTree;
  	
  	/** �������, ������� ���������� ����� � �������� ����� ��� �� ��������� ������*/
  	private TableView<FileWrap> tableView;
  	
  	/** ������� ������� �������. ������ ��� ����: �������������, ������������, �����*/
   	private AcessType acessType;
   	
   	/** ����� �� ���������� ����� ��� ������ ������� ������������*/
   	private double limit;
   	
   	/** ���������� � ������� ��� ����� ������ � ������� � �������*/
   	private Controller controller;
  	
   	/**
   	 * @param contoller ���������� � ������� ��� ����� ������ � ������� � �������
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
	
	/** ����� ���������� ������ ����� � �������*/
	public void addFile() {
		FileTreeItem selectedItem = (FileTreeItem) cataloguerTree.getSelectionModel().getSelectedItem();
		if(selectedItem == null || selectedItem.getType() == CataloguerItemType.file) return;
		FileChooser chooser = new FileChooser();
		File selectedFile = chooser.showOpenDialog(Frame.primaryStage);
		if(selectedFile == null) return;
		FileWrap newFile = new FileWrap(selectedFile);
		if(acessType == AcessType.user && newFile.length() /(1024 * 1024) > limit) {
			Alert limitExceed = new Alert(AlertType.ERROR);
			limitExceed.setTitle("������");
			limitExceed.setHeaderText("�������� ����� ����� ���������� � �����");
			limitExceed.showAndWait();
			return;
		}
		if(selectedItem.fileIsExist(newFile)) {
			Alert fileIsExistAlert = new Alert(AlertType.ERROR);
			fileIsExistAlert.setTitle("������");
			fileIsExistAlert.setHeaderText("����� ���� ��� ���� � ������ �����");
			fileIsExistAlert.show();
			return;
		}
		selectedItem.add(newFile);
		selectedItem.getChildren().add(new FileTreeItem(newFile));
		if(acessType == AcessType.user)
			limit -= (double) newFile.length() / (1024*1024);
		System.out.println(limit);
	}
	
	/** ����� ���������� ����� ����� � �������*/
	public void addFolder() {
		FileTreeItem selectedItem = (FileTreeItem) cataloguerTree.getSelectionModel().getSelectedItem();
		if(selectedItem == null || selectedItem.getType() == CataloguerItemType.file) return;
		FolderNameInputDialog folderNameInputDialog = new FolderNameInputDialog(selectedItem);
		folderNameInputDialog.showAndWait();		
	}
	
	
	/** ����� �������� �� �������� ����� ��� �����*/
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
	
	/** ����� ��������� ������� ��� ��������*/
	public void exitProgram() {
		DatabaseHandler handler = new DatabaseHandler(this);
		handler.saveInformation();
		Frame.primaryStage.close();
		System.exit(0);
	}
	
	/** ����� ��������� ������*/
	public void editPassword() {
		EditPasswordDialog editPasswordDialog = new EditPasswordDialog(this);
		editPasswordDialog.showAndWait();
	}
	
	/** ���� � ������� ��� ������������ ��� ��� �������������*/
	public void enterInSystem() {
		EnterInSystemDialog enterInSystemDialog = new EnterInSystemDialog(this);
		enterInSystemDialog.showAndWait();
	}
	
	/** 
	 * �����, ������������ ��� ��������� � ���� �����, ���������� � ����� �������� ������
	 * @param list - ���������, � ������� ������������ ����� 
	 * @param searchRequest - ������
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
	 * ����� ��������������� � ������� ������ ������, ��������� �� ��������� �������
	 * @param searchRequest - ������
	 */
	public void search(String searchRequest) {
		searchRequest = searchRequest.toLowerCase();
		ObservableList<FileWrap> files = FXCollections.observableArrayList();
		getFileList((FileTreeItem) cataloguerTree.getRoot(), files, searchRequest);
		tableView.setItems(files);
	}
	
	/** 
	 * ��������� ��������� ����
	 * @param - ����, ��������� � ������� ��� TreeView
	 * */
	public void openFile(FileWrap file) {
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.open(file);
		} catch (IOException e) {
			System.out.println("�������� ���� �������� �������������");
		} /*catch(IllegalAccessException e) {
			System.out.println("���� ������ �� ������� �������");			
		}*/
	}

	/**
	 * ������������� ����������� �� ���������� ������ � ������ ������������
	 * @param limit - ��������������� �������� ������� ���������� ������
	 */	
	public void setLimit(double limit) {
		this.limit = limit;
	}
	
	/**���������� �������� �������� ����������� �� ���������� ������ 
	 * @return �������� ������� �� ����������
	 */	
	public double getLimit() {
		return limit;
	}
	
	/**���������� ��� ������ ������� � ������
	 * @return �������� ������ �������
	 */
	public AcessType getAcessType() {
		return acessType;
	}
	
	/**�������������� �������� ������ ������� � ������
	 * @param acessType ������� �������
	 */
	public void setAcessType(AcessType acessType) {
		this.acessType = acessType;
		if(acessType != AcessType.guest) controller.unblockButtons();
	}
	
	/**���������� �������� ������ ��������������
	 * @return ������ ��������������
	 */
	public String getAdminPassword() {
		return adminPassword;
	}
	
	/**������������� �������� ������ �������������� 
	 * @param adminPassword �������� ���������������� ������ ��������������
	 */	
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}
	
	/**���������� �������� ������ ������������
	 * @return ������ ������������
	 */
	public String getUserPassword() {
		return userPassword;
	}
	
	/**������������� �������� ������ ������������ 
	 * @param userPassword �������� ���������������� ������ ������������
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
	
	/**��������� ������� ������ �������� 
	 * @return ������� ��������
	 */
	public FileTreeItem getTreeRoot() {
		return (FileTreeItem) cataloguerTree.getRoot();
	}	
}

