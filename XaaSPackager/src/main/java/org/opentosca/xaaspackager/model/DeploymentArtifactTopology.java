package org.opentosca.xaaspackager.model;

import javax.xml.namespace.QName;

/**
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class DeploymentArtifactTopology {

	private QName serviceTemplate;
	private String nodeTemplate;
	private QName artifactType;
	private String deploymentArtifact;

	public DeploymentArtifactTopology(QName serviceTemplate,
			String nodeTemplate, QName artifactType, String deploymentArtifact) {
		this.serviceTemplate = serviceTemplate;
		this.nodeTemplate = nodeTemplate;
		this.artifactType = artifactType;
		this.deploymentArtifact = deploymentArtifact;
	}

	/**
	 * @return the serviceTemplate
	 */
	public QName getServiceTemplate() {
		return serviceTemplate;
	}

	/**
	 * @return the nodeTemplate
	 */
	public String getNodeTemplate() {
		return nodeTemplate;
	}

	/**
	 * @return the artifactType
	 */
	public QName getArtifactType() {
		return artifactType;
	}

	/**
	 * @return the deploymentArtifact
	 */
	public String getDeploymentArtifact() {
		return deploymentArtifact;
	}

}
