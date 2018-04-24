package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.collections.ObservableList;

/** �����, � ������� ���������� ������ � ���������
 *  ��������, � ��� �� ���� ���������������� ���������� � ���� ������
 *  � �������
 *  @author Eugene
 *  @version 1.0
 * */

public class DatabaseHandler {
	
	/** ������, ������������ ��� ���������� ����� */
	private String line; 
	/** ������ ������������, ��������� ��� ���������� �����*/
	private String userPassword;
	/** ������ ��������������, ���������� ��� ���������� �����*/
	private String adminPassword;
	/** �����, � ������� ���������� �������� ��� �� ������� ��� ����������*/
	private Account account;
	/** ����� �� ���������� ������, ������� ��� ������ � �����*/
	private double limit;
	
	DatabaseHandler(Account account) {
		this.account = account;
	}
	
	/** �����, ��������� ����������� ��������� �������� � ������ �������������
	 *  ������ ��� ���������� �����*/
	private FileTreeItem createStandartCatalog() {
		FileTreeItem root = new FileTreeItem("���");	
		FileTreeItem treeMusicItem = new FileTreeItem("������");
		FileTreeItem treeVideoItem = new FileTreeItem("�����");
		FileTreeItem treeImageItem = new FileTreeItem("�����������");
		FileTreeItem treeBooksItem = new FileTreeItem("�����");
		FileTreeItem treeDocItem = new FileTreeItem("���������");		
		root.getChildren().addAll(treeMusicItem, treeVideoItem, treeImageItem, treeBooksItem, treeDocItem);
		return root;
	}
	
	/** ����������� ����� ���������� � ���������� ����������� ��������� ��������
	 * @param parent - ���� � �������� �� ������ ����� ���� ����������� ������
	 * @param reader - ����������� ���������� �� �����
	 * */
	private void read(FileTreeItem parent, BufferedReader reader) throws IOException {
		while((line = reader.readLine()) != null) {
			if(line.equals("end")) return;
			else if(line.equals("Files")) {
				while(!(line = reader.readLine()).equals("end")) {
					FileWrap file = new FileWrap(line);
					FileTreeItem item = new FileTreeItem(file);
					parent.getChildren().add(item);
					parent.add(file);
				}
				return;
			} else if(line.equals("Folder")) {
				FileTreeItem item = new FileTreeItem(reader.readLine());
				parent.getChildren().add(item);
				read(item, reader);
			}
		}		
	}
	
	/** �����, ���������� ���� ���������� ������ ������������.
	 * ���� ��� ���� ��� ������� �����, �� � ����� ��������������� 10
	 * @param date - ���� ���������� ������
	 * */
	private void defineLimit(Date date) {
		double limitAtDay = 10;
		Date currentDate = new Date();
		if(currentDate.getYear() != date.getYear() || currentDate.getMonth() != date.getMonth() 
				|| currentDate.getDay() != date.getDay()) 
			limit = limitAtDay;		
	}
	
	/** �����, ������������ ����� �� ���������� ������*/
	public double getLimit() {
		return limit;
	}
	
	/** �����, � ������� ��������� ���������� ���� ���������� �� �����*/
	public FileTreeItem parseInformation() {
		File file = new File("database.txt");
		if(!file.exists()) return createStandartCatalog();
		FileTreeItem root = null;
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			line = reader.readLine();
			if(!line.equals("Limit")) throw new IOException();
			limit = Double.parseDouble(reader.readLine());
			Date date = new SimpleDateFormat().parse(reader.readLine());
			defineLimit(date);
			
			line = reader.readLine();
			String temp;
			if(!line.equals("Password")) throw new IOException();
			
			adminPassword = reader.readLine();			
			temp = reader.readLine();
			while(!temp.equals("Password")) {
				adminPassword += temp;
				temp = reader.readLine();
			}
		
			userPassword = reader.readLine();
			temp = reader.readLine();			
			
			while(!temp.equals("Folder")) {
				userPassword += temp;
				temp = reader.readLine();
			}
				
			adminPassword = Encoder.decode(adminPassword);
			userPassword = Encoder.decode(userPassword);
			
			if(!temp.equals("Folder")) throw new IOException();
			root = new FileTreeItem(reader.readLine());
			read(root, reader);
				
		} catch(IOException e) {
			e.printStackTrace();
			return createStandartCatalog();
		} catch(ParseException e) {
			e.printStackTrace();
			return createStandartCatalog();
		}
		
		return root;		
	}
	
	/** �����, ������������ ��������� ����� ������ ������������*/
	public String getUserPassword() {
		return userPassword;
	}
	
	/** �����, ������������ ��������� ����� ������ ��������������*/
	public String getAdminPassword() {
		return adminPassword;
	}
    
	/** ����������� ����� ������ ����������� ��������� �������� � ����
	 * @param catalog - ����, ���������� � ������� ������������ �� ������ �����
	 * @param writer - �������� ������ ���������� � ����
	 * */
	private void write(FileTreeItem catalog, BufferedWriter writer) throws IOException {
		if(catalog == null) return;
		if(catalog.getType() == CataloguerItemType.file) return;
		writer.write("Folder\n" + catalog.getValue() + "\n");
				
		ObservableList<FileTreeItem> nodes = catalog.getNodes();
		int size = nodes.size();
		
		for(int i = 0; i < size; i++) {
			FileTreeItem node = nodes.get(i);
			write(node, writer);
		}
		
		ObservableList<FileWrap> files = catalog.getFiles();
		int filesQuantity = files.size();
		
		writer.write("Files\n");
		for(int j = 0; j < filesQuantity; j++) 
			writer.write(files.get(j).getAbsolutePath() + "\n");
		writer.write("end\n");
	}
	
	/** ����� ������ ���� ���������� �������� � ����*/
	public void saveInformation() {
		File database = new File("database.txt");
		if(!database.exists()) {
			try {
				database.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	  
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(database))) {
			writer.write("Limit\n" + account.getLimit() + "\n" + new SimpleDateFormat().format(new Date()) + "\n");
			writer.write("Password\n" + Encoder.encode(account.getAdminPassword()) + "\n");
			writer.write("Password\n" + Encoder.encode(account.getUserPassword()) + "\n");
			write(account.getTreeRoot(), writer);
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}
}
