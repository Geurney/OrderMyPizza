package ordermypizza;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PizzaShop {
	private String shopName;
	private String shopAddr;
	private String shopPhone;
	private String shopPswd;
    private PizzaFactory pizzaFactory; // May be a string to xml.
    
	public PizzaShop() {
	}
	
	public PizzaShop(String shopName, String shopAddr, String shopPhone, String shopPswd) {
		this.shopName = shopName;
		this.shopAddr = shopAddr;
		this.shopPhone = shopPhone;
		this.shopPswd = shopPswd;
	}

	/**
	 * @return the shopName
	 */
	public String getShopName() {
		return shopName;
	}

	/**
	 * @param shopName the shopName to set
	 */
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	/**
	 * @return the shopAddr
	 */
	public String getShopAddr() {
		return shopAddr;
	}

	/**
	 * @param shopAddr the shopAddr to set
	 */
	public void setShopAddr(String shopAddr) {
		this.shopAddr = shopAddr;
	}

	/**
	 * @return the shopPhone
	 */
	public String getShopPhone() {
		return shopPhone;
	}

	/**
	 * @param shopPhone the shopPhone to set
	 */
	public void setShopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
	}

	/**
	 * @return the shopPswd
	 */
	public String getShopPswd() {
		return shopPswd;
	}

	/**
	 * @param shopPswd the shopPswd to set
	 */
	public void setShopPswd(String shopPswd) {
		this.shopPswd = shopPswd;
	}

	/**
	 * @return the pizzaFactory
	 */
	public PizzaFactory getPizzaFactory() {
		return pizzaFactory;
	}

	/**
	 * @param pizzaFactory the pizzaFactory to set
	 */
	public void setPizzaFactory(PizzaFactory pizzaFactory) {
		this.pizzaFactory = pizzaFactory;
	}

}