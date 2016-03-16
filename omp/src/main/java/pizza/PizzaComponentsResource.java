/**
 * 
 */
package pizza;

import java.util.ArrayList;
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
 * PizzaCompoents Resource abstract class
 * 
 * @author Geurney
 *
 */
public abstract class PizzaComponentsResource<T extends PizzaComponent, R> {
	@Context
	protected UriInfo uriInfo;
	@Context
	protected Request request;
	@Context
	protected Response response;

	/**
	 * crust, cheese, sauce, meat, veg
	 */
	protected String type;

	/**
	 * Get the Pizza Components
	 * 
	 * @return Pizza Components
	 */
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getComponents() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return findPizzaComponents(hash_uid);
	}

	/**
	 * Get the Pizza Components with token
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @return Pizza Components
	 */
	@Path("/authorize/{token}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getComponents(@PathParam("token") String token) {
		return findPizzaComponents(token);
	}

	/**
	 * Add the Pizza Cheese into PizzaFactory. This operation fails on following
	 * conditions:1.Token is null 2.Parameters are missing 3.costs/prices are
	 * negative 4.Identifier already exists
	 * 
	 * @param identifier
	 *            PizzaComponent identifier. Should be unique.
	 * @param description
	 *            PizzaComponent description. Should not be null
	 * @param cost1
	 *            PizzaComponent small size cost. Should not be negative
	 * @param price1
	 *            PizzaComponent small size price. Should not be negative
	 * @param cost2
	 *            PizzaComponent medium size cost. Should not be negative
	 * @param price2
	 *            PizzaComponent medium size price. Should not be negative
	 * @param cost3
	 *            PizzaComponent large size cost. Should not be negative
	 * @param price3
	 *            PizzaComponent large size price. Should not be negative
	 * @return response
	 * 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response postPizzaComponent(
			@FormParam("identifier") String newIdentifier,
			@FormParam("description") String description,
			@FormParam("cost1") String cost1,
			@FormParam("price1") String price1,
			@FormParam("cost2") String cost2,
			@FormParam("price2") String price2,
			@FormParam("cost3") String cost3, @FormParam("price3") String price3) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return newPizzaComponent(hash_uid, newIdentifier, description, cost1,
				price1, cost2, price2, cost3, price3);
	}

	/**
	 * Add the Pizza Cheese into PizzaFactory. This operation fails on following
	 * conditions:1.Token is null 2.Parameters are missing 3.costs/prices are
	 * negative 4.Identifier already exists
	 * 
	 * @param token
	 *            Pizza Factory token. Should not be null
	 * @param newIdentifier
	 *            PizzaComponent identifier. Should be unique.
	 * @param description
	 *            PizzaComponent description. Should not be null
	 * @param cost1
	 *            PizzaComponent small size cost. Should not be negative
	 * @param price1
	 *            PizzaComponent small size price. Should not be negative
	 * @param cost2
	 *            PizzaComponent medium size cost. Should not be negative
	 * @param price2
	 *            PizzaComponent medium size price. Should not be negative
	 * @param cost3
	 *            PizzaComponent large size cost. Should not be negative
	 * @param price3
	 *            PizzaComponent large size price. Should not be negative
	 * @return response
	 * 
	 */
	@Path("/authorize/{token}")
	@POST
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response postPizzaComponent(@PathParam("token") String token,
			@FormParam("identifier") String newIdentifier,
			@FormParam("description") String description,
			@FormParam("cost1") String cost1,
			@FormParam("price1") String price1,
			@FormParam("cost2") String cost2,
			@FormParam("price2") String price2,
			@FormParam("cost3") String cost3, @FormParam("price3") String price3) {
		return newPizzaComponent(token, newIdentifier, description, cost1,
				price1, cost2, price2, cost3, price3);
	}

	/**
	 * Add the Pizza Cheese into PizzaFactory with JSON. This operation fails on
	 * following conditions:1.Token is null 2.Parameters are missing
	 * 3.costs/prices are negative 4.Identifier already exists
	 * 
	 * @param component
	 *            T
	 * @return response
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postPizzaComponent(T component) {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		double[] costs = component.getCosts();
		double[] prices = component.getPrices();
		return newPizzaComponent(hash_uid, component.getIdentifier(),
				component.getDescription(), String.valueOf(costs[0]),
				String.valueOf(prices[0]), String.valueOf(costs[1]),
				String.valueOf(prices[1]), String.valueOf(costs[2]),
				String.valueOf(prices[2]));
	}

	/**
	 * Add the Pizza Cheese into PizzaFactory with JSON with token for curl.
	 * This operation fails on following conditions:1.Token is null 2.Parameters
	 * are missing 3.costs/prices are negative 4.Identifier already exists
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @param component
	 *            T
	 * @return response
	 */
	@Path("/authorize/{token}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postPizzaComponent(@PathParam("token") String token,
			T component) {
		double[] costs = component.getCosts();
		double[] prices = component.getPrices();
		return newPizzaComponent(token, component.getIdentifier(),
				component.getDescription(), String.valueOf(costs[0]),
				String.valueOf(prices[0]), String.valueOf(costs[1]),
				String.valueOf(prices[1]), String.valueOf(costs[2]),
				String.valueOf(prices[2]));
	}

	/**
	 * Delete All Pizza Components
	 * 
	 * @return response
	 */
	@DELETE
	public Response deletePizzaComponents() {
		String hash_uid = UserUtils.getCurrentUserObscureID();
		return removePizzaComponents(hash_uid);
	}

	/**
	 * Delete All Pizza Components with token
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @return response
	 */
	@Path("/authorize/{token}")
	@DELETE
	public Response deletePizzaComponents(@PathParam("token") String token) {
		return removePizzaComponents(token);
	}

	/**
	 * For specific pizza component
	 * 
	 */
	@Path("{identifier}")
	public abstract R handleComponent(@PathParam("identifier") String identifier);

	/**
	 * Get the Pizza Components
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @return Pizza Components
	 */
	protected abstract Response findPizzaComponents(String token);

	/**
	 * Add the Pizza Component into PizzaFactory. This operation fails on
	 * following conditions:1.Token is null 2.Parameters are missing
	 * 3.costs/prices are negative 4.Identifier already exists
	 * 
	 * @param token
	 *            Pizza Factory token. Should not be null
	 * @param identifier
	 *            PizzaComponent identifier. Should be unique.
	 * @param description
	 *            PizzaComponent description. Should not be null
	 * @param cost1
	 *            PizzaComponent small size cost. Should not be negative
	 * @param price1
	 *            PizzaComponent small size price. Should not be negative
	 * @param cost2
	 *            PizzaComponent medium size cost. Should not be negative
	 * @param price2
	 *            PizzaComponent medium size price. Should not be negative
	 * @param cost3
	 *            PizzaComponent large size cost. Should not be negative
	 * @param price3
	 *            PizzaComponent large size price. Should not be negative
	 * @return response
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected Response newPizzaComponent(String token, String identifier,
			String description, String cost1, String price1, String cost2,
			String price2, String cost3, String price3) {
		if (token == null) {
			return RestResponse.FORBIDDEN();
		}
		if (identifier == null || description == null || cost1 == null
				|| cost2 == null || cost3 == null || price1 == null
				|| price2 == null || price3 == null) {
			return RestResponse.BAD();
		}
		double ct1 = Double.valueOf(cost1);
		double ct2 = Double.valueOf(cost2);
		double ct3 = Double.valueOf(cost3);
		double pc1 = Double.valueOf(price1);
		double pc2 = Double.valueOf(price2);
		double pc3 = Double.valueOf(price3);
		if (ct1 < 0 || ct2 < 0 || ct3 < 0 || pc1 < 0 || pc2 < 0 || pc3 < 0) {
			return RestResponse.BAD();
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		Entity pizzaFactory = null;
		try {
			pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty(type);
			if (list == null) {
				list = new ArrayList<EmbeddedEntity>();
			} else {
				for (EmbeddedEntity e : list) {
					String id = (String) e.getProperty("identifier");
					if (id != null && id.equals(identifier)) {
						return RestResponse.BAD();
					}
				}
			}
			List<Double> costs = new ArrayList<Double>();
			costs.add(ct1);
			costs.add(ct2);
			costs.add(ct3);

			List<Double> prices = new ArrayList<Double>();
			prices.add(pc1);
			prices.add(pc2);
			prices.add(pc3);

			EmbeddedEntity component = new EmbeddedEntity();
			component.setProperty("identifier", identifier);
			component.setProperty("description", description);
			component.setProperty("costs", costs);
			component.setProperty("prices", prices);

			list.add(component);
			pizzaFactory.setProperty(type, list);
			datastore.put(pizzaFactory);
			response = RestResponse.OK;
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND();
		}
		return response;
	}

	/**
	 * Delete Pizza Component
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @return response
	 */
	protected Response removePizzaComponents(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN();
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		try {
			Entity pizzaFactory = datastore.get(key);
			pizzaFactory.removeProperty(type);
			response = RestResponse.OK;
		} catch (Exception e) {
			response = RestResponse.NOT_FOUND();
		}
		return response;
	}

}
