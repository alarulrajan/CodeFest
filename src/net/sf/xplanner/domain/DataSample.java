package net.sf.xplanner.domain;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * XplannerPlus, agile planning software.
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
 */

@Entity
@Table(name = "datasample")
public class DataSample implements java.io.Serializable, Identifiable {
    
    /** The Constant SAMPLE_TIME. */
    public static final String SAMPLE_TIME = "id.sampleTime";
    
    /** The Constant REFERENCE_ID. */
    public static final String REFERENCE_ID = "id.referenceId";
    
    /** The Constant ASPECT. */
    public static final String ASPECT = "id.aspect";
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 7180535099586780565L;
    
    /** The value. */
    private double value;
    
    /** The data sample id. */
    private DataSampleId dataSampleId = new DataSampleId();

    /**
     * Instantiates a new data sample.
     */
    public DataSample() {
    }

    /**
     * Instantiates a new data sample.
     *
     * @param midnight
     *            the midnight
     * @param id
     *            the id
     * @param aspect
     *            the aspect
     * @param value
     *            the value
     */
    public DataSample(final Date midnight, final int id, final String aspect,
            final double value) {
        this.setSampleTime(midnight);
        this.setReferenceId(id);
        this.setAspect(aspect);
        this.value = value;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    @Column(name = "\"value\"", precision = 22, scale = 0)
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

    /**
     * Gets the data sample id.
     *
     * @return the data sample id
     */
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "sampleTime", column = @Column(name = "sampleTime", nullable = false)),
            @AttributeOverride(name = "referenceId", column = @Column(name = "referenceId", nullable = false)),
            @AttributeOverride(name = "aspect", column = @Column(name = "aspect", nullable = false)) })
    public DataSampleId getDataSampleId() {
        return this.dataSampleId;
    }

    /**
     * Sets the data sample id.
     *
     * @param dataSampleId
     *            the new data sample id
     */
    public void setDataSampleId(final DataSampleId dataSampleId) {
        this.dataSampleId = dataSampleId;
    }

    /**
     * Gets the sample time.
     *
     * @return the sample time
     */
    @Transient
    public Date getSampleTime() {
        return this.dataSampleId.getSampleTime();
    }

    /**
     * Sets the sample time.
     *
     * @param sampleTime
     *            the new sample time
     */
    public void setSampleTime(final Date sampleTime) {
        this.dataSampleId.setSampleTime(sampleTime);
    }

    /**
     * Gets the reference id.
     *
     * @return the reference id
     */
    @Transient
    public int getReferenceId() {
        return this.dataSampleId.getReferenceId();
    }

    /**
     * Sets the reference id.
     *
     * @param referenceId
     *            the new reference id
     */
    public void setReferenceId(final int referenceId) {
        this.dataSampleId.setReferenceId(referenceId);
    }

    /**
     * Gets the aspect.
     *
     * @return the aspect
     */
    @Transient
    public String getAspect() {
        return this.dataSampleId.getAspect();
    }

    /**
     * Sets the aspect.
     *
     * @param aspect
     *            the new aspect
     */
    public void setAspect(final String aspect) {
        this.dataSampleId.setAspect(aspect);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Identifiable#getId()
     */
    @Override
    @Transient
    public int getId() {
        return 0;
    }
}
