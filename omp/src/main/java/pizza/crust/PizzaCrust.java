/**
 * 
 */
package pizza.crust;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizza.PizzaComponent;

/**
 * Pizza Crust Class
 * 
 * @author Geurney
 *
 */
@XmlRootElement(name = "PizzaCrust")
@XmlSeeAlso(PizzaComponent.class)
public class PizzaCrust extends PizzaComponent {
	/**
	 * Constructor
	 */
	public PizzaCrust() {
		super();
	}
}
