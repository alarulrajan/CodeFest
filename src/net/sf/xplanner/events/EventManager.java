package net.sf.xplanner.events;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Person;

import org.apache.struts.action.ActionForm;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

import com.technoetic.xplanner.domain.Identifiable;
import com.technoetic.xplanner.domain.Nameable;

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

public class EventManager implements ApplicationContextAware {
	
	/** The application context. */
	private ApplicationContext applicationContext;

	/* (non-Javadoc)
	 * @see org.springframework.context.ApplicationContextAware#setApplicationContext(org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(
			final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
     * Publish update event.
     *
     * @param actionForm
     *            the action form
     * @param domainObject
     *            the domain object
     * @param editor
     *            the editor
     */
	public void publishUpdateEvent(final ActionForm actionForm,
			final Nameable domainObject, final Person editor) {
		final ApplicationEvent event = new ObjectUpdated(actionForm,
				new EventSource(domainObject, editor));
		this.applicationContext.publishEvent(event);
	}

	/**
     * Publish create event.
     *
     * @param domainObject
     *            the domain object
     * @param editor
     *            the editor
     */
	public void publishCreateEvent(final Identifiable domainObject,
			final Person editor) {
		final ApplicationEvent event = new ObjectCreated(new EventSource(
				domainObject, editor));
		this.applicationContext.publishEvent(event);
	}

	/**
     * Publish delete event.
     *
     * @param domainObject
     *            the domain object
     * @param editor
     *            the editor
     */
	public void publishDeleteEvent(final DomainObject domainObject,
			final Person editor) {
		final ApplicationEvent event = new ObjectDeleted(new EventSource(
				domainObject, editor));
		this.applicationContext.publishEvent(event);
	}
}
