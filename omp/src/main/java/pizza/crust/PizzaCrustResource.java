package pizza.crust;

import java.util.List;

import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import pizza.PizzaComponent;
import pizza.PizzaComponentResource;
import rest.RestResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * PizzaCrust Resource
 * 
 * @author Geurney
 *
 */
public class PizzaCrustResource extends PizzaComponentResource<PizzaCrust> {

	/**
	 * Pizza Crust Resource Constructor
	 * 
	 * @param uriInfo uriInfo
	 * @param request request
	 * @param identifier identifier
	 */
	public PizzaCrustResource(UriInfo uriInfo, Request request,
			String identifier) {
		super(uriInfo, request, identifier);
		type = "crust";
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Response findPizzaComponent(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN();
		}
		PizzaComponent component = null;
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty(type);
			if (list == null) {
				return RestResponse.NOT_FOUND();
			}
			response = RestResponse.NOT_FOUND();
			for (EmbeddedEntity e : list) {
				String id = (String) e.getProperty("identifier");
				if (id != null && id.equals(identifier)) {
					component = entityToObject(e);
					response = RestResponse.OK(component);
					break;
				}
			}
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND();
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	public static PizzaCrust entityToObject(EmbeddedEntity entity) {
		if (entity == null) {
			return null;
		}
		PizzaCrust component = new PizzaCrust();
		component.setIdentifier((String) entity.getProperty("identifier"));
		component.setDescription((String) entity.getProperty("description"));
		List<Double> costs = (List<Double>) entity.getProperty("costs");
		List<Double> prices = (List<Double>) entity.getProperty("prices");
		component.setCosts(costs);
		component.setPrices(prices);
		return component;
	}

}
