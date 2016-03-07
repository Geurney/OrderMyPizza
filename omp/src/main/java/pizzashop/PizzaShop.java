package pizzashop;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizza.Pizza;
import user.User;

@XmlRootElement
@XmlSeeAlso(User.class)
public class PizzaShop extends User {
	/**
	 * Pizza Factory
	 */
	private PizzaFactory pizzaFactory;

	/**
	 * Pizza Shop constructor
	 * 
	 * @param name
	 *            Shop name
	 * @param address
	 *            Shop address
	 * @param phone
	 *            Shop phone
	 */
	public PizzaShop(String name, String address, String phone) {
		super(name, address, phone);
	}

	/**
	 * Default constructor
	 */
	public PizzaShop() {
		super();
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

}