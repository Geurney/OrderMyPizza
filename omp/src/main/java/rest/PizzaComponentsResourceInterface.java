/**
 * 
 */
package rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import pizza.PizzaComponent;

/**
 * Pizza Components Resource. When using for browser, user must login as
 * pizzashop to be authorized. When using for curl, user must provide token
 * associated with its pizzashop to be authorized with path /authorize/{token}.
 * 
 * @author Geurney
 *
 */
public interface PizzaComponentsResourceInterface<T extends PizzaComponent> {
	/**
	 * Get Pizza Components
	 * 
	 * @return list of Pizza Components
	 */
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	List<T> getComponents();

	/**
	 * Get Pizza Components with token for curl
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @return list of Pizza Components
	 */
	@GET
	@Produces({ MediaType.TEXT_XML, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_JSON })
	List<T> getComponents(String token);

	/**
	 * Add the Pizza Component into PizzaFactory with JSON
	 * 
	 * @param component
	 *            Pizza Component
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	void postPizzaComponent(T component);

	/**
	 * Add the Pizza Component into PizzaFactory with JSON with token for curl
	 * 
	 * @param token
	 *            Pizza Factory token
	 * @param component
	 *            Pizza Component
	 */
	@Path("/authorize/{token}")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	void postPizzaComponent(String token, T component);

	/**
	 * Delete All Pizza Components
	 * 
	 */
	@DELETE
	void deletePizzaComponents();

	/**
	 * Delete All Pizza Components with token for curl
	 * 
	 * @param token
	 *            Pizza Factory token
	 */
	@Path("/authorize/{token}")
	@DELETE
	void deletePizzaComponents(String token);

}
