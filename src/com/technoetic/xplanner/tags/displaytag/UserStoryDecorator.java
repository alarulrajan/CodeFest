/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Jun 29, 2004
 * Time: 12:32:50 AM
 */
package com.technoetic.xplanner.tags.displaytag;

import net.sf.xplanner.domain.UserStory;

import org.displaytag.decorator.TableDecorator;

/**
 * The Class UserStoryDecorator.
 */
public class UserStoryDecorator extends TableDecorator {
    
    /**
     * Gets the percent completed.
     *
     * @return the percent completed
     */
    public double getPercentCompleted() {
        final UserStory story = this.getUserStory();
        return HoursDecorator.getPercentCompletedScore(
                story.getEstimatedHours(), story.getCachedActualHours(),
                story.getTaskBasedRemainingHours(), story.isCompleted());
    }

    /**
     * Gets the remaining hours.
     *
     * @return the remaining hours
     */
    public double getRemainingHours() {
        final UserStory story = this.getUserStory();
        return HoursDecorator.getRemainingHoursScore(
                story.getCachedActualHours(),
                story.getTaskBasedRemainingHours(), story.isCompleted());
    }

    /**
     * Gets the tracker name.
     *
     * @return the tracker name
     */
    public String getTrackerName() {
        return PersonIdDecorator.getPersonName(this.getUserStory()
                .getTrackerId());
    }

    /**
     * Gets the task count.
     *
     * @return the task count
     */
    public int getTaskCount() {
        return this.getUserStory().getTasks().size();
    }

    /**
     * Gets the original estimate and actual percent difference.
     *
     * @return the original estimate and actual percent difference
     */
    public int getOriginalEstimateAndActualPercentDifference() {
        return HoursDecorator.getPercentDifference(this.getUserStory()
                .getTaskBasedEstimatedOriginalHours(), this.getUserStory()
                .getCachedActualHours());
    }

    /**
     * Gets the original estimate and actual percent difference formatted.
     *
     * @return the original estimate and actual percent difference formatted
     */
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

    /**
     * Gets the original estimate to current estimate score.
     *
     * @return the original estimate to current estimate score
     */
    public double getOriginalEstimateToCurrentEstimateScore() {
        final double originalHours = this.getUserStory()
                .getTaskBasedEstimatedOriginalHours();
        final double finalHours = this.getUserStory().getEstimatedHours();
        return this.getDifference(originalHours, finalHours);
    }

    /**
     * Gets the difference.
     *
     * @param originalHours
     *            the original hours
     * @param finalHours
     *            the final hours
     * @return the difference
     */
    private double getDifference(final double originalHours,
            final double finalHours) {
        final double diff = Math.abs(originalHours - finalHours);
        if (this.getUserStory().isCompleted()) {
            return diff;
        } else {
            return -diff;
        }
    }

    /**
     * Gets the actual to original estimate score.
     *
     * @return the actual to original estimate score
     */
    public double getActualToOriginalEstimateScore() {
        final double originalHours = this.getUserStory()
                .getTaskBasedEstimatedOriginalHours();
        final double finalHours = this.getUserStory().getCachedActualHours();
        return this.getDifference(originalHours, finalHours);
    }

    /**
     * Gets the actual to estimate score.
     *
     * @return the actual to estimate score
     */
    public double getActualToEstimateScore() {
        final double originalHours = this.getUserStory().getEstimatedHours();
        final double finalHours = this.getUserStory().getCachedActualHours();
        return this.getDifference(originalHours, finalHours);
    }

    /**
     * Gets the user story.
     *
     * @return the user story
     */
    private UserStory getUserStory() {
        return (UserStory) this.getCurrentRowObject();
    }

}