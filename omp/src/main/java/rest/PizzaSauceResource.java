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
import javax.ws.rs.Produces;
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
 * Pizza Sauce REST service
 * 
 * @author Geurney
 *
 */
public class PizzaSauceResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	String identifier;

	/**
	 * Get the Pizza Sauce
	 * 
	 * @return Pizza Sauce
	 */
	public PizzaSauceResource(UriInfo uriInfo, Request request,
			String identifier) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.identifier = identifier;
	}

	@SuppressWarnings("unchecked")
	@GET
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPizzaSauce() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> sauces = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("sauce");
			PizzaSauce sauce = null;
			if (sauces != null) {
				for (EmbeddedEntity e : sauces) {
					if (e.getProperty("identifier").equals(identifier)) {
						sauce = new PizzaSauce();
						sauce.setIdentifier((String) e
								.getProperty("identifier"));
						sauce.setDescription((String) e
								.getProperty("description"));
						List<Double> costs = (List<Double>) e
								.getProperty("costs");
						List<Double> prices = (List<Double>) e
								.getProperty("prices");
						sauce.setCosts(costs);
						sauce.setPrices(prices);
						break;
					}
				}
			}
			response = Response.ok(sauce).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Your pizza factory is not built yet!").build();
		}
		return response;
	}

	/**
	 * Add/Update the Pizza Sauce into PizzaFactory
	 */
	@SuppressWarnings("unchecked")
	@PUT
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newPizzaSauce(@FormParam("identifier") String identifier,
			@FormParam("description") String description,
			@FormParam("cost1") String cost1,
			@FormParam("price1") String price1,
			@FormParam("cost2") String cost2,
			@FormParam("price2") String price2,
			@FormParam("cost3") String cost3, @FormParam("price3") String price3) {

		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> sauces = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("sauce");
			if (sauces != null) {
				for (EmbeddedEntity e : sauces) {
					if (e.getProperty("identifier").equals(this.identifier)) {
						if (identifier != null
								&& !identifier.equals(this.identifier)) {
							for (EmbeddedEntity f : sauces) {
								if (f.getProperty("identifier").equals(
										identifier)) {
									response = Response
											.status(Response.Status.CONFLICT)
											.type(MediaType.TEXT_PLAIN)
											.entity("Identifier alread exists!")
											.build();
									return response;
								}
							}
							e.setProperty("identifier", identifier);
						}
						if (description != null) {
							e.setProperty("description", description);
						}
						List<Double> costs = (List<Double>) e
								.getProperty("costs");
						if (cost1 != null) {
							costs.set(0, Double.valueOf(cost1));
						}
						if (cost2 != null) {
							costs.set(1, Double.valueOf(cost2));
						}
						if (cost3 != null) {
							costs.set(2, Double.valueOf(cost3));
						}
						List<Double> prices = (List<Double>) e
								.getProperty("prices");
						if (price1 != null) {
							prices.set(0, Double.valueOf(price1));
						}
						if (price2 != null) {
							prices.set(1, Double.valueOf(price2));
						}
						if (price3 != null) {
							prices.set(2, Double.valueOf(price3));
						}
						pizzaFactory.setProperty("sauce", sauces);
						datastore.put(pizzaFactory);
						response = Response.ok(
								"Pizza Sauces updated successfully!").build();
						return response;
					}
				}
				response = Response.status(Response.Status.NOT_FOUND)
						.type(MediaType.TEXT_PLAIN).entity("Sauce not found!")
						.build();
			} else {
				response = Response.status(Response.Status.NOT_FOUND)
						.type(MediaType.TEXT_PLAIN)
						.entity("Sauce not found!").build();
			}
		} catch (EntityNotFoundException e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Your pizza factory is not built yet!").build();
		}
		return response;
	}

	/**
	 * Delete Pizza Sauce
	 * 
	 */
	@SuppressWarnings("unchecked")
	@DELETE
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	public Response deleteCustomer() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> sauces = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("sauce");
			Iterator<EmbeddedEntity> iterator = sauces.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getProperty("identifier")
						.equals(identifier)) {
					iterator.remove();
					pizzaFactory.setProperty("sauce", sauces);
					datastore.put(pizzaFactory);
					response = Response.ok("Sauce is deleted successfully!")
							.build();
					return response;
				}
			}
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN).entity("Sauce not found!")
					.build();
		} catch (Exception e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Pizza Factory not found!").build();
		}
		return response;
	}
}
