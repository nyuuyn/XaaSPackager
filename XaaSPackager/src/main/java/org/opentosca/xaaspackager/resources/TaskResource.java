package org.opentosca.xaaspackager.resources;

import java.net.URI;
import java.net.URISyntaxException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;
import org.opentosca.xaaspackager.model.PackageTaskState;
import org.opentosca.xaaspackager.model.PackageTaskState.PackageState;

public class TaskResource {

	private PackageTaskState state;

	public TaskResource(PackageTaskState state) {
		this.state = state;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getTask() {
		return Response.ok(new Viewable("index", this.state)).build();
	}

	@Path("/state")
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public Response getState() {
		return Response.ok(this.state.getCurrentState().toString()).build();
	}

	@Path("/download")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getDownload() throws URISyntaxException {
		if (this.state.getCurrentState().equals(PackageState.PACKAGED)) {

			return Response
					.ok(state.getPackedCsarPath().toFile())
					.header("Content-Disposition",
							"attachment; filename=\""
									+ this.state.getPackedCsarPath()
											.getFileName() + "\"").build();
		} else {
			URI redirectUri = new URI("/tasks/" + this.state.getId());
			return Response.seeOther(redirectUri).build();
		}
	}

}
