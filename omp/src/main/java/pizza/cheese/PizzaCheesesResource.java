package pizza.cheese;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import pizza.PizzaComponentsResource;
import rest.RestResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

/**
 * PizzaCheeses REST Resource
 * 
 * @author Geurney
 *
 */
public class PizzaCheesesResource extends
		PizzaComponentsResource<PizzaCheese, PizzaCheeseResource> {

	/**
	 * Constructor
	 */
	public PizzaCheesesResource() {
		super();
		type = "cheese";
	}

	/**
	 * 
	 */
	@Override
	public PizzaCheeseResource handleComponent(String identifier) {
		return new PizzaCheeseResource(uriInfo, request, identifier);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Response findPizzaComponents(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN;
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		List<PizzaCheese> components = null;
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty(type);
			components = new ArrayList<PizzaCheese>();
			if (list != null) {
				for (EmbeddedEntity e : list) {
					PizzaCheese component = PizzaCheeseResource
							.entityToObject(e);
					components.add(component);
				}
			}
			GenericEntity<List<PizzaCheese>> lists = new GenericEntity<List<PizzaCheese>>(
					components) {
			};
			response = RestResponse.OK(lists);
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;
	}

}
