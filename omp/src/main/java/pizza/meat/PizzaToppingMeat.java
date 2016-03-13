/**
 * 
 */
package pizza.meat;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizza.PizzaComponent;

/**
 * @author Geurney
 *
 */
@XmlRootElement(name="PizzaToppingMeat")
@XmlSeeAlso(PizzaComponent.class)
public class PizzaToppingMeat extends PizzaComponent {
	public PizzaToppingMeat() {
		super();
	}
}
