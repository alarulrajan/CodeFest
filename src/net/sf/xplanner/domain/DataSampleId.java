package net.sf.xplanner.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DataSampleId implements Serializable {
	private static final long serialVersionUID = -5204295414444697497L;
	private Date sampleTime;
	private int referenceId;
	private String aspect;

	@Column(name = "sampleTime", nullable = false, length = 19)
	public Date getSampleTime() {
		return this.sampleTime;
	}

	public void setSampleTime(final Date sampleTime) {
		this.sampleTime = sampleTime;
	}

	@Column(name = "referenceId", nullable = false)
	public int getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(final int referenceId) {
		this.referenceId = referenceId;
	}

	@Column(name = "aspect", nullable = false)
	public String getAspect() {
		return this.aspect;
	}

	public void setAspect(final String aspect) {
		this.aspect = aspect;
	}

}
