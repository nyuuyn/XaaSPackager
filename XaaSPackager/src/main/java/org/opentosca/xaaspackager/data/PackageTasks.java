package org.opentosca.xaaspackager.data;

import java.util.ArrayList;
import java.util.List;

import org.opentosca.xaaspackager.model.PackageTaskState;

/**
 * @author Kálmán Képes - kepeskn@studi.informatik.uni-stuttgart.de
 *
 */
public class PackageTasks {
	
	public List<PackageTaskState> tasks = new ArrayList<PackageTaskState>();
	
	private PackageTasks() {
	}

	private static class SingletonHolder {
		private static final PackageTasks INSTANCE = new PackageTasks();
	}

	public static PackageTasks getInstance() {
		return SingletonHolder.INSTANCE;
	}

}
