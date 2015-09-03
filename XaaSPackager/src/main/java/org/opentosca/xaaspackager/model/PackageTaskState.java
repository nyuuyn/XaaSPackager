package org.opentosca.xaaspackager.model;

import java.nio.file.Path;
import java.util.logging.Logger;

/**
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class PackageTaskState {

	private static final Logger LOGGER = Logger
			.getLogger(PackageTaskState.class.getName());

	public enum PackageState {
		DOWNLOADING, DOWNLOADED, PACKAGING, PACKAGED, ERROR
	}

	private final long id;
	private PackageState currentState = PackageState.DOWNLOADING;
	private String currentMessage = "CSAR is beeing downloaded";

	private Path deploymentArtifactPath;
	private DeploymentArtifactTopology deploymentArtifactTopology;

	private Path downloadedCsarPath = null;
	private Path unpackedCsarPath = null;
	private Path serviceTemplatePath = null;
	private Path packedCsarPath = null;

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
	 * @param currentState
	 *            the currentState to set
	 */
	public void setCurrentState(PackageState currentState) {
		LOGGER.info("Changing state of task " + this.id + " to " + currentState);
		this.currentState = currentState;
	}

	/**
	 * @return the currentState
	 */
	public PackageState getCurrentState() {
		return currentState;
	}

	/**
	 * @return the currentMessage
	 */
	public String getCurrentMessage() {
		return currentMessage;
	}

	/**
	 * @param currentMessage
	 *            the currentMessage to set
	 */
	public void setCurrentMessage(String currentMessage) {
		LOGGER.info("Changing message of task " + this.id + " to "
				+ currentMessage);
		this.currentMessage = currentMessage;
	}

	/**
	 * @return the deploymentArtifactPath
	 */
	public Path getDeploymentArtifactPath() {
		return deploymentArtifactPath;
	}

	/**
	 * @return the deploymentArtifactTopology
	 */
	public DeploymentArtifactTopology getDeploymentArtifactTopology() {
		return deploymentArtifactTopology;
	}

	/**
	 * @return the downloadedCsarPath
	 */
	public Path getDownloadedCsarPath() {
		return downloadedCsarPath;
	}

	/**
	 * @param downloadedCsarPath
	 *            the downloadedCsarPath to set
	 */
	public void setDownloadedCsarPath(Path downloadedCsarPath) {
		LOGGER.info("Setting path of downloaded csar of task " + this.id
				+ " to " + downloadedCsarPath);
		this.downloadedCsarPath = downloadedCsarPath;
	}

	/**
	 * @return the unpackedCsarPath
	 */
	public Path getUnpackedCsarPath() {
		return unpackedCsarPath;
	}

	/**
	 * @param unpackedCsarPath
	 *            the unpackedCsarPath to set
	 */
	public void setUnpackedCsarPath(Path unpackedCsarPath) {
		LOGGER.info("Setting path of unpacked csar of task " + this.id + " to "
				+ unpackedCsarPath);
		this.unpackedCsarPath = unpackedCsarPath;
	}

	/**
	 * @return the serviceTemplatePath
	 */
	public Path getServiceTemplatePath() {
		return serviceTemplatePath;
	}

	/**
	 * @param serviceTemplatePath
	 *            the serviceTemplatePath to set
	 */
	public void setServiceTemplatePath(Path serviceTemplatePath) {
		LOGGER.info("Setting path of serviceTemplate of task " + this.id
				+ " to " + serviceTemplatePath);
		this.serviceTemplatePath = serviceTemplatePath;
	}

	/**
	 * @return the packedCsarPath
	 */
	public Path getPackedCsarPath() {
		return packedCsarPath;
	}

	/**
	 * @param packedCsarPath
	 *            the packedCsarPath to set
	 */
	public void setPackedCsarPath(Path packedCsarPath) {
		LOGGER.info("Setting path of packed csar of task " + this.id + " to "
				+ packedCsarPath);
		this.packedCsarPath = packedCsarPath;
	}
}
