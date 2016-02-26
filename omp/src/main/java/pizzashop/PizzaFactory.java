package pizzashop;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import pizza.Pizza;
import pizza.PizzaComponent;
import pizza.PizzaTopping;
import user.User;

/**
 * Pizza Factory to create pizza
 * 
 * @author geurney
 *
 */
@XmlRootElement
public class PizzaFactory {
	/**
	 * Available Pizza Components in the factory
	 */
	private HashMap<PizzaComponent.PizzaComponentType, PizzaComponent> components;

	/**
	 * Default Constructor
	 */
	public PizzaFactory() {

	}

	/**
	 * Add pizza component
	 * 
	 * @param component
	 *            to add
	 */
	public void addComponent(PizzaComponent component) {
		components.put(component.getType(), component);
	}

	/**
	 * Create Pizza
	 * 
	 * @param crust
	 *            Pizza Crust
	 * @param cheese
	 *            Pizza Cheese
	 * @param sauce
	 *            Pizza Sauce
	 * @param toppings
	 *            Pizza toppings
	 * @return Pizza
	 */
	public Pizza createPizza(String crust, String cheese, String sauce,
			ArrayList<String> toppings) {
		Pizza pizza = new Pizza();

		PizzaComponent pizzaCrust = new PizzaComponent();
		pizza.setCurst(pizzaCrust);

		PizzaComponent pizzaCheese = new PizzaComponent();
		pizza.setCheese(pizzaCheese);

		PizzaComponent pizzaSauce = new PizzaComponent();
		pizza.setSauce(pizzaSauce);

		for (String topping : toppings) {
			PizzaTopping pizzaTopping = new PizzaTopping();
			pizza.addToppings(pizzaTopping);
		}

		return pizza;
	}
}