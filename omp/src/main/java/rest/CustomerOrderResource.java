/**
 * 
 */
package rest;

import java.util.ArrayList;
import java.util.Date;
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
public class CustomerOrderResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	String num;

	public CustomerOrderResource(UriInfo uriInfo, Request request, String num) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.num = num;
	}

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
		try {
			Key key = KeyFactory.createKey("Order", num);
			Entity entity = datastore.get(key);
			String customer = (String) entity.getProperty("customer");
			if (customer != num) {
				response = Response.status(Response.Status.FORBIDDEN)
						.type(MediaType.TEXT_PLAIN).entity("You must log in!")
						.build();
			} else {
				Order order = new Order();
				order.setPizzaShop((String) entity.getProperty("pizzashop"));
				order.setDescription((String) entity.getProperty("description"));
				order.setDate((Date) entity.getProperty("date"));
				order.setSize((String) entity.getProperty("size"));
				order.setStatus((String) entity.getProperty("status"));
				order.setPrice((double) entity.getProperty("price"));
				response = Response.ok(order).build();
			}
		} catch (Exception e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN).entity("Order not found!")
					.build();
		}
		return response;
	}

	/**
	 * Add the Order into datastore
	 */
	@PUT
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newOrder(@FormParam("pizzashop") String pizzashop,
			@FormParam("customer") String customer,
			@FormParam("size") String size, @FormParam("crust") String crust,
			@FormParam("cheese") String cheese,
			@FormParam("sauce") String sauce, @FormParam("meat1") String meat1,
			@FormParam("meat2") String meat2, @FormParam("meat3") String meat3,
			@FormParam("veg1") String veg1, @FormParam("veg2") String veg2,
			@FormParam("veg3") String veg3) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null || !hash_uid.equals(customer)) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", pizzashop);
		try {
			datastore.get(key);
		} catch (EntityNotFoundException e) {
			response = Response.status(Response.Status.BAD_REQUEST)
					.type(MediaType.TEXT_PLAIN).entity("PizzaShop not exists!")
					.build();
			return response;
		}
		Date date = new Date();
		String number = UserUtils.obsecure(pizzashop + customer + date);
		Entity order = new Entity("Order", number);
		order.setProperty("number", number);
		order.setProperty("pizzashop", pizzashop);
		order.setProperty("customer", customer);
		order.setProperty("crust", crust);
		order.setProperty("cheese", cheese);
		order.setProperty("sauce", sauce);
		List<String> meats = new ArrayList<String>();
		if (meat1 != null) {
			meats.add(meat1);
		}
		if (meat2 != null) {
			meats.add(meat2);
		}
		if (meat3 != null) {
			meats.add(meat3);
		}
		List<String> vegs = new ArrayList<String>();
		if (veg1 != null) {
			vegs.add(veg1);
		}
		if (veg2 != null) {
			vegs.add(veg2);
		}
		if (veg3 != null) {
			vegs.add(veg3);
		}
		order.setProperty("meat", meats);
		order.setProperty("veg", vegs);
		order.setProperty("status", "new");
		order.setProperty("date", date);
		datastore.put(order);
		response = Response.ok("Order is created!").build();
		return response;
	}

	/**
	 * Delete order
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
			Key key = KeyFactory.createKey("Order", num);
			Entity order = datastore.get(key);
			String customer = (String) order.getProperty("customer");
			if (customer != num) {
				response = Response.status(Response.Status.FORBIDDEN)
						.type(MediaType.TEXT_PLAIN).entity("You must log in!")
						.build();
			} else {
				datastore.delete(key);
				response = Response.ok("Order is deleted successfully!")
						.build();
			}
		} catch (Exception e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN).entity("Order not found!")
					.build();
		}
		return response;
	}
}
