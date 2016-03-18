package net.sf.xplanner.web;

import java.util.List;

import net.sf.xplanner.dao.TaskDao;
import net.sf.xplanner.dao.UserStoryDao;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.technoetic.xplanner.domain.repository.TaskRepository;

@Controller
public class MePage extends BasePage<Person> {
	@Autowired
	private TaskDao taskDao;
	@Autowired
	private UserStoryDao userStoryDao;
	@Autowired
	private TaskRepository taskRepository;

	@RequestMapping("/me/status/{id}")
	public ModelAndView list(@PathVariable final Integer id) {
		final ModelAndView modelAndView = this.getModelAndView("view/meStatus",
				id);
		// grab a list of all active tasks
		final List<Task> tasks = this.taskDao.getCurrentTasksForPerson(id);

		modelAndView.addObject("currentActiveTasksForPerson",
				this.taskRepository.getCurrentActiveTasks(tasks));
		modelAndView.addObject("currentPendingTasksForPerson",
				this.taskRepository.getCurrentPendingTasks(tasks));
		modelAndView.addObject("currentCompletedTasksForPerson",
				this.taskRepository.getCurrentCompletedTasks(tasks));
		modelAndView.addObject("futureTasksForPerson",
				this.taskRepository.getFutureTasksForPerson(id));
		modelAndView.addObject("storiesForCustomer",
				this.userStoryDao.getStoriesForPersonWhereCustomer(id));
		modelAndView.addObject("storiesForTracker",
				this.userStoryDao.getStoriesForPersonWhereTracker(id));
		return modelAndView;
	}

	public void setTaskRepository(final TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public void setTaskDao(final TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void setUserStoryDao(final UserStoryDao userStoryDao) {
		this.userStoryDao = userStoryDao;
	}
	// @RequestMapping("/{objectType}/edit/{objectId}")
	// public String edit(@PathVariable String objectType, @PathVariable Integer
	// objectId){
	// System.out.println(objectType);
	// return objectType;
	// }
}
