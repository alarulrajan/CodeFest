/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;

import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class ExceptionHandler.
 */
public class ExceptionHandler extends org.apache.struts.action.ExceptionHandler {
    
    /** The Constant LOG. */
    protected static final Logger LOG = LogUtil.getLogger();

    /* (non-Javadoc)
     * @see org.apache.struts.action.ExceptionHandler#storeException(javax.servlet.http.HttpServletRequest, java.lang.String, org.apache.struts.action.ActionError, org.apache.struts.action.ActionForward, java.lang.String)
     */
    @Override
    protected void storeException(final HttpServletRequest request,
            final String property, final ActionError error,
            final ActionForward forward, final String scope) {
        final Throwable exception = (Throwable) request
                .getAttribute(Globals.EXCEPTION_KEY);
        ExceptionHandler.LOG.warn("Uncaught exception", exception);
        super.storeException(request, property, error, forward, scope);
    }
}