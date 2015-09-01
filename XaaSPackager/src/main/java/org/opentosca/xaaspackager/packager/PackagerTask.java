package org.opentosca.xaaspackager.packager;

import org.opentosca.xaaspackager.model.PackageTaskState;

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
		// TODO Auto-generated method stub

	}

}
