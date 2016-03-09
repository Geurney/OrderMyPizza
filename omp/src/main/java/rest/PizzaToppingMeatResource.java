/**
 * 
 */
package rest;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import pizza.PizzaToppingMeat;
import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Pizza Meat Topping REST service
 * 
 * @author Geurney
 *
 */
public class PizzaToppingMeatResource implements
		PizzaComponentResourceInterface<PizzaToppingMeat> {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Pizza Component Identifier
	 */
	String identifier;

	/**
	 * Pizza Topping Meat Resource Constructor
	 * 
	 * @param uriInfo
	 *            uriInfo
	 * @param request
	 *            request
	 * @param identifier
	 *            Pizza Component Identifier
	 */
	public PizzaToppingMeatResource(UriInfo uriInfo, Request request,
			String identifier) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.identifier = identifier;
	}

	/**
	 * Get Pizza Topping Meat in datastore. This operation fails on following
	 * conditions:1.the PizzaFactory is not found. 2.Pizza Topping Meat are
	 * empty in the PizzaFactory 3.This Pizza Topping Meat is not found.
	 * 
	 * @return Pizza Topping Meat
	 */
	@Override
	public PizzaToppingMeat getPizzaComponent() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return findPizzaComponent(hash_uid);
	}

	/**
	 * Find Pizza Topping Meat in datastore with token for curl. This operation
	 * fails on following conditions:1.the PizzaFactory is not found. 2.Pizza
	 * Topping Meats are empty in the PizzaFactory 3.This Pizza Topping Meat is
	 * not found.
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @return Pizza Topping Meat
	 */
	@Override
	public PizzaToppingMeat getPizzaComponent(@PathParam("token") String token) {
		return findPizzaComponent(token);
	}

	/**
	 * 
	 * Update the Pizza Topping Meat in PizzaFactory with form. This operation
	 * fails on following conditions: 1.the PizzaFactory is not found. 2.Pizza
	 * Topping Meats are empty in the PizzaFactory 3.This Pizza Topping Meat is
	 * not found. 4.New identifier(different from current) already exists. 5.
	 * Original costs/prices are not set.
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @param newIdentifier
	 *            PizzaComponent identifier. Should be unique.
	 * @param description
	 *            PizzaComponent description
	 * @param cost1
	 *            PizzaComponent small size cost. Should not be negative
	 * @param price1
	 *            PizzaComponent small size price. Should not be negative
	 * @param cost2
	 *            PizzaComponent medium size cost. Should not be negative
	 * @param price2
	 *            PizzaComponent medium size price. Should not be negative
	 * @param cost3
	 *            PizzaComponent large size cost. Should not be negative
	 * @param price3
	 *            PizzaComponent large size price. Should not be negative
	 * 
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void putPizzaComponent(@FormParam("identifier") String identifier,
			@FormParam("description") String description,
			@FormParam("cost1") String cost1,
			@FormParam("price1") String price1,
			@FormParam("cost2") String cost2,
			@FormParam("price2") String price2,
			@FormParam("cost3") String cost3, @FormParam("price3") String price3) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		updatePizzaComponent(hash_uid, identifier, description, cost1, price1,
				cost2, price2, cost3, price3);
	}

	/**
	 * 
	 * Update the Pizza Topping Meat in PizzaFactory with form with token for
	 * curl. This operation fails on following conditions: 1.the PizzaFactory is
	 * not found. 2.Pizza Topping Meats are empty in the PizzaFactory 3.This
	 * Pizza Topping Meat is not found. 4.New identifier(different from current)
	 * already exists. 5. Original costs/prices are not set.
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @param newIdentifier
	 *            PizzaComponent identifier. Should be unique.
	 * @param description
	 *            PizzaComponent description
	 * @param cost1
	 *            PizzaComponent small size cost. Should not be negative
	 * @param price1
	 *            PizzaComponent small size price. Should not be negative
	 * @param cost2
	 *            PizzaComponent medium size cost. Should not be negative
	 * @param price2
	 *            PizzaComponent medium size price. Should not be negative
	 * @param cost3
	 *            PizzaComponent large size cost. Should not be negative
	 * @param price3
	 *            PizzaComponent large size price. Should not be negative
	 * 
	 */
	@Path("/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void putPizzaComponentToken(@PathParam("token") String token,
			@FormParam("identifier") String identifier,
			@FormParam("description") String description,
			@FormParam("cost1") String cost1,
			@FormParam("price1") String price1,
			@FormParam("cost2") String cost2,
			@FormParam("price2") String price2,
			@FormParam("cost3") String cost3, @FormParam("price3") String price3) {

		updatePizzaComponent(token, identifier, description, cost1, price1,
				cost2, price2, cost3, price3);
	}

	/**
	 * Update the Pizza Topping Meat in Pizza Factory with JSON
	 * 
	 * @param component
	 *            Pizza Topping Meat
	 */
	@Override
	public void putPizzaComponent(PizzaToppingMeat component) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		double[] costs = component.getCosts();
		double[] prices = component.getPrices();
		updatePizzaComponent(hash_uid, component.getIdentifier(),
				component.getDescription(), String.valueOf(costs[0]),
				String.valueOf(prices[0]), String.valueOf(costs[1]),
				String.valueOf(prices[1]), String.valueOf(costs[2]),
				String.valueOf(prices[2]));
	}

	/**
	 * Update the Pizza Topping Meat in PizzaFactory with JSON with token for
	 * curl
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @param component
	 *            Pizza Topping Meat
	 */
	@Override
	public void putPizzaComponent(@PathParam("token") String token,
			PizzaToppingMeat component) {
		double[] costs = component.getCosts();
		double[] prices = component.getPrices();
		updatePizzaComponent(token, component.getIdentifier(),
				component.getDescription(), String.valueOf(costs[0]),
				String.valueOf(prices[0]), String.valueOf(costs[1]),
				String.valueOf(prices[1]), String.valueOf(costs[2]),
				String.valueOf(prices[2]));
	}

	/**
	 * Delete Pizza Topping Meat
	 * 
	 */
	@Override
	public void deletePizzaComponent() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		removePizzaComponent(hash_uid);
	}

	/**
	 * Delete Pizza Topping Meat with token for curl
	 * 
	 * @param token
	 *            Pizza Factory token
	 */
	@Override
	public void deletePizzaComponentToken(@PathParam("token") String token) {
		removePizzaComponent(token);
	}

	/**
	 * Find Pizza Topping Meat in datastore. This operation fails on following
	 * conditions:1.the PizzaFactory is not found. 2.Pizza Topping Meats are
	 * empty in the PizzaFactory 3.This Pizza Topping Meat is not found.
	 * 
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @return PizzaToppingMeat
	 */
	@SuppressWarnings("unchecked")
	private PizzaToppingMeat findPizzaComponent(String token) {
		if (token == null) {
			return null;
		}
		PizzaToppingMeat component = null;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("meat");
			if (list == null) {
				return null;
			}
			for (EmbeddedEntity e : list) {
				String id = (String) e.getProperty("identifier");
				if (id != null && id.equals(identifier)) {
					component = entityToObject(e);
					break;
				}
			}
		} catch (EntityNotFoundException e) {
		}
		return component;
	}

	/**
	 * Update the Pizza Topping Meat in PizzaFactory. This operation fails on
	 * following conditions: 1.the PizzaFactory is not found. 2.Pizza Topping
	 * Meats are empty in the PizzaFactory 3.This Pizza Topping Meat is not
	 * found. 4.New identifier(different from current) already exists. 5.
	 * Original costs/prices are not set.
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @param newIdentifier
	 *            PizzaComponent identifier. Should be unique.
	 * @param description
	 *            PizzaComponent description
	 * @param cost1
	 *            PizzaComponent small size cost. Should not be negative
	 * @param price1
	 *            PizzaComponent small size price. Should not be negative
	 * @param cost2
	 *            PizzaComponent medium size cost. Should not be negative
	 * @param price2
	 *            PizzaComponent medium size price. Should not be negative
	 * @param cost3
	 *            PizzaComponent large size cost. Should not be negative
	 * @param price3
	 *            PizzaComponent large size price. Should not be negative
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void updatePizzaComponent(String token, String newIdentifier,
			String description, String cost1, String price1, String cost2,
			String price2, String cost3, String price3) {
		if (token == null) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("meat");
			if (list == null) {
				return;
			}
			for (EmbeddedEntity e : list) {
				String id = (String) e.getProperty("identifier");
				if (id != null && id.equals(identifier)) {
					if (newIdentifier != null
							&& !newIdentifier.equals(identifier)) {
						for (EmbeddedEntity f : list) {
							id = (String) f.getProperty("identifier");
							if (id != null && id.equals(newIdentifier)) {
								return;
							}
						}
						e.setProperty("identifier", newIdentifier);
					}
					if (description != null) {
						e.setProperty("description", description);
					}
					List<Double> costs = (List<Double>) e.getProperty("costs");
					if (costs == null || costs.size() != 3) {
						return;
					}
					if (cost1 != null && Double.valueOf(cost1) >= 0) {
						costs.set(0, Double.valueOf(cost1));
					}
					if (cost2 != null && Double.valueOf(cost2) >= 0) {
						costs.set(1, Double.valueOf(cost2));
					}
					if (cost3 != null && Double.valueOf(cost3) >= 0) {
						costs.set(2, Double.valueOf(cost3));
					}
					List<Double> prices = (List<Double>) e
							.getProperty("prices");
					if (prices == null || prices.size() != 3) {
						return;
					}
					if (price1 != null && Double.valueOf(price1) >= 0) {
						prices.set(0, Double.valueOf(price1));
					}
					if (price2 != null && Double.valueOf(price2) >= 0) {
						prices.set(1, Double.valueOf(price2));
					}
					if (price3 != null && Double.valueOf(price3) >= 0) {
						prices.set(2, Double.valueOf(price3));
					}
					pizzaFactory.setProperty("meat", list);
					datastore.put(pizzaFactory);
				}
			}
		} catch (EntityNotFoundException e) {
		}
	}

	/**
	 * Delete Pizza Topping Meat
	 * 
	 * @param token
	 *            Pizza Factory token
	 */
	@SuppressWarnings("unchecked")
	private void removePizzaComponent(String token) {
		if (token == null) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("meat");
			if (list == null) {
				return;
			}
			Iterator<EmbeddedEntity> iterator = list.iterator();
			while (iterator.hasNext()) {
				String id = (String) iterator.next().getProperty("identifier");
				if (id != null && id.equals(identifier)) {
					iterator.remove();
					pizzaFactory.setProperty("meat", list);
					datastore.put(pizzaFactory);
					break;
				}
			}
		} catch (EntityNotFoundException e) {
		}
	}

	/**
	 * Convert entity to Pizza Topping Meat
	 * 
	 * @param entity
	 *            Entity
	 * @return Pizza Topping Meat
	 */
	@SuppressWarnings("unchecked")
	public static PizzaToppingMeat entityToObject(EmbeddedEntity entity) {
		if (entity == null) {
			return null;
		}
		PizzaToppingMeat component = new PizzaToppingMeat();
		component.setIdentifier((String) entity.getProperty("identifier"));
		component.setDescription((String) entity.getProperty("description"));
		List<Double> costs = (List<Double>) entity.getProperty("costs");
		List<Double> prices = (List<Double>) entity.getProperty("prices");
		component.setCosts(costs);
		component.setPrices(prices);
		return component;
	}

}
