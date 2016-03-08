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
	 * Default constructor
	 * 
	 * @param token
	 *            Token
	 */
	public Admin() {
		super();
	}
}
