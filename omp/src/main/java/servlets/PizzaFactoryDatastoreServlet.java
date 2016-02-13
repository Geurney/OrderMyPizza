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

import ordermypizza.UserIDObscure;

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

	private String pizzaComponentToHtml(EmbeddedEntity component) {
		if (component == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("<p>");
		sb.append(component.getProperty("description"));
		sb.append("</p>\n<p>");
		float[] costs = (float[]) component.getProperty("costs");
		for (float i : costs) {
			sb.append(i + " ");
		}
		sb.append("</p>\n<p>");
		float[] prices = (float[]) component.getProperty("prices");
		for (float i : prices) {
			sb.append(i + " ");
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

			String hash_uid = UserIDObscure.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("PizzaFactory", hash_uid);

			try {
				Entity pizzaFactory = datastore.get(key);
				List<EmbeddedEntity> crusts = (List<EmbeddedEntity>) pizzaFactory
						.getProperty("crust");
				List<EmbeddedEntity> cheeses = (List<EmbeddedEntity>) pizzaFactory
						.getProperty("cheese");
				List<EmbeddedEntity> sauces = (List<EmbeddedEntity>) pizzaFactory
						.getProperty("sauce");
				List<EmbeddedEntity> toppings_meat = (List<EmbeddedEntity>) pizzaFactory
						.getProperty("topping_meat");
				List<EmbeddedEntity> toppings_veg = (List<EmbeddedEntity>) pizzaFactory
						.getProperty("topping_veg");

				resp.getWriter().println("<h3>" + "crust:" + "</h3>\n");
				for (EmbeddedEntity i : crusts) {
					resp.getWriter().println(pizzaComponentToHtml(i));
				}
				resp.getWriter().println("<h3>" + "cheese:" + "</h3>\n");
				for (EmbeddedEntity i : cheeses) {
					resp.getWriter().println(pizzaComponentToHtml(i));
				}
				resp.getWriter().println("<h3>" + "sauce:" + "</h3>\n");
				for (EmbeddedEntity i : sauces) {
					resp.getWriter().println(pizzaComponentToHtml(i));
				}
				resp.getWriter().println("<h3>" + "topping meat:" + "</h3>\n");
				for (EmbeddedEntity i : toppings_meat) {
					resp.getWriter().println(pizzaComponentToHtml(i));
				}
				resp.getWriter().println("<h3>" + "topping veg:" + "</h3>\n");
				for (EmbeddedEntity i : toppings_veg) {
					resp.getWriter().println(pizzaComponentToHtml(i));
				}
			} catch (EntityNotFoundException e) {
				resp.getWriter()
						.println("<h2>Error: PizzaFactory not found!</h2>");
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

			String hash_uid = UserIDObscure.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("PizzaFactory", hash_uid);

			Entity pizzaFactory = null;
			try {
				pizzaFactory = datastore.get(key);
				resp.getWriter().println(
						"<h2>" + "PizzaShop updating..." + "</h2>");
			} catch (EntityNotFoundException e) {
				pizzaFactory = new Entity("PizzaShop", hash_uid);
				resp.getWriter().println(
						"<h2>" + "PizzaShop storing..." + "</h2>");
			} finally {
				String type = req.getParameter("type");
				String description = req.getParameter("description");
				float[] costs = new float[] {Float.parseFloat(req.getParameter("cost1")), Float.parseFloat(req.getParameter("cost2")), Float.parseFloat(req.getParameter("cost3"))};
				float[] prices = new float[] {Float.parseFloat(req.getParameter("price1")), Float.parseFloat(req.getParameter("price2")), Float.parseFloat(req.getParameter("price3"))};
				
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

			String hash_uid = UserIDObscure.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("PizzaShop", hash_uid);
			try {
				Entity pizzaShop = datastore.get(key);
				datastore.delete(key);
				resp.getWriter().println(
						"<h2>" + "PizzaShop is deleted!" + "</h2>");
			} catch (EntityNotFoundException e) {
				resp.getWriter()
						.println("<h2>Error: PizzaShop not found!</h2>");
			}
		} else {
			resp.getWriter().println(
					"<p>Please <a href=\""
							+ userService.createLoginURL(thisURL)
							+ "\">sign in</a>.</p>");
		}
	}
}
