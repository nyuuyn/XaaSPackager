package org.opentosca.xaaspackager.data;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



/**
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class Configuration {

	private static String wineryAddress = "http://dev.winery.opentosca.org/winery/";
	private static String containerAddress = "http://localhost:1337/container";

	/**
	 * The name of the properties file.
	 */
	private static final String PROPERTIES_FILENAME = "xaaspackager.config.properties";

	/**
	 * The properties as loaded from the file system.
	 */
	private static Properties properties = new Properties();
	
	private Configuration() {
	}
	
	static {
		InputStream inputStream = null;
		try {
			inputStream = Configuration.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME);
			if (inputStream == null) {
				throw new FileNotFoundException();
			}
			properties.load(inputStream);
		} catch (IOException e) {

		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {

			}
		}
	}

	private static class SingletonHolder {
		private static final Configuration INSTANCE = new Configuration();
	}

	public static Configuration getInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * @return the wineryAddress
	 */
	public String getWineryAddress() {
		return this.properties.getProperty("xaaspackager.winery.address");
	}

	/**
	 * @param wineryAddress
	 *            the wineryAddress to set
	 */
	public void setWineryAddress(String wineryAddress) {
		this.properties.setProperty("xaaspackager.winery.address", wineryAddress);		
	}

	/**
	 * @return the containerAddress
	 */
	public String getContainerAddress() {
		return this.properties.getProperty("xaaspackager.container.address");		
	}

	/**
	 * @param containerAddress
	 *            the containerAddress to set
	 */
	public void setContainerAddress(String containerAddress) {
		this.properties.setProperty("xaaspackager.container.address", containerAddress);		
	}

}
