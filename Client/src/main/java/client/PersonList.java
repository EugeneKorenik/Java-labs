package client;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/** Class-wrap of ArrayList<Account>. It need for us to write and read list of
 * persons from file using JAXB */
@XmlRootElement(name = "personList")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonList implements Serializable {
	
	private ArrayList<Person> personList;
	
	/** Default constructor */
	public PersonList() {
		personList = new ArrayList<Person>();
	}
	
	/** Add new person to acrhive
	 * @param person new person */
	public void add(Person person) {
		personList.add(person);		
	}
	
	/** Remove person from list with a given person index 
	 * @param index of a person (not number in a list)
	 * @return the name of removed person */
	 public String remove(int index) {
	 String name = null;
	 int i = 0;
	 for( ; i < personList.size(); i++)
		if(personList.get(i).getIndex() == index) {
				name = personList.get(i).getName();
				personList.remove(i);
				
				for( ; i< personList.size(); i++)
					personList.get(i).setIndex(i + 1);
			}
		Person.setStaticIndex(i + 1);
		return name;
	}
	
	/** Update concrete object in a list
	 * @param new version of object */
	public void edit(Person person) {
		int index = person.getIndex();
		
		for(int i = 0; i < personList.size(); i++) {
			Person temp = personList.get(i);
			if(temp.getIndex() == index) {
				temp.setName(person.getName());
				temp.setBirthday(person.getBirthday());
				temp.setCharacteristic(person.getCharacteristic());
				temp.setEducation(person.getEduation());
				temp.setPost(person.getPost());
				break;
			}
		}		
	}
	
	/** Remove person from archive 
	 * @param person person that must be removed */
	public void remove(Person person) {
		personList.remove(person);
	}
	
	/** Установка нового списка людей
	 * @param personList Новый список люде	 */
	public void setList(ArrayList<Person> personList) {
		this.personList = personList;
	}
	
	/** Return quantity of person in archive 
	 * @return size	of list */
	public int size() {
		return personList.size();
	}
	
	/** Return a person with a given number in collection
	 * @param index number in collection
	 * @return Объект person with a given number */
	public Person get(int index) {
		return personList.get(index);
	}
}
