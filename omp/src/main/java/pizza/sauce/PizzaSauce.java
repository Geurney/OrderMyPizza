/**
 * 
 */
package pizza.sauce;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizza.PizzaComponent;

/**
 * PizzaSauce Class
 * @author Geurney
 *
 */
@XmlRootElement(name="PizzaSauce")
@XmlSeeAlso(PizzaComponent.class)
public class PizzaSauce extends PizzaComponent {
	public PizzaSauce() {
		super();
	}
}
