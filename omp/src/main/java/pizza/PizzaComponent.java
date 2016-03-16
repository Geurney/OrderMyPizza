package pizza;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * PizzaCompoment is the abstraction of all pizza components.
 * 
 * @author Geurney
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
public class PizzaComponent {
	/**
	 * Pizza Component Sizes
	 */
	public static enum PizzaComponentSize {
		SMALL, MEDIUM, LARGE
	}

	/**
	 * Component Identifier
	 */
	private String identifier;

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

	/**
	 * Default Constructor
	 */
	public PizzaComponent() {
		costs = new double[3];
		prices = new double[3];
		for (int i = 0; i < 3; i++) {
			costs[i] = -1;
			prices[i] = -1;
		}
	}

	/**
	 * Get Pizza Component identifier
	 * 
	 * @return the identifier
	 */
	@XmlElement
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Set Pizza Component identifier
	 * 
	 * @param identifier
	 *            the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Get size
	 * 
	 * @return Size
	 */
	public PizzaComponentSize getSize() {
		return size;
	}

	/**
	 * Set size
	 * 
	 * @param size
	 *            Size
	 */
	public void setSize(PizzaComponentSize size) {
		this.size = size;
	}

	/**
	 * Get component description
	 * 
	 * @return the description
	 */
	@XmlElement
	public String getDescription() {
		return description;
	}

	/**
	 * Set component description
	 * 
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Get component all costs
	 * 
	 * @return the costs
	 */
	@XmlElement
	public double[] getCosts() {
		return costs;
	}

	/**
	 * Get specific cost
	 * 
	 * @param size
	 *            Size
	 * @return cost
	 */
	public double getCost(int size) {
		if (size >= 3 || size < 0) {
			return -1;
		}
		return costs[size];
	}

	/**
	 * Get specific price
	 * 
	 * @param size
	 *            Size
	 * @return price
	 */
	public double getPrice(int size) {
		if (size >= 3 || size < 0) {
			return -1;
		}
		return prices[size];
	}

	/**
	 * Set component costs
	 * 
	 * @param small
	 *            Small size cost
	 * @param medium
	 *            Medium size cost
	 * @param large
	 *            Large size cost
	 */
	public void setCosts(double small, double medium, double large) {
		costs[0] = small;
		costs[1] = medium;
		costs[2] = large;
	}

	/**
	 * Set component costs
	 * 
	 * @param costs
	 *            Costs
	 */
	public void setCosts(List<Double> costs) {
		if (costs == null || costs.size() != 3) {
			return;
		}
		this.costs[0] = costs.get(0);
		this.costs[1] = costs.get(1);
		this.costs[2] = costs.get(2);
	}

	/**
	 * Get current component cost
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
	 * Get component all prices
	 * 
	 * @return the price
	 */
	@XmlElement
	public double[] getPrices() {
		return prices;
	}

	/**
	 * Get current component price
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
	 * Set component prices
	 * 
	 * @param small
	 *            Small size price
	 * @param medium
	 *            Medium size price
	 * @param large
	 *            Large size price
	 */
	public void setPrices(double small, double medium, double large) {
		prices[0] = small;
		prices[1] = medium;
		prices[2] = large;
	}

	/**
	 * Set component prices
	 * 
	 * @param prices
	 *            Prices
	 */
	public void setPrices(List<Double> prices) {
		if (prices == null || prices.size() != 3) {
			return;
		}
		this.prices[0] = prices.get(0);
		this.prices[1] = prices.get(1);
		this.prices[2] = prices.get(2);
	}

	/**
	 * Get component profits
	 * 
	 * @return profits
	 */
	@XmlElement
	public double[] getProfits() {
		double[] profits = new double[3];
		profits[0] = prices[0] - costs[0];
		profits[1] = prices[1] - costs[1];
		profits[2] = prices[2] - costs[2];
		return profits;
	}

	/**
	 * Get current component profit
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