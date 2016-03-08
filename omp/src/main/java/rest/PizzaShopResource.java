/**
 * 
 */
package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

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
 * Pizzashop REST service
 * 
 * @author Geurney
 *
 */
@Path("/pizzashop")
public class PizzaShopResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Get the current pizzashop's profile
	 * 
	 * @return Pizzashop profile
	 */
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public PizzaShop getCurrentPizashop() {
		PizzaShop pizzaShop = null;
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			return null;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", hash_uid);
		try {
			Entity entity = datastore.get(key);
			pizzaShop = new PizzaShop();
			pizzaShop.setToken(hash_uid);
			pizzaShop.setEmail((String) entity.getProperty("email"));
			pizzaShop.setName((String) entity.getProperty("name"));
			pizzaShop.setAddress((String) entity.getProperty("address"));
			pizzaShop.setPhone((String) entity.getProperty("phone"));
		} catch (EntityNotFoundException e) {
		}
		return pizzaShop;
	}
	
	/**
	 * Get the pizzashop's profile with token
	 * 
	 * @return Pizzashop profile
	 */
	@Path("{token}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public PizzaShop getPizashop(@PathParam("token") String token) {
		PizzaShop pizzaShop = null;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", token);
		try {
			Entity entity = datastore.get(key);
			pizzaShop = new PizzaShop();
			pizzaShop.setToken(token);
			pizzaShop.setEmail((String) entity.getProperty("email"));
			pizzaShop.setName((String) entity.getProperty("name"));
			pizzaShop.setAddress((String) entity.getProperty("address"));
			pizzaShop.setPhone((String) entity.getProperty("phone"));
		} catch (EntityNotFoundException e) {
		}
		return pizzaShop;
	}


	/**
	 * Get all pizzashops
	 * 
	 * @return list of pizzashops
	 */
	@Path("/all")
	@GET
	@Produces({MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<PizzaShop> getAllPizashops() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query q = new Query("PizzaShop");
		PreparedQuery pq = datastore.prepare(q);
		List<PizzaShop> pizzashops = new ArrayList<PizzaShop>();
		for (Entity result : pq.asIterable()) {
			PizzaShop pizzaShop = new PizzaShop();
			pizzaShop.setToken(result.getKey().getName());
			pizzaShop.setName((String) result.getProperty("name"));
			pizzaShop.setAddress((String) result.getProperty("address"));
			pizzaShop.setPhone((String) result.getProperty("phone"));
			pizzashops.add(pizzaShop);
		}
		return pizzashops;
	}

	/**
	 * Get PizzaShop(s) by name
	 * 
	 * @param name
	 *            PizzaShop name
	 * @return List of pizzashops
	 */
	@Path("/findbyname")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public List<PizzaShop> getPizzashopByName(@QueryParam("name") String name) {
		if (name == null) {
			return null;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Filter nameFilter = new FilterPredicate("name", FilterOperator.EQUAL,
				name);
		Query q = new Query("PizzaShop").setFilter(nameFilter);
		PreparedQuery pq = datastore.prepare(q);
		List<PizzaShop> pizzashops = new ArrayList<PizzaShop>();
		for (Entity result : pq.asIterable()) {
			PizzaShop pizzaShop = new PizzaShop();
			pizzaShop.setToken(result.getKey().getName());
			pizzaShop.setName((String) result.getProperty("name"));
			pizzaShop.setAddress((String) result.getProperty("address"));
			pizzaShop.setPhone((String) result.getProperty("phone"));
			pizzashops.add(pizzaShop);
		}
		return pizzashops;
	}

	/**
	 * Get PizzaShop by token
	 * 
	 * @param token
	 *            PizzaShop Hash ID
	 * @return PizzaShop
	 */
	@Path("/findbytoken")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public PizzaShop getPizzaShopByToken(@QueryParam("token") String token) {
		if(token == null) {
			return null;
		}
		PizzaShop pizzaShop = null;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", token);
		try {
			Entity entity = datastore.get(key);
			pizzaShop = new PizzaShop();
			pizzaShop.setToken(token);
			pizzaShop.setEmail((String) entity.getProperty("email"));
			pizzaShop.setName((String) entity.getProperty("name"));
			pizzaShop.setAddress((String) entity.getProperty("address"));
			pizzaShop.setPhone((String) entity.getProperty("phone"));
		} catch (EntityNotFoundException e) {
		}
		return pizzaShop;
	}

	/**
	 * Add a PizzaShop into datastore with form
	 * 
	 * @param name
	 *            PizzaShop name
	 * @param address
	 *            PizzaShop address
	 * @param phone
	 *            PizzaShop phone
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newPizzaShopXML(@FormParam("name") String name,
			@FormParam("address") String address,
			@FormParam("phone") String phone) {
		newPizzaShop(name, address, phone);
	}

	/**
	 * Add a PizzaShop into datastore with form
	 * 
	 * @param pizzaShop
	 *            PizzaShop
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void newPizzaShopJSON(PizzaShop pizzaShop) {
		if (pizzaShop != null) {
			newPizzaShop(pizzaShop.getName(), pizzaShop.getAddress(),
					pizzaShop.getPhone());
		}
	}

	/**
	 * Update a PizzaShop with form using token
	 * 
	 * @param name
	 *            PizzaShop name
	 * @param address
	 *            PizzaShop address
	 * @param phone
	 *            PizzaShop phone
	 */
	@Path("{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void updatePizzaShopXML(@PathParam("token") String token,
			@FormParam("name") String name,
			@FormParam("address") String address,
			@FormParam("phone") String phone) {
		updatePizzaShop(token, name, address, phone);
	}

	/**
	 * Update PizzaShop into datastore with JSON using token
	 * 
	 * @param pizzaShop
	 *            PizzaShop
	 */
	@Path("{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updatePizzaShopJSON(@PathParam("token") String token,
			PizzaShop pizzaShop) {
		if (pizzaShop != null) {
			updatePizzaShop(token, pizzaShop.getName(), pizzaShop.getAddress(),
					pizzaShop.getPhone());
		}
	}

	/**
	 * Delete current PizzaShop
	 * 
	 */
	@DELETE
	public void deletePizzaShop() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("PizzaShop", hash_uid);
			datastore.delete(key);
		} catch (Exception e) {
		}
	}

	/**
	 * Create/Update new PizzaShop
	 * 
	 * @param name
	 *            PizzaShop name
	 * @param address
	 *            PizzaShop address
	 * @param phone
	 *            PizzaShop phone
	 */
	private void newPizzaShop(String name, String address, String phone) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", hash_uid);
		Entity entity = null;
		try {
			entity = datastore.get(key);
		} catch (EntityNotFoundException e) {
			entity = new Entity("PizzaShop", hash_uid);
			entity.setProperty("email", UserUtils.getCurrentUserEmail());
		} finally {
			if (name != null) {
				entity.setProperty("name", name);
			}
			if (address != null) {
				entity.setProperty("address", address);
			}
			if (phone != null) {
				entity.setProperty("phone", phone);
			}
			datastore.put(entity);
		}
	}

	/**
	 * Update PizzaShop with using token
	 * 
	 * @param token
	 *            PizzaShop Hash ID
	 * @param name
	 *            PizzaShop name
	 * @param address
	 *            PizzaShop address
	 * @param phone
	 *            PizzaShop phone
	 */
	private void updatePizzaShop(String token, String name, String address,
			String phone) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", token);
		try {
			Entity entity = datastore.get(key);
			if (name != null) {
				entity.setProperty("name", name);
			}
			if (address != null) {
				entity.setProperty("address", address);
			}
			if (phone != null) {
				entity.setProperty("phone", phone);
			}
			datastore.put(entity);
		} catch (EntityNotFoundException e) {
		}
	}

}
