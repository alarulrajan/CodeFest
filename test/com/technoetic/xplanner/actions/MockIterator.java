package com.technoetic.xplanner.actions;

import java.util.Iterator;

/**
 * The Class MockIterator.
 */
public class MockIterator implements Iterator {
    
    /** The has next returns. */
    private boolean[] hasNextReturns;
    
    /** The has next call cnt. */
    private int hasNextCallCnt;
    
    /** The default val. */
    private boolean defaultVal;

    /** Instantiates a new mock iterator.
     *
     * @param hasNextReturns
     *            the has next returns
     */
    public MockIterator(boolean[] hasNextReturns) {
        this.hasNextReturns = hasNextReturns;
        this.hasNextCallCnt = 0;
        this.defaultVal = false;
    }

    /** Instantiates a new mock iterator.
     *
     * @param defaultValue
     *            the default value
     */
    public MockIterator(boolean defaultValue) {
        this.hasNextReturns = null;
        this.hasNextCallCnt = 0;
        this.defaultVal = defaultValue;
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#remove()
     */
    public void remove() {
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        if (hasNextReturns != null && hasNextCallCnt < hasNextReturns.length) {
            return hasNextReturns[hasNextCallCnt++];
        } else {
            return defaultVal;
        }
    }

    /* (non-Javadoc)
     * @see java.util.Iterator#next()
     */
    public Object next() {
        return null;
    }
}