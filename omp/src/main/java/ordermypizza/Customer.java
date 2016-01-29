package ordermypizza;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Customer {
	private String userName;
	private String userEmail;
	private String userAddr;
	private String userPhone;
	private String userPswd;

	public Customer() {
	}

	public Customer(String UserName, String UserEmail, String UserAddr,
			String UserPhone, String UserPswd) {
		this.userName = UserName;
		this.userEmail = UserEmail;
		this.setUserAddr(UserAddr);
		this.setUserPhone(UserPhone);
		this.userPswd = UserPswd;
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
	 * @return the userAddr
	 */
	public String getUserAddr() {
		return userAddr;
	}

	/**
	 * @param userAddr
	 *            the userAddr to set
	 */
	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}

	/**
	 * @return the userPhone
	 */
	public String getUserPhone() {
		return userPhone;
	}

	/**
	 * @param userPhone
	 *            the userPhone to set
	 */
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	/**
	 * @return the userPswrd
	 */
	public String getUserPswrd() {
		return userPswd;
	}

	/**
	 * @param userPswd
	 *            the userPswrd to set
	 */
	public void setUserPswrd(String userPswd) {
		this.userPswd = userPswd;
	}

}