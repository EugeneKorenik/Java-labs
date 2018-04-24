package application;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Класс-оболочка для класса File, предоставляет 
 * возможность таблице получить информации о конкретном файле
 * @author Eugene
 * @version 1.0
 */
public class FileWrap extends File {
	
	private String path;
	private String size;
	private String dateOfModifying;
	private String type;
	private String nameIgnoredCases;
	
	/** Конструктор создания обертки-файла по его пути
	 * @param - абсолютный путь файла
	 */
	FileWrap(String path) {
		super(path);
		this.path = path;
		findValues();
	}
	
	/** Конструктор создания обертки-файла на основе
	 * уже существуюшего файла
	 * @param file - файл, который нужно обернуть
	 */
	FileWrap(File file) {
		super(file.getAbsolutePath());
		path = file.getAbsolutePath();
		findValues();
	}
	
	/** Метод получения параметров файла и перевод их
	 * в формат String
	 */
	private void findValues() {
		size = Integer.toString((int)length() / 1024) + " кб";	
		dateOfModifying = new SimpleDateFormat().format(lastModified());
		nameIgnoredCases = getName().toLowerCase();
		String[] arr = path.split("\\.");
		if(arr.length > 1) type = arr[arr.length - 1];
	}
	
	/** Метод получения строки, содержащей размер файла
	 * @return размер файла в килобайтах
	 */
	public String getSize() {
		return size;
	}
	
	/** Метод получения строки, содержащей дату последнего
	 * изменения фала
	 * @return
	 */
	public String getDateOfModifying() {
		return dateOfModifying;
	}
	
	/** Метод возвращающий строк, храняющую формат файла
	 * @return - формат файла
	 */
	public String getType() {
		return type;
	}
	
	/** Метод, возвращающий имя файла в нижнем регистре */
	public String getNameIgnoreCase() {
		return nameIgnoredCases;
	}
}
