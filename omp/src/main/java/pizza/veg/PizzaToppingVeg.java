/**
 * 
 */
package pizza.veg;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizza.PizzaComponent;

/**
 * @author Geurney
 *
 */
@XmlRootElement(name="PizzaToppingVeg")
@XmlSeeAlso(PizzaComponent.class)
public class PizzaToppingVeg extends PizzaComponent {
	public PizzaToppingVeg() {
		super();
	}
}
