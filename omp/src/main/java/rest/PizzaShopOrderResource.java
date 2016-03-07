/**
 * 
 */
package rest;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * Order REST service
 * 
 * @author Geurney
 *
 */
public class PizzaShopOrderResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	String num;

	public PizzaShopOrderResource(UriInfo uriInfo, Request request, String num) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.num = num;
	}

	/**
	 * Get the current order profile
	 * 
	 * @return Order profile
	 */
	@SuppressWarnings("unchecked")
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
			String pizzaShop = (String) entity.getProperty("pizzashop");
			if (pizzaShop != num) {
				response = Response.status(Response.Status.FORBIDDEN)
						.type(MediaType.TEXT_PLAIN).entity("You must log in!")
						.build();
			} else {
				Order order = new Order();
				order.setCustomer((String) entity.getProperty("customer"));
				order.setDescription((String) entity.getProperty("description"));
				order.setDate((Date) entity.getProperty("date"));
				order.setSize((String) entity.getProperty("size"));
				order.setStatus((String) entity.getProperty("status"));
				order.setPrice((double) entity.getProperty("price"));
				order.setCost((double) entity.getProperty("cost"));

				order.setCrust((String) entity.getProperty("crust"));
				order.setCheese((String) entity.getProperty("cheese"));
				order.setSauce((String) entity.getProperty("sauce"));
				List<String> meats = (List<String>) entity.getProperty("meat");
				if (meats != null) {
					for (String meat : meats) {
						order.addMeat(meat);
					}
				}
				List<String> vegs = (List<String>) entity.getProperty("veg");
				if (vegs != null) {
					for (String veg : vegs) {
						order.addVeg(veg);
					}
				}
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
	 * Enque the Order
	 */
	@PUT
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response newOrder(@FormParam("pizzashop") String pizzashop,
			@FormParam("status") String status) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null || !hash_uid.equals(pizzashop)) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/worker").param("number", num)
				.param("status", status));
		response = Response.ok("Order is enqued!").build();
		return response;
	}
}
