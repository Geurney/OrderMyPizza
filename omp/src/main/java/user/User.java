/**
 * 
 */
package user;

import javax.xml.bind.annotation.XmlElement;

/**
 * User abstract class
 * 
 * @author Geurney
 *
 */
public abstract class User {
	/**
	 * User Types
	 *
	 */
	public static enum Type {
		CUSTOMER, PIZZASHOP, ADMIN
	}

	/**
	 * User name
	 */
	private String name;

	/**
	 * User Address
	 */
	private String address;

	/**
	 * User phone
	 */
	private String phone;

	/**
	 * Default constructor
	 */
	public User() {
	}

	/**
	 * User constructor
	 * 
	 * @param name
	 *            User name
	 * @param address
	 *            User address
	 * @param phone
	 *            User phone
	 * @param type
	 *            User type
	 */
	public User(String name, String address, String phone) {
		this.name = name;
		this.address = address;
		this.phone = phone;
	}

	/**
	 * Get user name
	 * 
	 * @return the name
	 */
	@XmlElement
	public String getName() {
		return name;
	}

	/**
	 * Set user name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Get user address
	 * 
	 * @return the address
	 */
	@XmlElement
	public String getAddress() {
		return address;
	}

	/**
	 * Set user address
	 * 
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Get user phone
	 * 
	 * @return the phone
	 */
	@XmlElement
	public String getPhone() {
		return phone;
	}

	/**
	 * Set user phone
	 * 
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
}
