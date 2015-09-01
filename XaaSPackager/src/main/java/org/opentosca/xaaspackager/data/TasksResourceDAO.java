package org.opentosca.xaaspackager.data;

import java.util.List;

import org.opentosca.xaaspackager.model.PackageTaskState;

public class TasksResourceDAO {

	public final List<PackageTaskState> tasks;

	public TasksResourceDAO() {
		this.tasks = PackageTasks.getInstance().tasks;
	}

	/**
	 * @return the tasks
	 */
	public List<PackageTaskState> getTasks() {
		return tasks;
	}
}
