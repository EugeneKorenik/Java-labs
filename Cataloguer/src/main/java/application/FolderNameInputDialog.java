package application;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;

/** 
 * Класс диалоговога окна для ввода
 * имени добавляемой папки 
 * @author Eugene
 */
public class FolderNameInputDialog extends TextInputDialog {
	
	private Alert folderIsExistAlert;
	private Button confirmInputButton;
	private FileTreeItem selectedItem;
	private String newFolderName;
	
	/** 
	 * Конструктор, в котором происходит перестройка графичсеского
	 * интерфейса
	 * @param selectedItem - узел, в который нужно добавить папку
	 */
	FolderNameInputDialog(FileTreeItem selectedItem) {
		this.selectedItem = selectedItem;
		
		setHeaderText("Ввод имени папки");
		setTitle("Добваление папки");
		ButtonBar bar = (ButtonBar) getDialogPane().getChildren().get(2);
		bar.getButtons().remove(0);
 
		Button confirmButton = new Button("Confirm");
		
		confirmButton.setOnMouseClicked(e->{
			newFolderName = getEditor().getText();
		    if(newFolderName.replaceAll(" ", "").equals("")) return;
			
			if(folderIsExist()) {
				folderIsExistAlert = new Alert(AlertType.ERROR);
				folderIsExistAlert.setHeaderText("Папка с таким именем уже существует");
				folderIsExistAlert.showAndWait();
				return;
			}
			selectedItem.getChildren().add(new FileTreeItem(newFolderName));
			close();						
		});
		
		bar.getButtons().add(confirmButton);		
	}
	
	/** Метод, проверяющий существование папки с введенным 
	 * именем в данном узле
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
