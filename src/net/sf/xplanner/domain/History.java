package net.sf.xplanner.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.technoetic.xplanner.domain.Identifiable;

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
@Table(name = "history")
public class History implements Identifiable, Serializable {
	public static final String CREATED = "created";
	public static final String UPDATED = "updated";
	public static final String DELETED = "deleted";
	public static final String REESTIMATED = "reestimated";
	public static final String ITERATION_STARTED = "started";
	public static final String ITERATION_CLOSED = "closed";
	public static final String MOVED = "moved";
	public static final String MOVED_IN = "moved in";
	public static final String MOVED_OUT = "moved out";
	public static final String CONTINUED = "continued";

	private static final long serialVersionUID = 87622707160315552L;
	private int id;
	private Date whenHappened;
	private Integer containerId;
	private Integer targetId;
	private String objectType;
	private String action;
	private String description;
	private Integer personId;
	private boolean notified;

	public History() {
	}

	public History(final int id) {
		this.id = id;
	}

	public History(final Date whenHappened, final Integer containerId,
			final Integer targetId, final String objectType,
			final String action, final String description,
			final Integer personId) {
		this.whenHappened = whenHappened;
		this.containerId = containerId;
		this.targetId = targetId;
		this.objectType = objectType;
		this.action = action;
		this.description = description;
		this.personId = personId;
	}

	@Override
	@Id
	@GeneratedValue(generator = "commonId")
	@GenericGenerator(name = "commonId", strategy = "com.technoetic.xplanner.db.hibernate.HibernateIdentityGenerator")
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "when_happened", length = 19)
	public Date getWhenHappened() {
		return this.whenHappened;
	}

	public void setWhenHappened(final Date whenHappened) {
		this.whenHappened = whenHappened;
	}

	@Column(name = "container_id")
	public Integer getContainerId() {
		return this.containerId;
	}

	public void setContainerId(final Integer containerId) {
		this.containerId = containerId;
	}

	@Column(name = "target_id")
	public Integer getTargetId() {
		return this.targetId;
	}

	public void setTargetId(final Integer targetId) {
		this.targetId = targetId;
	}

	@Column(name = "object_type")
	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(final String objectType) {
		this.objectType = objectType;
	}

	@Column(name = "action")
	public String getAction() {
		return this.action;
	}

	public void setAction(final String action) {
		this.action = action;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	@Column(name = "person_id")
	public Integer getPersonId() {
		return this.personId;
	}

	public void setPersonId(final Integer personId) {
		this.personId = personId;
	}

	@Column(name = "notified")
	public boolean getNotified() {
		return this.notified;
	}

	public void setNotified(final boolean notified) {
		this.notified = notified;
	}

	@Transient
	public boolean isNotified() {
		return this.notified;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (this.action == null ? 0 : this.action.hashCode());
		result = prime * result
				+ (this.containerId == null ? 0 : this.containerId.hashCode());
		result = prime * result
				+ (this.description == null ? 0 : this.description.hashCode());
		result = prime * result + this.id;
		result = prime * result + (this.notified ? 1231 : 1237);
		result = prime * result
				+ (this.objectType == null ? 0 : this.objectType.hashCode());
		result = prime * result
				+ (this.personId == null ? 0 : this.personId.hashCode());
		result = prime * result
				+ (this.targetId == null ? 0 : this.targetId.hashCode());
		result = prime
				* result
				+ (this.whenHappened == null ? 0 : this.whenHappened.hashCode());
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
		final History other = (History) obj;
		if (this.action == null) {
			if (other.action != null) {
				return false;
			}
		} else if (!this.action.equals(other.action)) {
			return false;
		}
		if (this.containerId == null) {
			if (other.containerId != null) {
				return false;
			}
		} else if (!this.containerId.equals(other.containerId)) {
			return false;
		}
		if (this.description == null) {
			if (other.description != null) {
				return false;
			}
		} else if (!this.description.equals(other.description)) {
			return false;
		}
		if (this.id != other.id) {
			return false;
		}
		if (this.notified != other.notified) {
			return false;
		}
		if (this.objectType == null) {
			if (other.objectType != null) {
				return false;
			}
		} else if (!this.objectType.equals(other.objectType)) {
			return false;
		}
		if (this.personId == null) {
			if (other.personId != null) {
				return false;
			}
		} else if (!this.personId.equals(other.personId)) {
			return false;
		}
		if (this.targetId == null) {
			if (other.targetId != null) {
				return false;
			}
		} else if (!this.targetId.equals(other.targetId)) {
			return false;
		}
		if (this.whenHappened == null) {
			if (other.whenHappened != null) {
				return false;
			}
		} else if (!this.whenHappened.equals(other.whenHappened)) {
			return false;
		}
		return true;
	}

}
