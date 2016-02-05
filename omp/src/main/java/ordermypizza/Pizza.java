package ordermypizza;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Pizza Definition
 * 
 * @author geurney
 *
 */
@XmlRootElement
public class Pizza {
	/**
	 * Pizza Crust
	 */
	private PizzaComponent curst;

	/**
	 * Pizza Cheese
	 */
	private PizzaComponent cheese;

	/**
	 * Pizza Sauce
	 */
	private PizzaComponent sauce;

	/**
	 * Pizza Toppings
	 */
	private ArrayList<PizzaTopping> toppings;

	/**
	 * Pizza cost
	 */
	private double cost;

	/**
	 * Pizza price
	 */
	private double price;

	/**
	 * Default Constructor
	 */
	public Pizza() {
		toppings = new ArrayList<PizzaTopping>();
	}
	
	/**
	 * @return the curst
	 */
	public PizzaComponent getCurst() {
		return curst;
	}

	/**
	 * @param curst
	 *            the curst to set
	 */
	public void setCurst(PizzaComponent curst) {
		this.curst = curst;
	}

	/**
	 * @return the cheese
	 */
	public PizzaComponent getCheese() {
		return cheese;
	}

	/**
	 * @param cheese
	 *            the cheese to set
	 */
	public void setCheese(PizzaComponent cheese) {
		this.cheese = cheese;
	}

	/**
	 * @return the sauce
	 */
	public PizzaComponent getSauce() {
		return sauce;
	}

	/**
	 * @param sauce
	 *            the sauce to set
	 */
	public void setSauce(PizzaComponent sauce) {
		this.sauce = sauce;
	}

	/**
	 * @return the toppings
	 */
	public ArrayList<PizzaTopping> getToppings() {
		return toppings;
	}

	/**
	 * @param toppings
	 *            the toppings to set
	 */
	public void setToppings(ArrayList<PizzaTopping> toppings) {
		this.toppings = toppings;
	}

	/**
	 * Add topping
	 * 
	 * @param topping
	 *            to add
	 */
	public void addToppings(PizzaTopping topping) {
		toppings.add(topping);
	}

	/**
	 * @return the cost
	 */
	public double getCost() {
		return cost;
	}

	/**
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

}