/**
 * 
 */
package servlets;

/**
 * @author user
 *
 */
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ordermypizza.UserIDObscure;

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
 * Handle customer datastore requests
 * 
 * @author Geurney
 *
 */
public class CustomerDatastoreServlet extends HttpServlet {

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

			String hash_uid = UserIDObscure.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("Customer", hash_uid);

			try {
				Entity customer = datastore.get(key);
				String name = (String) customer.getProperty("name");
				String address = (String) customer.getProperty("address");
				String phone = (String) customer.getProperty("phone");
				resp.getWriter()
						.println(
								"<h2>" + name + ": " + address + " " + phone
										+ " </h2>");
			} catch (EntityNotFoundException e) {
				resp.getWriter().println("<h2>Error: Customer not found!</h2>");
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

			String hash_uid = UserIDObscure.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("Customer", hash_uid);

			Entity customer = null;
			try {
				customer = datastore.get(key);
				resp.getWriter().println(
						"<h2>" + "Customer updating..." + "</h2>");
			} catch (EntityNotFoundException e) {
				customer = new Entity("Customer", hash_uid);
				resp.getWriter().println(
						"<h2>" + "Customer storing..." + "</h2>");
			} finally {
				String name = req.getParameter("name");
				if (name != null) {
					customer.setProperty("name", name);
				}
				String address = req.getParameter("address");
				if (address != null) {
					customer.setProperty("address", address);
				}
				String phone = req.getParameter("phone");
				if (phone != null) {
					customer.setProperty("phone", phone);
				}
				datastore.put(customer);
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

			String hash_uid = UserIDObscure.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("Customer", hash_uid);
			try {
				Entity customer = datastore.get(key);
				datastore.delete(key);
				resp.getWriter().println(
						"<h2>" + "Customer is deleted!" + "</h2>");
			} catch (EntityNotFoundException e) {
				resp.getWriter().println("<h2>Error: Customer not found!</h2>");
			}
		} else {
			resp.getWriter().println(
					"<p>Please <a href=\""
							+ userService.createLoginURL(thisURL)
							+ "\">sign in</a>.</p>");
		}
	}
}
