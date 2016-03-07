/**
 * 
 */
package rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
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

import pizzashop.Order;
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
 * Order REST service
 * 
 * @author Geurney
 *
 */
@Path("/order")
public class OrdersResource {
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
	@Path("/customer")
	@GET
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getCustomerOrders() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Filter customerFilter = new FilterPredicate("customer",
				FilterOperator.EQUAL, hash_uid);
		Query q = new Query("Order").setFilter(customerFilter);
		PreparedQuery pq = datastore.prepare(q);
		List<Order> orders = new ArrayList<Order>();
		for (Entity result : pq.asIterable()) {
			Order order = new Order();
			order.setPizzaShop((String) result.getProperty("pizzashop"));
			order.setDescription((String) result.getProperty("description"));
			order.setDate((Date) result.getProperty("date"));
			order.setSize((String) result.getProperty("size"));
			order.setStatus((String) result.getProperty("status"));
			order.setPrice((double) result.getProperty("price"));
			orders.add(order);
		}
		GenericEntity<List<Order>> list = new GenericEntity<List<Order>>(orders) {
		};
		return Response.ok(list).build();
	}

	/**
	 * Get the current order profile
	 * 
	 * @return Order profile
	 */
	@SuppressWarnings("unchecked")
	@Path("/pizzashop")
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
		Filter pizzaShopFilter = new FilterPredicate("customer",
				FilterOperator.EQUAL, hash_uid);
		Query q = new Query("Order").setFilter(pizzaShopFilter);
		PreparedQuery pq = datastore.prepare(q);
		List<Order> orders = new ArrayList<Order>();
		for (Entity result : pq.asIterable()) {
			Order order = new Order();
			order.setDescription((String) result.getProperty("description"));
			order.setDate((Date) result.getProperty("date"));
			order.setSize((String) result.getProperty("size"));
			order.setStatus((String) result.getProperty("status"));
			order.setPrice((double) result.getProperty("price"));
			order.setCost((double) result.getProperty("cost"));
			
			order.setCustomer((String) result.getProperty("customer"));
			order.setCrust((String) result.getProperty("crust"));
			order.setCheese((String) result.getProperty("cheese"));
			order.setSauce((String) result.getProperty("sauce"));
			List<String> meats = (List<String>) result.getProperty("meat");
			if (meats != null) {
				for (String meat : meats) {
					order.addMeat(meat);
				}
			}
			List<String> vegs = (List<String>) result.getProperty("veg");
			if (vegs != null) {
				for (String veg : vegs) {
					order.addVeg(veg);
				}
			}
			orders.add(order);
		}
		GenericEntity<List<Order>> list = new GenericEntity<List<Order>>(orders) {
		};
		return Response.ok(list).build();
	}

	/**
	 * Add the Order into datastore
	 */
	@POST
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
		Entity order = new Entity("Order", UserUtils.obsecure(pizzashop
				+ customer + date));
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
	 * For specific order
	 * 
	 */
	@Path("customer/{identifier}")
	public CustomerOrderResource handleCustomerOrder(
			@PathParam("num") String num) {
		return new CustomerOrderResource(uriInfo, request, num);
	}

	/**
	 * For specific order
	 * 
	 */
	@Path("pizzashop/{identifier}")
	public PizzaShopOrderResource handlePizzaShopOrder(
			@PathParam("num") String num) {
		return new PizzaShopOrderResource(uriInfo, request, num);
	}
}
