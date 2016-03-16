package pizza.sauce;

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
 * Pizza Sauces REST Resource
 * 
 * @author Geurney
 *
 */
public class PizzaSaucesResource extends
		PizzaComponentsResource<PizzaSauce, PizzaSauceResource> {

	/**
	 * Constructor
	 */
	public PizzaSaucesResource() {
		super();
		type = "sauce";
	}

	@Override
	public PizzaSauceResource handleComponent(String identifier) {
		return new PizzaSauceResource(uriInfo, request, identifier);
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
		List<PizzaSauce> components = null;
		try {
			Entity pizzaFactory = datastore.get(key);
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty(type);
			components = new ArrayList<PizzaSauce>();
			if (list != null) {
				for (EmbeddedEntity e : list) {
					PizzaSauce component = PizzaSauceResource.entityToObject(e);
					components.add(component);
				}
			}
			GenericEntity<List<PizzaSauce>> lists = new GenericEntity<List<PizzaSauce>>(
					components) {
			};
			response = RestResponse.OK(lists);
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND();
		}
		return response;
	}

}
