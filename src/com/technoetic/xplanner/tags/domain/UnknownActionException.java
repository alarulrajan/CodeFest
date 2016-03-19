/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.tags.domain;

import net.sf.xplanner.domain.DomainObject;

/**
 * The Class UnknownActionException.
 */
public class UnknownActionException extends RuntimeException {
    
    /**
     * Instantiates a new unknown action exception.
     *
     * @param action
     *            the action
     * @param targetBean
     *            the target bean
     */
    public UnknownActionException(final String action,
            final DomainObject targetBean) {
        super("Unknown action '" + action + "' on type "
                + targetBean.getClass().getName());
    }
}
