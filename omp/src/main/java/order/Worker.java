package order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pizza.Pizza;
import pizzafactory.PizzaFactory;
import pizzafactory.PizzaFactoryResource;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

/**
 * Worker for task Queue to create orders.
 * 
 * @author Geurney
 *
 */
public class Worker extends HttpServlet {
	private static final long serialVersionUID = 8044568576701452944L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();

		Filter identifierFilter = new FilterPredicate("identifier",
				FilterOperator.EQUAL, request.getParameter("pizzashop"));
		Query q = new Query("PizzaFactory").setFilter(identifierFilter);
		PreparedQuery pq = datastore.prepare(q);
		Entity entity = pq.asSingleEntity();
		if (entity == null) {
			return;
		}
		PizzaFactory pizzaFactory = PizzaFactoryResource.entityToObject(entity);
		Pizza pizza = pizzaFactory.preparePizza(request.getParameter("size"));
		pizzaFactory.buildCheese(request.getParameter("cheese"), pizza);
		pizzaFactory.buildCrust(request.getParameter("crust"), pizza);
		pizzaFactory.buildSauce(request.getParameter("sauce"), pizza);
		pizzaFactory.buildToppingMeat(request.getParameter("meat1"), pizza);
		pizzaFactory.buildToppingMeat(request.getParameter("meat2"), pizza);
		pizzaFactory.buildToppingMeat(request.getParameter("meat3"), pizza);
		pizzaFactory.buildToppingVeg(request.getParameter("veg1"), pizza);
		pizzaFactory.buildToppingVeg(request.getParameter("veg2"), pizza);
		pizzaFactory.buildToppingVeg(request.getParameter("veg3"), pizza);

		Entity order = new Entity("Order", request.getParameter("number"));
		order.setProperty("pizzashop", request.getParameter("pizzashop"));
		order.setProperty("customer", request.getParameter("customer"));
		order.setProperty("crust", request.getParameter("crust"));
		order.setProperty("cheese", request.getParameter("cheese"));
		order.setProperty("sauce", request.getParameter("sauce"));
		order.setProperty("size", request.getParameter("size"));
		order.setProperty("price", pizza.getIngredientsPrice());
		order.setProperty("cost", pizza.getIngredientsCost());
		order.setProperty("status", "done");
		order.setProperty("date", request.getParameter("date"));
		List<String> meats = new ArrayList<String>();
		if (request.getParameter("meat1") != null) {
			meats.add(request.getParameter("meat1"));
		}
		if (request.getParameter("meat2") != null) {
			meats.add(request.getParameter("meat2"));
		}
		if (request.getParameter("meat3") != null) {
			meats.add(request.getParameter("meat3"));
		}
		order.setProperty("meat", meats);

		List<String> vegs = new ArrayList<String>();
		if (request.getParameter("veg1") != null) {
			vegs.add(request.getParameter("veg1"));
		}
		if (request.getParameter("veg2") != null) {
			vegs.add(request.getParameter("veg2"));
		}
		if (request.getParameter("veg3") != null) {
			vegs.add(request.getParameter("veg3"));
		}
		order.setProperty("veg", vegs);
		datastore.put(order);
	}
}
