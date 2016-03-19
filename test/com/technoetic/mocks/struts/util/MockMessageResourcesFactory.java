package com.technoetic.mocks.struts.util;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

/**
 * A factory for creating MockMessageResources objects.
 */
public class MockMessageResourcesFactory extends MessageResourcesFactory {
    
    /** The get return null called. */
    public boolean getReturnNullCalled;
    
    /** The get return null return. */
    public Boolean getReturnNullReturn;

    /* (non-Javadoc)
     * @see org.apache.struts.util.MessageResourcesFactory#getReturnNull()
     */
    public boolean getReturnNull() {
        getReturnNullCalled = true;
        return getReturnNullReturn.booleanValue();
    }

    /** The set return null called. */
    public boolean setReturnNullCalled;
    
    /** The set return null return null. */
    public boolean setReturnNullReturnNull;

    /* (non-Javadoc)
     * @see org.apache.struts.util.MessageResourcesFactory#setReturnNull(boolean)
     */
    public void setReturnNull(boolean returnNull) {
        setReturnNullCalled = true;
        setReturnNullReturnNull = returnNull;
    }

    /** The create resources called. */
    public boolean createResourcesCalled;
    
    /** The create resources return. */
    public MessageResources createResourcesReturn;
    
    /** The create resources config. */
    public String createResourcesConfig;

    /* (non-Javadoc)
     * @see org.apache.struts.util.MessageResourcesFactory#createResources(java.lang.String)
     */
    public MessageResources createResources(String config) {
        createResourcesCalled = true;
        createResourcesConfig = config;
        return createResourcesReturn;
    }
}