package pizza;

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
	private PizzaCrust curst;

	/**
	 * Pizza Cheese
	 */
	private PizzaCheese cheese;

	/**
	 * Pizza Sauce
	 */
	private PizzaSauce sauce;

	/**
	 * Pizza Meat Toppings
	 */
	private ArrayList<PizzaToppingVeg> vegs;

	/**
	 * Pizza Vegetable Toppings
	 */
	private ArrayList<PizzaToppingMeat> meats;

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
		vegs = new ArrayList<PizzaToppingVeg>();
		meats = new ArrayList<PizzaToppingMeat>();
	}

	/**
	 * @return the curst
	 */
	public PizzaCrust getCurst() {
		return curst;
	}

	/**
	 * @param curst
	 *            the curst to set
	 */
	public void setCurst(PizzaCrust curst) {
		this.curst = curst;
	}

	/**
	 * @return the cheese
	 */
	public PizzaCheese getCheese() {
		return cheese;
	}

	/**
	 * @param cheese
	 *            the cheese to set
	 */
	public void setCheese(PizzaCheese cheese) {
		this.cheese = cheese;
	}

	/**
	 * @return the sauce
	 */
	public PizzaSauce getSauce() {
		return sauce;
	}

	/**
	 * @param sauce
	 *            the sauce to set
	 */
	public void setSauce(PizzaSauce sauce) {
		this.sauce = sauce;
	}

	/**
	 * @return the toppings
	 */
	public ArrayList<PizzaToppingVeg> getToppingVegs() {
		return vegs;
	}

	/**
	 * @param toppings
	 *            the toppings to set
	 */
	public void setToppingVegs(ArrayList<PizzaToppingVeg> toppings) {
		this.vegs = toppings;
	}

	/**
	 * @return the toppings
	 */
	public ArrayList<PizzaToppingMeat> getToppingMeats() {
		return meats;
	}

	/**
	 * @param toppings
	 *            the toppings to set
	 */
	public void setToppingMeats(ArrayList<PizzaToppingMeat> toppings) {
		this.meats = toppings;
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

	public void addToppingMeat(PizzaToppingMeat topping) {
		meats.add(topping);
	}

	public void addToppingVeg(PizzaToppingVeg topping) {
		vegs.add(topping);
	}

}