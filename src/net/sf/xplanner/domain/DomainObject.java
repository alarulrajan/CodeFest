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

@MappedSuperclass
public class DomainObject implements Identifiable {
	private int id;
	private Date lastUpdateTime;
	private Map<String, String> attributes = new HashMap<String, String>();

	@Override
	@Id
	@GeneratedValue(generator = "commonId")
	@GenericGenerator(name = "commonId", strategy = "com.technoetic.xplanner.db.hibernate.HibernateIdentityGenerator")
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_update", length = 19)
	public Date getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	@PreUpdate
	public void onUpdate() {
		this.lastUpdateTime = new Date();
	}

	public void setId(final int id) {
		this.id = id;
	}

	public void setLastUpdateTime(final Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

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

	public String getAttribute(final String attributeName) {
		return this.getAttributes().get(attributeName);
	}

	@Transient
	public Map<String, String> getAttributes() {
		return this.attributes;
	}

	protected void setAttributes(final Map<String, String> attributes) {
		this.attributes = attributes;
	}
}
