package pizza.veg;

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

public class PizzaToppingVegsResource extends
		PizzaComponentsResource<PizzaToppingVeg, PizzaToppingVegResource> {

	public PizzaToppingVegsResource() {
		super();
		type = "veg";
		System.out.println("HERE==Constructor");
	}

	@Override
	public PizzaToppingVegResource handleComponent(String identifier) {
		return new PizzaToppingVegResource(uriInfo, request, identifier);
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
		List<PizzaToppingVeg> components = null;
		try {
			Entity pizzaFactory = datastore.get(key);
			System.out.println("HERE--Got Factory");
			List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
					.getProperty(type);
			components = new ArrayList<PizzaToppingVeg>();
			if (list != null) {
				System.out.println("HERE--list is not null");
				for (EmbeddedEntity e : list) {
					PizzaToppingVeg component = PizzaToppingVegResource.entityToObject(e);
					components.add(component);
				}
			}
			GenericEntity<List<PizzaToppingVeg>> lists = new GenericEntity<List<PizzaToppingVeg>>(
					components) {
			};
			response = RestResponse.OK(lists);
			System.out.println("HERE--list is ok");
		} catch (EntityNotFoundException e) {
			response = RestResponse.NOT_FOUND;
		}
		return response;
	}

}
