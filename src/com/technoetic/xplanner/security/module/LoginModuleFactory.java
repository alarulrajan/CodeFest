/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.technoetic.xplanner.security.module;

import java.util.Map;

import com.technoetic.xplanner.security.LoginModule;

/**
 * A factory for creating LoginModule objects.
 */
public interface LoginModuleFactory {
	
	/**
     * New instance.
     *
     * @param options
     *            the options
     * @return the login module
     * @throws ConfigurationException
     *             the configuration exception
     */
	LoginModule newInstance(Map options) throws ConfigurationException;
}