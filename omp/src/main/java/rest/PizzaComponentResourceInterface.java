/**
 * 
 */
package rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pizza.PizzaComponent;

/**
 * Pizza Component Resource Interface. When using for browser, user must login
 * as pizzashop to be authorized. When using for curl, user must provide token
 * associated with its pizzashop to be authorized with path /authorize/{token}.
 * Note PUT method with form should also be implemented.
 * 
 * @author Geurney
 * @param <T>
 *            Pizza Component Type
 */

public interface PizzaComponentResourceInterface<T extends PizzaComponent> {
	/**
	 * Get Pizza Component in datastore.
	 * 
	 * @return Pizza Component
	 */
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	T getPizzaComponent();

	/**
	 * Get Pizza Component in datastore with token for curl
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @return Pizza Component
	 */
	@Path("/authorize/{token}")
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	T getPizzaComponent(@PathParam("token") String token);

	/**
	 * Update the Pizza Component in Pizza Factory with JSON
	 * 
	 * @param component
	 *            PizzaComponent
	 */
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	void putPizzaComponent(T component);

	/**
	 * Update the Pizza Component in PizzaFactory with JSON with token for curl
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @param pizzaCrust
	 *            Pizza Component
	 */
	@Path("/authorize/{token}")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void putPizzaComponent(@PathParam("token") String token, T component);

	/**
	 * Delete Pizza Component
	 * 
	 */
	@DELETE
	public void deletePizzaComponent();

	/**
	 * Delete Pizza Component with token for curl
	 * 
	 * @param token
	 *            Pizza Factory token
	 */
	@Path("/authorize/{token}")
	@DELETE
	public void deletePizzaComponentToken(@PathParam("token") String token);

}
