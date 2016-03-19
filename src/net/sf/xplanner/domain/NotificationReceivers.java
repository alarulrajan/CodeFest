package net.sf.xplanner.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

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
@Table(name = "notification_receivers")
public class NotificationReceivers implements java.io.Serializable {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -4537343448751435206L;
    
    /** The id. */
    private NotificationReceiversId id;

    /**
     * Instantiates a new notification receivers.
     */
    public NotificationReceivers() {
    }

    /**
     * Instantiates a new notification receivers.
     *
     * @param id
     *            the id
     */
    public NotificationReceivers(final NotificationReceiversId id) {
        this.id = id;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    @EmbeddedId
    @AttributeOverrides({
            @AttributeOverride(name = "projectId", column = @Column(name = "project_id", nullable = false)),
            @AttributeOverride(name = "personId", column = @Column(name = "person_id", nullable = false)) })
    public NotificationReceiversId getId() {
        return this.id;
    }

    /**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
    public void setId(final NotificationReceiversId id) {
        this.id = id;
    }

}
