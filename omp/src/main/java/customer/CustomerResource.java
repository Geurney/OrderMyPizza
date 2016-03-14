/**
 * 
 */
package customer;

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

import order.CustomerOrderResource;
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

/**
 * Customer REST service
 * 
 * @author Geurney
 *
 */
@Path("/customer")
public class CustomerResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Get the current customer's profile
	 * 
	 * @return Customer profile
	 */
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getCustomer() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return findCustomer(hash_uid);
	}

	/**
	 * Get the customer's profile with token for curl
	 * 
	 * @param token
	 *            Customer Hash ID
	 * @return Customer Profile
	 */
	@Path("/authorize/{token}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getCustomer(@PathParam("token") String token) {
		return findCustomer(token);
	}

	/**
	 * Get all customers' profiles for as admin
	 * 
	 * @param token
	 *            Admin token
	 * @return Customers' profiles
	 */
	@Path("/_ah/{token}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getCustomerAh(@PathParam("token") String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("Admin", token);
		try {
			datastore.get(key);
			Query q = new Query("Customer");
			PreparedQuery pq = datastore.prepare(q);
			List<Customer> customers = new ArrayList<Customer>();
			for (Entity result : pq.asIterable()) {
				Customer customer = new Customer();
				customer.setToken(result.getKey().getName());
				customer.setEmail((String) result.getProperty("email"));
				customer.setName((String) result.getProperty("name"));
				customer.setCity((String) result.getProperty("city"));
				customer.setPhone((String) result.getProperty("phone"));
				customers.add(customer);
			}
			GenericEntity<List<Customer>> list = new GenericEntity<List<Customer>>(
					customers) {
			};
			response = RestResponse.OK(list);
		} catch (EntityNotFoundException e) {
			response = RestResponse.FORBIDDEN;
		}
		return response;
	}

	/**
	 * Add/Update new customer into datastore with form
	 * 
	 * @param name
	 *            Customer name
	 *
	 * @param phone
	 *            Customer phone
	 * @param city
	 *            Customer city
	 * @param latitude
	 *            Customer latitude
	 * @param longitude
	 *            Customer longitude
	 * @return Response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response postCustomer(@FormParam("name") String name,
			@FormParam("phone") String phone, @FormParam("city") String city,
			@FormParam("latitude") String latitude,
			@FormParam("longitude") String longitude) {
		return newCustomer(name, phone, city, latitude, longitude);
	}

	/**
	 * Add/Update new customer into datastore with JSON
	 * 
	 * @param customer
	 *            Customer
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postCustomer(Customer customer) {
		return newCustomer(customer.getName(), customer.getPhone(),
				customer.getCity(), String.valueOf(customer.getLatitude()),
				String.valueOf(customer.getLongitude()));
	}

	/**
	 * Update the customer into datastore with form with token for curl
	 * 
	 * @param token
	 *            Customer Hash ID
	 * 
	 * @param name
	 *            Customer name
	 * 
	 * @param phone
	 *            Customer phone
	 * @param latitude
	 *            Customer latitude
	 * @param longitude
	 *            Customer longitude
	 * @param city
	 *            Customer city
	 * @return Response
	 * 
	 */
	@Path("/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response updateCustomerTkXML(@PathParam("token") String token,
			@FormParam("name") String name, @FormParam("phone") String phone,
			@FormParam("city") String city,
			@FormParam("latitude") String latitude,
			@FormParam("longitude") String longitude) {
		return updateCustomer(token, name, phone, city, latitude, longitude);
	}

	/**
	 * Update the customer into datastore with JSON using token
	 * 
	 * @param token
	 *            Customer Hash ID
	 * @param customer
	 *            Customer
	 * @param phone
	 *            Customer phone
	 * @return Response
	 * 
	 */
	@Path("/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCustomerTkAppJSON(@PathParam("token") String token,
			Customer customer) {
		return updateCustomer(token, customer.getName(), customer.getPhone(),
				customer.getCity(), String.valueOf(customer.getLatitude()),
				String.valueOf(customer.getLongitude()));
	}

	/**
	 * Delete current customer
	 * 
	 * @return Response
	 */
	@DELETE
	public Response deleteCustomer() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return removeCustomer(hash_uid);
	}

	/**
	 * Delete current customer
	 * 
	 * @param token
	 *            Customer Hash ID
	 * @return Response
	 */
	@Path("/authorize/{token}")
	@DELETE
	public Response deleteCustomer(@PathParam("token") String token) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return removeCustomer(hash_uid);
	}

	/**
	 * Handle customer order requests
	 * 
	 * @return Customer Order Resource
	 */
	@Path("/order")
	public CustomerOrderResource handleOrders() {
		return new CustomerOrderResource(uriInfo, request);
	}

	/**
	 * Get the customer's profile
	 * 
	 * @param token
	 *            Customer token
	 * @return Customer profile
	 */
	private Response findCustomer(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("Customer", token);
		Customer customer = null;
		try {
			Entity entity = datastore.get(key);
			customer = entityToObject(entity);
			response = RestResponse.OK(customer);
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;
	}

	/**
	 * Create/Update customer
	 * 
	 * @param name
	 *            Customer name
	 * 
	 * @param phone
	 *            Customer phone
	 * @param city
	 *            Customer city
	 * @param latitude
	 *            Customer latitude
	 * @param longitude
	 *            Customer longitude
	 * @return Response
	 */
	private Response newCustomer(String name, String phone, String city,
			String latitude, String longitude) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			return RestResponse.FORBIDDEN;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("Customer", hash_uid);
		Entity entity = null;
		try {
			entity = datastore.get(key);
		} catch (EntityNotFoundException e) {
			entity = new Entity("Customer", hash_uid);
			entity.setProperty("email", UserUtils.getCurrentUserEmail());
		} finally {
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
		}
		return RestResponse.OK;
	}

	/**
	 * Update customer with using token
	 * 
	 * @param token
	 *            Customer Hash ID
	 * @param name
	 *            Customer name
	 * @param phone
	 *            Customer phone
	 * @param city
	 *            Customer city
	 * @param latitude
	 *            Customer latitude
	 * @param longitude
	 *            Customer longitude
	 * @return Response
	 */
	private Response updateCustomer(String token, String name, String phone,
			String city, String latitude, String longitude) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("Customer", token);
		try {
			Entity entity = datastore.get(key);
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
	 * Delete current customer
	 * 
	 * @param token
	 *            Customer Hash ID
	 * @return Response
	 */
	private Response removeCustomer(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("Customer", token);
			datastore.delete(key);
			response = RestResponse.OK;
		} catch (Exception e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;
	}

	/**
	 * Convert an entity to object.
	 * 
	 * @param entity
	 *            Entity
	 * @return Customer
	 */
	public static Customer entityToObject(Entity entity) {
		if (entity == null || !entity.getKind().equals("Customer")) {
			return null;
		}
		Customer customer = new Customer();
		customer.setToken((String) entity.getProperty("token"));
		customer.setEmail((String) entity.getProperty("email"));
		customer.setName((String) entity.getProperty("name"));
		customer.setCity((String) entity.getProperty("city"));
		customer.setPhone((String) entity.getProperty("phone"));
		GeoPt location = (GeoPt) entity.getProperty("location");
		if (location != null) {
			customer.setLatitude((double) location.getLatitude());
			customer.setLongitude((double) location.getLongitude());
		}
		return customer;
	}

}
