package ordermypizza;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Customer {
	private String userName;
	private String userEmail;
	private String userPswrd;

	public Customer() {
	}

	public Customer(String UserName, String UserEmail, String UserPswrd) {
		this.userName = UserName;
		this.userEmail = UserEmail;
		this.userPswrd = UserPswrd;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userEmail
	 */
	public String getUserEmail() {
		return userEmail;
	}

	/**
	 * @param userEmail
	 *            the userEmail to set
	 */
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	/**
	 * @return the userPswrd
	 */
	public String getUserPswrd() {
		return userPswrd;
	}

	/**
	 * @param userPswrd
	 *            the userPswrd to set
	 */
	public void setUserPswrd(String userPswrd) {
		this.userPswrd = userPswrd;
	}

}