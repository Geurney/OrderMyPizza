package pizza.meat;

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
 * Pizza Meat Toppings REST Resource
 * 
 * @author Geurney
 *
 */
public class PizzaToppingMeatsResource extends
		PizzaComponentsResource<PizzaToppingMeat, PizzaToppingMeatResource> {

	/**
	 * Constructor
	 */
	public PizzaToppingMeatsResource() {
		super();
		type = "meat";
	}

	@Override
	public PizzaToppingMeatResource handleComponent(String identifier) {
		return new PizzaToppingMeatResource(uriInfo, request, identifier);
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
		List<PizzaToppingMeat> components = null;
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty(type);
			components = new ArrayList<PizzaToppingMeat>();
			if (list != null) {
				for (EmbeddedEntity e : list) {
					PizzaToppingMeat component = PizzaToppingMeatResource
							.entityToObject(e);
					components.add(component);
				}
			}
			GenericEntity<List<PizzaToppingMeat>> lists = new GenericEntity<List<PizzaToppingMeat>>(
					components) {
			};
			response = RestResponse.OK(lists);
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;
	}

}
