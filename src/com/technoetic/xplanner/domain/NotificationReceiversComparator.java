package com.technoetic.xplanner.domain;

import java.util.Comparator;

import net.sf.xplanner.domain.Person;

/**
 * The Class NotificationReceiversComparator.
 */
public class NotificationReceiversComparator implements Comparator {
    
    /* (non-Javadoc)
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(final Object o1, final Object o2) {
        return ((Person) o1).getName().compareTo(((Person) o2).getName());
    }
}
