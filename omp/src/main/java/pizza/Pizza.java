package pizza;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import pizza.PizzaComponent.PizzaComponentSize;

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
	private PizzaCrust crust;

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
	 * Pizza Size
	 */
	private PizzaComponentSize size;

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
	 * Get crust
	 * 
	 * @return the crust
	 */
	@XmlElement
	public PizzaCrust getCurst() {
		return crust;
	}

	/**
	 * Set Crust
	 * 
	 * @param crust
	 *            the crust to set
	 */
	public void setCurst(PizzaCrust crust) {
		this.crust = crust;
	}

	/**
	 * Get cheese
	 * 
	 * @return the cheese
	 */
	@XmlElement
	public PizzaCheese getCheese() {
		return cheese;
	}

	/**
	 * Set cheese
	 * 
	 * @param cheese
	 *            the cheese to set
	 */
	public void setCheese(PizzaCheese cheese) {
		this.cheese = cheese;
	}

	/**
	 * Get sauce
	 * 
	 * @return the sauce
	 */
	@XmlElement
	public PizzaSauce getSauce() {
		return sauce;
	}

	/**
	 * Set sauce
	 * 
	 * @param sauce
	 *            the sauce to set
	 */
	public void setSauce(PizzaSauce sauce) {
		this.sauce = sauce;
	}

	/**
	 * Get vegetable toppings
	 * 
	 * @return the toppings
	 */
	@XmlElement
	public List<PizzaToppingVeg> getToppingVegs() {
		return vegs;
	}

	/**
	 * Get meat toppings
	 * 
	 * @return the toppings
	 */
	@XmlElement
	public ArrayList<PizzaToppingMeat> getToppingMeats() {
		return meats;
	}

	/**
	 * Get ingredient cost
	 * 
	 * @return ingredient cost
	 */
	public double getIngredientsCost() {
		double ingredCost = 0;
		if (crust != null) {
			ingredCost += crust.getCost();
		}
		if (cheese != null) {
			ingredCost += sauce.getCost();
		}
		if (sauce != null) {
			ingredCost += cheese.getCost();
		}
		if (meats != null) {
			for (PizzaToppingMeat meat : meats) {
				ingredCost += meat.getCost();
			}
		}
		if (vegs != null) {
			for (PizzaToppingVeg veg : vegs) {
				ingredCost += veg.getCost();
			}
		}
		return ingredCost;
	}

	/**
	 * Get ingredient price
	 * 
	 * @return ingredient price
	 */
	public double getIngredientsPrice() {
		double ingredPrice = 0;
		if (crust != null) {
			ingredPrice += crust.getPrice();
		}
		if (cheese != null) {
			ingredPrice += sauce.getPrice();
		}
		if (sauce != null) {
			ingredPrice += cheese.getPrice();
		}
		if (meats != null) {
			for (PizzaToppingMeat meat : meats) {
				ingredPrice += meat.getPrice();
			}
		}
		if (vegs != null) {
			for (PizzaToppingVeg veg : vegs) {
				ingredPrice += veg.getPrice();
			}
		}
		return ingredPrice;
	}

	/**
	 * Get ingredient profits
	 * 
	 * @return ingredient profits
	 */
	public double getIngredientProfit() {
		return getIngredientsPrice() - getIngredientsCost();
	}

	/**
	 * Set cost
	 * 
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(double cost) {
		this.cost = cost;
	}

	/**
	 * Get cost
	 * 
	 * @return cost
	 */
	public double getCost() {
		return cost;
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
	 * Add meat topping
	 * 
	 * @param topping
	 *            Topping to add
	 */
	public void addToppingMeat(PizzaToppingMeat topping) {
		meats.add(topping);
	}

	/**
	 * Add vegetable topping
	 * 
	 * @param topping
	 *            Topping to add
	 */
	public void addToppingVeg(PizzaToppingVeg topping) {
		vegs.add(topping);
	}

	/**
	 * Get pizza size
	 * 
	 * @return the size
	 */
	public PizzaComponentSize getSize() {
		return size;
	}

	/**
	 * Set pizza size
	 * 
	 * @param size
	 *            the size to set
	 */
	public void setSize(PizzaComponentSize size) {
		this.size = size;
	}

}