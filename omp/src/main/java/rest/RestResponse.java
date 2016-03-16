/**
 * 
 */
package rest;

import javax.ws.rs.core.Response;

/**
 * Rest Response
 * 
 * @author Geurney
 *
 */
public final class RestResponse {
	/**
	 * Forbidden, wrong/null token or hash_uid
	 */
	public static Response FORBIDDEN = Response.status(
			Response.Status.FORBIDDEN).build();
	/**
	 * Resource not exist
	 */
	public static Response NOT_FOUND = Response.status(
			Response.Status.NOT_FOUND).build();

	/**
	 * Wrong parameters
	 */
	public static Response BAD = Response.status(Response.Status.BAD_REQUEST)
			.build();

	/**
	 * OK
	 */
	public static Response OK = Response.ok().build();

	/**
	 * OK
	 * 
	 * @param object
	 *            Rest Resource
	 * @return Response
	 */
	public static Response OK(Object object) {
		return Response.ok(object).build();
	}

	/**
	 * Forbidden
	 * 
	 * @return Response
	 */
	public static Response FORBIDDEN() {
		return Response.status(Response.Status.FORBIDDEN)
				.entity("{\"Error\":\"You must log in!\"}").build();
	}

	/**
	 * Not found
	 * 
	 * @return response
	 */
	public static Response NOT_FOUND() {
		return Response
				.status(Response.Status.NOT_FOUND)
				.entity("{\"Error\":\"The resource you are looking for doesn't exist!\"}")
				.build();
	}
	
	public static Response BAD() {
		return Response
				.status(Response.Status.BAD_REQUEST)
				.entity("{\"Error\":\"The input parameters you provide are invalid!\"}")
				.build();
	}

	private RestResponse() {

	}
}
