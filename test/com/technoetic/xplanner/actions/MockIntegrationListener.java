package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.domain.Integration;

/**
 * The listener interface for receiving mockIntegration events. The class that
 * is interested in processing a mockIntegration event implements this
 * interface, and the object created with that class is registered with a
 * component using the component's
 * <code>addMockIntegrationListener<code> method. When
 * the mockIntegration event occurs, that object's appropriate
 * method is invoked.
 *
 * @see MockIntegrationEvent
 */
public class MockIntegrationListener implements IntegrationListener {
    
    /** The on event called. */
    public boolean onEventCalled;
    
    /** The on event event type. */
    public int onEventEventType;
    
    /** The on event integration. */
    public Integration onEventIntegration;
    
    /** The on event request. */
    public HttpServletRequest onEventRequest;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.IntegrationListener#onEvent(int, com.technoetic.xplanner.domain.Integration, javax.servlet.http.HttpServletRequest)
     */
    public void onEvent(int eventType, Integration integration, HttpServletRequest request) {
        onEventCalled = true;
        onEventEventType = eventType;
        onEventIntegration = integration;
        onEventRequest = request;
    }
}
