package client;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/** Account, contains login, password and access level
 * @author Eugene */
@XmlRootElement(name = "account")
@XmlAccessorType(XmlAccessType.FIELD)
public class Account implements Serializable {
	
	/** Accounts login */
    private String login;
    
    /** Accounts password */
	private String password;
	
	/** Accounts access level */
	private int rights;
	
	/** Constructor
	 * @param login Login of new account
	 * @param password Password of new account
	 * @param rights Access level of new account
	 */
	public Account(String login, String password, int rights) {
	   this.login = login;
	   this.password = password;
	   this.rights = rights;
	}
	
	/** Default constructor. Need for us to work with JAXB */
	public Account() {}
		
	/** Return accounts login
	 * @return Accounts login */
	public String getLogin() {
		return login;
	}
	
	/** Return accounts password
	 * @return Accounts password */
	public String getPassword() {
		return password;
	}
	
	/** Return accounts acess level
	 * @return Accounts access level */
	public int getAccessRight() {
		return rights;
	}
	
	/** Set new acess level
	 * @param right New access level */
	public void setRight(int right) {
		this.rights = right;
	}
}
