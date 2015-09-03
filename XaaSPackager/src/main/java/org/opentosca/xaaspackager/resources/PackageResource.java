package org.opentosca.xaaspackager.resources;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.namespace.QName;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.opentosca.xaaspackager.data.DATopologyDataSource;
import org.opentosca.xaaspackager.data.PackageTasks;
import org.opentosca.xaaspackager.model.DeploymentArtifactTopology;
import org.opentosca.xaaspackager.model.PackageTaskState;
import org.opentosca.xaaspackager.packager.PackagerTask;

/**
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class PackageResource {

	private static final Logger LOG = Logger.getLogger(PackageResource.class
			.getName());

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadDeploymentArtifact(
			@FormDataParam("file") InputStream uploadedInputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail,
			@FormDataParam("artifactType") QName artifactType)
			throws IOException, URISyntaxException {
		LOG.info("Begining to handle FileUpload request of artifactType "
				+ artifactType);
		// create temp dir for receiving sent file
		java.nio.file.Path downloadTempDir = Files
				.createTempDirectory("XaaSPackager");
		java.nio.file.Path downloadedFile = Paths.get(
				downloadTempDir.toString(), fileDetail.getFileName());
		FileUtils.copyInputStreamToFile(uploadedInputStream,
				downloadedFile.toFile());

		// TODO we should make some checks on the file e.g. file ending, maybe
		// contents. May greatly increase complexity of DAT however.

		PackageTaskState newTask = null;

		for (DeploymentArtifactTopology dat : new DATopologyDataSource()
				.getDATs()) {
			if (dat.getArtifactType().equals(artifactType)) {
				newTask = new PackageTaskState(downloadedFile, dat);
			}
		}

		if (newTask == null) {
			return Response
					.serverError()
					.entity("Couldn't find Topology for requested artifactType")
					.build();
		}

		
		PackageTasks.getInstance().tasks.add(newTask);

		new Thread(new PackagerTask(newTask)).start();
		// TODO redirect to task resource or just return location in header
		
		URI newTaskUri = new URI("/XaaSPackager/tasks/" + newTask.getId());
		return Response.seeOther(newTaskUri).build();
		//return Response.status(200).entity("upload successful").build();
	}
}
