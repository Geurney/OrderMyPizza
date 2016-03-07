package pizzashop;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

import pizza.Pizza;
import pizza.PizzaCheese;
import pizza.PizzaCrust;
import pizza.PizzaSauce;
import pizza.PizzaToppingMeat;
import pizza.PizzaToppingVeg;

/**
 * Pizza Factory to create pizza
 * 
 * @author Geurney
 *
 */
@XmlRootElement
public class PizzaFactory {

	/**
	 * Pizza to build
	 */
	private Pizza pizza;

	/**
	 * Available crusts
	 */
	private HashMap<String, PizzaCrust> crusts;

	/**
	 * Available cheese
	 */
	private HashMap<String, PizzaCheese> cheeses;

	/**
	 * Available sauces
	 */
	private HashMap<String, PizzaSauce> sauces;

	/**
	 * Available meat toppings
	 */
	private HashMap<String, PizzaToppingMeat> meats;

	/**
	 * Available vegetable toppings
	 */
	private HashMap<String, PizzaToppingVeg> vegs;

	/**
	 * Default Constructor
	 */
	public PizzaFactory() {
		crusts = new HashMap<String, PizzaCrust>();
		cheeses = new HashMap<String, PizzaCheese>();
		sauces = new HashMap<String, PizzaSauce>();
		meats = new HashMap<String, PizzaToppingMeat>();
		vegs = new HashMap<String, PizzaToppingVeg>();
	}

	/**
	 * Add a crust if the identifier not exist in the factory.
	 * 
	 * @param crust
	 *            Crust to be added
	 * @return True if crust does not exist. False if the crust exists.
	 */
	public boolean addCrust(PizzaCrust crust) {
		if (crusts.containsKey(crust.getIdentifier())) {
			return false;
		}
		crusts.put(crust.getIdentifier(), crust);
		return true;
	}

	/**
	 * Put a crust into factory. Replace if already exits.
	 * 
	 * @param crust
	 *            Crust to be added
	 */
	public void putCrust(PizzaCrust crust) {
		crusts.put(crust.getIdentifier(), crust);
	}

	/**
	 * Add a cheese if the identifier not exist in the factory.
	 * 
	 * @param cheese
	 *            Cheese to be added
	 * @return True if cheese does not exist. False if the cheese exists.
	 */
	public boolean addCheese(PizzaCheese cheese) {
		if (cheeses.containsKey(cheese.getIdentifier())) {
			return false;
		}
		cheeses.put(cheese.getIdentifier(), cheese);
		return true;
	}

	/**
	 * Put a cheese into factory. Replace if already exits.
	 * 
	 * @param cheese
	 *            Cheese to be added
	 */
	public void putCheese(PizzaCheese cheese) {
		cheeses.put(cheese.getIdentifier(), cheese);
	}

	/**
	 * Add a sauce if the identifier not exist in the factory.
	 * 
	 * @param sauce
	 *            sauce to be added
	 * @return True if sauce does not exist. False if the sauce exists.
	 */
	public boolean addSauce(PizzaSauce sauce) {
		if (sauces.containsKey(sauce.getIdentifier())) {
			return false;
		}
		sauces.put(sauce.getIdentifier(), sauce);
		return true;
	}

	/**
	 * Put a sauce into factory. Replace if already exits.
	 * 
	 * @param sauce
	 *            Sauce to be added
	 */
	public void putSauce(PizzaSauce sauce) {
		sauces.put(sauce.getIdentifier(), sauce);
	}

	/**
	 * Add a meat topping if the identifier not exist in the factory.
	 * 
	 * @param topping
	 *            topping to be added
	 * @return True if topping does not exist. False if the topping exists.
	 */
	public boolean addToppingMeat(PizzaToppingMeat topping) {
		if (meats.containsKey(topping.getIdentifier())) {
			return false;
		}
		meats.put(topping.getIdentifier(), topping);
		return true;
	}

