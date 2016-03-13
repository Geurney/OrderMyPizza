/**
 * 
 */
package customer;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import order.Order;
import user.User;

/**
 * Customer data structure
 * 
 * @author Geurney
 *
 */
@XmlRootElement(name = "Customer")
@XmlSeeAlso(User.class)
@XmlAccessorType(XmlAccessType.NONE)
public class Customer extends User {
	/**
	 * Customer's orders
	 */
	private List<Order> orders = new ArrayList<Order>();

	/**
	 * Default constructor
	 * 
	 * @param token
	 *            Token
	 */
	public Customer() {
		super();
	}

	/**
	 * Get orders
	 * 
	 * @return the orders
	 */
	@XmlElement
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