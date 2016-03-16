/**
 * For curl test
 */
package rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

/**
 * This is for curl test.
 * 
 * @author Geurney
 *
 */
@Path("/test")
public class TestResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	@Context
	Response response;

	@Path("/ds")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getDatastore() {
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Query q = new Query("Test");
		PreparedQuery pq = datastore.prepare(q);
		List<Test> tests = new ArrayList<Test>();
		for (Entity result : pq.asIterable()) {
			Test test = entityToObject(result);
			tests.add(test);
		}
		GenericEntity<List<Test>> list = new GenericEntity<List<Test>>(tests) {
		};
		response = RestResponse.OK(list);
		return response;
	}

	@Path("/ds")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postDatastore(Test test) {
		String keyname = test.getKeyName();
		if (keyname == null) {
			return RestResponse.BAD;
		}
		String name = test.getName();
		String phone = test.getPhone();
		String address = test.getAddress();
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		Entity entity = new Entity("Test", keyname);
		entity.setProperty("keyname", keyname);
		if (name != null) {
			entity.setProperty("name", name);
		}
		if (phone != null) {
			entity.setProperty("phone", phone);
		}
		if (address != null) {
			entity.setProperty("address", address);
		}
		datastore.put(entity);
		return RestResponse.OK;
	}

	@Path("/mc/{keyname}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getCache(@PathParam("keyname") String keyname) {
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));
		Object[] values = (Object[]) syncCache.get(keyname);
		return RestResponse.OK(cacheToObject(values));
	}

	@Path("/mc")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postCache(Test test) {
		String keyname = test.getKeyName();
		String name = test.getName();
		String phone = test.getPhone();
		String address = test.getAddress();
		if (keyname == null || name == null || phone == null || address == null) {
			return RestResponse.BAD;
		}
		Object[] values = new Object[4];
		values[0] = keyname;
		values[1] = name;
		values[2] = phone;
		values[3] = address;
		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
		syncCache.setErrorHandler(ErrorHandlers
				.getConsistentLogAndContinue(Level.INFO));
		syncCache.put(keyname, values);
		return RestResponse.OK;
	}

	@Path("/qu")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	public Response getQueue() {
		Queue queue = QueueFactory.getDefaultQueue();
		return RestResponse.OK("{\"queuename\":\""
				+ queue.fetchStatistics().getQueueName()
				+ "\",\"numoftasks\":\""
				+ queue.fetchStatistics().getNumTasks() + "\"}");
	}

	@Path("/qu")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postQueue(Test test) {
		String keyname = test.getKeyName();
		String name = test.getName();
		String phone = test.getPhone();
		String address = test.getAddress();
		if (keyname == null || name == null || phone == null || address == null) {
			return RestResponse.BAD;
		}
		TaskOptions task = TaskOptions.Builder.withUrl("/curl/test/worker")
				.param("keyname", keyname).param("name", name)
				.param("phone", phone).param("address", address);
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(task);
		return RestResponse.OK;
	}

	/**
	 * Convert entity to object
	 * 
	 * @param entity
	 *            Entity
	 * @return test object
	 */
	public static Test entityToObject(Entity entity) {
		if (entity == null || entity.getKind().equals("test")) {
			return null;
		}
		Test test = new Test();
		String keyname = (String) entity.getProperty("keyname");
		String name = (String) entity.getProperty("name");
		String phone = (String) entity.getProperty("phone");
		String address = (String) entity.getProperty("address");
		if (keyname != null) {
			test.setKeyName(keyname);
		}
		if (name != null) {
			test.setName(name);
		}
		if (phone != null) {
			test.setPhone(phone);
		}
		if (address != null) {
			test.setAddress(address);
		}
		return test;
	}

	public static Test cacheToObject(Object[] values) {
		if (values == null || values.length != 4) {
			return null;
		}
		String keyname = (String) values[0];
		String name = (String) values[1];
		String phone = (String) values[2];
		String address = (String) values[3];
		Test test = new Test();
		if (keyname != null) {
			test.setKeyName(keyname);
		}
		if (name != null) {
			test.setName(name);
		}
		if (phone != null) {
			test.setPhone(phone);
		}
		if (address != null) {
			test.setAddress(address);
		}
		return test;
	}

}
