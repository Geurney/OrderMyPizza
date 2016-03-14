/**
 * 
 */
package pizzafactory;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
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

import pizza.PizzaComponentsResource;
import pizza.cheese.PizzaCheese;
import pizza.cheese.PizzaCheesesResource;
import pizza.crust.PizzaCrust;
import pizza.crust.PizzaCrustsResource;
import pizza.meat.PizzaToppingMeat;
import pizza.meat.PizzaToppingMeatsResource;
import pizza.sauce.PizzaSauce;
import pizza.sauce.PizzaSaucesResource;
import pizza.veg.PizzaToppingVeg;
import pizza.veg.PizzaToppingVegsResource;
import rest.RestResponse;
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
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getPizzaFactory() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return findPizzaFactory(hash_uid);
	}

	/**
	 * Get the current Pizza Factory profile
	 * 
	 * @return Pizza Factory profile
	 */
	@Path("/findbyidentifier/{identifier}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getPizzaFactory(@PathParam("identifier") String identifier) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return findPizzaFactory(hash_uid);
	}

	/**
	 * Add a new Pizza Factory into datastore. This operation fails on following
	 * condition: 1.Pizza Shop not exists. 2.Pizza Shop doesn't have identifier.
	 * 3. Pizza Factory already exsits.
	 */
	@POST
	public Response postPizzaFactory() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return newPizzaFactory(hash_uid);
	}

	/**
	 * Add a new Pizza Factory into datastore with tokne for curl. This
	 * operation fails on following condition: 1.Pizza Shop not exists. 2.Pizza
	 * Shop doesn't have identifier. 3. Pizza Factory already exsits.
	 */
	@Path("/authorize/{token}")
	@POST
	public Response postPizzaFactory(@PathParam("token") String token) {
		return newPizzaFactory(token);
	}

	/**
	 * Delete current PizzaFactory
	 * 
	 */
	@DELETE
	public Response deletePizzaShop() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return removePizzaShop(hash_uid);
	}

	/**
	 * Delete Pizza Factory with token for curl
	 * 
	 */
	@Path("/authorize/{token}")
	@DELETE
	public Response deletePizzaShop(@PathParam("token") String token) {
		return removePizzaShop(token);
	}

	/**
	 * Dispatch PizzaComponentsResource request
	 * 
	 * @param type
	 *            PizzaComponent type
	 * @return PizzaComponentsResource
	 */
	@SuppressWarnings("rawtypes")
	@Path("{type}")
	public PizzaComponentsResource pizzaComponentsResourceDispatch(
			@PathParam("type") String type) {
		switch (type) {
		case "crust":
			return handlePizzaCrusts();
		case "cheese":
			return handlePizzaCheeses();
		case "sauce":
			return handlePizzaSauces();
		case "meat":
			return handlePizzaToppingMeats();
		case "veg":
			return handlePizzaToppingVegs();
		}
		return null;
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

	/**
	 * Get the current Pizza Factory profile
	 * 
	 * @return Pizza Factory profile
	 */
	private Response findPizzaFactory(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		PizzaFactory pizzaFactory = null;
		try {
			Entity entity = datastore.get(key);
			pizzaFactory = entityToObject(entity);
			response = RestResponse.OK(pizzaFactory);
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;
	}

	/**
	 * Add a new Pizza Factory into datastore with tokne for curl. This
	 * operation fails on following condition: 1.Pizza Shop not exists. 2.Pizza
	 * Shop doesn't have identifier. 3. Pizza Factory already exsits.
	 */
	private Response newPizzaFactory(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key shop_key = KeyFactory.createKey("PizzaShop", token);
		try {
			Entity pizzaShop = datastore.get(shop_key);
			Key factory_key = KeyFactory.createKey("PizzaFactory", token);
			Entity pizzaFactory = null;
			try {
				pizzaFactory = datastore.get(factory_key);
			} catch (EntityNotFoundException e) {
				pizzaFactory = new Entity("PizzaFactory", token);
				String id = (String) pizzaShop.getProperty("identifier");
				if (id == null) {
					return RestResponse.NOT_FOUND;
				}
				pizzaFactory.setProperty("identifier", id);
				datastore.put(pizzaFactory);
				response = RestResponse.OK;
			}
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;
	}

	/**
	 * Delete Pizza Factory from datastore
	 * 
	 * @param token
	 *            Pizza Factory token
	 */
	private Response removePizzaShop(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			Key key = KeyFactory.createKey("PizzaFactory", token);
			datastore.delete(key);
			response = RestResponse.OK;
		} catch (Exception e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;

	}

	/**
	 * Convert an entity to pizzashop
	 * 
	 * @param entity
	 *            Entity
	 * 
	 * @return PizzaShop
	 */
	@SuppressWarnings("unchecked")
	public static PizzaFactory entityToObject(Entity entity) {
		if (entity == null || !entity.getKind().equals("PizzaFactory")) {
			return null;
		}
		PizzaFactory pizzaFactory = new PizzaFactory();
		pizzaFactory.setIdentifier((String) entity.getProperty("identifier"));
		List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) entity
				.getProperty("crust");
		if (crusts != null) {
			List<PizzaCrust> crustList = new ArrayList<PizzaCrust>();
			for (EmbeddedEntity e : crusts) {
				PizzaCrust crust = new PizzaCrust();
				crust.setIdentifier((String) e.getProperty("identifier"));
				crust.setDescription((String) e.getProperty("description"));
				List<Double> costs = (List<Double>) e.getProperty("costs");
				List<Double> prices = (List<Double>) e.getProperty("prices");
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
				List<Double> prices = (List<Double>) e.getProperty("prices");
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
				List<Double> prices = (List<Double>) e.getProperty("prices");
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
				List<Double> prices = (List<Double>) e.getProperty("prices");
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
				List<Double> prices = (List<Double>) e.getProperty("prices");
				veg.setCosts(costs);
				veg.setPrices(prices);
				vegList.add(veg);
			}
			pizzaFactory.addToppingVeg(vegList);
		}
		return pizzaFactory;
	}
}
