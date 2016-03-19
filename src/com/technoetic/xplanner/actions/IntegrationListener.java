package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.domain.Integration;

/**
 * The listener interface for receiving integration events. The class that is
 * interested in processing a integration event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addIntegrationListener<code> method. When
 * the integration event occurs, that object's appropriate
 * method is invoked.
 *
 * @see IntegrationEvent
 */
public interface IntegrationListener {
	
	/** The Constant INTEGRATION_READY_EVENT. */
	public static final int INTEGRATION_READY_EVENT = 1;

	/**
     * On event.
     *
     * @param eventType
     *            the event type
     * @param integration
     *            the integration
     * @param request
     *            the request
     */
	public void onEvent(int eventType, Integration integration,
			HttpServletRequest request);
}