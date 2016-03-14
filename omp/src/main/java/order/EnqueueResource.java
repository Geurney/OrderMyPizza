package order;

import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import pizzafactory.PizzaFactory;
import pizzafactory.PizzaFactoryResource;
import pizzashop.PizzaShop;
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
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

@Path("/enqueue")
public class EnqueueResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

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
	public Response postOrder(@FormParam("pizzashop") String pizzashop,
			@FormParam("size") String size, @FormParam("crust") String crust,
			@FormParam("cheese") String cheese,
			@FormParam("sauce") String sauce, @FormParam("meat1") String meat1,
			@FormParam("meat2") String meat2, @FormParam("meat3") String meat3,
			@FormParam("veg1") String veg1, @FormParam("veg2") String veg2,
			@FormParam("veg3") String veg3) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return newOrder(hash_uid, pizzashop, size, crust, cheese, sauce, meat1,
				meat2, meat3, veg1, veg2, veg3);
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
	public Response postOrder(@PathParam("token") String token,
			@FormParam("pizzashop") String pizzashop,
			@FormParam("size") String size, @FormParam("crust") String crust,
			@FormParam("cheese") String cheese,
			@FormParam("sauce") String sauce, @FormParam("meat1") String meat1,
			@FormParam("meat2") String meat2, @FormParam("meat3") String meat3,
			@FormParam("veg1") String veg1, @FormParam("veg2") String veg2,
			@FormParam("veg3") String veg3) {
		return newOrder(token, pizzashop, size, crust, cheese, sauce, meat1,
				meat2, meat3, veg1, veg2, veg3);
	}

	/**
	 * Add the order into datastore
	 * 
	 * @param order
	 *            Order
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postOrder(Order order) {
		if (order == null) {
			return RestResponse.BAD;
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
		return newOrder(hash_uid, order.getPizzaShop(), order.getSize(),
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
	public Response postOrder(@PathParam("token") String token, Order order) {
		if (order == null) {
			return RestResponse.BAD;
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
		return newOrder(token, order.getPizzaShop(), order.getSize(),
				order.getCrust(), order.getCheese(), order.getSauce(), meat1,
				meat2, meat3, veg1, veg2, veg3);
	}

	/**
	 * Update the Order status
	 * 
	 * @param num
	 *            Order number
	 * @param status
	 *            New Order status
	 */
	@Path("/findbynumber/{number}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putOrder(@PathParam("number") String num,
			@FormParam("status") String status) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return updateOrder(hash_uid, num, status);
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
	@Path("/findbynumber/{number}/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response putOrder(@PathParam("token") String token,
			@PathParam("number") String num, @FormParam("status") String status) {
		return updateOrder(token, num, status);
	}

	private Response newOrder(String token, String pizzaShop, String size,
			String crust, String cheese, String sauce, String meat1,
			String meat2, String meat3, String veg1, String veg2, String veg3) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		if (pizzaShop == null || size == null || crust == null
				|| cheese == null || sauce == null) {
			return RestResponse.BAD;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("Customer", token);
		Entity customer;
		try {
			customer = datastore.get(key);
		} catch (EntityNotFoundException e) {
			return RestResponse.NOT_FOUND;
		}

		Filter identifierFilter = new FilterPredicate("identifier",
				FilterOperator.EQUAL, pizzaShop);
		Query q = new Query("PizzaFactory").setFilter(identifierFilter);
		PreparedQuery pq = datastore.prepare(q);
		Entity entity = pq.asSingleEntity();
		if (entity == null) {
			return RestResponse.NOT_FOUND;
		}
		PizzaFactory pizzaFactory = PizzaFactoryResource.entityToObject(entity);

		TaskOptions task = TaskOptions.Builder.withUrl("/worker")
				.param("number", PizzaShop.generateOrderID())
				.param("pizzashop", pizzaShop)
				.param("customer", (String) customer.getProperty("email"))
				.param("size", size);
		if (crust != null && !pizzaFactory.hasCrust(crust)) {
			return RestResponse.BAD;
		} else {
			task.param("crust", crust);
		}
		if (cheese != null && !pizzaFactory.hasCheese(cheese)) {
			return RestResponse.BAD;
		} else if (cheese != null) {
			task.param("cheese", cheese);
		}
		if (sauce != null && !pizzaFactory.hasSauce(sauce)) {
			return RestResponse.BAD;
		} else if (sauce != null) {
			task.param("sauce", sauce);
		}
		if (meat1 != null && !pizzaFactory.hasMeat(meat1)) {
			return RestResponse.BAD;
		} else if (meat1 != null) {
			task.param("meat1", meat1);
		}
		if (meat2 != null && !pizzaFactory.hasMeat(meat2)) {
			return RestResponse.BAD;
		} else if (meat2 != null) {
			task.param("meat2", meat2);
		}
		if (meat3 != null && !pizzaFactory.hasMeat(meat3)) {
			return RestResponse.BAD;
		} else if (meat3 != null) {
			task.param("meat3", meat3);
		}
		if (veg1 != null && !pizzaFactory.hasVeg(veg1)) {
			return RestResponse.BAD;
		} else if (veg1 != null) {
			task.param("veg1", veg1);
		}
		if (veg2 != null && !pizzaFactory.hasVeg(veg2)) {
			return RestResponse.BAD;
		} else if (veg2 != null) {
			task.param("veg2", veg2);
		}
		if (veg3 != null && !pizzaFactory.hasVeg(veg3)) {
			return RestResponse.BAD;
		} else if (veg3 != null) {
			task.param("veg3", veg3);
		}
		task.param("date", new Date().toString());
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(task);
		return RestResponse.OK;
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
	private Response updateOrder(String token, String num, String status) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		if (num == null || status == null) {
			return RestResponse.BAD;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key p_key = KeyFactory.createKey("PizzaShop", token);
		try {
			Entity pizzaShop = datastore.get(p_key);
			Key o_key = KeyFactory.createKey("Order", num);
			Entity order = datastore.get(o_key);
			if (!((String) pizzaShop.getProperty("identifier"))
					.equals((String) order.getProperty("pizzashop"))) {
				return RestResponse.BAD;
			}
			Queue queue = QueueFactory.getDefaultQueue();
			queue.add(TaskOptions.Builder.withUrl("/worker")
					.param("number", num).param("status", status));
			response = RestResponse.OK;
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;
	}
}
