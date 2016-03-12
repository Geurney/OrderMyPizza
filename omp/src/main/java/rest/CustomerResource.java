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

import user.Customer;
import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
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
	public Customer getCustomer() {
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
	public Customer getCustomer(@PathParam("token") String token) {
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
	public List<Customer> getCustomerAh(@PathParam("token") String token) {
		List<Customer> customers = new ArrayList<Customer>();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("Admin", token);
		try {
			datastore.get(key);
			Query q = new Query("Customer");
			PreparedQuery pq = datastore.prepare(q);
			for (Entity result : pq.asIterable()) {
				Customer customer = new Customer();
				customer.setToken(result.getKey().getName());
				customer.setEmail((String) result.getProperty("email"));
				customer.setName((String) result.getProperty("name"));
				customer.setAddress((String) result.getProperty("address"));
				customer.setPhone((String) result.getProperty("phone"));
				customers.add(customer);
			}
		} catch (EntityNotFoundException e) {
		}
		return customers;
	}

	/**
	 * Add/Update new customer into datastore with form
	 * 
	 * @param name
	 *            Customer name
	 * @param address
	 *            Customer address
	 * @param phone
	 *            Customer phone
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void postCustomer(@FormParam("name") String name,
			@FormParam("address") String address,
			@FormParam("phone") String phone) {
		newCustomer(name, address, phone);
	}

	/**
	 * Add/Update new customer into datastore with JSON
	 * 
	 * @param customer
	 *            Customer
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void postCustomer(Customer customer) {
		if (customer != null) {
			newCustomer(customer.getName(), customer.getAddress(),
					customer.getPhone());
		}
	}

	/**
	 * Update the customer into datastore with form with token for curl
	 * 
	 * @param token
	 *            Customer Hash ID
	 * 
	 * @param name
	 *            Customer name
	 * @param address
	 *            Customer address
	 * @param phone
	 *            Customer phone
	 * 
	 */
	@Path("/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void updateCustomerTkXML(@PathParam("token") String token,
			@FormParam("name") String name,
			@FormParam("address") String address,
			@FormParam("phone") String phone) {
		updateCustomer(token, name, address, phone);
	}

	/**
	 * Update the customer into datastore with JSON using token
	 * 
	 * @param token
	 *            Customer Hash ID
	 * @param name
	 *            Customer name
	 * @param address
	 *            Customer address
	 * @param phone
	 *            Customer phone
	 * 
	 */
	@Path("/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateCustomerTkAppJSON(@PathParam("token") String token,
			Customer c) {
		if (c != null) {
			updateCustomer(token, c.getName(), c.getAddress(), c.getPhone());
		}
	}

	/**
	 * Delete current customer
	 * 
	 */
	@DELETE
	public void deleteCustomer() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		removeCustomer(hash_uid);
	}

	/**
	 * Delete current customer
	 * 
	 * @param token
	 *            Customer Hash ID
	 */
	@Path("/authorize/{token}")
	@DELETE
	public void deleteCustomer(@PathParam("token") String token) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		removeCustomer(hash_uid);
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
	private Customer findCustomer(String token) {
		if (token == null) {
			return null;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("Customer", token);
		Customer customer = null;
		try {
			Entity entity = datastore.get(key);
			customer = entityToObject(entity);
		} catch (EntityNotFoundException e) {
		}
		return customer;
	}

	/**
	 * Create/Update customer
	 * 
	 * @param name
	 *            Customer name
	 * @param address
	 *            Customer address
	 * @param phone
	 *            Customer phone
	 */
	private void newCustomer(String name, String address, String phone) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			return;
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
	 * Update customer with using token
	 * 
	 * @param token
	 *            Customer Hash ID
	 * @param name
	 *            Customer name
	 * @param address
	 *            Customer address
	 * @param phone
	 *            Customer phone
	 */
	private void updateCustomer(String token, String name, String address,
			String phone) {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("Customer", token);
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

	/**
	 * Delete current customer
	 * 
	 * @param token
	 *            Customer Hash ID
	 */
	private void removeCustomer(String token) {
		if (token == null) {
			return;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("Customer", token);
			datastore.delete(key);
		} catch (Exception e) {
		}
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
		customer.setAddress((String) entity.getProperty("address"));
		customer.setPhone((String) entity.getProperty("phone"));
		return customer;
	}

}
