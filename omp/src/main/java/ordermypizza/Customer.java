/**
 * 
 */
package ordermypizza;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Customer
 * 
 * @author Geurney
 *
 */
@XmlRootElement
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
}