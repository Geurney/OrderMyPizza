/**
 * 
 */
package user;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Admin
 * 
 * @author Geurney
 *
 */
@XmlRootElement
@XmlSeeAlso(User.class)
public class Admin extends User {
	/**
	 * Admin Constructor
	 * 
	 * @param name
	 *            Admin name
	 * @param address
	 *            Admin address
	 * @param phone
	 *            Admin phone
	 */
	public Admin(String name, String address, String phone) {
		super(name, address, phone);
	}
}
