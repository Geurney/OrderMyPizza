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
	 * User token
	 */
	private String token;

	/**
	 * User email
	 */
	private String email;

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
	 * Get token
	 * 
	 * @return User hash id
	 */
	@XmlElement
	public String getToken() {
		return token;
	}

	/**
	 * Set token only if current token is null
	 * 
	 * @param token
	 *            Token to set
	 */
	public void setToken(String token) {
		if (this.token == null) {
			this.token = token;
		}
	}

	/**
	 * Get user email
	 * 
	 * @return the email
	 */
	@XmlElement
	public String getEmail() {
		return email;
	}

	/**
	 * Set user email
	 * 
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
