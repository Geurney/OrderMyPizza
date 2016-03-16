package order;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Order (viewed as tasks for task queues)
 * 
 * @author Geurney
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Order {

	/**
	 * Pizza Shop identifier
	 */
	private String pizzaShop;

	/**
	 * Customer email
	 */
	private String customer;

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
	 * Date
	 */
	private String date;

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
	 * Order status new,verified,ready,done, cancel
	 */
	private String status;

	/**
	 * Order number
	 */
	private String number;

	public Order() {
		meats = new ArrayList<String>();
		vegs = new ArrayList<String>();
	}

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
	 *            the pizzaShop identifier to set
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
		if (meat == null) {
			return;
		}
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
		if (veg == null) {
			return;
		}
		vegs.add(veg);
	}

	/**
	 * Get description
	 * 
	 * @return Description
	 */
	@XmlElement
	public String getDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(size + " size, " + crust + " crust, " + cheese
				+ "cheese, with " + sauce + " sauce.");
		if (meats.size() == 0) {
			sb.append(" No meat toppings.");
		}
		{
			sb.append(" Include meat topping: ");
			for (String s : meats) {
				sb.append(s + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		if (vegs.size() == 0) {
			sb.append(" No vegetable toppings.");
		} else {
			sb.append(" Include vegetable topping: ");
			for (String s : vegs) {
				sb.append(s + ",");
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}

	/**
	 * Set date
	 * 
	 * @return the date
	 */
	@XmlElement
	public String getDate() {
		return date;
	}

	/**
	 * Get date
	 * 
	 * @param date
	 *            the date to set
	 */
	public void setDate(String date) {
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
	public float getProfit() {
		return (float)price - (float)cost;
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

	/**
	 * Get order number
	 * 
	 * @return the number
	 */
	@XmlElement
	public String getNumber() {
		return number;
	}

	/**
	 * Set order number
	 * 
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

}