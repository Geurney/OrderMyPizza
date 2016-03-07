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

import pizzashop.PizzaFactory;
import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * PizzaFactory REST service
 * 
 * @author Geurney
 *
 */
@Path("/pizzafactory")
public class PizzaFactoryResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Get the current PizzaFactory profile
	 * 
	 * @return PizzaFactory profile
	 */
	@GET
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPizzaFactory() {
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
			Entity entity = datastore.get(key);
			PizzaFactory pizzaFactory = new PizzaFactory();
			response = Response.ok(pizzaFactory).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Please complete your profile!").build();
		}
		return response;
	}

	/**
	 * Add the PizzaFactory into datastore
	 */
	@POST
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newPizzaFactory(@FormParam("name") String name,
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
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		Entity pizzaFactory = null;
		try {
			pizzaFactory = datastore.get(key);
		} catch (EntityNotFoundException e) {
			pizzaFactory = new Entity("PizzaFactory", hash_uid);
		} finally {
			datastore.put(pizzaFactory);
		}
		response = Response.ok("PizzaFactory profile updated successfully!")
				.build();
		return response;
	}

	/**
	 * Delete current PizzaFactory
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
			Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
			datastore.delete(key);
			response = Response.ok("PizzaFactory is deleted successfully!")
					.build();
		} catch (Exception e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("PizzaFactory not found!").build();
		}
		return response;
	}

	@Path("/pizzacomponent")
	public PizzaComponentResource GetPizzaCrust() {
		return new PizzaComponentResource();
	}
	
	/*
	@Path("{crust}")
	public PizzaComponentResource GetPizzaCrust(@PathParam("crust") String crust) {
		return new PizzaComponentResource(crust);
	}

	@Path("{cheese}")
	public PizzaComponentResource GetPizzaCheese(
			@PathParam("cheese") String cheese) {
		return new PizzaComponentResource(cheese);
	}

	@Path("{sauce}")
	public PizzaComponentResource GetPizzaSauce(@PathParam("sauce") String sauce) {
		return new PizzaComponentResource(sauce);
	}

	@Path("{topping}")
	public PizzaToppingResource GetPizzaTopping(
			@PathParam("topping") String topping) {
		return new PizzaToppingResource(topping);
	}
*/	
	
}
