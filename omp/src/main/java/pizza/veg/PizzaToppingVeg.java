/**
 * 
 */
package pizza.veg;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizza.PizzaComponent;

/**
 * Pizza Vegetable Topping Class
 * 
 * @author Geurney
 *
 */
@XmlRootElement(name = "PizzaToppingVeg")
@XmlSeeAlso(PizzaComponent.class)
public class PizzaToppingVeg extends PizzaComponent {
	/**
	 * Constructor
	 */
	public PizzaToppingVeg() {
		super();
	}
}
