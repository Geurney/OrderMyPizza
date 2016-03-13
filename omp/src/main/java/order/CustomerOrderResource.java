/**
 * 
 */
package order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import pizzafactory.PizzaFactory;
import pizzafactory.PizzaFactoryResource;
import pizzashop.PizzaShop;
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
	 * Add order into datastore
	 * 
	 * @param pizzaShop
	 *            PizzaShop identifier
	 * @param size
	 *            Pizza Size
	 * @param crust
	 *            Pizza Crust
	 * @param cheese
	 *            Pizza Cheese
	 * @param sauce
	 *            Pizza Sauce
	 * @param meat1
	 *            Pizza Topping Meat1
	 * @param meat2
	 *            Pizza Topping Meat2
	 * @param meat3
	 *            Pizza Topping Meat3
	 * @param veg1
	 *            Pizza Topping Vegetable1
	 * @param veg2
	 *            Pizza Topping Vegetable2
	 * @param veg3
	 *            Pizza Topping Vegetable3
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void postOrder(@FormParam("pizzashop") String pizzashop,
			@FormParam("size") String size, @FormParam("crust") String crust,
			@FormParam("cheese") String cheese,
			@FormParam("sauce") String sauce, @FormParam("meat1") String meat1,
			@FormParam("meat2") String meat2, @FormParam("meat3") String meat3,
			@FormParam("veg1") String veg1, @FormParam("veg2") String veg2,
			@FormParam("veg3") String veg3) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		newOrder(hash_uid, pizzashop, size, crust, cheese, sauce, meat1, meat2,
				meat3, veg1, veg2, veg3);
	}

	/**
	 * Add order into datastore with token for curl
	 * 
	 * @param token
	 *            Customer token
	 * @param pizzaShop
	 *            PizzaShop identifier
	 * @param size
	 *            Pizza Size
	 * @param crust
	 *            Pizza Crust
	 * @param cheese
	 *            Pizza Cheese
	 * @param sauce
	 *            Pizza Sauce
	 * @param meat1
	 *            Pizza Topping Meat1
	 * @param meat2
	 *            Pizza Topping Meat2
	 * @param meat3
	 *            Pizza Topping Meat3
	 * @param veg1
	 *            Pizza Topping Vegetable1
	 * @param veg2
	 *            Pizza Topping Vegetable2
	 * @param veg3
	 *            Pizza Topping Vegetable3
	 */
	@Path("/authorize/{token}")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void postOrder(String token,
			@FormParam("pizzashop") String pizzashop,
			@FormParam("size") String size, @FormParam("crust") String crust,
			@FormParam("cheese") String cheese,
			@FormParam("sauce") String sauce, @FormParam("meat1") String meat1,
			@FormParam("meat2") String meat2, @FormParam("meat3") String meat3,
			@FormParam("veg1") String veg1, @FormParam("veg2") String veg2,
			@FormParam("veg3") String veg3) {
		newOrder(token, pizzashop, size, crust, cheese, sauce, meat1, meat2,
				meat3, veg1, veg2, veg3);
	}

	/**
	 * Add the order into datastore
	 * 
	 * @param order
	 *            Order
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void postOrder(Order order) {
		if (order == null) {
			return;
		}
		String hash_uid = UserUtils.getCurrentUserObscureID();
		List<String> meats = order.getMeats();
		String meat1 = null;
		String meat2 = null;
		String meat3 = null;
		if (meats != null && meats.size() > 0) {
			int i = meats.size();
			if (i == 3) {
				meat3 = meats.get(3);
				i--;
			}
			if (i == 2) {
				meat2 = meats.get(2);
				i--;
			}
			if (i == 1) {
				meat1 = meats.get(1);
			}
		}
		List<String> vegs = order.getVegs();
		String veg1 = null;
		String veg2 = null;
		String veg3 = null;
		if (vegs != null && vegs.size() > 0) {
			int i = vegs.size();
			if (i == 3) {
				veg3 = vegs.get(3);
				i--;
			}
			if (i == 2) {
				veg2 = vegs.get(2);
				i--;
			}
			if (i == 1) {
				veg1 = vegs.get(1);
			}
		}
		newOrder(hash_uid, order.getPizzaShop(), order.getSize(),
				order.getCrust(), order.getCheese(), order.getSauce(), meat1,
				meat2, meat3, veg1, veg2, veg3);
	}

	/**
	 * Add the order into datastore
	 * 
	 * @param order
	 *            Order
	 */
	@Path("/authorize/{token}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void postOrder(String token, Order order) {
		if (order == null) {
			return;
		}
		List<String> meats = order.getMeats();
		String meat1 = null;
		String meat2 = null;
		String meat3 = null;
		if (meats != null && meats.size() > 0) {
			int i = meats.size();
			if (i == 3) {
				meat3 = meats.get(3);
				i--;
			}
			if (i == 2) {
				meat2 = meats.get(2);
				i--;
			}
			if (i == 1) {
				meat1 = meats.get(1);
			}
		}
		List<String> vegs = order.getVegs();
		String veg1 = null;
		String veg2 = null;
		String veg3 = null;
		if (vegs != null && vegs.size() > 0) {
			int i = vegs.size();
			if (i == 3) {
				veg3 = vegs.get(3);
				i--;
			}
			if (i == 2) {
				veg2 = vegs.get(2);
				i--;
			}
			if (i == 1) {
				veg1 = vegs.get(1);
			}
		}
		newOrder(token, order.getPizzaShop(), order.getSize(),
				order.getCrust(), order.getCheese(), order.getSauce(), meat1,
				meat2, meat3, veg1, veg2, veg3);
	}

	/**
	 * Delete an order
	 * 
	 */
	@Path("{number}")
	@DELETE
	public void deleteOrder(@PathParam("number") String number) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		removeOrder(hash_uid, number);
	}

	/**
	 * Delete an order with token for curl
	 * 
	 */
	@Path("{number}/authorize/{token}")
	@DELETE
	public void deleteOrder(@PathParam("token") String token,
			@PathParam("number") String number) {
		removeOrder(token, number);
	}

	/**
	 * Get orders
	 * 
	 * @param token
	 *            Customer token
	 * @return List of orders
	 */
	private List<Order> findOrders(String token) {
		if (token == null) {
			return null;
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
		} catch (EntityNotFoundException e) {
		}
		return orders;
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
	private Order findOrder(String token, String number) {
		if (token == null || number == null) {
			return null;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("Customer", token);
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
	 * Add order into datastore
	 * 
	 * @param token
	 *            Customer token
	 * @param pizzaShop
	 *            PizzaShop identifier
	 * @param size
	 *            Pizza Size
	 * @param crust
	 *            Pizza Crust
	 * @param cheese
	 *            Pizza Cheese
	 * @param sauce
	 *            Pizza Sauce
	 * @param meat1
	 *            Pizza Topping Meat1
	 * @param meat2
	 *            Pizza Topping Meat2
	 * @param meat3
	 *            Pizza Topping Meat3
	 * @param veg1
	 *            Pizza Topping Vegetable1
	 * @param veg2
	 *            Pizza Topping Vegetable2
	 * @param veg3
	 *            Pizza Topping Vegetable3
	 */
	private void newOrder(String token, String pizzaShop, String size,
			String crust, String cheese, String sauce, String meat1,
			String meat2, String meat3, String veg1, String veg2, String veg3) {
		if (token == null || pizzaShop == null || size == null || crust == null
				|| cheese == null || sauce == null) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("Customer", token);
		Entity customer;
		try {
			customer = datastore.get(key);
		} catch (EntityNotFoundException e) {
			return;
		}

		Filter identifierFilter = new FilterPredicate("identifier",
				FilterOperator.EQUAL, pizzaShop);
		Query q = new Query("PizzaFactory").setFilter(identifierFilter);
		PreparedQuery pq = datastore.prepare(q);
		Entity entity = pq.asSingleEntity();
		if (entity == null) {
			return;
		}
		PizzaFactory pizzaFactory = PizzaFactoryResource.entityToObject(entity);
		Entity order = new Entity("Order", PizzaShop.generateOrderID());


		if (pizzaFactory.hasCheese(cheese)) {
			order.setProperty("cheese", cheese);
		} else {
			return;
		}

		order.setProperty("pizzaShop", pizzaShop);
		order.setProperty("customer", customer.getProperty("email"));
		order.setProperty("size", size);
		order.setProperty("status", "new");
		order.setProperty("date", new Date());
		datastore.put(order);
	}

	/**
	 * Delete an order
	 * 
	 * @param token
	 *            Customer token
	 * @param num
	 *            Order number
	 */
	private void removeOrder(String token, String num) {
		if (token == null || num == null) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key c_key = KeyFactory.createKey("Customer", token);
			Entity customer = datastore.get(c_key);
			Key o_key = KeyFactory.createKey("Order", num);
			Entity order = datastore.get(o_key);
			if (customer.getProperty("email") != order.getProperty("customer")) {
				return;
			} else {
				datastore.delete(o_key);
			}
		} catch (Exception e) {
		}
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
		order.setDate((Date) entity.getProperty("date"));
		order.setPrice((double) entity.getProperty("price"));
		return order;
	}
}
