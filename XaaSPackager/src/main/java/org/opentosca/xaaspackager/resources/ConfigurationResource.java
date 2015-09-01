package org.opentosca.xaaspackager.resources;

import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.media.multipart.FormDataParam;
import org.opentosca.xaaspackager.data.Configuration;

/**
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class ConfigurationResource {

	private static final Logger LOG = Logger
			.getLogger(ConfigurationResource.class.getName());

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response setConfiguration(
			@FormDataParam("wineryAddress") String wineryAddress,
			@FormDataParam("containerAddress") String containerAddress,
			@Context ServletContext context) {

		LOG.info("Handling config update request");

		if (wineryAddress != null) {
			LOG.info("Update for wineryAddress requested");
			Configuration.getInstance().setWineryAddress(wineryAddress);
		}

		if (containerAddress != null) {
			LOG.info("Update for containerAddress requested");
			Configuration.getInstance().setContainerAddress(containerAddress);
		}
		UriBuilder builder = UriBuilder.fromResource(MainResource.class);

		// TODO Fetch web app name from context
		builder.path("./XaaSPackager");
		return Response.seeOther(builder.build()).build();
	}

}
