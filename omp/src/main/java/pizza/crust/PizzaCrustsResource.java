package pizza.crust;

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
 * PizzaCrusts REST Resource
 * 
 * @author Geurney
 *
 */
public class PizzaCrustsResource extends
		PizzaComponentsResource<PizzaCrust, PizzaCrustResource> {

	/**
	 * Constructor
	 */
	public PizzaCrustsResource() {
		super();
		type = "crust";
	}

	@Override
	public PizzaCrustResource handleComponent(String identifier) {
		return new PizzaCrustResource(uriInfo, request, identifier);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Response findPizzaComponents(String token) {
		if (token == null) {
			return RestResponse.FORBIDDEN();
		}
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Key key = KeyFactory.createKey("PizzaFactory", token);
		List<PizzaCrust> components = null;
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty(type);
			components = new ArrayList<PizzaCrust>();
			if (list != null) {
				for (EmbeddedEntity e : list) {
					PizzaCrust component = PizzaCrustResource.entityToObject(e);
					components.add(component);
				}
			}
			GenericEntity<List<PizzaCrust>> lists = new GenericEntity<List<PizzaCrust>>(
					components) {
			};
			response = RestResponse.OK(lists);
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND();
		}
		return response;
	}

}
