package org.opentosca.xaaspackager.model;

import java.nio.file.Path;

/**
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class PackageTaskState {

	public enum PackageState {
		DOWNLOADED, PACKAGING, PACKAGED, ERROR
	}

	private final long id;
	private PackageState currentState = PackageState.DOWNLOADED;
	private Path deploymentArtifactPath;
	private DeploymentArtifactTopology deploymentArtifactTopology;

	public PackageTaskState(Path deploymentArtifactPath,
			DeploymentArtifactTopology deploymentArtifactTopology) {
		this.id = System.currentTimeMillis();
		this.deploymentArtifactPath = deploymentArtifactPath;
		this.deploymentArtifactTopology = deploymentArtifactTopology;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @return the currentState
	 */
	public PackageState getCurrentState() {
		return currentState;
	}
}
