/**
 * 
 */
package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import pizza.PizzaToppingVeg;
import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * PizzaTopping REST service
 * 
 * @author Geurney
 *
 */
@Path("/pizzatopping")
public class PizzaToppingResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Get the current PizzaTopping profile
	 * 
	 * @return PizzaTopping profile
	 */
	@GET
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPizzatopping() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("pizzatopping", hash_uid);
		try {
			Entity entity = datastore.get(key);
			PizzaToppingVeg pizzaTopping = new PizzaToppingVeg();
/*			pizzaTopping.setName((String) entity.getProperty("name"));
			pizzaTopping.setAddress((String) entity.getProperty("address"));
			pizzaTopping.setPhone((String) entity.getProperty("phone"));*/
			response = Response.ok(pizzaTopping).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Please complete your profile!").build();
		}
		return response;
	}

	/**
	 * Add the PizzaTopping into datastore
	 */
	@POST
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newPizzatopping(@FormParam("name") String name,
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
		Key key = KeyFactory.createKey("PizzaTopping", hash_uid);
		Entity pizzaTopping = null;
		try {
			pizzaTopping = datastore.get(key);
		} catch (EntityNotFoundException e) {
			pizzaTopping = new Entity("PizzaTopping", hash_uid);
		} finally {
			if (name != null) {
				pizzaTopping.setProperty("name", name);
			}
			if (address != null) {
				pizzaTopping.setProperty("address", address);
			}
			if (phone != null) {
				pizzaTopping.setProperty("phone", phone);
			}
			datastore.put(pizzaTopping);
		}
		response = Response.ok("PizzaTopping profile updated successfully!")
				.build();
		return response;
	}

	/**
	 * Delete current PizzaTopping
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
			Key key = KeyFactory.createKey("pizzatopping", hash_uid);
			datastore.delete(key);
			response = Response.ok("PizzaTopping is deleted successfully!")
					.build();
		} catch (Exception e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("PizzaTopping not found!").build();
		}
		return response;
	}
}
