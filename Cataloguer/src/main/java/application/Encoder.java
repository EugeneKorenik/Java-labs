package application;

import java.util.Random;

public class Encoder {
		
	/** ����� ���������� ������
	 * @param password - ������, ������� ����������
	 * �����������
	 * @return �������������� ������
	 * */
	public static String encode(String password) {
		char[] arr = password.toCharArray();
		Random random = new Random();
		int sub = (int) Math.abs(random.nextInt()) % 10;
		for(int i = 0; i < arr.length; i++)
			arr[i] -= sub;
		String str = Character.toString((char)sub);
		return str + new String(arr);
	}
	
	/** ����� ����������� ������
	 * 
	 * @param encoded �������������� ������, ��������� 
	 * �� ���� ������
	 * @return �������������� ������
	 */
	public static String decode(String encoded) {
		char[] arr = encoded.substring(1).toCharArray();
		int add =  (int) encoded.charAt(0);
		for(int i = 0; i < arr.length; i++)
			arr[i] += add;
		String decoded = new String(arr);
		return decoded;
	}	
}

