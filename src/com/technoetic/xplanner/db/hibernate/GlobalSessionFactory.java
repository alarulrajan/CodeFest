package com.technoetic.xplanner.db.hibernate;

import org.hibernate.SessionFactory;

/**
 * A factory for creating GlobalSession objects.
 */
public class GlobalSessionFactory {
    
    /** The factory. */
    private static SessionFactory factory;

    /**
     * Gets the.
     *
     * @return the session factory
     */
    public static SessionFactory get() {
        return GlobalSessionFactory.factory;
    }

    /**
     * Sets the.
     *
     * @param factory
     *            the factory
     */
    public static void set(final SessionFactory factory) {
        GlobalSessionFactory.factory = factory;
    }
}