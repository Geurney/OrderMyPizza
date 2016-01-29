package ordermypizza;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PizzaShop {
	private String shopName;
	private String shopAddr;
	private String shopPhone;

	public PizzaShop() {
	}

	public PizzaShop(String shopName, String shopAddr, String shopPhone) {
		this.shopName = shopName;
		this.shopAddr = shopAddr;
		this.shopPhone = shopPhone;
	}

	/**
	 * @return the shopName
	 */
	public String getshopName() {
		return shopName;
	}

	/**
	 * @param shopName
	 *            the shopName to set
	 */
	public void setshopName(String shopName) {
		this.shopName = shopName;
	}

	/**
	 * @return the shopAddr
	 */
	public String getshopAddr() {
		return shopAddr;
	}

	/**
	 * @param shopAddr
	 *            the shopAddr to set
	 */
	public void setshopAddr(String shopAddr) {
		this.shopAddr = shopAddr;
	}

	/**
	 * @return the shopPhone
	 */
	public String getshopPhone() {
		return shopPhone;
	}

	/**
	 * @param shopPhone
	 *            the shopPhone to set
	 */
	public void setshopPhone(String shopPhone) {
		this.shopPhone = shopPhone;
	}

}