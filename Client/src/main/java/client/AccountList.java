package client;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/** Class-wrap of ArrayList<Account>. It need for us to write and read list of
 * accounts from file using JAXB */
@XmlRootElement(name = "accountList")
@XmlAccessorType(XmlAccessType.FIELD)
public class AccountList implements Serializable {
	
	/** Collection of accouts */
	private ArrayList<Account> accountList;
	
	/** Default constructor */
	public AccountList() {
		accountList = new ArrayList<Account>();
	}
	
	/** Add new account to list
	 * @param account new account */
	public void add(Account account) {
		accountList.add(account);
	}
	
	/** Set new list of accounts
	 * @param accountList new list of accounts */
	public void setList(ArrayList<Account> accountList) {
		this.accountList = accountList;
	}
	
	/** Provide a size of accounts
	 * @return size of list */
	public int size() {
		return accountList.size();
	}
	
	/** Return account with a given number in collection
	 * @param index Number of elements
	 * @return account which have this number in collection */
	public Account get(int index) {
		return accountList.get(index);
	}
	
	/** Return account with a given login
	 * @param login Login of account we want get
	 * @return account with logig */
	public Account get(String login) {
		for(int i = 0; i < accountList.size(); i++)
			if(accountList.get(i).getLogin().equals(login))
				return accountList.get(i);
		return null;
	}
	
	/** Edit access level in concrete account with a given login
	 * @param login login of account in which must be changes
	 * @param right new access level */
	public void edit(String login, int right) {
		for(int i = 0; i < accountList.size(); i++) {
			Account temp = accountList.get(i);
			if(temp.getLogin().equals(login))
				temp.setRight(right);
		}
	}
}
