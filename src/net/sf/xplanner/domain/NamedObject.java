package net.sf.xplanner.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.technoetic.xplanner.domain.Nameable;

/**
 * The Class NamedObject.
 */
@MappedSuperclass
public class NamedObject extends DomainObject implements Nameable {
    
    /** The name. */
    private String name;
    
    /** The description. */
    private String description;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Nameable#getName()
     */
    @Override
    @Column(name = "name", length = 255)
    public String getName() {
        return this.name;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Nameable#getDescription()
     */
    @Override
    @Column(name = "description", length = 65535)
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.DomainObject#toString()
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.DomainObject#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + (this.description == null ? 0 : this.description.hashCode());
        result = prime * result
                + (this.name == null ? 0 : this.name.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.DomainObject#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final NamedObject other = (NamedObject) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

}
