/**
 * 
 */
package org.opentosca.xaaspackager.data;

import java.util.List;

import javax.xml.namespace.QName;

/**
 * @author nyu
 *
 */
public class MainResourceDAO {

	public final String wineryAddress;
	public final String containerAddress;
	public final List<QName> artifactTypes;

	public MainResourceDAO(Configuration configuration,
			List<QName> artifactTypes) {
		this.wineryAddress = configuration.getWineryAddress();
		this.containerAddress = configuration.getContainerAddress();
		this.artifactTypes = artifactTypes;
	}

	/**
	 * @return the wineryAddress
	 */
	public String getWineryAddress() {
		return wineryAddress;
	}

	/**
	 * @return the containerAddress
	 */
	public String getContainerAddress() {
		return containerAddress;
	}

	/**
	 * @return the artifactTypes
	 */
	public List<QName> getArtifactTypes() {
		return artifactTypes;
	}

}
