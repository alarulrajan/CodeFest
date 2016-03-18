package net.sf.xplanner.rest;

import net.sf.xplanner.dao.TaskDao;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.enums.TaskStatus;

public class UpdateManager {
	private TaskDao taskDao;

	public void setTaskDao(final TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	@SuppressWarnings("boxing")
	public Result updateTaskStatus(final int id, final String status,
			final double originalEstimate) {
		final Task task = this.taskDao.getById(id);
		if (task == null) {
			return new Result(true, 404, "Task not found");
		}
		final TaskStatus taskStatus = TaskStatus.fromName(status);
		if (task.getNewStatus().equals(taskStatus)) {
			return new Result(true, 401, "Task status not changed");
		}
		if (taskStatus.equals(TaskStatus.NON_STARTED)) {
			return new Result(true, 402,
					"Moving Task to not started not implemented");
		}
		if (taskStatus.equals(TaskStatus.STARTED)) {
			task.setOriginalEstimate(originalEstimate);
			task.setCompleted(false);
		} else if (taskStatus.equals(TaskStatus.COMPLETED)) {
			task.setCompleted(true);
		}
		this.taskDao.save(task);
		return new Result(false, 0, "Task updated");

	}
}
