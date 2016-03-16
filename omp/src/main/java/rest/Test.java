/**
 * 
 */
package rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is for test class
 * 
 * @author Geurney
 *
 */
@XmlRootElement(name = "TestObject")
public class Test {

	private String keyname;
	
	private String name;

	private String phone;

	private String address;

	public Test() {

	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the key
	 */
	public String getKeyName() {
		return keyname;
	}

	/**
	 * @param key the key to set
	 */
	public void setKeyName(String keyname) {
		this.keyname = keyname;
	}

}
