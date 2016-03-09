/**
 * 
 */
package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import pizza.PizzaSauce;
import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Pizza Sauces REST service
 * 
 * @author Geurney
 *
 */
public class PizzaSaucesResource implements
		PizzaComponentsResourceInterface<PizzaSauce> {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Get the Pizza Sauces
	 * 
	 * @return Pizza Sauces
	 */
	@Override
	public List<PizzaSauce> getComponents() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return findPizzaComponents(hash_uid);
	}

	/**
	 * Get the Pizza Sauces with token
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @return Pizza Sauces
	 */
	@Override
	public List<PizzaSauce> getComponents(@PathParam("token") String token) {
		return findPizzaComponents(token);
	}

	/**
	 * Add the Pizza Sauce into PizzaFactory. This operation fails on following
	 * conditions:1.Token is null 2.Parameters are missing 3.costs/prices are
	 * negative 4.Identifier already exists
	 * 
	 * @param token
	 *            Pizza Factory token. Should not be null
	 * @param newIdentifier
	 *            PizzaComponent identifier. Should be unique.
	 * @param description
	 *            PizzaComponent description. Should not be null
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
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void postPizzaComponent(
			@FormParam("identifier") String newIdentifier,
			@FormParam("description") String description,
			@FormParam("cost1") String cost1,
			@FormParam("price1") String price1,
			@FormParam("cost2") String cost2,
			@FormParam("price2") String price2,
			@FormParam("cost3") String cost3, @FormParam("price3") String price3) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		newPizzaComponent(hash_uid, newIdentifier, description, cost1, price1,
				cost2, price2, cost3, price3);
	}

	/**
	 * Add the Pizza Sauce into PizzaFactory. This operation fails on following
	 * conditions:1.Token is null 2.Parameters are missing 3.costs/prices are
	 * negative 4.Identifier already exists
	 * 
	 * @param token
	 *            Pizza Factory token. Should not be null
	 * @param newIdentifier
	 *            PizzaComponent identifier. Should be unique.
	 * @param description
	 *            PizzaComponent description. Should not be null
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
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void postPizzaComponent(@PathParam("token") String token,
			@FormParam("identifier") String newIdentifier,
			@FormParam("description") String description,
			@FormParam("cost1") String cost1,
			@FormParam("price1") String price1,
			@FormParam("cost2") String cost2,
			@FormParam("price2") String price2,
			@FormParam("cost3") String cost3, @FormParam("price3") String price3) {
		newPizzaComponent(token, newIdentifier, description, cost1, price1,
				cost2, price2, cost3, price3);
	}

	/**
	 * Add the Pizza Sauce into PizzaFactory with JSON. This operation fails on
	 * following conditions:1.Token is null 2.Parameters are missing
	 * 3.costs/prices are negative 4.Identifier already exists
	 * 
	 * @param component
	 *            PizzaSauce
	 */
	@Override
	public void postPizzaComponent(PizzaSauce component) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		double[] costs = component.getCosts();
		double[] prices = component.getPrices();
		newPizzaComponent(hash_uid, component.getIdentifier(),
				component.getDescription(), String.valueOf(costs[0]),
				String.valueOf(prices[0]), String.valueOf(costs[1]),
				String.valueOf(prices[1]), String.valueOf(costs[2]),
				String.valueOf(prices[2]));
	}

	/**
	 * Add the Pizza Sauce into PizzaFactory with JSON with token for curl. This
	 * operation fails on following conditions:1.Token is null 2.Parameters are
	 * missing 3.costs/prices are negative 4.Identifier already exists
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @param component
	 *            PizzaSauce
	 */
	@Override
	public void postPizzaComponent(@PathParam("token") String token,
			PizzaSauce component) {
		double[] costs = component.getCosts();
		double[] prices = component.getPrices();
		newPizzaComponent(token, component.getIdentifier(),
				component.getDescription(), String.valueOf(costs[0]),
				String.valueOf(prices[0]), String.valueOf(costs[1]),
				String.valueOf(prices[1]), String.valueOf(costs[2]),
				String.valueOf(prices[2]));
	}

	/**
	 * Delete All Pizza Sauces
	 * 
	 */
	@Override
	public void deletePizzaComponents() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		removePizzaComponents(hash_uid);
	}

	/**
	 * Delete All Pizza Sauces with token
	 * 
	 * @param token
	 *            Pizza Factory token
	 */
	@Override
	public void deletePizzaComponents(@PathParam("token") String token) {
		removePizzaComponents(token);
	}

	/**
	 * For specific pizza sauce
	 * 
	 */
	@Path("{identifier}")
	public PizzaSauceResource handlePizzaSauce(
			@PathParam("identifier") String identifier) {
		return new PizzaSauceResource(uriInfo, request, identifier);
	}

	/**
	 * Get the Pizza Sauces
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @return Pizza Sauces
	 */
	@SuppressWarnings("unchecked")
	private List<PizzaSauce> findPizzaComponents(String token) {
		if (token == null) {
			return null;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		List<PizzaSauce> components = null;
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("sauce");
			components = new ArrayList<PizzaSauce>();
			if (list != null) {
				for (EmbeddedEntity e : list) {
					PizzaSauce component = PizzaSauceResource.entityToObject(e);
					components.add(component);
				}
			}
		} catch (EntityNotFoundException e) {
		}
		return components;
	}

	/**
	 * Add the Pizza Sauce into PizzaFactory. This operation fails on following
	 * conditions:1.Token is null 2.Parameters are missing 3.costs/prices are
	 * negative 4.Identifier already exists
	 * 
	 * @param token
	 *            Pizza Factory token. Should not be null
	 * @param newIdentifier
	 *            PizzaComponent identifier. Should be unique.
	 * @param description
	 *            PizzaComponent description. Should not be null
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
	private void newPizzaComponent(String token, String identifier,
			String description, String cost1, String price1, String cost2,
			String price2, String cost3, String price3) {
		if (token == null) {
			return;
		}
		if (identifier == null || description == null || cost1 == null
				|| cost2 == null || cost3 == null || price1 == null
				|| price2 == null || price3 == null) {
			return;
		}
		double ct1 = Double.valueOf(cost1);
		double ct2 = Double.valueOf(cost2);
		double ct3 = Double.valueOf(cost3);
		double pc1 = Double.valueOf(price1);
		double pc2 = Double.valueOf(price2);
		double pc3 = Double.valueOf(price3);
		if (ct1 < 0 || ct2 < 0 || ct3 < 0 || pc1 < 0 || pc2 < 0 || pc3 < 0) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		Entity pizzaFactory = null;
		try {
			pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("sauce");
			if (list == null) {
				list = new ArrayList<EmbeddedEntity>();
			} else {
				for (EmbeddedEntity e : list) {
					String id = (String) e.getProperty("identifier");
					if (id != null && id.equals(identifier)) {
						return;
					}
				}
			}
			List<Double> costs = new ArrayList<Double>();
			costs.add(ct1);
			costs.add(ct2);
			costs.add(ct3);

			List<Double> prices = new ArrayList<Double>();
			prices.add(pc1);
			prices.add(pc2);
			prices.add(pc3);

			EmbeddedEntity component = new EmbeddedEntity();
			component.setProperty("identifier", identifier);
			component.setProperty("description", description);
			component.setProperty("costs", costs);
			component.setProperty("prices", prices);

			list.add(component);
			pizzaFactory.setProperty("sauce", list);
			datastore.put(pizzaFactory);
		} catch (EntityNotFoundException e) {
		}
	}

	/**
	 * Delete Pizza Component
	 * 
	 * @param token
	 *            Pizza Factory token
	 */
	private void removePizzaComponents(String token) {
		if (token == null) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		try {
			Entity pizzaFactory = datastore.get(key);
			pizzaFactory.removeProperty("sauce");
		} catch (Exception e) {
		}
	}
}
