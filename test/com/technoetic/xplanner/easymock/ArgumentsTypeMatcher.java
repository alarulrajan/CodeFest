package com.technoetic.xplanner.easymock;

import org.apache.commons.lang.StringUtils;
import org.easymock.ArgumentsMatcher;

/**
 * The Class ArgumentsTypeMatcher.
 */
public class ArgumentsTypeMatcher implements ArgumentsMatcher {
    
    /* (non-Javadoc)
     * @see org.easymock.ArgumentsMatcher#matches(java.lang.Object[], java.lang.Object[])
     */
    public boolean matches(Object[] expected, Object[] actual) {
        for (int i = 0; i < expected.length; i++) {
            if (!expected[i].getClass().isAssignableFrom(actual[i].getClass())) {
                return false;
            }
        }
        return true;
    }

    /* (non-Javadoc)
     * @see org.easymock.ArgumentsMatcher#toString(java.lang.Object[])
     */
    public String toString(Object[] arguments) {
        return StringUtils.join(arguments, ",");
    }
}
