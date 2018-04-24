package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import java.io.File;

/** ������������ �������� ����� ��������
 * ������ ���� �����: ����� � ����
*/
enum CataloguerItemType {file, folder};

/**����� - �������� ��� ������ TreeItem<String>,
 * ���������� ���������� � ���� ���� � ���������� � 
 * ��� ������
 * @author Eugene
 * @version 1.0
 */
public class FileTreeItem extends TreeItem<String> {
	
	/**������ ������ ���������� � ����*/
	private ObservableList<FileWrap> files;
	
	/** ��� ����*/
	private CataloguerItemType type;
    
	/** ���������� �������� ����-�����
	 * @param file - ����, ������� ����� ���������
	 * � ������ ����
	 */
	FileTreeItem(FileWrap file) {
		super(file.getName());
		files = FXCollections.observableArrayList();
		files.add(file);
		type = CataloguerItemType.file;
	}	
	
	/** �������� ����-�����
	 * @param name - ��� ����� �����
	 */
	FileTreeItem(String name){
		super(name);
		files = FXCollections.observableArrayList();
		type = CataloguerItemType.folder;
	}
	
	/** ����� ���������� ������ ����� � �����
	 * @param file - ����������� ����
	 */
	public void add(FileWrap file) {
		files.add(file);
	}
	
	/** �����, ����������� ������� ����� � ������ ���� 
	 * @param file - ����, ������� �������� ���� 
	 * ��������� � ������
	 * @return ������������ �� ���� � ������
	 */
	public boolean fileIsExist(File file) {
		int size = files.size();
		for(int i = 0; i < size; i++)
			if(files.get(i).getAbsolutePath().equals(file.getAbsolutePath()))
				return true;
		return false;
	}
	
	/** �����, ������������ ������ ������ ������� ����
	 * @return ������ ����� ������� ����
	 */
	public ObservableList<FileWrap> getFiles() {
		return files;
	}
	
	/** �����, ������������ ���� ������� ���� */
	public CataloguerItemType getType() {
		return type;
	}
	
	/** �����, ������������ �������� ���� ���� FileTreeItems*/
	public ObservableList<FileTreeItem> getNodes(){
		ObservableList<TreeItem<String>> treeItemList = getChildren();
		ObservableList<FileTreeItem> fileTreeItem = FXCollections.observableArrayList();
		int size = treeItemList.size();
		for(int i = 0; i < size; i++) 
			fileTreeItem.add((FileTreeItem) treeItemList.get(i));		
		return fileTreeItem;
	}

}
