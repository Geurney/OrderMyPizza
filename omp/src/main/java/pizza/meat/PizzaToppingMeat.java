/**
 * 
 */
package pizza.meat;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizza.PizzaComponent;

/**
 * Pizza Meat Topping Class
 * 
 * @author Geurney
 *
 */
@XmlRootElement(name = "PizzaToppingMeat")
@XmlSeeAlso(PizzaComponent.class)
public class PizzaToppingMeat extends PizzaComponent {
	/**
	 * Constructor
	 */
	public PizzaToppingMeat() {
		super();
	}
}
