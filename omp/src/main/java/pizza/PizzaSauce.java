/**
 * 
 */
package pizza;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * @author Geurney
 *
 */
@XmlRootElement
@XmlSeeAlso(PizzaComponent.class)
public class PizzaSauce extends PizzaComponent {

}
