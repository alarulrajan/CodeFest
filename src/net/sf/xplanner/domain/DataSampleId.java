package net.sf.xplanner.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The Class DataSampleId.
 */
@Embeddable
public class DataSampleId implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5204295414444697497L;
	
	/** The sample time. */
	private Date sampleTime;
	
	/** The reference id. */
	private int referenceId;
	
	/** The aspect. */
	private String aspect;

	/**
     * Gets the sample time.
     *
     * @return the sample time
     */
	@Column(name = "sampleTime", nullable = false, length = 19)
	public Date getSampleTime() {
		return this.sampleTime;
	}

	/**
     * Sets the sample time.
     *
     * @param sampleTime
     *            the new sample time
     */
	public void setSampleTime(final Date sampleTime) {
		this.sampleTime = sampleTime;
	}

	/**
     * Gets the reference id.
     *
     * @return the reference id
     */
	@Column(name = "referenceId", nullable = false)
	public int getReferenceId() {
		return this.referenceId;
	}

	/**
     * Sets the reference id.
     *
     * @param referenceId
     *            the new reference id
     */
	public void setReferenceId(final int referenceId) {
		this.referenceId = referenceId;
	}

	/**
     * Gets the aspect.
     *
     * @return the aspect
     */
	@Column(name = "aspect", nullable = false)
	public String getAspect() {
		return this.aspect;
	}

	/**
     * Sets the aspect.
     *
     * @param aspect
     *            the new aspect
     */
	public void setAspect(final String aspect) {
		this.aspect = aspect;
	}

}
