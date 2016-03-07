/**
 * 
 */
package rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import pizza.PizzaCheese;
import pizza.PizzaCrust;
import pizza.PizzaSauce;
import pizza.PizzaToppingMeat;
import pizza.PizzaToppingVeg;
import pizzashop.PizzaFactory;
import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * Pizza Factory REST service
 * 
 * @author Geurney
 *
 */
@Path("/pizzafactory")
public class PizzaFactoryResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	/**
	 * Get the current Pizza Factory profile
	 * 
	 * @return Pizza Factory profile
	 */
	@SuppressWarnings("unchecked")
	@GET
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML,
			MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Response getPizzaFactory() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		try {
			Entity entity = datastore.get(key);
			PizzaFactory pizzaFactory = new PizzaFactory();

			List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) entity
					.getProperty("crust");
			if (crusts != null) {
				List<PizzaCrust> crustList = new ArrayList<PizzaCrust>();
				for (EmbeddedEntity e : crusts) {
					PizzaCrust crust = new PizzaCrust();
					crust.setIdentifier((String) e.getProperty("identifier"));
					crust.setDescription((String) e.getProperty("description"));
					List<Double> costs = (List<Double>) e.getProperty("costs");
					List<Double> prices = (List<Double>) e
							.getProperty("prices");
					crust.setCosts(costs);
					crust.setPrices(prices);
					crustList.add(crust);
				}
				pizzaFactory.addCrust(crustList);
			}

			List<EmbeddedEntity> cheeses = (List<EmbeddedEntity>) entity
					.getProperty("cheese");
			if (cheeses != null) {
				List<PizzaCheese> cheeseList = new ArrayList<PizzaCheese>();
				for (EmbeddedEntity e : cheeses) {
					PizzaCheese cheese = new PizzaCheese();
					cheese.setIdentifier((String) e.getProperty("identifier"));
					cheese.setDescription((String) e.getProperty("description"));
					List<Double> costs = (List<Double>) e.getProperty("costs");
					List<Double> prices = (List<Double>) e
							.getProperty("prices");
					cheese.setCosts(costs);
					cheese.setPrices(prices);
					cheeseList.add(cheese);
				}
				pizzaFactory.addCheese(cheeseList);
			}

			List<EmbeddedEntity> sauces = (List<EmbeddedEntity>) entity
					.getProperty("sauce");
			if (sauces != null) {
				List<PizzaSauce> sauceList = new ArrayList<PizzaSauce>();
				for (EmbeddedEntity e : sauces) {
					PizzaSauce sauce = new PizzaSauce();
					sauce.setIdentifier((String) e.getProperty("identifier"));
					sauce.setDescription((String) e.getProperty("description"));
					List<Double> costs = (List<Double>) e.getProperty("costs");
					List<Double> prices = (List<Double>) e
							.getProperty("prices");
					sauce.setCosts(costs);
					sauce.setPrices(prices);
					sauceList.add(sauce);
				}
				pizzaFactory.addSauce(sauceList);
			}
			
			List<EmbeddedEntity> meats = (List<EmbeddedEntity>) entity
					.getProperty("meat");
			if (meats != null) {
				List<PizzaToppingMeat> meatList = new ArrayList<PizzaToppingMeat>();
				for (EmbeddedEntity e : meats) {
					PizzaToppingMeat meat = new PizzaToppingMeat();
					meat.setIdentifier((String) e.getProperty("identifier"));
					meat.setDescription((String) e.getProperty("description"));
					List<Double> costs = (List<Double>) e.getProperty("costs");
					List<Double> prices = (List<Double>) e
							.getProperty("prices");
					meat.setCosts(costs);
					meat.setPrices(prices);
					meatList.add(meat);
				}
				pizzaFactory.addToppingMeat(meatList);
			}

			List<EmbeddedEntity> vegs = (List<EmbeddedEntity>) entity
					.getProperty("veg");
			if (vegs != null) {
				List<PizzaToppingVeg> vegList = new ArrayList<PizzaToppingVeg>();
				for (EmbeddedEntity e : vegs) {
					PizzaToppingVeg veg = new PizzaToppingVeg();
					veg.setIdentifier((String) e.getProperty("identifier"));
					veg.setDescription((String) e.getProperty("description"));
					List<Double> costs = (List<Double>) e.getProperty("costs");
					List<Double> prices = (List<Double>) e
							.getProperty("prices");
					veg.setCosts(costs);
					veg.setPrices(prices);
					vegList.add(veg);
				}
				pizzaFactory.addToppingVeg(vegList);
			}

			response = Response.ok(pizzaFactory).build();
		} catch (EntityNotFoundException e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Pizza Factory not found!").build();
		}
		return response;
	}

	/**
	 * Add a new Pizza Factory into datastore
	 */
	@POST
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	public Response newPostPizzaFactory() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		Entity pizzaFactory = null;
		try {
			pizzaFactory = datastore.get(key);
			response = Response.ok("Pizza Factory already exists!").build();
		} catch (EntityNotFoundException e) {
			pizzaFactory = new Entity("PizzaFactory", hash_uid);
			datastore.put(pizzaFactory);
			response = Response.ok("Pizza Factory is created!").build();
		}
		return response;
	}

	/**
	 * Add a new Pizza Factory into datastore
	 */
	@PUT
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	public Response newPutPizzaFactory() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
		Entity pizzaFactory = null;
		try {
			pizzaFactory = datastore.get(key);
			response = Response.ok("Pizza Factory already exists!").build();
		} catch (EntityNotFoundException e) {
			pizzaFactory = new Entity("PizzaFactory", hash_uid);
			datastore.put(pizzaFactory);
			response = Response.ok("Pizza Factory is created!").build();
		}
		return response;
	}

	/**
	 * Delete current PizzaFactory
	 * 
	 */
	@DELETE
	@Produces({ MediaType.TEXT_PLAIN, MediaType.TEXT_XML, MediaType.TEXT_HTML })
	public Response deleteCustomer() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		if (hash_uid == null) {
			response = Response.status(Response.Status.FORBIDDEN)
					.type(MediaType.TEXT_PLAIN).entity("You must log in!")
					.build();
			return response;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
			datastore.delete(key);
			response = Response.ok("Pizza Factory is deleted!").build();
		} catch (Exception e) {
			response = Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.TEXT_PLAIN)
					.entity("Pizza Factory not found!").build();
		}
		return response;
	}

	/**
	 * For Pizza Crusts Resource
	 * 
	 * @return Crusts Resource
	 */
	@Path("/crust")
	public PizzaCrustsResource handlePizzaCrusts() {
		return new PizzaCrustsResource();
	}

	/**
	 * For Pizza Cheeses Resource
	 * 
	 * @return Cheeses Resource
	 */
	@Path("/cheese")
	public PizzaCheesesResource handlePizzaCheeses() {
		return new PizzaCheesesResource();
	}

	/**
	 * For Pizza Sauces Resource
	 * 
	 * @return Sauces Resource
	 */
	@Path("/sauce")
	public PizzaSaucesResource handlePizzaSauces() {
		return new PizzaSaucesResource();
	}

	/**
	 * For Pizza Meat Toppings Resource
	 * 
	 * @return Pizza Meat Toppings Resource
	 */
	@Path("/meat")
	public PizzaToppingMeatsResource handlePizzaToppingMeats() {
		return new PizzaToppingMeatsResource();
	}

	/**
	 * For Pizza Vegetable Toppings Resource
	 * 
	 * @return Pizza Vegetable Toppings Resource
	 */
	@Path("/veg")
	public PizzaToppingVegsResource handlePizzaToppingVegs() {
		return new PizzaToppingVegsResource();
	}

}
