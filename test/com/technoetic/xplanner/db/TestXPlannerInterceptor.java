package com.technoetic.xplanner.db;

import java.io.Serializable;
import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import com.technoetic.xplanner.db.hibernate.XPlannerInterceptor;

/**
 * The Class TestXPlannerInterceptor.
 */
public class TestXPlannerInterceptor extends TestCase {
    
    /** The interceptor. */
    private XPlannerInterceptor interceptor;
    
    /** The mock entity. */
    private Object mockEntity;
    
    /** The mock id. */
    private Serializable mockId;
    
    /** The mock current state. */
    private Object[] mockCurrentState;
    
    /** The mock previous state. */
    private Object[] mockPreviousState;
    
    /** The mock property names. */
    private String[] mockPropertyNames;
    
    /** The mock types. */
    private Type[] mockTypes;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        mockEntity = new Object();
        mockId = new Serializable() {
        };
        mockCurrentState = new Object[2];
        mockPreviousState = new Object[2];
        mockTypes = new Type[]{Hibernate.STRING, Hibernate.STRING};
        interceptor = new XPlannerInterceptor();
    }

    /** Test on flush dirty update last update time.
     */
    public void testOnFlushDirtyUpdateLastUpdateTime() {
        mockPropertyNames = new String[]{"foo", "lastUpdateTime"};

        boolean result = interceptor.onFlushDirty(mockEntity, mockId, mockCurrentState,
                mockPreviousState, mockPropertyNames, mockTypes);

        assertTrue("wrong result", result);
        assertTrue("wrong value in state", mockCurrentState[1] instanceof Date);
    }

    /** Test on save update last update time.
     */
    public void testOnSaveUpdateLastUpdateTime() {
        mockPropertyNames = new String[]{"foo", "lastUpdateTime"};

        boolean result = interceptor.onSave(mockEntity, mockId, mockCurrentState, mockPropertyNames, mockTypes);

        assertTrue("wrong result", result);
        assertTrue("wrong value in state", mockCurrentState[1] instanceof Date);
    }

    /** Test on flush dirty update no last update time.
     */
    public void testOnFlushDirtyUpdateNoLastUpdateTime() {
        mockPropertyNames = new String[]{"foo", "bar"};

        boolean result = interceptor.onFlushDirty(mockEntity, mockId, mockCurrentState,
                mockPreviousState, mockPropertyNames, mockTypes);

        assertFalse("wrong result", result);
        assertFalse("wrong value in state", mockCurrentState[1] instanceof Date);
    }

    /** Test on save update no last update time.
     */
    public void testOnSaveUpdateNoLastUpdateTime() {
        mockPropertyNames = new String[]{"foo", "bar"};

        boolean result = interceptor.onSave(mockEntity, mockId, mockCurrentState, mockPropertyNames, mockTypes);

        assertFalse("wrong result", result);
        assertFalse("wrong value in state", mockCurrentState[1] instanceof Date);
    }
}
