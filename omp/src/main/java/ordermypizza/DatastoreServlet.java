package ordermypizza;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;
import java.util.*;
import java.util.logging.*;

import com.google.appengine.api.datastore.*;
import com.google.appengine.api.memcache.*;

@SuppressWarnings("serial")
public class DatastoreServlet extends HttpServlet {
	private void handlePizzaShop(HttpServletRequest req,
			HttpServletResponse resp, String shopName) throws IOException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		resp.setContentType("text/html");
		resp.getWriter().println("<html><body>");

		Key key = KeyFactory.createKey("PizzaShop", shopName);
		try {
			Entity pizzaShop = datastore.get(key);
			String shopAddr = (String) pizzaShop.getProperty("shopAddr");
			String shopPhone = (String) pizzaShop.getProperty("shopPhone");
			resp.getWriter().println(
					"<h2>" + shopName + ": " + shopAddr + " " + shopPhone
							+ " </h2>");
		} catch (EntityNotFoundException e) {
			resp.getWriter().println("<h2>Shop not found</h2>");
		}

		resp.getWriter().println("</body></html>");
	}

	private void handlePizzaShop(HttpServletRequest req,
			HttpServletResponse resp, String shopName, String shopAddr,
			String shopPhone) throws IOException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		resp.setContentType("text/html");
		resp.getWriter().println("<html><body>");

		Entity pizzaShop = new Entity("PizzaShop", shopName);
		pizzaShop.setProperty("shopAddr", shopAddr);
		pizzaShop.setProperty("shopPhone", shopPhone);
		datastore.put(pizzaShop);
		resp.getWriter().println(
				"<h2>Stored " + shopName + ": " + shopAddr + " " + shopPhone
						+ " in Datastore</h2>");

		resp.getWriter().println("</body></html>");
	}

	private void handleCustomer(HttpServletRequest req,
			HttpServletResponse resp, String userName) throws IOException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		resp.setContentType("text/html");
		resp.getWriter().println("<html><body>");

		Key key = KeyFactory.createKey("Customer", userName);
		try {
			Entity customer = datastore.get(key);
			String userEmail = (String) customer.getProperty("userEmail");
			String userAddr = (String) customer.getProperty("userAddr");
			String userPhone = (String) customer.getProperty("userPhone");
			resp.getWriter().println(
					"<h2>" + userName + " " + userEmail + ": " + userAddr + " "
							+ userPhone + " </h2>");
		} catch (EntityNotFoundException e) {
			resp.getWriter().println("<h2>Customer not found</h2>");
		}

		resp.getWriter().println("</body></html>");
	}

	private void handleCustomer(HttpServletRequest req,
			HttpServletResponse resp, String userName, String userEmail,
			String userAddr, String userPhone, String userPswd)
			throws IOException {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		resp.setContentType("text/html");
		resp.getWriter().println("<html><body>");

		Entity customer = new Entity("Customer", userName);
		customer.setProperty("userEmail", userEmail);
		customer.setProperty("userAddr", userAddr);
		customer.setProperty("userPhone", userPhone);
		customer.setProperty("userPswd", userPswd);
		datastore.put(customer);
		resp.getWriter().println(
				"<h2>Stored " + userName + " " + userEmail + ": " + userAddr
						+ " " + userPhone + " in Datastore</h2>");

		resp.getWriter().println("</body></html>");
	}

	private void handleWrong(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		resp.getWriter().println("<html><body>");

		resp.getWriter().println("<h2>Wrong parameters</h2>");

		resp.getWriter().println("</body></html>");
	}

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String name = req.getParameter("PizzaShop");
		if (name != null) {
			String shopAddr = req.getParameter("shopAddr");
			if (shopAddr == null) {
				handlePizzaShop(req, resp, name);
			} else {
				handlePizzaShop(req, resp, name, shopAddr,
						req.getParameter("shopPhone"));
			}
		} else {
			name = req.getParameter("Customer");
			if (name != null) {
				String userEmail = req.getParameter("userEmail");
				if (userEmail == null) {
					handleCustomer(req, resp, name);
				} else {
					handleCustomer(req, resp, name, userEmail,
							req.getParameter("userAddr"),
							req.getParameter("userPhone"),
							req.getParameter("userPswd"));
				}
			} else {
				handleWrong(req, resp);
			}
		}
	}
}
