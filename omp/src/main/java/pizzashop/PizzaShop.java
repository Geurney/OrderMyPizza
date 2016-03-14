package pizzashop;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import order.Order;
import pizzafactory.PizzaFactory;
import user.User;

/**
 * PizzaShop Class
 * 
 * @author Geurney
 *
 */
@XmlRootElement(name = "PizzaShop")
@XmlSeeAlso(User.class)
@XmlAccessorType(XmlAccessType.NONE)
public class PizzaShop extends User {
	/**
	 * Pizza Shop identifier
	 */
	private String identifier;

	/**
	 * Pizza Factory
	 */
	private PizzaFactory pizzaFactory;

	/**
	 * Default constructor
	 * 
	 */
	public PizzaShop() {
		super();
	}

	/**
	 * Get pizza shop identifier
	 * 
	 * @return the identifier
	 */
	@XmlElement
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Set pizza shop identifier
	 * 
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Get Pizza Factory
	 * 
	 * @return the pizzaFactory
	 */
	public PizzaFactory getPizzaFactory() {
		return pizzaFactory;
	}

	/**
	 * Set Pizza Factory
	 * 
	 * @param pizzaFactory
	 *            the pizzaFactory to set
	 */
	public void setPizzaFactory(PizzaFactory pizzaFactory) {
		this.pizzaFactory = pizzaFactory;
	}

	/**
	 * Verify the shop's factory has all ingredients
	 *
	 * @param order
	 *            Order
	 * @return True if all ingredients are available
	 */
	public boolean verifyOrder(Order order) {
		boolean isAvailable = true;
		isAvailable &= pizzaFactory.hasCheese(order.getCheese());
		return isAvailable;
	}

	/**
	 * Generate unique order id
	 * 
	 * @return Order ID
	 */
	public static String generateOrderID() {
		return UUID.randomUUID().toString();
	}
}