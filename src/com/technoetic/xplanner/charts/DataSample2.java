package com.technoetic.xplanner.charts;

import java.io.Serializable;
import java.util.Date;

import net.sf.xplanner.domain.DomainObject;

/**
 * The Class DataSample2.
 */
public class DataSample2 extends DomainObject implements Serializable {
    
    /** The sample time. */
    private Date sampleTime;
    
    /** The reference id. */
    private int referenceId;
    
    /** The aspect. */
    private String aspect;
    
    /** The value. */
    private double value;

    /**
     * Instantiates a new data sample2.
     */
    // For Hibernate
    DataSample2() {
    }

    /**
     * Instantiates a new data sample2.
     *
     * @param sampleTime
     *            the sample time
     * @param referenceId
     *            the reference id
     * @param aspect
     *            the aspect
     * @param value
     *            the value
     */
    public DataSample2(final Date sampleTime, final int referenceId,
            final String aspect, final double value) {
        this.sampleTime = sampleTime;
        this.referenceId = referenceId;
        this.aspect = aspect;
        this.value = value;
    }

    /**
     * Gets the sample time.
     *
     * @return the sample time
     */
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

    /**
     * Gets the value.
     *
     * @return the value
     */
    public double getValue() {
        return this.value;
    }

    /**
     * Sets the value.
     *
     * @param value
     *            the new value
     */
    public void setValue(final double value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.DomainObject#toString()
     */
    @Override
    public String toString() {
        return this.aspect + " of oid " + this.referenceId + " on "
                + this.sampleTime + " was " + this.value;
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.DomainObject#equals(java.lang.Object)
     */
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

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.DomainObject#hashCode()
     */
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
