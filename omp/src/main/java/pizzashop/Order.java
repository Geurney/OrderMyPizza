package pizzashop;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import pizza.Pizza;

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
	private String pizzaShop;

	/**
	 * Customer
	 */
	private String customer;

	/**
	 * Pizza
	 */
	private Pizza pizza;

	/**
	 * Crust identifier
	 */
	private String crust;

	/**
	 * Cheese identifier
	 */
	private String cheese;

	/**
	 * Sauce identifier
	 */
	private String sauce;

	/**
	 * Meat Toppings identifier
	 */
	private List<String> meats;

	/**
	 * Vegetable Toppings identifier
	 */
	private List<String> vegs;

	/**
	 * Description
	 */
	private String description;

	/**
	 * Date
	 */
	private Date date;

	/**
	 * Price
	 */
	private double price;

	/**
	 * Cost
	 */
	private double cost;

	/**
	 * Size small, medium, large
	 */
	private String size;

	/**
	 * Order status new,verified,ready,done,scancel
	 */
	private String status;

	/**
	 * Get PizzaShop
	 * 
	 * @return the pizzaShop
	 */
	@XmlElement
	public String getPizzaShop() {
		return pizzaShop;
	}

	/**
	 * Set PizzaShop
	 * 
	 * @param pizzaShop
	 *            the pizzaShop to set
	 */
	public void setPizzaShop(String pizzaShop) {
		this.pizzaShop = pizzaShop;
	}

	/**
	 * Get Customer
	 * 
	 * @return the customer
	 */
	@XmlElement
	public String getCustomer() {
		return customer;
	}

	/**
	 * Set Customer
	 * 
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * Get Pizza
	 * 
	 * @return the pizza
	 */
	@XmlElement
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

	/**
	 * Get crust
	 * 
	 * @return the crust
	 */
	@XmlElement
	public String getCrust() {
		return crust;
	}

	/**
	 * Set crust
	 * 
	 * @param crust
	 *            the crust to set
	 */
	public void setCrust(String crust) {
		this.crust = crust;
	}

	/**
	 * Get cheese
	 * 
	 * @return the cheese identifier
	 */
	@XmlElement
	public String getCheese() {
		return cheese;
	}

	/**
	 * Set cheese
	 * 
	 * @param cheese
	 *            cheese identifier
	 */
	public void setCheese(String cheese) {
		this.cheese = cheese;
	}

	/**
	 * Get sauce
	 * 
	 * @return the sauce identifier
	 */
	@XmlElement
	public String getSauce() {
		return sauce;
	}

	/**
	 * Set sauce
	 * 
	 * @param sauce
	 *            sauce identifier
	 */
	public void setSauce(String sauce) {
		this.sauce = sauce;
	}

	/**
	 * Get meat toppings
	 * 
	 * @return the meats identifier
	 */
	@XmlElement
	public List<String> getMeats() {
		return meats;
	}

	/**
	 * Add meat toppings
	 * 
	 * @param meat
	 *            the meat identifier
	 */
	public void addMeat(String meat) {
		meats.add(meat);
	}

	/**
	 * Get vegetable toppings
	 * 
	 * @return the vegs identifier
	 */
	@XmlElement
	public List<String> getVegs() {
		return vegs;
	}

	/**
	 * Add vegetable topping
	 * 
	 * @param veg
	 *            the vegetable topping identifier
	 */
	public void addVeg(String veg) {
		vegs.add(veg);
	}

	/**
	 * Set description
	 * 
	 * @param description
	 *            description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get description
	 */
	@XmlElement
	public String getDescription() {
		return description;
	}

	/**
	 * Set date
	 * 
	 * @return the date
	 */
	@XmlElement
	public Date getDate() {
		return date;
	}

	/**
	 * Get date
	 * 
	 * @param date
	 *            the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Get price
	 * 
	 * @return the price
	 */
	@XmlElement
	public double getPrice() {
		return price;
	}

	/**
	 * Set price
	 * 
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * Get cost
	 * 
	 * @return cost
	 */
	@XmlElement
	public double getCost() {
		return cost;
	}

	/**
	 * Set cost
	 * 
	 * @param cost
	 *            Cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * Set profit
	 * 
	 * @param profit
	 *            Profit
	 */
	@XmlElement
	public double getProfit() {
		return price - cost;
	}

	/**
	 * Get status
	 * 
	 * @return the status
	 */
	@XmlElement
	public String getStatus() {
		return status;
	}

	/**
	 * Set status
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Get size
	 * 
	 * @return size
	 */
	@XmlElement
	public String getSize() {
		return size;
	}

	/**
	 * Set size
	 * 
	 * @param size
	 *            Size
	 */
	public void setSize(String size) {
		this.size = size;
	}

}