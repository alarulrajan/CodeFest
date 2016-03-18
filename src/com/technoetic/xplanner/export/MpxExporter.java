package com.technoetic.xplanner.export;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.mpxj.Duration;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectHeader;
import net.sf.mpxj.Resource;
import net.sf.mpxj.Task;
import net.sf.mpxj.TimeUnit;
import net.sf.mpxj.mpx.MPXWriter;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.lang.StringUtils;
import org.hibernate.classic.Session;

public class MpxExporter implements Exporter {
	@Override
	public void initializeHeaders(final HttpServletResponse response) {
		response.setHeader("Content-type", "application/mpx");
		response.setHeader("Content-disposition", "inline; filename=export.mpx");
	}

	@Override
	public byte[] export(final Session session, final Object object)
			throws ExportException {
		try {
			final ProjectFile file = new ProjectFile();
			file.setAutoTaskID(true);
			file.setAutoTaskUniqueID(true);
			file.setAutoResourceID(true);
			file.setAutoResourceUniqueID(true);
			file.setAutoOutlineLevel(true);
			file.setAutoOutlineNumber(true);
			file.setAutoWBS(true);
			// Add a default calendar called "Standard"
			// file.addDefaultBaseCalendar();

			final ResourceRegistry resourceRegistry = new ResourceRegistry(
					session.find("from person in " + Person.class), file);

			if (object instanceof Project) {
				this.exportProject(file, (Project) object, resourceRegistry);
			} else if (object instanceof Iteration) {
				this.exportIteration(file, null, (Iteration) object,
						resourceRegistry);
			}

			final ByteArrayOutputStream data = new ByteArrayOutputStream();
			final MPXWriter writer = new MPXWriter();
			writer.write(file, data);
			return data.toByteArray();
		} catch (final Exception e) {
			throw new ExportException("exception during export", e);
		}
	}

	private String filterString(final String s) {
		return s.replaceAll("\r", "");
	}

	protected void exportProject(final ProjectFile file, final Project project,
			final ResourceRegistry resources) {
		final ProjectHeader header = file.getProjectHeader();
		final Task projectLevelTask = file.addTask();
		header.setProjectTitle(project.getName());
		projectLevelTask.setName(project.getName());
		if (!StringUtils.isEmpty(project.getDescription())) {
			projectLevelTask.setNotes(this.filterString(project
					.getDescription()));
		}

		long earliestStartTime = Long.MAX_VALUE;
		final List<Iteration> iterations = new ArrayList<Iteration>(
				project.getIterations());
		Collections.sort(iterations, new Comparator<Iteration>() {
			@Override
			public int compare(final Iteration o1, final Iteration o2) {
				final Iteration i1 = o1;
				final Iteration i2 = o2;
				return i1.getStartDate().compareTo(i2.getStartDate());
			}
		});
		for (final Iterator<Iteration> iterator = iterations.iterator(); iterator
				.hasNext();) {
			final Iteration iteration = iterator.next();
			this.exportIteration(file, projectLevelTask, iteration, resources);
			if (iteration.getStartDate().getTime() < earliestStartTime) {
				earliestStartTime = iteration.getStartDate().getTime();
			}
		}

		if (earliestStartTime < Long.MAX_VALUE) {
			header.setStartDate(new Date(earliestStartTime));
		} else {
			header.setStartDate(new Date());
		}
	}

	protected Task exportIteration(final ProjectFile file,
			final Task projectLevelTask, final Iteration iteration,
			final ResourceRegistry resources) {
		Task iterationLevelTask = null;
		if (projectLevelTask != null) {
			iterationLevelTask = projectLevelTask.addTask();
		} else {
			iterationLevelTask = file.addTask();
			file.getProjectHeader().setStartDate(iteration.getStartDate());
			file.getProjectHeader().setProjectTitle(iteration.getName());
		}
		iterationLevelTask.setName(iteration.getName());
		iterationLevelTask.setStart(iteration.getStartDate());
		iterationLevelTask.setFinish(iteration.getEndDate());
		if (StringUtils.isNotEmpty(iteration.getDescription())) {
			iterationLevelTask.setNotes(this.filterString(iteration
					.getDescription()));
		}
		for (final Iterator<UserStory> iterator = iteration.getUserStories()
				.iterator(); iterator.hasNext();) {
			final UserStory userStory = iterator.next();
			this.exportUserStory(iterationLevelTask, userStory, resources);
		}
		return iterationLevelTask;
	}

