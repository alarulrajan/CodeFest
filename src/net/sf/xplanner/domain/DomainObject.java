package net.sf.xplanner.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * The Class DomainObject.
 */
@MappedSuperclass
public class DomainObject implements Identifiable {
    
    /** The id. */
    private int id;
    
    /** The last update time. */
    private Date lastUpdateTime;
    
    /** The attributes. */
    private Map<String, String> attributes = new HashMap<String, String>();

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Identifiable#getId()
     */
    @Override
    @Id
    @GeneratedValue(generator = "commonId")
    @GenericGenerator(name = "commonId", strategy = "com.technoetic.xplanner.db.hibernate.HibernateIdentityGenerator")
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    /**
     * Gets the last update time.
     *
     * @return the last update time
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update", length = 19)
    public Date getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    /**
     * On update.
     */
    @PreUpdate
    public void onUpdate() {
        this.lastUpdateTime = new Date();
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Sets the last update time.
     *
     * @param lastUpdateTime
     *            the new last update time
     */
    public void setLastUpdateTime(final Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.id;
        result = prime
                * result
                + (this.lastUpdateTime == null ? 0 : this.lastUpdateTime
                        .hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final DomainObject other = (DomainObject) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.lastUpdateTime == null) {
            if (other.lastUpdateTime != null) {
                return false;
            }
        } else if (!this.lastUpdateTime.equals(other.lastUpdateTime)) {
            return false;
        }
        return true;
    }

    /**
     * Gets the attribute.
     *
     * @param attributeName
     *            the attribute name
     * @return the attribute
     */
    public String getAttribute(final String attributeName) {
        return this.getAttributes().get(attributeName);
    }

    /**
     * Gets the attributes.
     *
     * @return the attributes
     */
    @Transient
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    /**
     * Sets the attributes.
     *
     * @param attributes
     *            the attributes
     */
    protected void setAttributes(final Map<String, String> attributes) {
        this.attributes = attributes;
    }
}
