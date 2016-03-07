package pizzashop;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

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
	@XmlElement
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