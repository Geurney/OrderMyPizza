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

import pizzashop.Order;
import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Order REST service
 * 
 * @author Geurney
 *
 */
@Path("/order")
public class OrderResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Get the current order profile
	 * 
	 * @return Order profile
	 */
	@GET
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getOrder() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("Order", hash_uid);
		try {
			Entity entity = datastore.get(key);
			Order order = new Order();
			order.setName((String) entity.getProperty("name"));
			order.setAddress((String) entity.getProperty("address"));
			order.setPhone((String) entity.getProperty("phone"));
			response = Response.ok(order).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Please complete your profile!").build();
		}
		return response;
	}

	/**
	 * Add the Order into datastore
	 */
	@POST
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newOrder(@FormParam("name") String name,
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
		Key key = KeyFactory.createKey("Order", hash_uid);
		Entity order = null;
		try {
			order = datastore.get(key);
		} catch (EntityNotFoundException e) {
			order = new Entity("Customer", hash_uid);
		} finally {
			if (name != null) {
				order.setProperty("name", name);
			}
			if (address != null) {
				order.setProperty("address", address);
			}
			if (phone != null) {
				order.setProperty("phone", phone);
			}
			datastore.put(order);
		}
		response = Response.ok("Order profile updated successfully!").build();
		return response;
	}

	/**
	 * Delete current order
	 * 
	 */
	@DELETE
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	public Response deleteOrder() {
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
			Key key = KeyFactory.createKey("Order", hash_uid);
			datastore.delete(key);
			response = Response.ok("Order is deleted successfully!").build();
		} catch (Exception e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN).entity("Order not found!")
					.build();
		}
		return response;
	}
}
