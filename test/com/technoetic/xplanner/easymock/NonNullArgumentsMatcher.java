package com.technoetic.xplanner.easymock;

import org.apache.commons.lang.StringUtils;
import org.easymock.ArgumentsMatcher;

/**
 * The Class NonNullArgumentsMatcher.
 */
class NonNullArgumentsMatcher implements ArgumentsMatcher {
    
    /* (non-Javadoc)
     * @see org.easymock.ArgumentsMatcher#matches(java.lang.Object[], java.lang.Object[])
     */
    public boolean matches(Object[] expected, Object[] actual) {
        for (int i = 0; i < actual.length; i++) {
            if (actual[i] == null) {
                return false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.easymock.ArgumentsMatcher#toString(java.lang.Object[])
     */
    public String toString(Object[] parameters) {
        return StringUtils.join(parameters, ",");
    }
}
