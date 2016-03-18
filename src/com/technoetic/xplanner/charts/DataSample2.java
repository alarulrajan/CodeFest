package com.technoetic.xplanner.charts;

import java.io.Serializable;
import java.util.Date;

import net.sf.xplanner.domain.DomainObject;

public class DataSample2 extends DomainObject implements Serializable {
	private Date sampleTime;
	private int referenceId;
	private String aspect;
	private double value;

	// For Hibernate
	DataSample2() {
	}

	public DataSample2(final Date sampleTime, final int referenceId,
			final String aspect, final double value) {
		this.sampleTime = sampleTime;
		this.referenceId = referenceId;
		this.aspect = aspect;
		this.value = value;
	}

	public Date getSampleTime() {
		return this.sampleTime;
	}

	public void setSampleTime(final Date sampleTime) {
		this.sampleTime = sampleTime;
	}

	public int getReferenceId() {
		return this.referenceId;
	}

	public void setReferenceId(final int referenceId) {
		this.referenceId = referenceId;
	}

	public String getAspect() {
		return this.aspect;
	}

	public void setAspect(final String aspect) {
		this.aspect = aspect;
	}

	public double getValue() {
		return this.value;
	}

	public void setValue(final double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.aspect + " of oid " + this.referenceId + " on "
				+ this.sampleTime + " was " + this.value;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof DataSample2)) {
			return false;
		}

		final DataSample2 dataSample = (DataSample2) o;

		if (this.referenceId != dataSample.referenceId) {
			return false;
		}
		if (this.value != dataSample.value) {
			return false;
		}
		if (this.aspect != null ? !this.aspect.equals(dataSample.aspect)
				: dataSample.aspect != null) {
			return false;
		}
		if (this.sampleTime != null ? !this.sampleTime
				.equals(dataSample.sampleTime) : dataSample.sampleTime != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = this.sampleTime != null ? this.sampleTime.hashCode() : 0;
		result = 29 * result + this.referenceId;
		result = 29 * result
				+ (this.aspect != null ? this.aspect.hashCode() : 0);
		temp = Double.doubleToLongBits(this.value);
		result = 29 * result + (int) (temp ^ temp >>> 32);
		return result;
	}
}
