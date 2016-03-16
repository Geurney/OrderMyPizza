/**
 * 
 */
package pizza.cheese;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizza.PizzaComponent;

/**
 * PizzaCheese Class
 * 
 * @author Geurney
 *
 */
@XmlRootElement(name = "PizzaCheese")
@XmlSeeAlso(PizzaComponent.class)
public class PizzaCheese extends PizzaComponent {
	/**
	 * Constructor
	 */
	public PizzaCheese() {
		super();
	}
}
