package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import java.io.File;

/** Перечисление форматов узлов каталога
 * бывает двух видов: папка и файл
*/
enum CataloguerItemType {file, folder};

/**Класс - оболочка для класса TreeItem<String>,
 * содержащий информацию о типе узла и хранящихся в 
 * нем файлах
 * @author Eugene
 * @version 1.0
 */
public class FileTreeItem extends TreeItem<String> {
	
	/**Список файлов хранящихся в узле*/
	private ObservableList<FileWrap> files;
	
	/** Тип узла*/
	private CataloguerItemType type;
    
	/** Констуктор создания узла-файла
	 * @param file - файл, который будет храниться
	 * в данном узле
	 */
	FileTreeItem(FileWrap file) {
		super(file.getName());
		files = FXCollections.observableArrayList();
		files.add(file);
		type = CataloguerItemType.file;
	}	
	
	/** Создание узла-папки
	 * @param name - имя новой папки
	 */
	FileTreeItem(String name){
		super(name);
		files = FXCollections.observableArrayList();
		type = CataloguerItemType.folder;
	}
	
	/** Метод добавления нового файла в папку
	 * @param file - добавляемый файл
	 */
	public void add(FileWrap file) {
		files.add(file);
	}
	
	/** Метод, проверяющий наличие файла в данном узел 
	 * @param file - файл, наличие которого надо 
	 * проверить в списке
	 * @return Присутствует ли файл в списке
	 */
	public boolean fileIsExist(File file) {
		int size = files.size();
		for(int i = 0; i < size; i++)
			if(files.get(i).getAbsolutePath().equals(file.getAbsolutePath()))
				return true;
		return false;
	}
	
	/** Метод, возвращающий списко файлов данного узла
	 * @return Список фалов данного узла
	 */
	public ObservableList<FileWrap> getFiles() {
		return files;
	}
	
	/** Метод, возвращающий типа данного узла */
	public CataloguerItemType getType() {
		return type;
	}
	
	/** Метод, возвращающий дочерние узоы типа FileTreeItems*/
	public ObservableList<FileTreeItem> getNodes(){
		ObservableList<TreeItem<String>> treeItemList = getChildren();
		ObservableList<FileTreeItem> fileTreeItem = FXCollections.observableArrayList();
		int size = treeItemList.size();
		for(int i = 0; i < size; i++) 
			fileTreeItem.add((FileTreeItem) treeItemList.get(i));		
		return fileTreeItem;
	}

}
