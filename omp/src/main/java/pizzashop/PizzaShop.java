package pizzashop;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import user.User;

@XmlRootElement
@XmlSeeAlso(User.class)
public class PizzaShop extends User {
	/**
	 * Pizza Factory
	 */
	private PizzaFactory pizzaFactory; // May be a string to xml.

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
		super(name, address, phone, Type.PIZZASHOP);
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

}