	protected void exportUserStory(final Task iterationLevelTask,
			final UserStory userStory, final ResourceRegistry resources) {
		final Task storyLevelTask = iterationLevelTask.addTask();
		storyLevelTask.setName(userStory.getName());
		if (StringUtils.isNotEmpty(userStory.getDescription())) {
			storyLevelTask.setNotes(this.filterString(userStory
					.getDescription()));
		}
		long earliestTaskStartTime = Long.MAX_VALUE;
		final Collection<net.sf.xplanner.domain.Task> storyTasks = userStory
				.getTasks();
		if (storyTasks.size() > 0) {
			storyLevelTask.setWork(Duration.getInstance(
					userStory.getTaskBasedEstimatedHours(), TimeUnit.HOURS));
			for (final Iterator<net.sf.xplanner.domain.Task> iterator = storyTasks
					.iterator(); iterator.hasNext();) {
				final net.sf.xplanner.domain.Task task = iterator.next();
				final Task taskLevelTask = storyLevelTask.addTask();
				taskLevelTask.setName(task.getName());
				taskLevelTask.setDuration(Duration.getInstance(
						task.getEstimatedHours(), TimeUnit.HOURS));
				if (StringUtils.isNotEmpty(task.getDescription())) {
					taskLevelTask.setNotes(this.filterString(task
							.getDescription()));
				}
				if (task.getAcceptorId() != 0) {
					taskLevelTask.addResourceAssignment(resources
							.getResource(task.getAcceptorId()));
				} else {
					taskLevelTask.setWork(Duration.getInstance(
							task.getEstimatedHours(), TimeUnit.HOURS));
				}
				if (task.getActualHours() != 0) {
					taskLevelTask.setActualWork(Duration.getInstance(
							task.getActualHours(), TimeUnit.HOURS));
					final Date startTime = this.getStartTime(
							iterationLevelTask, task);
					if (startTime != null) {
						taskLevelTask.setActualStart(startTime);
					}
				}
				if (task.getTimeEntries().size() > 0) {
					earliestTaskStartTime = this.exportTimeEntries(task,
							taskLevelTask, earliestTaskStartTime);
				} else {
					taskLevelTask.setActualStart(iterationLevelTask
							.getActualStart());
				}
			}
			if (earliestTaskStartTime == Long.MAX_VALUE) {
				earliestTaskStartTime = iterationLevelTask.getStart().getTime();
			}
			final List tasks = storyLevelTask.getChildTasks();
			for (int i = 0; i < tasks.size(); i++) {
				final Task task = (Task) tasks.get(i);
				if (task.getActualStart() == null) {
					task.setActualStart(new Date(earliestTaskStartTime));
				}
			}
		} else {
			storyLevelTask.setActualStart(iterationLevelTask.getStart());
			storyLevelTask.setDuration(Duration.getInstance(
					userStory.getEstimatedHours(), TimeUnit.HOURS));
			storyLevelTask.setWork(Duration.getInstance(
					userStory.getEstimatedHours(), TimeUnit.HOURS));
			final int trackerId = userStory.getTrackerId();
			if (trackerId != 0) {
				storyLevelTask.addResourceAssignment(resources
						.getResource(trackerId));
			}
		}
	}

	private long exportTimeEntries(final net.sf.xplanner.domain.Task task,
			final Task taskLevelTask, long earliestTaskStartTime) {
		long startTime = Long.MAX_VALUE;
		for (final Iterator timeItr = task.getTimeEntries().iterator(); timeItr
				.hasNext();) {
			final TimeEntry timeEntry = (TimeEntry) timeItr.next();
			if (timeEntry.getStartTime() != null
					&& timeEntry.getStartTime().getTime() < startTime) {
				startTime = timeEntry.getStartTime().getTime();
			}
		}
		if (startTime < Long.MAX_VALUE) {
			taskLevelTask.setActualStart(new Date(startTime));
			if (startTime < earliestTaskStartTime) {
				earliestTaskStartTime = startTime;
			}
		}
		return earliestTaskStartTime;
	}

	private Date getStartTime(final Task iterationLevelTask,
			final net.sf.xplanner.domain.Task task) {
		Date start = null;
		for (final Iterator iterator = task.getTimeEntries().iterator(); iterator
				.hasNext();) {
			final TimeEntry timeEntry = (TimeEntry) iterator.next();
			Date timeEntryStart = timeEntry.getStartTime();
			if (timeEntryStart == null) {
				timeEntryStart = timeEntry.getReportDate();
				final long durationMs = (long) timeEntry.getDuration() * 3600000L;
				final long timeEntryEnd = timeEntryStart.getTime() + durationMs;
				if (timeEntryEnd > iterationLevelTask.getFinish().getTime()) {
					timeEntryStart = new Date(iterationLevelTask.getFinish()
							.getTime() - durationMs);
				}
			}
			if (start == null || timeEntryStart != null
					&& timeEntryStart.getTime() < start.getTime()) {
				start = timeEntryStart;
			}
		}
		return start;
	}

	protected static class ResourceRegistry {
		private final HashMap resources = new HashMap();

		public ResourceRegistry(final List people, final ProjectFile mpxFile) {
			try {
				for (int i = 0; i < people.size(); i++) {
					final Person person = (Person) people.get(i);
					final Resource resource = mpxFile.addResource();
					resource.setName(person.getName());
					this.resources.put(new Integer(person.getId()), resource);
				}
			} catch (final RuntimeException ex) {
				throw ex;
			}
		}

		public Resource getResource(final int i) {
			return (Resource) this.resources.get(new Integer(i));
		}
	}
}
