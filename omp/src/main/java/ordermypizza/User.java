/**
 * 
 */
package ordermypizza;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * User abstract class
 * 
 * @author Geurney
 *
 */
@XmlRootElement
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
	 * User type
	 */
	private Type type;

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
	public User(String name, String address, String phone, Type type) {
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.type = type;
	}

	/**
	 * Get user name
	 * 
	 * @return the name
	 */
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

	/**
	 * Get user type
	 * 
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

	/**
	 * Set user type
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(Type type) {
		this.type = type;
	}

}
