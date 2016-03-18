package net.sf.xplanner.domain.view;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.xplanner.domain.NamedObject;

import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.StoryStatus;

@XmlRootElement
public class UserStoryView extends NamedObject {
	private double estimatedHours;
	private int priority;
	private char status = StoryStatus.DRAFT.getCode();
	private Double originalEstimatedHours;
	private char dispositionCode = StoryDisposition.PLANNED.getCode();
	private double postponedHours;
	private int orderNo;
	private double actualHours;
	private int trackerId;
	private List<TaskView> tasks = new ArrayList<TaskView>();

	public double getEstimatedHours() {
		return this.estimatedHours;
	}

	public void setEstimatedHours(final double estimatedHours) {
		this.estimatedHours = estimatedHours;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(final int priority) {
		this.priority = priority;
	}

	public char getStatus() {
		return this.status;
	}

	public void setStatus(final char status) {
		this.status = status;
	}

	public Double getOriginalEstimatedHours() {
		return this.originalEstimatedHours;
	}

	public void setOriginalEstimatedHours(final Double originalEstimatedHours) {
		this.originalEstimatedHours = originalEstimatedHours;
	}

	public char getDispositionCode() {
		return this.dispositionCode;
	}

	public void setDispositionCode(final char dispositionCode) {
		this.dispositionCode = dispositionCode;
	}

	public double getPostponedHours() {
		return this.postponedHours;
	}

	public void setPostponedHours(final double postponedHours) {
		this.postponedHours = postponedHours;
	}

	public int getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(final int orderNo) {
		this.orderNo = orderNo;
	}

	public double getActualHours() {
		return this.actualHours;
	}

	public void setActualHours(final double actualHours) {
		this.actualHours = actualHours;
	}

	@XmlElement(name = "task")
	public List<TaskView> getTasks() {
		return this.tasks;
	}

	public void setTasks(final List<TaskView> tasks) {
		this.tasks = tasks;
	}

	public int getTrackerId() {
		return this.trackerId;
	}

	public void setTrackerId(final int trackerId) {
		this.trackerId = trackerId;
	}
}
