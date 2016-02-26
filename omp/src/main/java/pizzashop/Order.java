package pizzashop;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizza.Pizza;
import user.Customer;
import user.User;

/**
 * Order (viewed as tasks for task queues)
 * @author Geurney
 *
 */
@XmlRootElement
public class Order {
	/**
	 * Pizza Shop
	 */
	private PizzaShop pizzaShop;
	
	/**
	 * Customer
	 */
	private Customer customer;
	
	/**
	 * Pizza
	 */
	private Pizza pizza;
	
	
}