package org.opentosca.xaaspackager.resources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;

import org.glassfish.jersey.server.mvc.Viewable;
import org.opentosca.xaaspackager.data.Configuration;
import org.opentosca.xaaspackager.data.DATopologyDataSource;
import org.opentosca.xaaspackager.data.MainResourceDAO;
import org.opentosca.xaaspackager.model.DeploymentArtifactTopology;

/**
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
@Path("/")
public class MainResource {

	private static final Logger LOG = Logger.getLogger(MainResource.class
			.getName());

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response root() {
		LOG.info("Mainpage is requested");

		List<QName> artifactTypes = new ArrayList<QName>();

		try {
			for (DeploymentArtifactTopology dat : new DATopologyDataSource()
					.getDATs()) {
				artifactTypes.add(dat.getArtifactType());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.ok(
				new Viewable("index", new MainResourceDAO(Configuration
						.getInstance(), artifactTypes))).build();
	}

	@Path("/package")
	public PackageResource getPackageResource() {
		return new PackageResource();
	}

	@Path("/configuration")
	public ConfigurationResource getConfigurationResource() {
		return new ConfigurationResource();
	}

	@Path("/tasks")
	public TasksResource getTasksResource() {
		return new TasksResource();
	}

}
