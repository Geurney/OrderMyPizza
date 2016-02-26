/**
 * 
 */
package servlets;

/**
 * @author user
 *
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.EmbeddedEntity;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Handle pizza shop datastore requests
 * 
 * @author DQ
 *
 */
public class PizzaFactoryDatastoreServlet extends HttpServlet {

	@SuppressWarnings("unchecked")
	private String pizzaComponentToHtml(EmbeddedEntity component) {
		if (component == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<p>");
		sb.append(component.getProperty("description"));
		sb.append("</p>\n<p>");
		ArrayList<String> costs = (ArrayList<String>) component
				.getProperty("costs");
		for (int i = 0; i < costs.size(); i++) {
			sb.append(costs.get(i) + " ");
		}
		sb.append("</p>\n<p>");
		ArrayList<String> prices = (ArrayList<String>) component
				.getProperty("prices");
		for (int i = 0; i < costs.size(); i++) {
			sb.append(prices.get(i) + " ");
		}
		sb.append("</p>");
		return sb.toString();
	}

	/*
	 * Handle get request
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		String thisURL = req.getRequestURI();

		resp.setContentType("text/html");
		if (req.getUserPrincipal() != null) {
			User user = userService.getCurrentUser();
			resp.getWriter().println(
					"<p>Hello, " + req.getUserPrincipal().getName()
							+ "!  You can <a href=\""
							+ userService.createLogoutURL(thisURL)
							+ "\">sign out</a>.</p>");

			String hash_uid = UserUtils.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("PizzaFactory", hash_uid);

			try {
				Entity pizzaFactory = datastore.get(key);
				String type = req.getParameter("type");
				if (type != null) {
					List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
							.getProperty(type);
					resp.getWriter().println(
							"<h3>" + type + ":" + "</h3>\n");
					if (list != null) {
						for (EmbeddedEntity i : list) {
							resp.getWriter().println(pizzaComponentToHtml(i));
						}
					}
					return;
				}
				List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
						.getProperty("crust");
				List<EmbeddedEntity> cheeses = (List<EmbeddedEntity>) pizzaFactory
						.getProperty("cheese");
				List<EmbeddedEntity> sauces = (List<EmbeddedEntity>) pizzaFactory
						.getProperty("sauce");
				List<EmbeddedEntity> topping_meat = (List<EmbeddedEntity>) pizzaFactory
						.getProperty("topping_meat");
				List<EmbeddedEntity> topping_veg = (List<EmbeddedEntity>) pizzaFactory
						.getProperty("topping_veg");

				if (crusts != null) {
					resp.getWriter().println("<h3>" + "crust:" + "</h3>\n");
					for (EmbeddedEntity i : crusts) {
						resp.getWriter().println(pizzaComponentToHtml(i));
					}
				}
				if (cheeses != null) {
					resp.getWriter().println("<h3>" + "cheese:" + "</h3>\n");
					for (EmbeddedEntity i : cheeses) {
						resp.getWriter().println(pizzaComponentToHtml(i));
					}
				}
				if (sauces != null) {
					resp.getWriter().println("<h3>" + "sauce:" + "</h3>\n");
					for (EmbeddedEntity i : sauces) {
						resp.getWriter().println(pizzaComponentToHtml(i));
					}
				}
				if (topping_meat != null) {
					resp.getWriter().println(
							"<h3>" + "topping meat:" + "</h3>\n");
					for (EmbeddedEntity i : topping_meat) {
						resp.getWriter().println(pizzaComponentToHtml(i));
					}
				}
				if (topping_veg != null) {
					resp.getWriter().println(
							"<h3>" + "topping veg:" + "</h3>\n");
					for (EmbeddedEntity i : topping_veg) {
						resp.getWriter().println(pizzaComponentToHtml(i));
					}
				}
			} catch (EntityNotFoundException e) {
				resp.getWriter().println(
						"<h2>Error: PizzaFactory not found!</h2>");
			}
		} else {
			resp.getWriter().println(
					"<p>Please <a href=\""
							+ userService.createLoginURL(thisURL)
							+ "\">sign in</a>.</p>");
		}
	}

	/*
	 * Handle post request
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		String thisURL = req.getRequestURI();

		resp.setContentType("text/html");
		if (req.getUserPrincipal() != null) {
			User user = userService.getCurrentUser();
			resp.getWriter().println(
					"<p>Hello, " + req.getUserPrincipal().getName()
							+ "!  You can <a href=\""
							+ userService.createLogoutURL(thisURL)
							+ "\">sign out</a>.</p>");

			String hash_uid = UserUtils.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("PizzaFactory", hash_uid);

			Entity pizzaFactory = null;
			try {
				pizzaFactory = datastore.get(key);
				resp.getWriter().println(
						"<h2>" + "PizzaFactory updating..." + "</h2>");
			} catch (EntityNotFoundException e) {
				pizzaFactory = new Entity("PizzaFactory", hash_uid);
				resp.getWriter().println(
						"<h2>" + "PizzaFactory storing..." + "</h2>");
			} finally {
				String type = req.getParameter("type");
				String description = req.getParameter("description");
				ArrayList<String> costs = new ArrayList<String>();
				costs.add(req.getParameter("cost1"));
				costs.add(req.getParameter("cost2"));
				costs.add(req.getParameter("cost3"));
				ArrayList<String> prices = new ArrayList<String>();
				prices.add(req.getParameter("price1"));
				prices.add(req.getParameter("price2"));
				prices.add(req.getParameter("price3"));

				EmbeddedEntity component = new EmbeddedEntity();
				component.setProperty("description", description);
				component.setProperty("costs", costs);
				component.setProperty("prices", prices);

				List<EmbeddedEntity> list = (List<EmbeddedEntity>) pizzaFactory
						.getProperty(type);
				if (list == null) {
					list = new ArrayList<EmbeddedEntity>();
				}
				list.add(component);
				pizzaFactory.setProperty(type, list);
				datastore.put(pizzaFactory);
				resp.getWriter().println("<h3>" + type + ":" + "</h3>\n");
				resp.getWriter().println(pizzaComponentToHtml(component));
			}
		} else {
			resp.getWriter().println(
					"<p>Please <a href=\""
							+ userService.createLoginURL(thisURL)
							+ "\">sign in</a>.</p>");
		}
	}

	/*
	 * Handle put request
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	/*
	 * Handle delete request
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		String thisURL = req.getRequestURI();

		resp.setContentType("text/html");
		if (req.getUserPrincipal() != null) {
			User user = userService.getCurrentUser();
			resp.getWriter().println(
					"<p>Hello, " + req.getUserPrincipal().getName()
							+ "!  You can <a href=\""
							+ userService.createLogoutURL(thisURL)
							+ "\">sign out</a>.</p>");

			String hash_uid = UserUtils.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("PizzaFactory", hash_uid);
			try {
				Entity pizzaFactory = datastore.get(key);
				datastore.delete(key);
				resp.getWriter().println(
						"<h2>" + "PizzaFactory is deleted!" + "</h2>");
			} catch (EntityNotFoundException e) {
				resp.getWriter().println(
						"<h2>Error: PizzaFactory not found!</h2>");
			}
		} else {
			resp.getWriter().println(
					"<p>Please <a href=\""
							+ userService.createLoginURL(thisURL)
							+ "\">sign in</a>.</p>");
		}
	}
}
