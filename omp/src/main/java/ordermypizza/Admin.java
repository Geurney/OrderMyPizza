/**
 * 
 */
package ordermypizza;

/**
 * Admin
 * 
 * @author Geurney
 *
 */
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
		super(name, address, phone, Type.ADMIN);
	}
}
