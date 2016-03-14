/**
 * 
 */
package pizzashop;

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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import order.PizzaShopOrderResource;
import rest.RestResponse;
import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.GeoRegion.Circle;
import com.google.appengine.api.datastore.Query.StContainsFilter;

/**
 * PizzaShop REST service
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

	@Path("/city/{city}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getShopByCity(@PathParam("city") String city) {
		Filter cityFilter = new FilterPredicate("identifier",
				FilterOperator.EQUAL, city);
		Query q = new Query("PizzaShop").setFilter(cityFilter);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<PizzaShop> pizzashops = new ArrayList<PizzaShop>();
		for (Entity result : pq.asIterable()) {
			PizzaShop pizzaShop = entityToPizzaShop(result);
			pizzashops.add(pizzaShop);
		}
		GenericEntity<List<PizzaShop>> list = new GenericEntity<List<PizzaShop>>(
				pizzashops) {
		};
		return RestResponse.OK(list);
	}

	@Path("/center/{latlnt}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getShopbyCenter(@PathParam("latlnt") String latlnt) {
		String[] latlnt_split = latlnt.split(",");
		GeoPt center = new GeoPt(Float.valueOf(latlnt_split[0]),
				Float.valueOf(latlnt_split[1]));
		double radius = 5000;
		Filter f = new StContainsFilter("location", new Circle(center, radius));
		Query q = new Query("PizzaShop").setFilter(f);
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		PreparedQuery pq = datastore.prepare(q);
		List<PizzaShop> pizzashops = new ArrayList<PizzaShop>();
		for (Entity result : pq.asIterable()) {
			PizzaShop pizzaShop = entityToPizzaShop(result);
			pizzashops.add(pizzaShop);
		}
		GenericEntity<List<PizzaShop>> list = new GenericEntity<List<PizzaShop>>(
				pizzashops) {
		};
		return RestResponse.OK(list);
	}

	/**
	 * Get the current pizzashop's profile
	 * 
	 * @return PizzaShop profile
	 */
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getCurrentPizashop() {
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
	public Response getPizashop(@PathParam("token") String token) {
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
	public Response getAllPizashops() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query q = new Query("PizzaShop");
		PreparedQuery pq = datastore.prepare(q);
		List<PizzaShop> pizzashops = new ArrayList<PizzaShop>();
		for (Entity result : pq.asIterable()) {
			PizzaShop pizzaShop = entityToPizzaShop(result);
			pizzashops.add(pizzaShop);
		}
		GenericEntity<List<PizzaShop>> list = new GenericEntity<List<PizzaShop>>(
				pizzashops) {
		};
		return RestResponse.OK(list);
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
	public Response getPizzashopByName(@PathParam("name") String name) {
		if (name == null) {
			return RestResponse.NOT_FOUND;
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
		GenericEntity<List<PizzaShop>> list = new GenericEntity<List<PizzaShop>>(
				pizzashops) {
		};
		return RestResponse.OK(list);
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
	public Response getPizzaShopByToken(
			@PathParam("identifier") String identifier) {
		if (identifier == null) {
			return RestResponse.NOT_FOUND;
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
			response = RestResponse.OK(pizzaShop);
		} else {
			response = RestResponse.NOT_FOUND;
		}
		return response;
	}

	/**
	 * Add a PizzaShop into datastore with form
	 * 
	 * @param identifier
	 *            PizzaShop identifier
	 * @param name
	 *            PizzaShop name
	 * @param phone
	 *            PizzaShop phone
	 * @param city
	 *            PizzaShop city
	 * @param latitude
	 *            PizzaShop latitude
	 * @param longitude
	 *            PizzaShop longitude
	 * @return Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response postPizzaShop(@FormParam("identifier") String identifier,
			@FormParam("name") String name, @FormParam("phone") String phone,
			@FormParam("city") String city,
			@FormParam("latitude") String latitude,
			@FormParam("longitude") String longitude) {
		return newPizzaShop(identifier, name, phone, city, latitude, longitude);
	}

	/**
	 * Add a PizzaShop into datastore with form
	 * 
	 * @param pizzaShop
	 *            PizzaShop
	 * @return Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postPizzaShop(PizzaShop pizzaShop) {
		return newPizzaShop(pizzaShop.getIdentifier(), pizzaShop.getName(),
				pizzaShop.getPhone(), pizzaShop.getCity(),
				String.valueOf(pizzaShop.getLatitude()),
				String.valueOf(pizzaShop.getLongitude()));
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
	 * @param phone
	 *            PizzaShop phone
	 * @param city
	 *            PizzaShop city
	 * @param latitude
	 *            PizzaShop latitude
	 * @param longitude
	 *            PizzaShop longitude
	 * @return Response
	 */
	@Path("/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updatePizzaShopXML(@PathParam("token") String token,
			@FormParam("identifier") String identifier,
			@FormParam("name") String name, @FormParam("phone") String phone,
			@FormParam("city") String city,
			@FormParam("latitude") String latitude,
			@FormParam("longitude") String longitude) {
		return updatePizzaShop(token, identifier, name, phone, city, latitude,
				longitude);
	}

	/**
	 * Update PizzaShop into datastore with JSON using token
	 * 
	 * @param token
	 *            PizzaShop token
	 * @param pizzaShop
	 *            PizzaShop
	 * @return Response
	 */
	@Path("/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updatePizzaShopJSON(@PathParam("token") String token,
			PizzaShop pizzaShop) {
		return updatePizzaShop(token, pizzaShop.getIdentifier(),
				pizzaShop.getName(), pizzaShop.getPhone(), pizzaShop.getCity(),
				String.valueOf(pizzaShop.getLatitude()),
				String.valueOf(pizzaShop.getLongitude()));
	}

	/**
	 * Delete current PizzaShop
	 * 
	 * @return Response
	 */
	@DELETE
	public Response deletePizzaShop() {
		String token = UserUtils.getCurrentUserObscureID();
		return removePizzaShop(token);
	}

	/**
	 * Delete PizzaShop with token for curl
	 * 
	 * @param token
	 *            PizzaShop token
	 * @return Response
	 */
	@Path("/authorize/{token}")
	@DELETE
	public Response deletePizzaShop(@PathParam("token") String token) {
		return removePizzaShop(token);
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
	 * @return Response
	 */
	private Response findPizzaShop(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaShop", token);
		PizzaShop pizzaShop = null;
		try {
			Entity entity = datastore.get(key);
			pizzaShop = entityToPizzaShop(entity);
			response = RestResponse.OK(pizzaShop);
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;
	}

	/**
	 * Create/Update new PizzaShop
	 * 
	 * @param name
	 *            PizzaShop name
	 * @param phone
	 *            PizzaShop phone
	 * @param city
	 *            PizzaShop city
	 * 
	 * @param latitude
	 *            Customer latitude
	 * @param longitude
	 *            Customer longitude
	 * @return Response
	 */
	private Response newPizzaShop(String identifier, String name, String phone,
			String city, String latitude, String longitude) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			return RestResponse.FORBIDDEN;
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
			if (phone != null) {
				entity.setProperty("phone", phone);
			}
			if (city != null) {
				entity.setProperty("city", city);
			}

			datastore.put(entity);
			if (latitude != null && longitude != null) {
				GeoPt location = new GeoPt(Float.valueOf(latitude),
						Float.valueOf(longitude));
				entity.setProperty("location", location);
			}
			datastore.put(entity);
		}
		return RestResponse.OK;
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
	 * 
	 * @param phone
	 *            PizzaShop phone
	 * @param city
	 *            PizzaShop city
	 * @param latitude
	 *            Customer latitude
	 * @param longitude
	 *            Customer longitude
	 * @return Response
	 */
	private Response updatePizzaShop(String token, String identifier,
			String name, String phone, String city, String latitude,
			String longitude) {
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
			if (phone != null) {
				entity.setProperty("phone", phone);
			}
			if (city != null) {
				entity.setProperty("city", city);
			}
			if (latitude != null && longitude != null) {
				GeoPt location = new GeoPt(Float.valueOf(latitude),
						Float.valueOf(longitude));
				entity.setProperty("location", location);
			}
			datastore.put(entity);
			response = RestResponse.OK;
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;
	}

	/**
	 * Delete PizzaShop
	 * 
	 * @param token
	 *            PizzaShop token
	 * @return Response
	 */
	private Response removePizzaShop(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("PizzaShop", token);
			datastore.delete(key);
			response = RestResponse.OK;
		} catch (Exception e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;
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
		pizzaShop.setToken((String) entity.getKey().getName());
		pizzaShop.setIdentifier((String) entity.getProperty("identifier"));
		pizzaShop.setEmail((String) entity.getProperty("email"));
		pizzaShop.setName((String) entity.getProperty("name"));
		pizzaShop.setCity((String) entity.getProperty("city"));
		pizzaShop.setPhone((String) entity.getProperty("phone"));
		GeoPt location = (GeoPt) entity.getProperty("location");
		if (location != null) {
			pizzaShop.setLatitude((double) location.getLatitude());
			pizzaShop.setLongitude((double) location.getLongitude());
		}
		return pizzaShop;
	}
}