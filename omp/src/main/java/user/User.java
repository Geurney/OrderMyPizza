/**
 * 
 */
package user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * User abstract class
 * 
 * @author Geurney
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class User {

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
	 * User phone
	 */
	private String phone;

	/**
	 * User City Name
	 */
	private String City;

	/**
	 * Latitude and longitude
	 */
	private double[] location = new double[2];

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

	/**
	 * Get City Name
	 * 
	 * @return the city
	 */
	@XmlElement
	public String getCity() {
		return City;
	}

	/**
	 * Set City
	 * 
	 * @param city
	 *            the City to set
	 */
	public void setCity(String city) {
		City = city;
	}

	/**
	 * Get latitude
	 * 
	 * @return the latitude
	 */
	@XmlElement
	public double getLatitude() {
		return location[0];
	}

	/**
	 * Set latitude
	 * 
	 * @param latitude
	 *            the latitude to set
	 */
	public void setLatitude(double latitude) {
		location[0] = latitude;
	}

	/**
	 * Get longitude
	 * 
	 * @return the longitude
	 */
	@XmlElement
	public double getLongitude() {
		return location[1];
	}

	/**
	 * Set longitude
	 * 
	 * @param longitude
	 *            the longitude to set
	 */
	public void setLongitude(double longitude) {
		location[1] = longitude;
	}

}