	/**
	 * Put a meat topping into factory. Replace if already exits.
	 * 
	 * @param topping
	 *            topping to be added
	 */
	public void putToppingMeat(PizzaToppingMeat topping) {
		meats.put(topping.getIdentifier(), topping);
	}

	/**
	 * Add a vegetable topping if the identifier not exist in the factory.
	 * 
	 * @param topping
	 *            topping to be added
	 * @return True if topping does not exist. False if the topping exists.
	 */
	public boolean addToppingVeg(PizzaToppingVeg topping) {
		if (vegs.containsKey(topping.getIdentifier())) {
			return false;
		}
		vegs.put(topping.getIdentifier(), topping);
		return true;
	}

	/**
	 * Put a vegetable topping into factory. Replace if already exits.
	 * 
	 * @param topping
	 *            topping to be added
	 */
	public void putToppingVeg(PizzaToppingVeg topping) {
		vegs.put(topping.getIdentifier(), topping);
	}

	/**
	 * Prepare a raw pizza
	 * 
	 * @return pizza
	 */
	public Pizza preparePizza() {
		pizza = new Pizza();
		return pizza;
	}

	/**
	 * Build Crust
	 * 
	 * @param id
	 *            Crust identifier
	 * @return True if crust exists in the factory.
	 */
	public boolean buildCrust(String id) {
		if (pizza == null) {
			pizza = new Pizza();
		}
		PizzaCrust crust = crusts.get(id);
		if (crust == null) {
			return false;
		}
		pizza.setCurst(crust);
		return true;
	}

	/**
	 * Build cheese
	 * 
	 * @param id
	 *            Cheese identifier
	 * @return True if cheese exists in the factory
	 */
	public boolean buildCheese(String id) {
		if (pizza == null) {
			pizza = new Pizza();
		}
		PizzaCheese cheese = cheeses.get(id);
		if (cheese == null) {
			return false;
		}
		pizza.setCheese(cheese);
		return true;
	}

	/**
	 * Build sauce
	 * 
	 * @param id
	 *            Sauce identifier
	 * @return True if sauce exists in the factory
	 */
	public boolean buildSauce(String id) {
		if (pizza == null) {
			pizza = new Pizza();
		}
		PizzaSauce sauce = sauces.get(id);
		if (sauce == null) {
			return false;
		}
		pizza.setSauce(sauce);
		return true;
	}

	/**
	 * Build meat topping
	 * 
	 * @param id
	 *            Meat topping identifier
	 * @return True if topping exists in the factory
	 */
	public boolean buildToppingMeat(String id) {
		if (pizza == null) {
			pizza = new Pizza();
		}
		PizzaToppingMeat topping = meats.get(id);
		if (topping == null) {
			return false;
		}
		pizza.addToppingMeat(topping);
		return true;
	}

	/**
	 * Build vegetable topping
	 * 
	 * @param id
	 *            Vegetable topping identifier
	 * @return True if topping exists in the factory
	 */
	public boolean buildToppingVeg(String id) {
		if (pizza == null) {
			pizza = new Pizza();
		}
		PizzaToppingVeg topping = vegs.get(id);
		if (topping == null) {
			return false;
		}
		pizza.addToppingVeg(topping);
		return true;
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
			ArrayList<String> toppingmeats, ArrayList<String> toppingvegs) {
		Pizza pizza = new Pizza();

		PizzaCrust pizzaCrust = new PizzaCrust();
		pizza.setCurst(pizzaCrust);

		PizzaCheese pizzaCheese = new PizzaCheese();
		pizza.setCheese(pizzaCheese);

		PizzaSauce pizzaSauce = new PizzaSauce();
		pizza.setSauce(pizzaSauce);

		for (String topping : toppingmeats) {
			PizzaToppingMeat meat = new PizzaToppingMeat();
			pizza.addToppingMeat(meat);
		}
		for (String topping : toppingvegs) {
			PizzaToppingVeg veg = new PizzaToppingVeg();
			pizza.addToppingVeg(veg);
		}

		return pizza;
	}
}