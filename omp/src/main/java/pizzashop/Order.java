package pizzashop;

import javax.xml.bind.annotation.XmlRootElement;

import pizza.Pizza;
import user.Customer;

/**
 * Order (viewed as tasks for task queues)
 * 
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

	/**
	 * Get PizzaShop
	 * 
	 * @return the pizzaShop
	 */
	public PizzaShop getPizzaShop() {
		return pizzaShop;
	}

	/**
	 * Set PizzaShop
	 * 
	 * @param pizzaShop
	 *            the pizzaShop to set
	 */
	public void setPizzaShop(PizzaShop pizzaShop) {
		this.pizzaShop = pizzaShop;
	}

	/**
	 * Get Customer
	 * 
	 * @return the customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * Set Customer
	 * 
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * Get Pizza
	 * 
	 * @return the pizza
	 */
	public Pizza getPizza() {
		return pizza;
	}

	/**
	 * Set Pizza
	 * 
	 * @param pizza
	 *            the pizza to set
	 */
	public void setPizza(Pizza pizza) {
		this.pizza = pizza;
	}

}