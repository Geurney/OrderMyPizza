package pizzashop;

import java.util.UUID;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizza.Pizza;
import user.User;

@XmlRootElement
@XmlSeeAlso(User.class)
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
		isAvailable &= pizzaFactory.hasCrust(order.getCrust());
		isAvailable &= pizzaFactory.hasCheese(order.getCheese());
		isAvailable &= pizzaFactory.hasSauce(order.getSauce());
		for (String meat : order.getMeats()) {
			isAvailable &= pizzaFactory.hasMeat(meat);
		}
		for (String veg : order.getVegs()) {
			isAvailable &= pizzaFactory.hasVeg(veg);
		}
		return isAvailable;
	}

	/**
	 * Create Pizza
	 * 
	 * @param order
	 *            Order
	 * @return Pizza
	 */
	public Pizza createPizza(Order order) {
		Pizza pizza = pizzaFactory.preparePizza();
		pizzaFactory.buildCrust(order.getCrust(), pizza);
		pizzaFactory.buildCheese(order.getCheese(), pizza);
		pizzaFactory.buildSauce(order.getSauce(), pizza);
		pizzaFactory.buildToppingMeat(order.getMeats(), pizza);
		pizzaFactory.buildToppingVeg(order.getVegs(), pizza);
		return pizza;
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