package client;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/** Person */
@XmlRootElement(name = "person")
@XmlAccessorType(XmlAccessType.FIELD)
public class Person implements Serializable {
	
	private String name, nameInLowerCase;
	private String post;
	private String education;
	private String characteristic;	
	private String birthday;
	
	private static int staticIndex;
	private int index;
	
	/** Constructor 
	 * @param name persons name
	 * @param post persons post
	 * @param education persons education 
	 * @param characteristic persons characteristic
	 * @param birthday persons birthday */
	public Person(String name, String post, String education, String characteristic, String birthday) {
		this.name = name;
		this.post = post;
		this.education = education;
		this.characteristic = characteristic;
		this.birthday = birthday;
		
		nameInLowerCase = name.toLowerCase();
		index = staticIndex;
		staticIndex++;
	}
	
	/** Default constructor. Need for us to work with JAXB */
	public Person() {}
	
	/** Set person new name
	 * @param name new Person name */
	public void setName(String name) {
		this.name = name;
	}
	
	/** Set person new post
	 * @param post new persons post*/
	public void setPost(String post) {
		this.post = post;
	}
	
	/** Set a string with educational institutions
	 * @param education string with educational institutions */
	public void setEducation(String education) {
		this.education = education;
	}
	
	/** Set new person characteristic
	 * @param characteristic new person characteristic*/
	public void setCharacteristic(String characteristic) {
		this.characteristic = characteristic;
	}
	
	/** Set new birthday 
	 * @param birthday new persons birthday */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	/** Return persons name
	 * @return persons name */
	public String getName() {
		return name;
	}
	
	/** Return persons post
	 * @return persons post */
	public String getPost() {
		return post;
	}
	
	/** Return string with contains educational institutions
	 * @return string which contains educational institutions */
	public String getEduation() {
		return education;
	}
	
	/** Return string with persons characteristic
	 * @return string with persons characteristic */
	public String getCharacteristic() {
		return characteristic;
	}
	
	/** Return string with persons birthday
	 * @return string with persons birthday */
	public String getBirthday() {
		return birthday;
	}
	
	/** Set this person new index
	 * @param index new index */
	public void setIndex(int index) {
		this.index = index;
	}
	
	/** Return an index of object
	 * @return index of object */
	public int getIndex() {
		return index;
	}
	
	/** Return persons name in lower case. It use in a search.
	 * @return persons name in lower case */
	public String getNameInLowerCase() {
		if(nameInLowerCase == null)
			nameInLowerCase = name.toLowerCase();
		return nameInLowerCase;
	}
	
	/** Set new static index. It use after reading list of person from file
	 * using JAXB
	 * @param new static index */
	public static void setStaticIndex(int index) {
		staticIndex = index;
	}
}
