/**
 * 
 */
package rest;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * Test Worker for curl test
 * 
 * @author Geurney
 *
 */
public class TestWorker extends HttpServlet {

	private static final long serialVersionUID = -8948135566557771095L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		System.out.println("HERE!");
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		String keyname = request.getParameter("keyname");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		if (keyname == null || name == null || phone == null || address == null) {
			return;
		}
		Entity entity = new Entity("Test", keyname);
		entity.setProperty("keyname", keyname);
		entity.setProperty("name", name);
		entity.setProperty("phone", phone);
		entity.setProperty("address", address);
		datastore.put(entity);
		Object[] values = new Object[4];
		values[0] = keyname;
		values[1] = name;
		values[2] = phone;
		values[3] = address;
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));
		syncCache.put(keyname, values);
	}

}
