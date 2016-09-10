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

	public class ResponseWrapper{
		public List<QName> artefactTypes = new ArrayList<QName>();
		public List<QName> nodeTypes = new ArrayList<QName>();
		
		ResponseWrapper(List<QName> artefactTypes, List<QName> nodeTypes){
			this.artefactTypes = artefactTypes;
			this.nodeTypes = nodeTypes;
		}
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response root() {
		LOG.info("Mainpage is requested");

		List<DeploymentArtifactTopology> dats = new ArrayList<DeploymentArtifactTopology>();
		
		try {
			dats.addAll(new DATopologyDataSource().getDATs());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.ok(
				new Viewable("index", new MainResourceDAO(Configuration
						.getInstance(), dats))).build();
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
