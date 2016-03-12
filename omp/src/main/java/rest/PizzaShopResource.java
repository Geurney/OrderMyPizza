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
	 * @return PizzaShop profile
	 */
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public PizzaShop getCurrentPizashop() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return findPizzaShop(hash_uid);
	}

	/**
	 * Get the pizzashop's profile with token
	 * 
	 * @param token
	 *            Pizza Shop token
	 * @return Pizzashop profile
	 */
	@Path("/authorize/{token}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public PizzaShop getPizashop(@PathParam("token") String token) {
		return findPizzaShop(token);
	}

	/**
	 * Get all pizzashops
	 * 
	 * @return list of pizzashops
	 */
	@Path("/all")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public List<PizzaShop> getAllPizashops() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query q = new Query("PizzaShop");
		PreparedQuery pq = datastore.prepare(q);
		List<PizzaShop> pizzashops = new ArrayList<PizzaShop>();
		for (Entity result : pq.asIterable()) {
			PizzaShop pizzaShop = entityToPizzaShop(result);
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
	@Path("/findbyname/{name}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public List<PizzaShop> getPizzashopByName(@PathParam("name") String name) {
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
			PizzaShop pizzaShop = entityToPizzaShop(result);
			pizzashops.add(pizzaShop);
		}
		return pizzashops;
	}

	/**
	 * Get PizzaShop by identifier
	 * 
	 * @param identifier
	 *            PizzaShop identifier
	 * @return PizzaShop
	 */
	@Path("/findbyidentifier/{identifier}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public PizzaShop getPizzaShopByToken(
			@PathParam("identifier") String identifier) {
		if (identifier == null) {
			return null;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Filter identifierFilter = new FilterPredicate("identifier",
				FilterOperator.EQUAL, identifier);
		Query q = new Query("PizzaShop").setFilter(identifierFilter);
		PreparedQuery pq = datastore.prepare(q);
		Entity entity = pq.asSingleEntity();
		PizzaShop pizzaShop = null;
		if (entity != null) {
			pizzaShop = entityToPizzaShop(entity);
		}
		return pizzaShop;
	}

	/**
	 * Add a PizzaShop into datastore with form
	 * 
	 * @param identifier
	 *            PizzaShop identifier
	 * @param name
	 *            PizzaShop name
	 * @param address
	 *            PizzaShop address
	 * @param phone
	 *            PizzaShop phone
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void postPizzaShop(@FormParam("identifier") String identifier,
			@FormParam("name") String name,
			@FormParam("address") String address,
			@FormParam("phone") String phone) {
		newPizzaShop(identifier, name, address, phone);
	}

	/**
	 * Add a PizzaShop into datastore with form
	 * 
	 * @param pizzaShop
	 *            PizzaShop
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void postPizzaShop(PizzaShop pizzaShop) {
		if (pizzaShop != null) {
			newPizzaShop(pizzaShop.getIdentifier(), pizzaShop.getName(),
					pizzaShop.getAddress(), pizzaShop.getPhone());
		}
	}

	/**
	 * Update a PizzaShop with form using token
	 * 
	 * @param token
	 *            PizzaShop token
	 * @param identifier
	 *            PizzaShop identifier
	 * @param name
	 *            PizzaShop name
	 * @param address
	 *            PizzaShop address
	 * @param phone
	 *            PizzaShop phone
	 */
	@Path("/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void updatePizzaShopXML(@PathParam("token") String token,
			@FormParam("identifier") String identifier,
			@FormParam("name") String name,
			@FormParam("address") String address,
			@FormParam("phone") String phone) {
		updatePizzaShop(token, identifier, name, address, phone);
	}

	/**
	 * Update PizzaShop into datastore with JSON using token
	 * 
	 * @param token
	 *            PizzaShop token
	 * @param pizzaShop
	 *            PizzaShop
	 */
	@Path("/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updatePizzaShopJSON(@PathParam("token") String token,
			PizzaShop pizzaShop) {
		if (pizzaShop != null) {
			updatePizzaShop(token, pizzaShop.getIdentifier(),
					pizzaShop.getName(), pizzaShop.getAddress(),
					pizzaShop.getPhone());
		}
	}

	/**
	 * Delete current PizzaShop
	 * 
	 */
	@DELETE
	public void deletePizzaShop() {
		String token = UserUtils.getCurrentUserObscureID();
		removePizzaShop(token);
	}

	/**
	 * Delete PizzaShop with token for curl
	 * 
	 * @param token
	 *            PizzaShop token
	 */
	@Path("/authorize/{token}")
	@DELETE
	public void deletePizzaShop(@PathParam("token") String token) {
		removePizzaShop(token);
	}

	/**
	 * Handle order request
	 * 
	 * @return Pizza Shop Order Resource
	 */
	@Path("/order")
	public PizzaShopOrderResource handleOrder() {
		return new PizzaShopOrderResource(uriInfo, request);
	}

	/**
	 * Get the pizzashop's profile with token
	 * 
	 * @param token
	 *            Pizza Shop token
	 * @return Pizzashop profile
	 */
	private PizzaShop findPizzaShop(String token) {
		if (token == null) {
			return null;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", token);
		PizzaShop pizzaShop = null;
		try {
			Entity entity = datastore.get(key);
			pizzaShop = entityToPizzaShop(entity);
		} catch (EntityNotFoundException e) {
		}
		return pizzaShop;
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
	private void newPizzaShop(String identifier, String name, String address,
			String phone) {
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
			if (identifier != null) {
				entity.setProperty("identifier", identifier);
			}
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
	 * @param identifier
	 *            PizzaShop identifier
	 * @param name
	 *            PizzaShop name
	 * @param address
	 *            PizzaShop address
	 * @param phone
	 *            PizzaShop phone
	 */
	private void updatePizzaShop(String token, String identifier, String name,
			String address, String phone) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", token);
		try {
			Entity entity = datastore.get(key);
			if (identifier != null) {
				entity.setProperty("identifier", identifier);
			}
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

	/**
	 * Delete PizzaShop
	 * 
	 * @param token
	 *            PizzaShop token
	 */
	private void removePizzaShop(String token) {
		if (token == null) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("PizzaShop", token);
			datastore.delete(key);
		} catch (Exception e) {
		}
	}

	/**
	 * Convert entity to PizzaShop
	 * 
	 * @param entity
	 *            Entity
	 * @return PizzaShop
	 */
	public static PizzaShop entityToPizzaShop(Entity entity) {
		if (entity == null || !entity.getKind().equals("PizzaShop")) {
			return null;
		}
		PizzaShop pizzaShop = new PizzaShop();
		pizzaShop.setIdentifier((String) entity.getProperty("identifier"));
		pizzaShop.setToken((String) entity.getKey().getName());
		pizzaShop.setEmail((String) entity.getProperty("email"));
		pizzaShop.setName((String) entity.getProperty("name"));
		pizzaShop.setAddress((String) entity.getProperty("address"));
		pizzaShop.setPhone((String) entity.getProperty("phone"));
		return pizzaShop;
	}
}
