/**
 * 
 */
package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import pizza.PizzaCrust;
import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Pizza Crusts REST service
 * 
 * @author Geurney
 *
 */
public class PizzaCrustsResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Get the Pizza Crusts
	 * 
	 * @return Pizza Crusts
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public List<PizzaCrust> getPizzaCrusts() {
		List<PizzaCrust> pizzaCrusts = new ArrayList<PizzaCrust>();
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			return null;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("crust");
			if (crusts != null) {
				for (EmbeddedEntity e : crusts) {
					PizzaCrust crust = new PizzaCrust();
					crust.setIdentifier((String) e.getProperty("identifier"));
					crust.setDescription((String) e.getProperty("description"));
					List<Double> costs = (List<Double>) e.getProperty("costs");
					List<Double> prices = (List<Double>) e
							.getProperty("prices");
					crust.setCosts(costs);
					crust.setPrices(prices);
					pizzaCrusts.add(crust);
				}
			}
		} catch (EntityNotFoundException e) {
		}
		return pizzaCrusts;
	}

	/**
	 * Add/Update the Pizza Crust into PizzaFactory with form
	 * 
	 * @param identifier
	 *            PizzaCrust identifier
	 * @param description
	 *            PizzaCrust
	 * @param cost1
	 *            PizzaCrust cost1
	 * @param price1
	 *            PizzaCrust price1
	 * @param cost2
	 *            PizzaCrust cost2
	 * @param price2
	 *            PizzaCrust price2
	 * @param cost3
	 *            PizzaCrust cost3
	 * @param price3
	 *            PizzaCrust price3
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newPostPizzaCrustXML(
			@FormParam("identifier") String identifier,
			@FormParam("description") String description,
			@FormParam("cost1") String cost1,
			@FormParam("price1") String price1,
			@FormParam("cost2") String cost2,
			@FormParam("price2") String price2,
			@FormParam("cost3") String cost3, @FormParam("price3") String price3) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
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
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		Entity pizzaFactory = null;

		try {
			pizzaFactory = datastore.get(key);
		} catch (EntityNotFoundException e) {
			pizzaFactory = new Entity("PizzaFactory", hash_uid);
		} finally {
			List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("crust");
			if (crusts != null) {
				for (EmbeddedEntity e : crusts) {
					if (e.getProperty("identifier").equals(identifier)) {
						return;
					}
				}
			} else {
				crusts = new ArrayList<EmbeddedEntity>();
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

			crusts.add(component);
			pizzaFactory.setProperty("crust", crusts);
			datastore.put(pizzaFactory);
		}
	}

	/**
	 * Add/Update the Pizza Crust into PizzaFactory with JSON
	 * 
	 * @param pizzaCrust
	 *            PizzaCrust
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void newPostPizzaCrustJSON(PizzaCrust pizzaCrust) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			return;
		}
		String identifier = pizzaCrust.getIdentifier();
		String description = pizzaCrust.getDescription();
		double[] costs = pizzaCrust.getCosts();
		double[] prices = pizzaCrust.getPrices();

		if (identifier == null || description == null || costs[0] < 0
				|| costs[1] < 0 || costs[2] < 0 || prices[0] < 0
				|| prices[1] < 0 || prices[2] < 0) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		Entity pizzaFactory = null;

		try {
			pizzaFactory = datastore.get(key);
		} catch (EntityNotFoundException e) {
			pizzaFactory = new Entity("PizzaFactory", hash_uid);
		} finally {
			List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("crust");
			if (crusts != null) {
				for (EmbeddedEntity e : crusts) {
					if (e.getProperty("identifier").equals(identifier)) {
						return;
					}
				}
			} else {
				crusts = new ArrayList<EmbeddedEntity>();
			}
			List<Double> costs_list = new ArrayList<Double>();
			costs_list.add(costs[0]);
			costs_list.add(costs[1]);
			costs_list.add(costs[2]);

			List<Double> prices_list = new ArrayList<Double>();
			prices_list.add(prices[0]);
			prices_list.add(prices[1]);
			prices_list.add(prices[2]);

			EmbeddedEntity component = new EmbeddedEntity();
			component.setProperty("identifier", identifier);
			component.setProperty("description", description);
			component.setProperty("costs", costs_list);
			component.setProperty("prices", prices_list);

			crusts.add(component);
			pizzaFactory.setProperty("crust", crusts);
			datastore.put(pizzaFactory);
		}
	}

	/**
	 * Delete All Pizza Crusts
	 * 
	 */
	@DELETE
	public void deletePizzaCrusts() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
			Entity pizzaFactory = datastore.get(key);
			pizzaFactory.removeProperty("crust");
		} catch (Exception e) {
		}
	}

	/**
	 * For specific pizza crust
	 * 
	 */
	@Path("{identifier}")
	public PizzaCrustResource handlePizzaCrust(
			@PathParam("identifier") String identifier) {
		return new PizzaCrustResource(uriInfo, request, identifier);
	}

}
