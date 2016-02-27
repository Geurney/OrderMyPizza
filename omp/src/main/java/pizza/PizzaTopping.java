package pizza;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Pizza Topping
 * 
 * @author geurney
 *
 */
@XmlRootElement
@XmlSeeAlso(PizzaComponent.class)
public class PizzaTopping extends PizzaComponent {
	/**
	 * Pizza Topping Types
	 *
	 */
	public static enum PizzaToppingType {
		VEGETABLE, MEAT
	}

	/**
	 * Topping Type
	 */
	private PizzaToppingType type;

	/**
	 * Get topping type
	 * 
	 * @return the type
	 */
	public PizzaToppingType getToppingType() {
		return type;
	}

	/**
	 * Set topping type
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setToppingType(PizzaToppingType type) {
		this.type = type;
	}

}