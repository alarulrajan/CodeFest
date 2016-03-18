package net.sf.xplanner.domain;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.technoetic.xplanner.domain.Describable;
import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.domain.TaskStatus;

/**
 * XplannerPlus, agile planning software
 * 
 * @author Maksym_Chyrkov. Copyright (C) 2009 Maksym Chyrkov This program is
 *         free software: you can redistribute it and/or modify it under the
 *         terms of the GNU General Public License as published by the Free
 *         Software Foundation, either version 3 of the License, or (at your
 *         option) any later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 * 
 */

@Entity
@Table(name = "task")
public class Task extends NamedObject implements java.io.Serializable,
		Describable, NoteAttachable {
	private static final long serialVersionUID = 6196936706046250433L;
	private String type;
	private int acceptorId;
	private Date createdDate;
	private double estimatedHours;
	private double originalEstimate;
	private boolean completed;
	private char disposition = TaskDisposition.PLANNED.getCode();
	private List<TimeEntry> timeEntries = new ArrayList<TimeEntry>();
	private UserStory userStory;
	private double postponedHours;
	public static final String ADDED_ORIGINAL_HOURS = Task
			.getValidProperty("addedOriginalHours");
	public static final String ESTIMATED_ORIGINAL_HOURS = Task
			.getValidProperty("estimatedOriginalHours");
	public static final String ITERATION_START_ESTIMATED_HOURS = Task
			.getValidProperty("iterationStartEstimatedHours");

	private static String getValidProperty(final String property) {
		return Task.getValidProperty(Task.class, property);
	}

	public Task() {
	}

	@Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	@Column(name = "acceptor_id")
	public int getAcceptorId() {
		return this.acceptorId;
	}

	public void setAcceptorId(final int acceptorId) {
		this.acceptorId = acceptorId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "created_date", length = 10)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(final Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "estimated_hours", precision = 22, scale = 0)
	public double getEstimatedHours() {
		return this.estimatedHours;
	}

	public void setEstimatedHours(final double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	@Column(name = "original_estimate", precision = 22, scale = 0)
	public double getOriginalEstimate() {
		return this.originalEstimate;
	}

	public void setOriginalEstimate(final double originalEstimate) {
		this.originalEstimate = originalEstimate;
	}

	@Column(name = "is_complete")
	public boolean getCompleted() {
		return this.completed;
	}

	public void setCompleted(final boolean isComplete) {
		this.completed = isComplete;
	}

	@Column(name = "disposition", nullable = false, length = 1)
	public char getDisposition() {
		return this.disposition;
	}

	public void setDisposition(final char disposition) {
		this.disposition = disposition;
	}

	@OrderBy("reportDate")
	@OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE)
	public List<TimeEntry> getTimeEntries() {
		return this.timeEntries;
	}

	public void setTimeEntries(final List<TimeEntry> timeEntries) {
		this.timeEntries = timeEntries;
	}

	@ManyToOne
	@JoinColumn(name = "story_id")
	public UserStory getUserStory() {
		return this.userStory;
	}

	public void setUserStory(final UserStory userStory) {
		this.userStory = userStory;
	}

	@Deprecated
	@Transient
	public double getActualHours() {
		double actualHours = 0.0;
		if (this.timeEntries != null && this.timeEntries.size() > 0) {
			for (final TimeEntry entry : this.timeEntries) {
				actualHours += entry.getEffort();
			}
		}
		return actualHours;
	}

	@Deprecated
	@Transient
	public double getEstimatedOriginalHours() {
		if (this.isStarted()) {
			return this.getOriginalEstimate();
		}
		return this.getEstimatedHours();
	}

	@Transient
	public boolean isStarted() {
		return this.originalEstimate > 0;
	}

	@Transient
	@Deprecated
	public double getIterationStartEstimatedHours() {
		final TaskDisposition taskDisposition = TaskDisposition
				.fromCode(this.disposition);
		if (!taskDisposition.isOriginal()) {
			return 0;
		}
		return this.getEstimatedOriginalHours();
	}

	@Transient
	@Deprecated
	public double getAdjustedEstimatedHours() {
		if (this.isCompleted()) {
			return this.getActualHours();
		}
		return Math.max(this.getEstimatedHours(), this.getActualHours());
	}

	@Transient
	public boolean isCompleted() {
		return this.completed;
	}

	@Transient
	@Deprecated
	public double getCompletedOriginalHours() {
		return this.isCompleted() ? this.getEstimatedOriginalHours() : 0;
	}

	@Transient
	@Deprecated
	public double getCompletedHours() {
		return this.isCompleted() ? this.getActualHours() : 0;
	}

	@Deprecated
	protected static String getValidProperty(final Class beanClass,
			final String property) {
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(beanClass);
		} catch (final IntrospectionException e) {
			throw new RuntimeException("could not introspect " + beanClass, e);
		}
		final PropertyDescriptor[] properties = beanInfo
				.getPropertyDescriptors();
		boolean found = false;
		for (int i = 0; i < properties.length; i++) {
			if (properties[i].getName().equals(property)) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new RuntimeException("Could not find property " + property
					+ " in " + beanClass);
		}

		return property;

	}

	@Transient
	@Deprecated
	public double getRemainingHours() {
		return this.isCompleted() ? 0.0 : Math.max(this.getEstimatedHours()
				- this.getActualHours() - this.getPostponedHours(), 0.0);
	}

	@Transient
	@Deprecated
	public double getPostponedHours() {
		return this.postponedHours;
	}

	public void setPostponedHours(final double postponedHours) {
		this.postponedHours = postponedHours;
	}

	@Transient
	@Deprecated
	public void postpone() {
		this.postponeRemainingHours();
		this.setCompleted(false);
	}

	@Transient
	public void postponeRemainingHours() {
		this.setPostponedHours(this.getRemainingHours());
	}

	public void setDisposition(final TaskDisposition taskDisposition) {
		this.setDisposition(taskDisposition.getCode());
	}

	@Transient
	public String getDispositionName() {
		return TaskDisposition.fromCode(this.disposition).getName();
	}

	@Deprecated
	public void setEstimatedOriginalHours(final double d) {
		this.setOriginalEstimate(d);
	}

	public void setEstimatedOriginalHoursField(final double d) {
		this.setOriginalEstimate(d);
	}

	public void start() {
		if (!this.isStarted()) {
			this.setEstimatedOriginalHours(this.getEstimatedHours());
		}
	}

	public boolean isCurrentlyActive(final int personId) {
		if (this.timeEntries != null && this.timeEntries.size() > 0) {
			final Iterator itr = this.timeEntries.iterator();
			while (itr.hasNext()) {
				final TimeEntry entry = (TimeEntry) itr.next();
				if (entry.isCurrentlyActive(personId)) {
					return true;
				}
			}
		}
		return false;
	}

	@Transient
	public double getEstimatedOriginalHoursField() {
		return this.getOriginalEstimate();
	}

	@Transient
	public TaskStatus getStatus() {
		TaskStatus status;
		if (this.isCompleted()) {
			status = TaskStatus.COMPLETED;
		} else if (this.getActualHours() > 0.0) {
			status = TaskStatus.STARTED;
		} else {
			status = TaskStatus.NON_STARTED;
		}
		return status;
	}

	@Transient
	public net.sf.xplanner.domain.enums.TaskStatus getNewStatus() {
		if (this.isCompleted()) {
			return net.sf.xplanner.domain.enums.TaskStatus.COMPLETED;
		} else if (this.isStarted()) {
			return net.sf.xplanner.domain.enums.TaskStatus.STARTED;
		}
		return net.sf.xplanner.domain.enums.TaskStatus.NON_STARTED;
	}

	@Transient
	public double getEstimatedHoursBasedOnActuals() {
		return this.getActualHours() + this.getRemainingHours();
	}

	@Transient
	public double getAddedHours() {
		return this.isAdded() ? this.getEstimatedHours() : 0;
	}

	@Transient
	private boolean isAdded() {
		return this.getDisposition() == TaskDisposition.ADDED.getCode();
	}

	@Transient
	public String getDispositionNameKey() {
		return TaskDisposition.fromCode(this.disposition).getNameKey();
	}

	@Transient
	public double getAddedOriginalHours() {
		return TaskDisposition.fromCode(this.getDisposition()).isOriginal() ? 0
				: this.getEstimatedOriginalHours();
	}

	@Transient
	@Deprecated
	public double getOverestimatedOriginalHours() {
		if (this.isOverestimated(this.getEstimatedOriginalHours())) {
			return this.getEstimatedOriginalHours() - this.getActualHours();
		}
		return 0.0;
	}

	private boolean isOverestimated(final double estimatedHours) {
		return this.isCompleted() && this.getActualHours() < estimatedHours;
	}

	@Transient
	public double getUnderestimatedOriginalHours() {
		return this.isUnderestimated(this.getEstimatedOriginalHours()) ? this
				.getActualHours() - this.getEstimatedOriginalHours() : 0.0;
	}

	private boolean isUnderestimated(final double estimatedHours) {
		return this.getActualHours() > estimatedHours;
	}

	@Transient
	@Deprecated
	public double getCompletedRemainingHours() {
		return this.isCompleted() ? 0.0 : this.getEstimatedOriginalHours();
	}

	@Transient
	@Deprecated
	public double getOverestimatedHours() {
		return this.isOverestimated(this.getEstimatedHours()) ? this
				.getEstimatedHours() - this.getActualHours() : 0.0;
	}

	@Transient
	public double getUnderestimatedHours() {
		if (this.isDiscovered()) {
			final double result = this.isCompleted() ? 0.0 : Math.max(
					this.getEstimatedHours() - this.getActualHours(), 0.0);
			return this.getActualHours() + result;
		}
		return this.isUnderestimated(this.getEstimatedHours()) ? this
				.getActualHours() - this.getEstimatedHours() : 0.0;
	}

	@Transient
	private boolean isDiscovered() {
		return TaskDisposition.DISCOVERED.getCode() == this.getDisposition();
	}

}
