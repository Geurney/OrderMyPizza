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

import pizzashop.PizzaShop;
import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * Pizzashop REST service
 * 
 * @author Geurney
 *
 */
@Path("/pizzashop")
public class PizzashopResources {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Get the current pizzashop's profile
	 * 
	 * @return Pizzashop profile
	 */
	@GET
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getCurrentPizashop() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", hash_uid);
		try {
			Entity entity = datastore.get(key);
			PizzaShop pizzaShop = new PizzaShop();
			pizzaShop.setName((String) entity.getProperty("name"));
			pizzaShop.setAddress((String) entity.getProperty("address"));
			pizzaShop.setPhone((String) entity.getProperty("phone"));
			response = Response.ok(pizzaShop).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Please complete your profile!").build();
		}
		return response;
	}

	/**
	 * Get all pizzashops
	 * 
	 * @return list of pizzashops
	 */
	@Path("all")
	@GET
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getAllPizashops() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query q = new Query("PizzaShop");
		PreparedQuery pq = datastore.prepare(q);
		List<PizzaShop> pizzashops = new ArrayList<PizzaShop>();
		for (Entity result : pq.asIterable()) {
			PizzaShop pizzaShop = new PizzaShop();
			pizzaShop.setName((String) result.getProperty("name"));
			pizzaShop.setAddress((String) result.getProperty("address"));
			pizzaShop.setPhone((String) result.getProperty("phone"));
			pizzashops.add(pizzaShop);
		}
		GenericEntity<List<PizzaShop>> list = new GenericEntity<List<PizzaShop>>(
				pizzashops) {
		};
		return Response.ok(list).build();
	}

	/**
	 * Get a specific pizzashop
	 * 
	 * @param name
	 *            PizzaShop name
	 * @return The specific pizzashop profile
	 */
	@Path("{name}")
	@GET
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPizashop(@PathParam("name") String name) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Filter nameFilter = new FilterPredicate("name", FilterOperator.EQUAL,
				name);
		Query q = new Query("PizzaShop").setFilter(nameFilter);
		PreparedQuery pq = datastore.prepare(q);
		List<PizzaShop> pizzashops = new ArrayList<PizzaShop>();
		for (Entity result : pq.asIterable()) {
			PizzaShop pizzaShop = new PizzaShop();
			pizzaShop.setName((String) result.getProperty("name"));
			pizzaShop.setAddress((String) result.getProperty("address"));
			pizzaShop.setPhone((String) result.getProperty("phone"));
			pizzashops.add(pizzaShop);
		}
		GenericEntity<List<PizzaShop>> list = new GenericEntity<List<PizzaShop>>(
				pizzashops) {
		};
		response = Response.ok(list).build();
		return response;
	}

	/**
	 * Add the PizzaShop into datastore
	 */
	@POST
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newCustomer(@FormParam("name") String name,
			@FormParam("address") String address,
			@FormParam("phone") String phone) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", hash_uid);
		Entity pizzaShop = null;
		try {
			pizzaShop = datastore.get(key);
		} catch (EntityNotFoundException e) {
			pizzaShop = new Entity("PizzaShop", hash_uid);
		} finally {
			if (name != null) {
				pizzaShop.setProperty("name", name);
			}
			if (address != null) {
				pizzaShop.setProperty("address", address);
			}
			if (phone != null) {
				pizzaShop.setProperty("phone", phone);
			}
			datastore.put(pizzaShop);
		}
		response = Response.ok("PizzaShop profile updated successfully!")
				.build();
		return response;
	}

	/**
	 * Delete current PizzaShop
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
			Key key = KeyFactory.createKey("PizzaShop", hash_uid);
			datastore.delete(key);
			response = Response.ok("PizzaShop is deleted successfully!")
					.build();
		} catch (Exception e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN).entity("PizzaShop not found!")
					.build();
		}
		return response;
	}
}
