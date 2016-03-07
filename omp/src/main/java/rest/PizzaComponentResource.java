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
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
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
 * PizzaComponent REST service
 * 
 * @author Geurney
 *
 */
public class PizzaComponentResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Get the current PizzaComponent profile
	 * 
	 * @return PizzaComponent profile
	 */
	@Path("/crust")
	@GET
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPizzaComponent() {
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
			System.out.println("HERE!--------------------------");
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("crust");
			List<PizzaCrust> pc = new ArrayList<PizzaCrust>();
			if (crusts != null) {
				for (EmbeddedEntity e : crusts) {
					PizzaCrust crust = new PizzaCrust();
					crust.setIdentifier((String) e.getProperty("identifier"));
					crust.setDescription((String) e.getProperty("description"));
					List<Double> costs = (List<Double>) e.getProperty("costs");
					List<Double> prices = (List<Double>) e.getProperty("prices");
					crust.setCosts(costs);
					crust.setPrices(prices);
					pc.add(crust);
				}
			}
			GenericEntity<List<PizzaCrust>> list = new GenericEntity<List<PizzaCrust>>(
					pc) {
			};
			response = Response.ok(list).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Please complete your profile!").build();
		}
		return response;
	}

	/**
	 * Add the Pizza Crust into PizzaFactory
	 */
	@Path("/crust")
	@POST
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newPizzaComponent(
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
			response = Response.status(Response.Status.BAD_REQUEST)
					.type(MediaType.TEXT_PLAIN)
					.entity("You must provide all parameters!").build();
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
			List<Double> costs = new ArrayList<Double>();
			costs.add(Double.valueOf(cost1));
			costs.add(Double.valueOf(cost2));
			costs.add(Double.valueOf(cost3));

			List<Double> prices = new ArrayList<Double>();
			prices.add(Double.valueOf(price1));
			prices.add(Double.valueOf(price2));
			prices.add(Double.valueOf(price3));

			EmbeddedEntity component = new EmbeddedEntity();
			component.setProperty("description", description);
			component.setProperty("costs", costs);
			component.setProperty("prices", prices);

			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty("crust");
			if (list == null) {
				list = new ArrayList<EmbeddedEntity>();
			}
			list.add(component);
			pizzaFactory.setProperty("crust", list);
			datastore.put(pizzaFactory);
		}
		response = Response.ok("Pizza Component profile updated successfully!")
				.build();
		return response;
	}

	/**
	 * Delete current PizzaComponent
	 * 
	 */
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
		try {
			Key key = KeyFactory.createKey("PizzaComponent", hash_uid);
			datastore.delete(key);
			response = Response.ok("PizzaComponent is deleted successfully!")
					.build();
		} catch (Exception e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("PizzaComponent not found!").build();
		}
		return response;
	}
}
