/**
 * 
 */
package order;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import rest.RestResponse;
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
public class CustomerOrderResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	public CustomerOrderResource(UriInfo uriInfo, Request request) {
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
	public Response getOrders() {
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
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getOrders(@PathParam("token") String token) {
		return findOrders(token);
	}

	/**
	 * Get a order
	 * 
	 * @param number
	 *            Order number
	 * @return Order
	 */
	@Path("/findbynumber/{number}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getOrder(@PathParam("nubmer") String number) {
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
	@Path("/findbynumber/{number}/authorize/{token}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getOrder(@PathParam("token") String token,
			@PathParam("nubmer") String number) {
		return findOrder(token, number);
	}

	/**
	 * Delete an order
	 * @param number Order number
	 */
	@Path("/findbynumber/{number}")
	@DELETE
	public Response deleteOrder(@PathParam("number") String number) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return removeOrder(hash_uid, number);
	}

	/**
	 * Delete an order with token for curl
	 * @param token Customer token
	 * @param number order number
	 * 
	 */
	@Path("/findbynumber/{number}/authorize/{token}")
	@DELETE
	public Response deleteOrder(@PathParam("token") String token,
			@PathParam("number") String number) {
		return removeOrder(token, number);
	}

	/**
	 * Get orders
	 * 
	 * @param token
	 *            Customer token
	 * @return List of orders
	 */
	private Response findOrders(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN();
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("Customer", token);
		List<Order> orders = null;
		try {
			Entity customer = datastore.get(key);
			Filter customerFilter = new FilterPredicate("customer",
					FilterOperator.EQUAL, customer.getProperty("email"));
			Query q = new Query("Order").setFilter(customerFilter);
			PreparedQuery pq = datastore.prepare(q);
			orders = new ArrayList<Order>();
			for (Entity result : pq.asIterable()) {
				Order order = entityToObject(result);
				orders.add(order);
			}
			GenericEntity<List<Order>> lists = new GenericEntity<List<Order>>(
					orders) {
			};
			response = RestResponse.OK(lists);
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND();
		}
		return response;
	}

	/**
	 * Find order
	 * 
	 * @param token
	 *            Customer token
	 * @param number
	 *            Order number
	 * @return Order
	 */
	private Response findOrder(String token, String number) {
		if (token == null) {
			return RestResponse.FORBIDDEN();
		}
		if (number == null) {
			return RestResponse.BAD();
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("Customer", token);
			datastore.get(key);
		} catch (EntityNotFoundException e) {
			return RestResponse.NOT_FOUND();
		}
		Key key = KeyFactory.createKey("Order", number);
		Order order = null;
		try {
			Entity entity = datastore.get(key);
			order = entityToObject(entity);
			response = RestResponse.OK(order);
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND();
		}
		return response;
	}

	/**
	 * Delete an order
	 * 
	 * @param token
	 *            Customer token
	 * @param num
	 *            Order number
	 */
	private Response removeOrder(String token, String num) {
		if (token == null) {
			return RestResponse.FORBIDDEN();
		}
		if (num == null) {
			return RestResponse.NOT_FOUND();
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key c_key = KeyFactory.createKey("Customer", token);
			Entity customer = datastore.get(c_key);
			Key o_key = KeyFactory.createKey("Order", num);
			Entity order = datastore.get(o_key);
			if (customer.getProperty("email") != order.getProperty("customer")) {
				response = RestResponse.FORBIDDEN();
			} else {
				datastore.delete(o_key);
				response = RestResponse.OK;
			}
		} catch (Exception e) {
			response = RestResponse.NOT_FOUND();
		}
		return response;
	}

	/**
	 * Convert entity to customer order
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
		order.setDate((String) entity.getProperty("date"));
		order.setPrice((double) entity.getProperty("price"));
		return order;
	}

}
