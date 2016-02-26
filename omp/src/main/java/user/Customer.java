/**
 * 
 */
package user;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Customer data structure
 * 
 * @author Geurney
 *
 */
@XmlRootElement
@XmlSeeAlso(User.class)
public class Customer extends User {
	/**
	 * Customer constructor
	 * 
	 * @param name
	 *            Customer name
	 * @param address
	 *            Customer address
	 * @param phone
	 *            Customer phone
	 */
	public Customer(String name, String address, String phone) {
		super(name, address, phone, Type.CUSTOMER);
	}

	/**
	 * Default constructor
	 */
	public Customer() {
		super(Type.CUSTOMER);
	}
}