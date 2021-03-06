package pizzafactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import pizza.Pizza;
import pizza.cheese.PizzaCheese;
import pizza.crust.PizzaCrust;
import pizza.meat.PizzaToppingMeat;
import pizza.sauce.PizzaSauce;
import pizza.veg.PizzaToppingVeg;

/**
 * Pizza Factory to create pizza
 * 
 * @author Geurney
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class PizzaFactory {
	/**
	 * Pizza Factory identifier, same as its Pizza Shop
	 */
	private String identifier;

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
	 * Get identifier
	 * 
	 * @return the identifier
	 */
	@XmlElement
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Set identifier
	 * 
	 * @param identifier
	 *            the identifier to set
	 */

	synchronized public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Get list of crusts
	 * 
	 * @return crusts
	 */
	@XmlElement
	public List<PizzaCrust> getCrusts() {
		List<PizzaCrust> list = new ArrayList<PizzaCrust>();
		list.addAll(crusts.values());
		return list;
	}

	/**
	 * Add a crust if the identifier not exist in the factory.
	 * 
	 * @param crust
	 *            Crust to be added
	 * @return True if crust does not exist. False if the crust exists.
	 */
	synchronized public boolean addCrust(PizzaCrust crust) {
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
	synchronized public void putCrust(PizzaCrust crust) {
		crusts.put(crust.getIdentifier(), crust);
	}

	/**
	 * Add list of crusts
	 * 
	 * @param crustList
	 *            Crusts to be added
	 */
	synchronized public void addCrust(List<PizzaCrust> crustList) {
		for (PizzaCrust s : crustList) {
			addCrust(s);
		}
	}

	/**
	 * Get list of cheeses
	 * 
	 * @return cheeses
	 */
	@XmlElement
	public List<PizzaCheese> getCheeses() {
		List<PizzaCheese> list = new ArrayList<PizzaCheese>();
		list.addAll(cheeses.values());
		return list;
	}

	/**
	 * Add a cheese if the identifier not exist in the factory.
	 * 
	 * @param cheese
	 *            Cheese to be added
	 * @return True if cheese does not exist. False if the cheese exists.
	 */
	synchronized public boolean addCheese(PizzaCheese cheese) {
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
	synchronized public void putCheese(PizzaCheese cheese) {
		cheeses.put(cheese.getIdentifier(), cheese);
	}

	/**
	 * Add list of cheeses
	 * 
	 * @param cheeseList
	 *            cheeses to be added
	 */
	synchronized public void addCheese(List<PizzaCheese> cheeseList) {
		for (PizzaCheese s : cheeseList) {
			addCheese(s);
		}
	}

	/**
	 * Get list of sauces
	 * 
	 * @return sauces
	 */
	@XmlElement
	public List<PizzaSauce> getSauces() {
		List<PizzaSauce> list = new ArrayList<PizzaSauce>();
		list.addAll(sauces.values());
		return list;
	}

	/**
	 * Add a sauce if the identifier not exist in the factory.
	 * 
	 * @param sauce
	 *            sauce to be added
	 * @return True if sauce does not exist. False if the sauce exists.
	 */
	synchronized public boolean addSauce(PizzaSauce sauce) {
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
	synchronized public void putSauce(PizzaSauce sauce) {
		sauces.put(sauce.getIdentifier(), sauce);
	}

	/**
	 * Add list of sauces
	 * 
	 * @param sauceList
	 *            Sauces to be added
	 */
	synchronized public void addSauce(List<PizzaSauce> sauceList) {
		for (PizzaSauce s : sauceList) {
			addSauce(s);
		}
	}

	/**
	 * Get list of meat toppings
	 * 
	 * @return meat toppings
	 */
	@XmlElement
	public List<PizzaToppingMeat> getMeats() {
		List<PizzaToppingMeat> list = new ArrayList<PizzaToppingMeat>();
		list.addAll(meats.values());
		return list;
	}

	/**
	 * Get list of vegetable toppings
	 * 
	 * @return vegetable toppings
	 */
	@XmlElement
	public List<PizzaToppingVeg> getVegs() {
		List<PizzaToppingVeg> list = new ArrayList<PizzaToppingVeg>();
		list.addAll(vegs.values());
		return list;
	}

	/**
	 * Add a meat topping if the identifier not exist in the factory.
	 * 
	 * @param topping
	 *            topping to be added
	 * @return True if topping does not exist. False if the topping exists.
	 */
	synchronized public boolean addToppingMeat(PizzaToppingMeat topping) {
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
	synchronized public void putToppingMeat(PizzaToppingMeat topping) {
		meats.put(topping.getIdentifier(), topping);
	}

	/**
	 * Add list of meat toppings
	 * 
	 * @param meatList
	 *            Meat Toppings to be added
	 */
	synchronized public void addToppingMeat(List<PizzaToppingMeat> meatList) {
		for (PizzaToppingMeat s : meatList) {
			addToppingMeat(s);
		}
	}

	/**
	 * Add a vegetable topping if the identifier not exist in the factory.
	 * 
	 * @param topping
	 *            topping to be added
	 * @return True if topping does not exist. False if the topping exists.
	 */
	synchronized public boolean addToppingVeg(PizzaToppingVeg topping) {
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
	synchronized public void putToppingVeg(PizzaToppingVeg topping) {
		vegs.put(topping.getIdentifier(), topping);
	}

	/**
	 * Add list of vegetable toppings
	 * 
	 * @param vegList
	 *            Vetegable toppings to be added
	 */
	synchronized public void addToppingVeg(List<PizzaToppingVeg> vegList) {
		for (PizzaToppingVeg s : vegList) {
			addToppingVeg(s);
		}
	}

	/**
	 * Prepare a raw pizza
	 * 
	 * @param size
	 *            Pizza Size: small, medium, large
	 * @return pizza
	 */
	public Pizza preparePizza(String size) {
		Pizza pizza = new Pizza();
		pizza.setSize(size);
		return pizza;
	}

	/**
	 * Build Crust
	 * 
	 * @param id
	 *            Crust identifier
	 * @return True if crust exists in the factory.
	 */
	public boolean buildCrust(String id, Pizza pizza) {
		if (pizza == null) {
			return false;
		}
		if (id == null) {
			return true;
		}
		PizzaCrust crust = crusts.get(id);
		if (crust == null) {
			return false;
		}
		crust.setSize(pizza.getSize());
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
	public boolean buildCheese(String id, Pizza pizza) {
		if (pizza == null) {
			return false;
		}
		if (id == null) {
			return true;
		}
		PizzaCheese cheese = cheeses.get(id);
		if (cheese == null) {
			return false;
		}
		cheese.setSize(pizza.getSize());
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
	public boolean buildSauce(String id, Pizza pizza) {
		if (pizza == null) {
			return false;
		}
		if (id == null) {
			return true;
		}
		PizzaSauce sauce = sauces.get(id);
		if (sauce == null) {
			return false;
		}
		sauce.setSize(pizza.getSize());
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
	public boolean buildToppingMeat(String id, Pizza pizza) {
		if (pizza == null) {
			return false;
		}
		if (id == null) {
			return true;
		}
		PizzaToppingMeat topping = meats.get(id);
		if (topping == null) {
			return false;
		}
		topping.setSize(pizza.getSize());
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
	public boolean buildToppingVeg(String id, Pizza pizza) {
		if (pizza == null) {
			return false;
		}
		if (id == null) {
			return true;
		}
		PizzaToppingVeg topping = vegs.get(id);
		if (topping == null) {
			return false;
		}
		topping.setSize(pizza.getSize());
		pizza.addToppingVeg(topping);
		return true;
	}

	/**
	 * Build meat toppings
	 * 
	 * @param meats
	 *            meat toppings
	 */
	public void buildToppingMeat(List<String> meats, Pizza pizza) {
		if (meats == null) {
			return;
		}
		for (String meat : meats) {
			buildToppingMeat(meat, pizza);
		}
	}

	/**
	 * Build vegetable toppings
	 * 
	 * @param vegs
	 *            vegetable toppings
	 */
	public void buildToppingVeg(List<String> vegs, Pizza pizza) {
		if (vegs == null) {
			return;
		}
		for (String veg : vegs) {
			buildToppingMeat(veg, pizza);
		}
	}

	/**
	 * Check if contains this crust
	 * 
	 * @param crust
	 *            crust identifier
	 * @return True if contains
	 */
	public boolean hasCrust(String crust) {
		if (crust == null) {
			return false;
		}
		return crusts.containsKey(crust);
	}

	/**
	 * Check if contains this cheese
	 * 
	 * @param cheese
	 *            cheese identifier
	 * @return True if contains
	 */
	public boolean hasCheese(String cheese) {
		if (cheese == null) {
			return false;
		}
		return cheeses.containsKey(cheese);
	}

	/**
	 * Check if contains this sauce
	 * 
	 * @param sauce
	 *            sauce identifier
	 * @return True if contains
	 */
	public boolean hasSauce(String sauce) {
		if (sauce == null) {
			return false;
		}
		return sauces.containsKey(sauce);
	}

	/**
	 * Check if contains this meat topping
	 * 
	 * @param meat
	 *            meat topping identifier
	 * @return True if contains
	 */
	public boolean hasMeat(String meat) {
		if (meat == null) {
			return false;
		}
		return meats.containsKey(meat);
	}

	/**
	 * Check if contains this vegetable topping
	 * 
	 * @param veg
	 *            vegetable topping identifier
	 * @return True if contains
	 */
	public boolean hasVeg(String veg) {
		if (veg == null) {
			return false;
		}
		return vegs.containsKey(veg);
	}

}