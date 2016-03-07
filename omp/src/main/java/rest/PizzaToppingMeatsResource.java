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
import javax.ws.rs.core.GenericEntity;
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
 * Pizza Meat Toppings REST service
 * 
 * @author Geurney
 *
 */
public class PizzaToppingMeatsResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Get the Pizza Meat Toppings
	 * 
	 * @return Pizza Meat Toppings
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPizzaToppingMeats() {
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
			List<EmbeddedEntity> meats = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("meat");
			List<PizzaToppingMeat> pc = new ArrayList<PizzaToppingMeat>();
			if (meats != null) {
				for (EmbeddedEntity e : meats) {
					PizzaToppingMeat meat = new PizzaToppingMeat();
					meat.setIdentifier((String) e.getProperty("identifier"));
					meat.setDescription((String) e.getProperty("description"));
					List<Double> costs = (List<Double>) e.getProperty("costs");
					List<Double> prices = (List<Double>) e
							.getProperty("prices");
					meat.setCosts(costs);
					meat.setPrices(prices);
					pc.add(meat);
				}
			}
			GenericEntity<List<PizzaToppingMeat>> list = new GenericEntity<List<PizzaToppingMeat>>(
					pc) {
			};
			response = Response.ok(list).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Your pizza factory is not built yet!").build();
		}
		return response;
	}

	/**
	 * Add/Update the Pizza Meat Topping into PizzaFactory
	 */
	@SuppressWarnings("unchecked")
	@POST
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newPostPizzaToppingMeat(
			@FormParam("identifier") String identifier,
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
		if (identifier == null || description == null || cost1 == null
				|| cost2 == null || cost3 == null || price1 == null
				|| price2 == null || price3 == null) {
			response = Response
					.status(Response.Status.BAD_REQUEST)
					.type(MediaType.TEXT_PLAIN)
					.entity("You must provide all parameters! Identifier, Description, Cost1~3, Price1~3")
					.build();
			return response;
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
			List<EmbeddedEntity> meats = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("meat");
			if (meats != null) {
				for (EmbeddedEntity e : meats) {
					if (e.getProperty("identifier").equals(identifier)) {
						response = Response.status(Response.Status.CONFLICT)
								.type(MediaType.TEXT_PLAIN)
								.entity("Identifier alread exists!").build();
						return response;
					}
				}
			} else {
				meats = new ArrayList<EmbeddedEntity>();
			}
			List<Double> costs = new ArrayList<Double>();
			costs.add(Double.valueOf(cost1));
			costs.add(Double.valueOf(cost2));
			costs.add(Double.valueOf(cost3));

			List<Double> prices = new ArrayList<Double>();
			prices.add(Double.valueOf(price1));
			prices.add(Double.valueOf(price2));
			prices.add(Double.valueOf(price3));

			EmbeddedEntity component = new EmbeddedEntity();
			component.setProperty("identifier", identifier);
			component.setProperty("description", description);
			component.setProperty("costs", costs);
			component.setProperty("prices", prices);

			meats.add(component);
			pizzaFactory.setProperty("meat", meats);
			datastore.put(pizzaFactory);
		}
		response = Response.ok("Pizza Meat Topping updated successfully!")
				.build();
		return response;
	}

	/**
	 * Delete All Pizza meat toppings
	 * 
	 */
	@DELETE
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	public Response deletePizzaToppingMeats() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
			Entity pizzaFactory = datastore.get(key);
			pizzaFactory.removeProperty("meat");
			response = Response.ok("Pizza Meat Toppings are deleted!").build();
		} catch (Exception e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Pizza Factory not found!").build();
		}
		return response;
	}

	/**
	 * For specific pizza meat topping
	 * 
	 */
	@Path("{identifier}")
	public PizzaToppingMeatResource handlePizzaToppingMeat(
			@PathParam("identifier") String identifier) {
		return new PizzaToppingMeatResource(uriInfo, request, identifier);
	}

}
