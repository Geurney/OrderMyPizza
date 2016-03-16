/**
 * 
 */
package order;

import java.util.ArrayList;
import java.util.List;

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
	 * Get orders
	 * 
	 * @param token
	 *            PizzaShop token
	 * @return List of orders
	 */
	private Response findOrders(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN();
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
	 *            PizzaShop token
	 * @param number
	 *            Order number
	 * @return Order
	 */
	private Response findOrder(String token, String number) {
		if (token == null) {
			return RestResponse.FORBIDDEN();
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("PizzaShop", token);
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
		order.setDate((String) entity.getProperty("date"));
		order.setPrice((double) entity.getProperty("price"));
		order.setCost((double) entity.getProperty("cost"));
		return order;
	}
}
