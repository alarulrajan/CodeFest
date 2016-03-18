/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Jun 29, 2004
 * Time: 12:32:50 AM
 */
package com.technoetic.xplanner.tags.displaytag;

import net.sf.xplanner.domain.Task;

import org.displaytag.decorator.TableDecorator;

public class TaskDecorator extends TableDecorator {
	public double getPercentCompleted() {
		final Task task = this.getTask();
		return HoursDecorator.getPercentCompletedScore(
				task.getEstimatedHours(), task.getActualHours(),
				task.getRemainingHours(), task.isCompleted());
	}

	public double getRemainingHours() {
		final Task task = this.getTask();
		return HoursDecorator.getRemainingHoursScore(task.getActualHours(),
				task.getRemainingHours(), task.isCompleted());
	}

	public String getAcceptorName() {
		return PersonIdDecorator.getPersonName(this.getTask().getAcceptorId());
	}

	private Task getTask() {
		return (Task) this.getCurrentRowObject();
	}

}