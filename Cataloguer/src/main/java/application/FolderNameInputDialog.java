package application;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

/** 
 * ����� ����������� ���� ��� �����
 * ����� ����������� ����� 
 * @author Eugene
 */
public class FolderNameInputDialog extends TextInputDialog {
	
	private Alert folderIsExistAlert;
	private Button confirmInputButton;
	private FileTreeItem selectedItem;
	private String newFolderName;
	
	/** 
	 * �����������, � ������� ���������� ����������� �������������
	 * ����������
	 * @param selectedItem - ����, � ������� ����� �������� �����
	 */
	FolderNameInputDialog(FileTreeItem selectedItem) {
		this.selectedItem = selectedItem;
		
		setHeaderText("���� ����� �����");
		setTitle("���������� �����");
		ButtonBar bar = (ButtonBar) getDialogPane().getChildren().get(2);
		bar.getButtons().remove(0);
 
		Button confirmButton = new Button("Confirm");
		
		confirmButton.setOnMouseClicked(e->{
			newFolderName = getEditor().getText();
		    if(newFolderName.replaceAll(" ", "").equals("")) return;
			
			if(folderIsExist()) {
				folderIsExistAlert = new Alert(AlertType.ERROR);
				folderIsExistAlert.setHeaderText("����� � ����� ������ ��� ����������");
				folderIsExistAlert.showAndWait();
				return;
			}
			selectedItem.getChildren().add(new FileTreeItem(newFolderName));
			close();						
		});
		
		bar.getButtons().add(confirmButton);		
	}
	
	/** �����, ����������� ������������� ����� � ��������� 
	 * ������ � ������ ����
	*/
	private boolean folderIsExist() {
		ObservableList<FileTreeItem> list = selectedItem.getNodes();
		int size = list.size();
		for(int i = 0; i < size; i++) {
			FileTreeItem tempItem = list.get(i);
			if(tempItem.getType() == CataloguerItemType.folder && newFolderName.equals(tempItem.getValue()))
				return true;
		}
		return false;
	}
}
