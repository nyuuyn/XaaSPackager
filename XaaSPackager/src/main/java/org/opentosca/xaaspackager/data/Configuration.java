package org.opentosca.xaaspackager.data;

/**
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class Configuration {

	private static String wineryAddress = "http://dev.winery.opentosca.org/winery/";
	private static String containerAddress = "http://localhost:1337/container";

	private Configuration() {
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
		return wineryAddress;
	}

	/**
	 * @param wineryAddress
	 *            the wineryAddress to set
	 */
	public void setWineryAddress(String wineryAddress) {
		Configuration.wineryAddress = wineryAddress;
	}

	/**
	 * @return the containerAddress
	 */
	public String getContainerAddress() {
		return containerAddress;
	}

	/**
	 * @param containerAddress
	 *            the containerAddress to set
	 */
	public void setContainerAddress(String containerAddress) {
		Configuration.containerAddress = containerAddress;
	}

}
