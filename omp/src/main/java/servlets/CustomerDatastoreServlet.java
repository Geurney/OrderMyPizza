/**
 * 
 */
package servlets;

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
 * Handle customer datastore requests, matching url: "/customerprofile"
 * 
 * @author Geurney
 *
 */
public class CustomerDatastoreServlet extends HttpServlet {

	private static final long serialVersionUID = 5327462473337052693L;

	/**
	 * Handle get request by redirecting to "/customerprofile.jsp"
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
	 *      , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resp.sendRedirect("/customerprofile.jsp");
	}

	/**
	 * Handle post request by storing customer into datastore, redirect to
	 * "/customerprofile.jsp". Accept param: name, address,phone. If user does
	 * not logs in, send 403.
	 * 
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
	 *      , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		if (req.getUserPrincipal() != null) {
			User user = userService.getCurrentUser();
			System.out.println("Get Post Request to CustomerProfile: "
					+ req.getParameter("name"));
			String hash_uid = UserIDObscure.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("Customer", hash_uid);

			Entity customer = null;
			try {
				customer = datastore.get(key);
			} catch (EntityNotFoundException e) {
				customer = new Entity("Customer", hash_uid);
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
				resp.sendRedirect("/customerprofile.jsp");
			}
		} else {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN,
					"Please log in before proceeding!");
		}
	}

	/**
	 * Handle put request as post request.
	 * 
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest
	 *      , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	/**
	 * Handle delete request by deleting the customer in datastore, sending 204.
	 * If user does not logs in, send 403.
	 * 
	 * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest
	 *      , javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		UserService userService = UserServiceFactory.getUserService();
		if (req.getUserPrincipal() != null) {
			User user = userService.getCurrentUser();
			String hash_uid = UserIDObscure.obsecure(user.getUserId());
			DatastoreService datastore = DatastoreServiceFactory
					.getDatastoreService();
			Key key = KeyFactory.createKey("Customer", hash_uid);
			datastore.delete(key);
			resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} else {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN,
					"Please log in before proceeding!");
		}
	}
}
