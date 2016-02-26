/**
 * 
 */
package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import user.UserUtils;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
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
 * @author Geurney
 *
 */
public class PizzaShopDatastoreServlet extends HttpServlet {

	/*
	 * Handle get request
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
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
			Key key = KeyFactory.createKey("PizzaShop", hash_uid);

			try {
				Entity pizzaShop = datastore.get(key);
				String name = (String) pizzaShop.getProperty("name");
				String address = (String) pizzaShop.getProperty("address");
				String phone = (String) pizzaShop.getProperty("phone");
				resp.getWriter()
						.println(
								"<h2>" + name + ": " + address + " " + phone
										+ " </h2>");
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

	/*
	 * Handle post request
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 * , javax.servlet.http.HttpServletResponse)
	 */
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
			Key key = KeyFactory.createKey("PizzaShop", hash_uid);

			Entity pizzaShop = null;
			try {
				pizzaShop = datastore.get(key);
				resp.getWriter().println(
						"<h2>" + "PizzaShop updating..." + "</h2>");
			} catch (EntityNotFoundException e) {
				pizzaShop = new Entity("PizzaShop", hash_uid);
				resp.getWriter().println(
						"<h2>" + "PizzaShop storing..." + "</h2>");
			} finally {
				String name = req.getParameter("name");
				if (name != null) {
					pizzaShop.setProperty("name", name);
				}
				String address = req.getParameter("address");
				if (address != null) {
					pizzaShop.setProperty("address", address);
				}
				String phone = req.getParameter("phone");
				if (phone != null) {
					pizzaShop.setProperty("phone", phone);
				}
				datastore.put(pizzaShop);
				resp.getWriter()
						.println(
								"<h2>" + name + ": " + address + " " + phone
										+ " </h2>");
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
