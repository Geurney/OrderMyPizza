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
public class PizzaToppingMeat extends PizzaComponent {

}