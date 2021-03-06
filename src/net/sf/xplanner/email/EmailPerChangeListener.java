package net.sf.xplanner.email;

import net.sf.xplanner.events.ObjectCreated;
import net.sf.xplanner.events.ObjectDeleted;
import net.sf.xplanner.events.ObjectUpdated;
import net.sf.xplanner.events.XplannerEvent;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * XplannerPlus, agile planning software.
 *
 * @author Maksym. Copyright (C) 2009 Maksym Chyrkov This program is free
 *         software: you can redistribute it and/or modify it under the terms of
 *         the GNU General Public License as published by the Free Software
 *         Foundation, either version 3 of the License, or (at your option) any
 *         later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 */

public class EmailPerChangeListener implements ApplicationListener {
    
    /** The email helper. */
    private EmailHelper emailHelper;

    /**
     * Sets the email helper.
     *
     * @param emailHelper
     *            the new email helper
     */
    public void setEmailHelper(final EmailHelper emailHelper) {
        this.emailHelper = emailHelper;
    }

    /* (non-Javadoc)
     * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
     */
    @Override
    public void onApplicationEvent(final ApplicationEvent event) {
        if (!event.getClass().isAnnotationPresent(XplannerEvent.class)) {
            return;
        }
        if (event instanceof ObjectUpdated) {
            this.sendObjectWasUpdatedEmail((ObjectUpdated) event);
        } else if (event instanceof ObjectCreated) {
            this.sendObjectWasCreatedEmail((ObjectCreated) event);
        } else if (event instanceof ObjectDeleted) {
            this.sendObjectWasDeletedEmail((ObjectDeleted) event);
        }
    }

    /**
     * Send object was deleted email.
     *
     * @param event
     *            the event
     */
    private void sendObjectWasDeletedEmail(final ObjectDeleted event) {
        this.emailHelper.sendEmail(event);
    }

    /**
     * Send object was created email.
     *
     * @param event
     *            the event
     */
    private void sendObjectWasCreatedEmail(final ObjectCreated event) {
        this.emailHelper.sendEmail(event);
    }

    /**
     * Send object was updated email.
     *
     * @param event
     *            the event
     */
    private void sendObjectWasUpdatedEmail(final ObjectUpdated event) {
        this.emailHelper.sendEmail(event);
    }

}
