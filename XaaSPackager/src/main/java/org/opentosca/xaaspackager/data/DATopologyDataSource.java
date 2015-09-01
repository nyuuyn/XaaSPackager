package org.opentosca.xaaspackager.data;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.opentosca.xaaspackager.model.DeploymentArtifactTopology;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class DATopologyDataSource {

	public List<DeploymentArtifactTopology> getDATs() throws IOException {
		List<DeploymentArtifactTopology> dats = new ArrayList<DeploymentArtifactTopology>();

		URL datsURL = DATopologyDataSource.class
				.getResource("/deploymentArtifactTopologies.xml");
		InputStream is = datsURL.openStream();

		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();

		InputSource inputSource = new InputSource(is);

		String expression = "/dats/dat";
		try {
			NodeList list = (NodeList) xpath.evaluate(expression, inputSource,
					XPathConstants.NODESET);

			for (int index = 0; index < list.getLength(); index++) {
				Element datElement = (Element) list.item(index);
				NodeList datChildNodes = datElement.getChildNodes();
				QName serviceTemplate = null;
				String nodeTemplate = null;
				QName artifactType = null;
				String deploymentArtifact = null;

				for (int datChildIndex = 0; datChildIndex < datChildNodes
						.getLength(); datChildIndex++) {
					
					if(datChildNodes.item(datChildIndex).getNodeType() != Node.ELEMENT_NODE){
						continue;
					}
					
					Element datChild = (Element) datChildNodes
							.item(datChildIndex);

					switch (datChild.getLocalName()) {

					case "serviceTemplate":
						serviceTemplate = QName.valueOf(datChild
								.getTextContent());
						break;
					case "artifactType":
						artifactType = QName.valueOf(datChild.getTextContent());
						break;
					case "nodeTemplate":
						nodeTemplate = datChild.getTextContent();
						break;
					case "deploymentArtifact":
						deploymentArtifact = datChild.getTextContent();
						break;
					default:
						continue;
					}
				}

				if (serviceTemplate != null && artifactType != null
						&& nodeTemplate != null && deploymentArtifact != null) {
					dats.add(new DeploymentArtifactTopology(serviceTemplate,
							nodeTemplate, artifactType, deploymentArtifact));
				} else {
					continue;
				}
			}
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dats;
	}
}
