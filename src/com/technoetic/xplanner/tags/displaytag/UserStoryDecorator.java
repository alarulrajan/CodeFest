/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Jun 29, 2004
 * Time: 12:32:50 AM
 */
package com.technoetic.xplanner.tags.displaytag;

import net.sf.xplanner.domain.UserStory;

import org.displaytag.decorator.TableDecorator;

public class UserStoryDecorator extends TableDecorator {
	public double getPercentCompleted() {
		final UserStory story = this.getUserStory();
		return HoursDecorator.getPercentCompletedScore(
				story.getEstimatedHours(), story.getCachedActualHours(),
				story.getTaskBasedRemainingHours(), story.isCompleted());
	}

	public double getRemainingHours() {
		final UserStory story = this.getUserStory();
		return HoursDecorator.getRemainingHoursScore(
				story.getCachedActualHours(),
				story.getTaskBasedRemainingHours(), story.isCompleted());
	}

	public String getTrackerName() {
		return PersonIdDecorator.getPersonName(this.getUserStory()
				.getTrackerId());
	}

	public int getTaskCount() {
		return this.getUserStory().getTasks().size();
	}

	public int getOriginalEstimateAndActualPercentDifference() {
		return HoursDecorator.getPercentDifference(this.getUserStory()
				.getTaskBasedEstimatedOriginalHours(), this.getUserStory()
				.getCachedActualHours());
	}

	public String getOriginalEstimateAndActualPercentDifferenceFormatted() {
		if (this.getUserStory().getCachedActualHours() == 0.0d
				|| this.getUserStory().getTaskBasedEstimatedOriginalHours() == 0.0d) {
			return "&nbsp;";
		} else {
			return HoursDecorator.formatPercentDifference(this.getUserStory()
					.getTaskBasedEstimatedOriginalHours(), this.getUserStory()
					.getCachedActualHours());
		}
	}

	public double getOriginalEstimateToCurrentEstimateScore() {
		final double originalHours = this.getUserStory()
				.getTaskBasedEstimatedOriginalHours();
		final double finalHours = this.getUserStory().getEstimatedHours();
		return this.getDifference(originalHours, finalHours);
	}

	private double getDifference(final double originalHours,
			final double finalHours) {
		final double diff = Math.abs(originalHours - finalHours);
		if (this.getUserStory().isCompleted()) {
			return diff;
		} else {
			return -diff;
		}
	}

	public double getActualToOriginalEstimateScore() {
		final double originalHours = this.getUserStory()
				.getTaskBasedEstimatedOriginalHours();
		final double finalHours = this.getUserStory().getCachedActualHours();
		return this.getDifference(originalHours, finalHours);
	}

	public double getActualToEstimateScore() {
		final double originalHours = this.getUserStory().getEstimatedHours();
		final double finalHours = this.getUserStory().getCachedActualHours();
		return this.getDifference(originalHours, finalHours);
	}

	private UserStory getUserStory() {
		return (UserStory) this.getCurrentRowObject();
	}

}