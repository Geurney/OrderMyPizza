package pizza;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * PizzaCompoment is the abstraction of all pizza components.
 * 
 * @author Geurney
 *
 */
@XmlRootElement
public class PizzaComponent {
	/**
	 * Pizza Component Types
	 */
	public static enum PizzaComponentType {
		CRUST, CHEESE, SAUCE, TOPPING
	}

	/**
	 * Pizza Component Sizes
	 */
	public static enum PizzaComponentSize {
		SMALL, MEDIUM, LARGE
	}

	/**
	 * Component Type
	 */
	private PizzaComponentType type;

	/**
	 * Component Description
	 */
	private String description;

	/**
	 * Component size
	 */
	private PizzaComponentSize size;

	/**
	 * Component Cost
	 */
	private double costs[];

	/**
	 * Component Price
	 */
	private double prices[];
	
	public PizzaComponent() {
		costs = new double[PizzaComponentSize.values().length];
		prices = new double[PizzaComponentSize.values().length];
	}

	/**
	 * Get component type
	 * 
	 * @return the type
	 */
	public PizzaComponentType getType() {
		return type;
	}

	/**
	 * Set component type
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(PizzaComponentType type) {
		this.type = type;
	}

	/**
	 * Get component description
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Set component description
	 * 
	 * @param descripition
	 *            the description to set
	 */
	public void setDescripition(String description) {
		this.description = description;
	}

	/**
	 * Get component cost
	 * 
	 * @return the cost
	 */
	public double getCost() {
		switch (size) {
		case SMALL:
			return costs[0];
		case MEDIUM:
			return costs[1];
		case LARGE:
			return costs[2];
		}
		return 0;
	}

	/**
	 * Set component cost
	 * 
	 * @param cost
	 *            the cost to set
	 */
	public void setCost(double small, double medium, double large) {
		costs[0] = small;
		costs[1] = medium;
		costs[2] = large;
	}

	/**
	 * Get component price
	 * 
	 * @return the price
	 */
	public double getPrice() {
		switch (size) {
		case SMALL:
			return prices[0];
		case MEDIUM:
			return prices[1];
		case LARGE:
			return prices[2];
		}
		return 0;
	}

	/**
	 * Set component price
	 * 
	 * @param price
	 *            the price to set
	 */
	public void setPrice(double small, double medium, double large) {
		prices[0] = small;
		prices[1] = medium;
		prices[2] = large;
	}

	/**
	 * Get component profit
	 * 
	 * @return price - cost
	 */
	public double getProfit() {
		switch (size) {
		case SMALL:
			return prices[0] - costs[0];
		case MEDIUM:
			return prices[1] - costs[1];
		case LARGE:
			return prices[2] - costs[2];
		}
		return 0;
	}

}