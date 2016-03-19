package com.technoetic.mocks.struts.util;

import java.util.HashMap;
import java.util.Locale;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

/**
 * The Class MockMessageResources.
 */
public class MockMessageResources extends MessageResources {
    
    /** Instantiates a new mock message resources.
     *
     * @param factory
     *            the factory
     * @param config
     *            the config
     */
    public MockMessageResources(MessageResourcesFactory factory, String config) {
        super(factory, config);
    }

    /** The resources. */
    public HashMap resources = new HashMap();

    /** The get message called. */
    public boolean getMessageCalled;
    
    /** The get message locale. */
    public Locale getMessageLocale;
    
    /** The get message key. */
    public String getMessageKey;
    
    /** The get message return. */
    public String getMessageReturn;

    /* (non-Javadoc)
     * @see org.apache.struts.util.MessageResources#getMessage(java.util.Locale, java.lang.String)
     */
    public String getMessage(Locale locale, String key) {
        getMessageCalled = true;
        getMessageLocale = locale;
        getMessageKey = key;
        if (getMessageReturn == null) {
            return (String)resources.get(key);
        } else {
            return getMessageReturn;
        }
    }

    /** Sets the message.
     *
     * @param key
     *            the key
     * @param msg
     *            the msg
     */
    public void setMessage(String key, String msg) {
        resources.put(key, msg);
    }
}
