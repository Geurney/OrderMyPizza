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

import pizza.PizzaComponent;
import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
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
@Path("/pizzacomponent")
public class PizzaComponentResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	public PizzaComponentResource(String name) {
		
	}

	/**
	 * Get the current PizzaComponent profile
	 * 
	 * @return PizzaComponent profile
	 */
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
		Key key = KeyFactory.createKey("PizzaComponent", hash_uid);
		try {
			Entity entity = datastore.get(key);
			PizzaComponent pizzaComponent = new PizzaComponent();
			pizzaComponent.setName((String) entity.getProperty("name"));
			pizzaComponent.setAddress((String) entity.getProperty("address"));
			pizzaComponent.setPhone((String) entity.getProperty("phone"));
			response = Response.ok(pizzaComponent).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Please complete your profile!").build();
		}
		return response;
	}

	/**
	 * Add the PizzaComponent into datastore
	 */
	@POST
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newPizzaComponent(@FormParam("name") String name,
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
		Key key = KeyFactory.createKey("PizzaComponent", hash_uid);
		Entity pizzaComponent = null;
		try {
			pizzaComponent = datastore.get(key);
		} catch (EntityNotFoundException e) {
			pizzaComponent = new Entity("PizzaComponent", hash_uid);
		} finally {
			if (name != null) {
				pizzaComponent.setProperty("name", name);
			}
			if (address != null) {
				pizzaComponent.setProperty("address", address);
			}
			if (phone != null) {
				pizzaComponent.setProperty("phone", phone);
			}
			datastore.put(pizzaComponent);
		}
		response = Response.ok("PizzaComponent profile updated successfully!")
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
