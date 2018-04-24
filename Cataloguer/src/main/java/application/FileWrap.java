package application;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * �����-�������� ��� ������ File, ������������� 
 * ����������� ������� �������� ���������� � ���������� �����
 * @author Eugene
 * @version 1.0
 */
public class FileWrap extends File {
	
	private String path;
	private String size;
	private String dateOfModifying;
	private String type;
	private String nameIgnoredCases;
	
	/** ����������� �������� �������-����� �� ��� ����
	 * @param - ���������� ���� �����
	 */
	FileWrap(String path) {
		super(path);
		this.path = path;
		findValues();
	}
	
	/** ����������� �������� �������-����� �� ������
	 * ��� ������������� �����
	 * @param file - ����, ������� ����� ��������
	 */
	FileWrap(File file) {
		super(file.getAbsolutePath());
		path = file.getAbsolutePath();
		findValues();
	}
	
	/** ����� ��������� ���������� ����� � ������� ��
	 * � ������ String
	 */
	private void findValues() {
		size = Integer.toString((int)length() / 1024) + " ��";	
		dateOfModifying = new SimpleDateFormat().format(lastModified());
		nameIgnoredCases = getName().toLowerCase();
		String[] arr = path.split("\\.");
		if(arr.length > 1) type = arr[arr.length - 1];
	}
	
	/** ����� ��������� ������, ���������� ������ �����
	 * @return ������ ����� � ����������
	 */
	public String getSize() {
		return size;
	}
	
	/** ����� ��������� ������, ���������� ���� ����������
	 * ��������� ����
	 * @return
	 */
	public String getDateOfModifying() {
		return dateOfModifying;
	}
	
	/** ����� ������������ �����, ��������� ������ �����
	 * @return - ������ �����
	 */
	public String getType() {
		return type;
	}
	
	/** �����, ������������ ��� ����� � ������ �������� */
	public String getNameIgnoreCase() {
		return nameIgnoredCases;
	}
}
