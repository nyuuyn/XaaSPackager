package org.opentosca.xaaspackager.model;

import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

/**
 * @author Kálmán Képes - kalman.kepes@iaas.uni-stuttgart.de
 *
 */
public class DeploymentArtifactTopology {

	private QName serviceTemplate;
	private String nodeTemplate;
	private QName artifactType;
	private String deploymentArtifact;
	private Set<QName> topologyNodeTypes;

	public DeploymentArtifactTopology(QName serviceTemplate,
			String nodeTemplate, QName artifactType, String deploymentArtifact, Set<QName> topologyNodeTypes) {
		this.serviceTemplate = serviceTemplate;
		this.nodeTemplate = nodeTemplate;
		this.artifactType = artifactType;
		this.deploymentArtifact = deploymentArtifact;
		this.topologyNodeTypes = topologyNodeTypes;
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

	/**
	 * @return the toplogyNodeTypes
	 */
	public Set<QName> getTopologyNodeTypes() {
		return topologyNodeTypes;
	}
}
