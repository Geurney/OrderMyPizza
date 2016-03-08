/**
 * 
 */
package rest;

import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
 * Pizza Crust REST service
 * 
 * @author Geurney
 *
 */
public class PizzaCrustResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	String identifier;

	/**
	 * Get the Pizza Crust
	 * 
	 * @return Pizza Crust
	 */
	public PizzaCrustResource(UriInfo uriInfo, Request request,
			String identifier) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.identifier = identifier;
	}

	/**
	 * Get Pizza Crust
	 * 
	 * @return PizzaCrust
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public PizzaCrust getPizzaCrust() {
		PizzaCrust crust = null;
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
					if (e.getProperty("identifier").equals(identifier)) {
						crust = new PizzaCrust();
						crust.setIdentifier((String) e
								.getProperty("identifier"));
						crust.setDescription((String) e
								.getProperty("description"));
						List<Double> costs = (List<Double>) e
								.getProperty("costs");
						List<Double> prices = (List<Double>) e
								.getProperty("prices");
						crust.setCosts(costs);
						crust.setPrices(prices);
						break;
					}
				}
			}
		} catch (EntityNotFoundException e) {
		}
		return crust;
	}

	/**
	 * Get Pizza Crust with token
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @return PizzaCrust
	 */
	@SuppressWarnings("unchecked")
	@Path("/findbytoken/{token}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public PizzaCrust getPizzaCrust(@PathParam("token") String token) {
		PizzaCrust crust = null;
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
					if (e.getProperty("identifier").equals(identifier)) {
						crust = new PizzaCrust();
						crust.setIdentifier((String) e
								.getProperty("identifier"));
						crust.setDescription((String) e
								.getProperty("description"));
						List<Double> costs = (List<Double>) e
								.getProperty("costs");
						List<Double> prices = (List<Double>) e
								.getProperty("prices");
						crust.setCosts(costs);
						crust.setPrices(prices);
						break;
					}
				}
			}
		} catch (EntityNotFoundException e) {
		}
		return crust;
	}

	/**
	 * Update the Pizza Crust into PizzaFactory with form
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
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void updatePizzaCrust(@FormParam("identifier") String identifier,
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
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("crust");
			if (crusts != null) {
				for (EmbeddedEntity e : crusts) {
					if (e.getProperty("identifier").equals(this.identifier)) {
						if (identifier != null
								&& !identifier.equals(this.identifier)) {
							for (EmbeddedEntity f : crusts) {
								if (f.getProperty("identifier").equals(
										identifier)) {
									return;
								}
							}
							e.setProperty("identifier", identifier);
						}
						if (description != null) {
							e.setProperty("description", description);
						}
						List<Double> costs = (List<Double>) e
								.getProperty("costs");
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
						if (price1 != null && Double.valueOf(price1) >= 0) {
							prices.set(0, Double.valueOf(price1));
						}
						if (price2 != null && Double.valueOf(price2) >= 0) {
							prices.set(1, Double.valueOf(price2));
						}
						if (price3 != null && Double.valueOf(price3) >= 0) {
							prices.set(2, Double.valueOf(price3));
						}
						pizzaFactory.setProperty("crust", crusts);
						datastore.put(pizzaFactory);
					}
				}
			}
		} catch (EntityNotFoundException e) {
		}
	}

	/**
	 * Update the Pizza Crust into PizzaFactory with form using token
	 * 
	 * @param token
	 *            Pizza Factory token
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
	@Path("/findbytoken/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void updatePizzaCrust(@PathParam("token") String token,
			@FormParam("identifier") String identifier,
			@FormParam("description") String description,
			@FormParam("cost1") String cost1,
			@FormParam("price1") String price1,
			@FormParam("cost2") String cost2,
			@FormParam("price2") String price2,
			@FormParam("cost3") String cost3, @FormParam("price3") String price3) {

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("crust");
			if (crusts != null) {
				for (EmbeddedEntity e : crusts) {
					if (e.getProperty("identifier").equals(this.identifier)) {
						if (identifier != null
								&& !identifier.equals(this.identifier)) {
							for (EmbeddedEntity f : crusts) {
								if (f.getProperty("identifier").equals(
										identifier)) {
									return;
								}
							}
							e.setProperty("identifier", identifier);
						}
						if (description != null) {
							e.setProperty("description", description);
						}
						List<Double> costs = (List<Double>) e
								.getProperty("costs");
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
						if (price1 != null && Double.valueOf(price1) >= 0) {
							prices.set(0, Double.valueOf(price1));
						}
						if (price2 != null && Double.valueOf(price2) >= 0) {
							prices.set(1, Double.valueOf(price2));
						}
						if (price3 != null && Double.valueOf(price3) >= 0) {
							prices.set(2, Double.valueOf(price3));
						}
						pizzaFactory.setProperty("crust", crusts);
						datastore.put(pizzaFactory);
					}
				}
			}
		} catch (EntityNotFoundException e) {
		}
	}

	/**
	 * Update the Pizza Crust into PizzaFactory with JSON
	 * 
	 * @param pizzaCrust
	 *            Pizza Crust
	 */
	@SuppressWarnings("unchecked")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void newPizzaCrust(PizzaCrust pizzaCrust) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			return;
		}
		String identifier = pizzaCrust.getIdentifier();
		String description = pizzaCrust.getDescription();
		double[] costs = pizzaCrust.getCosts();
		double[] prices = pizzaCrust.getPrices();

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("crust");
			if (crusts != null) {
				for (EmbeddedEntity e : crusts) {
					if (e.getProperty("identifier").equals(this.identifier)) {
						if (identifier != null
								&& !identifier.equals(this.identifier)) {
							for (EmbeddedEntity f : crusts) {
								if (f.getProperty("identifier").equals(
										identifier)) {
									return;
								}
							}
							e.setProperty("identifier", identifier);
						}
						if (description != null) {
							e.setProperty("description", description);
						}
						List<Double> costs_list = (List<Double>) e
								.getProperty("costs");
						if (costs[0] >= 0) {
							costs_list.set(0, costs[0]);
						}
						if (costs[1] >= 0) {
							costs_list.set(1, costs[1]);
						}
						if (costs[2] >= 0) {
							costs_list.set(2, costs[2]);
						}

						List<Double> prices_list = (List<Double>) e
								.getProperty("prices");
						if (prices[0] >= 0) {
							prices_list.set(0, prices[0]);
						}
						if (prices[1] >= 0) {
							prices_list.set(1, prices[1]);
						}
						if (prices[2] >= 0) {
							prices_list.set(2, prices[2]);
						}
						pizzaFactory.setProperty("crust", crusts);
						datastore.put(pizzaFactory);
					}
				}
			}
		} catch (EntityNotFoundException e) {
		}
	}

	/**
	 * Update the Pizza Crust into PizzaFactory with JSON using token
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @param pizzaCrust
	 *            Pizza Crust
	 */
	@SuppressWarnings("unchecked")
	@Path("/findbytoken/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void newPizzaCrust(@PathParam("token") String token,
			PizzaCrust pizzaCrust) {
		String identifier = pizzaCrust.getIdentifier();
		String description = pizzaCrust.getDescription();
		double[] costs = pizzaCrust.getCosts();
		double[] prices = pizzaCrust.getPrices();

		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("crust");
			if (crusts != null) {
				for (EmbeddedEntity e : crusts) {
					if (e.getProperty("identifier").equals(this.identifier)) {
						if (identifier != null
								&& !identifier.equals(this.identifier)) {
							for (EmbeddedEntity f : crusts) {
								if (f.getProperty("identifier").equals(
										identifier)) {
									return;
								}
							}
							e.setProperty("identifier", identifier);
						}
						if (description != null) {
							e.setProperty("description", description);
						}
						List<Double> costs_list = (List<Double>) e
								.getProperty("costs");
						if (costs[0] >= 0) {
							costs_list.set(0, costs[0]);
						}
						if (costs[1] >= 0) {
							costs_list.set(1, costs[1]);
						}
						if (costs[2] >= 0) {
							costs_list.set(2, costs[2]);
						}

						List<Double> prices_list = (List<Double>) e
								.getProperty("prices");
						if (prices[0] >= 0) {
							prices_list.set(0, prices[0]);
						}
						if (prices[1] >= 0) {
							prices_list.set(1, prices[1]);
						}
						if (prices[2] >= 0) {
							prices_list.set(2, prices[2]);
						}
						pizzaFactory.setProperty("crust", crusts);
						datastore.put(pizzaFactory);
					}
				}
			}
		} catch (EntityNotFoundException e) {
		}
	}

	/**
	 * Delete crust
	 * 
	 */
	@SuppressWarnings("unchecked")
	@DELETE
	public void deletePizzaCrust() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("crust");
			Iterator<EmbeddedEntity> iterator = crusts.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getProperty("identifier")
						.equals(identifier)) {
					iterator.remove();
					pizzaFactory.setProperty("crust", crusts);
					datastore.put(pizzaFactory);
				}
			}
		} catch (Exception e) {
		}
	}

	/**
	 * Delete crust with token
	 * 
	 * @param token
	 *            Pizza Factory token
	 */
	@SuppressWarnings("unchecked")
	@Path("/findbytoken/{token}")
	@DELETE
	public void deletePizzaCrust(@PathParam("token") String token) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("crust");
			Iterator<EmbeddedEntity> iterator = crusts.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getProperty("identifier")
						.equals(identifier)) {
					iterator.remove();
					pizzaFactory.setProperty("crust", crusts);
					datastore.put(pizzaFactory);
				}
			}
		} catch (Exception e) {
		}
	}
}
