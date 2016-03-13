/**
 * 
 */
package order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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

	public PizzaShopOrderResource(UriInfo uriInfo, Request request) {
		this.uriInfo = uriInfo;
		this.request = request;
	}

	/**
	 * Get orders
	 * 
	 * @return List of Order profiles
	 */
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public List<Order> getOrders() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return findOrders(hash_uid);
	}

	/**
	 * Get orders with token for curl
	 * 
	 * @param token
	 *            Customer token
	 * @return List of orders
	 */
	@Path("/authorize/{token}")
	@GET
	public List<Order> getOrders(@PathParam("token") String token) {
		return findOrders(token);
	}

	/**
	 * Get a order
	 * 
	 * @param number
	 *            Order number
	 * @return Order
	 */
	@Path("{number}")
	@GET
	public Order getOrder(@PathParam("nubmer") String number) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return findOrder(hash_uid, number);
	}

	/**
	 * Get a order with token for curl
	 * 
	 * @param token
	 *            Customer Token
	 * @param number
	 *            Order number
	 * @return Order
	 */
	@Path("{number}/authorize/{token}")
	@GET
	public Order getOrder(String token, @PathParam("nubmer") String number) {
		return findOrder(token, number);
	}

	/**
	 * Update the Order status
	 * 
	 * @param num
	 *            Order number
	 * @param status
	 *            New Order status
	 */
	@Path("{number}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void putOrder(@PathParam("number") String num,
			@FormParam("status") String status) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		updateOrder(hash_uid, num, status);
	}

	/**
	 * Update the Order status
	 * 
	 * @param token
	 *            PizzaShop token
	 * @param num
	 *            Order number
	 * @param status
	 *            New Order status
	 */
	@Path("{number}/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void putOrder(@PathParam("token") String token,
			@PathParam("number") String num, @FormParam("status") String status) {
		updateOrder(token, num, status);
	}

	/**
	 * Update order status
	 * 
	 * @param token
	 *            PizzaShop token
	 * @param num
	 *            Order number
	 * @param status
	 *            New Order status
	 */
	private void updateOrder(String token, String num, String status) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key p_key = KeyFactory.createKey("PizzaShop", token);
		try {
			Entity pizzaShop = datastore.get(p_key);
			Key o_key = KeyFactory.createKey("Order", num);
			Entity order = datastore.get(o_key);
			if (!((String) pizzaShop.getProperty("identifier"))
					.equals((String) order.getProperty("pizzashop"))) {
				return;
			}
			Queue queue = QueueFactory.getDefaultQueue();
			queue.add(TaskOptions.Builder.withUrl("/worker")
					.param("number", num).param("status", status));
		} catch (EntityNotFoundException e) {
			return;
		}
	}

	/**
	 * Get orders
	 * 
	 * @param token
	 *            PizzaShop token
	 * @return List of orders
	 */
	private List<Order> findOrders(String token) {
		if (token == null) {
			return null;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", token);
		List<Order> orders = null;
		try {
			Entity pizzaShop = datastore.get(key);
			Filter pizzaShopFilter = new FilterPredicate("pizzashop",
					FilterOperator.EQUAL, pizzaShop.getProperty("identifier"));
			Query q = new Query("Order").setFilter(pizzaShopFilter);
			PreparedQuery pq = datastore.prepare(q);
			orders = new ArrayList<Order>();
			for (Entity result : pq.asIterable()) {
				Order order = entityToObject(result);
				orders.add(order);
			}
		} catch (EntityNotFoundException e) {
		}
		return orders;
	}

	/**
	 * Find order
	 * 
	 * @param token
	 *            PizzaShop token
	 * @param number
	 *            Order number
	 * @return Order
	 */
	private Order findOrder(String token, String number) {
		if (token == null || number == null) {
			return null;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("PizzaShop", token);
			datastore.get(key);
		} catch (EntityNotFoundException e) {
			return null;
		}
		Key key = KeyFactory.createKey("Order", number);
		Order order = null;
		try {
			Entity entity = datastore.get(key);
			order = entityToObject(entity);
		} catch (EntityNotFoundException e) {
		}
		return order;
	}

	/**
	 * Convert entity to Pizza Shop order
	 * 
	 * @param entity
	 *            Entity
	 * @return Order
	 */
	@SuppressWarnings("unchecked")
	public static Order entityToObject(Entity entity) {
		if (entity == null || !entity.getKind().equals("Order")) {
			return null;
		}
		Order order = new Order();
		order.setPizzaShop((String) entity.getProperty("pizzashop"));
		order.setCustomer((String) entity.getProperty("customer"));
		order.setNumber(entity.getKey().getName());
		order.setSize((String) entity.getProperty("size"));
		order.setCrust((String) entity.getProperty("crust"));
		order.setCheese((String) entity.getProperty("cheese"));
		order.setSauce((String) entity.getProperty("sauce"));
		List<String> meats = (List<String>) entity.getProperty("meat");
		if (meats != null) {
			for (String s : meats) {
				order.addMeat(s);
			}
		}
		List<String> vegs = (List<String>) entity.getProperty("veg");
		if (vegs != null) {
			for (String s : vegs) {
				order.addVeg(s);
			}
		}
		order.setStatus((String) entity.getProperty("status"));
		order.setDate((Date) entity.getProperty("date"));
		order.setPrice((double) entity.getProperty("price"));
		order.setPrice((double) entity.getProperty("cost"));
		return order;
	}
}
