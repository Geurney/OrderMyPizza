/**
 * 
 */
package user;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizzashop.Order;

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
	 * Customer's orders
	 */
	private List<Order> orders;

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
		super(name, address, phone);
		orders = new ArrayList<Order>();
	}

	/**
	 * Default constructor
	 */
	public Customer() {
		super();
	}

	/**
	 * Get orders
	 * 
	 * @return the orders
	 */
	public List<Order> getOrders() {
		return orders;
	}

	/**
	 * Add order
	 * 
	 * @param order
	 *            the order to add
	 */
	public void addOrder(Order order) {
		orders.add(order);
	}
}