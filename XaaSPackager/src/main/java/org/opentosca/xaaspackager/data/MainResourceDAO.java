/**
 * 
 */
package org.opentosca.xaaspackager.data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.QName;

import org.opentosca.xaaspackager.model.DeploymentArtifactTopology;

/**
 * @author nyu
 *
 */
public class MainResourceDAO {

	public final String wineryAddress;
	public final String containerAddress;
	public final List<DeploymentArtifactTopology>  dats;


	public MainResourceDAO(Configuration configuration,
			List<DeploymentArtifactTopology> dats) {
		this.wineryAddress = configuration.getWineryAddress();
		this.containerAddress = configuration.getContainerAddress();
		this.dats = dats;
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
	 * @return the dats
	 */
	public List<DeploymentArtifactTopology> getDats() {
		return dats;
	}
	
	public Set<QName> getAllNodeTypes(){
		
		Set<QName> nodeTypes = new HashSet<QName>();
		
		for(DeploymentArtifactTopology dat : this.dats){
			nodeTypes.addAll(dat.getTopologyNodeTypes());
		}
		
		return nodeTypes;
	}
}
