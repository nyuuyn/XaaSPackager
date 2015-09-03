package org.opentosca.xaaspackager.packager;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.opentosca.xaaspackager.data.Configuration;
import org.opentosca.xaaspackager.model.PackageTaskState;
import org.opentosca.xaaspackager.model.PackageTaskState.PackageState;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.zeroturnaround.zip.ZipUtil;

/**
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class PackagerTask implements Runnable {

	private PackageTaskState currentState;

	public PackagerTask(PackageTaskState newTaskState) {
		this.currentState = newTaskState;
	}

	@Override
	public void run() {
		/* fetch csar from winery */
		// info about the da-topology
		QName serviceTemplate = this.currentState
				.getDeploymentArtifactTopology().getServiceTemplate();
		String nodeTemplate = this.currentState.getDeploymentArtifactTopology()
				.getNodeTemplate();
		QName artifactType = this.currentState.getDeploymentArtifactTopology()
				.getArtifactType();
		String deploymentArtifact = this.currentState
				.getDeploymentArtifactTopology().getDeploymentArtifact();

		String wineryAddress = Configuration.getInstance().getWineryAddress();

		Client client = ClientBuilder.newClient();
		WebTarget serviceTemplateResource = client
				.target(wineryAddress)
				.path("servicetemplates")
				.path(URLEncoder.encode(URLEncoder.encode(serviceTemplate
						.getNamespaceURI())))
				.path(serviceTemplate.getLocalPart());

		Response serviceTemplateResourceResponse = serviceTemplateResource
				.request("application/xml").get();

		if (serviceTemplateResourceResponse.getStatus() == 200) {
			this.currentState
					.setCurrentMessage("ServiceTemplate was found. Checking for target deploymentArtifact "
							+ deploymentArtifact);
			String responseBody = serviceTemplateResourceResponse
					.readEntity(String.class);

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();

			InputSource source = new InputSource(new StringReader(responseBody));

			try {
				NodeList nodeTemplateList = (NodeList) xpath.evaluate(this
						.xpathQueryTargetDA(serviceTemplate, nodeTemplate,
								artifactType, deploymentArtifact), source,
						XPathConstants.NODESET);

				if (nodeTemplateList.getLength() == 1) {

					WebTarget csarResource = serviceTemplateResource.queryParam("csar", "");
					this.currentState.setCurrentState(PackageState.DOWNLOADING);
					this.currentState
							.setCurrentMessage("Beginning to download CSAR from "
									+ csarResource.getUri());
					
					
					Response csarResourceResponse = csarResource.request().get();										
					InputStream inputStream = (InputStream) csarResourceResponse
							.getEntity();

					// create temp dir for receiving file
					Path downloadTempDir = Files
							.createTempDirectory("XaaSPackager");
					Path downloadedFile = Paths.get(downloadTempDir.toString(),
							serviceTemplate.getLocalPart() + ".csar");
					FileUtils.copyInputStreamToFile(inputStream,
							downloadedFile.toFile());

					this.currentState.setDownloadedCsarPath(downloadedFile);
					this.currentState.setCurrentState(PackageState.DOWNLOADED);
					this.currentState
							.setCurrentMessage("CSAR was downloaded successfully. Beginning unpacking");

					Path unpackTempDir = Files
							.createTempDirectory("XaaSPackager");
					ZipUtil.unpack(this.currentState.getDownloadedCsarPath()
							.toFile(), unpackTempDir.toFile());

					this.currentState.setUnpackedCsarPath(unpackTempDir);
				} else {
					this.currentState.setCurrentState(PackageState.ERROR);
					this.currentState
							.setCurrentMessage("Found ServiceTemplate doesn't contain required NodeTemplate and DeploymentArtifact");
					return;
				}
			} catch (XPathExpressionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			this.currentState.setCurrentState(PackageState.ERROR);
			switch (serviceTemplateResourceResponse.getStatus() / 100) {
			case 4:
				this.currentState.setCurrentMessage("Couldn't find CSAR at "
						+ serviceTemplateResource.getUri());
				break;
			case 5:
				this.currentState
						.setCurrentMessage("Winery threw Server Error for requesting "
								+ serviceTemplateResource.getUri());
				break;
			default:
				break;
			}

			return;
		}

		/* add da */
		this.currentState.setCurrentState(PackageState.PACKAGING);
		this.currentState.setCurrentMessage("Adding deploymentArtifact");

		String relativeDAPath = "/xaaspackager/" + URLEncoder.encode(artifactType.toString())
				+ "/"
				+ this.currentState.getDeploymentArtifactPath().getFileName()
				+ "/"
				+ this.currentState.getDeploymentArtifactPath().getFileName();

		// copy da into csar temp dir
		Path daPath = Paths.get(this.currentState.getUnpackedCsarPath()
				.toString() + relativeDAPath);

		try {
			FileUtils.copyFile(this.currentState.getDeploymentArtifactPath()
					.toFile(), daPath.toFile());
			this.currentState
					.setCurrentMessage("Adding DA and ArtifactTemplate to ServiceTemplate");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// TODO add artifactTemplate and change DA on NodeTemplate
		// find serviceTemplate
		Path toscaMetaPath = Paths.get(this.currentState.getUnpackedCsarPath()
				.toString() + "/TOSCA-Metadata/TOSCA.meta");
		String toscaMeta = null;
		try {
			toscaMeta = FileUtils.readFileToString(toscaMetaPath.toFile());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (toscaMeta == null) {
			this.currentState.setCurrentState(PackageState.ERROR);
			this.currentState
					.setCurrentMessage("Couldn't find TOSCA.meta file in downloaded CSAR");
			return;
		}

		// split using newline seperators
		for (String toscaMetaLine : toscaMeta.split("[\r\n]+")) {
			if (toscaMetaLine.startsWith("Entry-Definitions:")) {
				String relativeServiceTemplatePath = toscaMetaLine.split(":")[1]
						.trim();
				Path serviceTemplatePath = Paths.get(this.currentState
						.getUnpackedCsarPath().toString(),
						relativeServiceTemplatePath);
				this.currentState.setServiceTemplatePath(serviceTemplatePath);
			}
		}

		if (this.currentState.getServiceTemplatePath() == null) {
			this.currentState.setCurrentState(PackageState.ERROR);
			this.currentState
					.setCurrentMessage("Couldn't find ServiceTemplate "
							+ serviceTemplate + " in downloaded CSAR");
			return;
		}

		// fetch defintions and add artifactTemplate
		try {
			String serviceTemplateContent = FileUtils
					.readFileToString(this.currentState
							.getServiceTemplatePath().toFile());

			XPathFactory xpathFactory = XPathFactory.newInstance();
			XPath xpath = xpathFactory.newXPath();

			InputSource source = new InputSource(new StringReader(
					serviceTemplateContent));

			Element definitionsElement = (Element) xpath.evaluate(
					this.xpathQueryTargetDefinitions(), source,
					XPathConstants.NODE);

			if (definitionsElement == null) {
				this.currentState.setCurrentState(PackageState.ERROR);
				this.currentState
						.setCurrentMessage("Couldn't find definitions element in ServiceTemplate document "
								+ this.currentState.getServiceTemplatePath());
			}

			Document definitionsDoc = definitionsElement.getOwnerDocument();

			String artifactTemplateId = "xaaspackagerTemplate"
					+ System.currentTimeMillis();

			Node artifactTemplateNode = this
					.createArtifactTemplateNode(artifactTemplateId,
							artifactType, Paths.get(relativeDAPath));

			artifactTemplateNode = definitionsDoc.importNode(
					artifactTemplateNode, true);
			definitionsElement.appendChild(artifactTemplateNode);

			// fetch deploymentArtifact and change it new artifactTemplate
			Element deploymentArtifactNode = (Element) xpath.evaluate(this
					.xpathQueryTargetDA(serviceTemplate, nodeTemplate,
							artifactType, deploymentArtifact), definitionsDoc,
					XPathConstants.NODE);

			if (deploymentArtifactNode == null) {
				this.currentState.setCurrentState(PackageState.ERROR);
				this.currentState
						.setCurrentMessage("Couldn't find target deploymentArtifact in "
								+ this.currentState.getServiceTemplatePath());
				return;
			}

			String nsPrefix = "ns" + System.currentTimeMillis();
			deploymentArtifactNode.setAttribute("xmlns:" + nsPrefix,
					serviceTemplate.getNamespaceURI());

			deploymentArtifactNode.setAttribute("artifactRef", nsPrefix + ":"
					+ artifactTemplateId);

			Transformer transformer = TransformerFactory.newInstance()
					.newTransformer();
			Result output = new StreamResult(this.currentState
					.getServiceTemplatePath().toFile());
			Source input = new DOMSource(definitionsDoc);

			transformer.transform(input, output);

		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* repackage csar */
		// create temp dir for receiving file

		try {
			Path packagingTempDir = Files.createTempDirectory("XaaSPackager");
			Path packagedCsarFile = Paths.get(packagingTempDir.toString(),
					serviceTemplate.getLocalPart() + ".csar");

			ZipUtil.pack(this.currentState.getUnpackedCsarPath().toFile(),
					packagedCsarFile.toFile());

			this.currentState.setPackedCsarPath(packagedCsarFile);
			this.currentState.setCurrentState(PackageState.PACKAGED);
			this.currentState
					.setCurrentMessage("CSAR packed and ready. See download link");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Node createArtifactTemplateNode(String artifactTemplateId,
			QName artifactType, Path relativeDAPath) {
		long id = System.currentTimeMillis();
		String artifactTemplate = "<ArtifactTemplate xmlns=\"http://docs.oasis-open.org/tosca/ns/2011/12\" id=\""
				+ artifactTemplateId
				+ "\" xmlns:ns"
				+ id
				+ "=\""
				+ artifactType.getNamespaceURI()
				+ "\" type=\"ns"
				+ id
				+ ":"
				+ artifactType.getLocalPart()
				+ "\"><ArtifactReferences><ArtifactReference reference=\""
				+ relativeDAPath.toString()
				+ "\"/></ArtifactReferences></ArtifactTemplate>";

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();

		InputSource source = new InputSource(new StringReader(artifactTemplate));
		try {
			Node artifactTemplateNode = (Node) xpath.evaluate(
					"/*[local-name()='ArtifactTemplate']", source,
					XPathConstants.NODE);
			return artifactTemplateNode;
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private String xpathQueryTargetDefinitions() {
		return "/*[local-name()='Definitions']";
	}

	private String xpathQueryTargetDA(QName serviceTemplate,
			String nodeTemplate, QName artifactType, String deploymentArtifact) {
		return this.xpathQueryTargetDefinitions()
				+ "/*[local-name()='ServiceTemplate' and @id='"
				+ serviceTemplate.getLocalPart()
				+ "']/*[local-name()='TopologyTemplate']/*[local-name()='NodeTemplate' and @id='"
				+ nodeTemplate
				+ "']/*[local-name()='DeploymentArtifacts']/*[local-name()='DeploymentArtifact' and @name='"
				+ deploymentArtifact + "']";
	}

}
