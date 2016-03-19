/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: mprokopowicz Date: Apr 1, 2006 Time: 9:16:06 PM.
 */
public class MethodCacheInvalidateForSelectedArgumentsInterceptor extends
		MethodCacheInvalidateInterceptor {

	/** The argument indexes. */
	int[] argumentIndexes;

	/**
     * Sets the argument indexes.
     *
     * @param argumentIndexes
     *            the new argument indexes
     */
	public void setArgumentIndexes(final int[] argumentIndexes) {
		this.argumentIndexes = argumentIndexes;
	}

	/**
     * Instantiates a new method cache invalidate for selected arguments
     * interceptor.
     *
     * @param cacheMap
     *            the cache map
     */
	public MethodCacheInvalidateForSelectedArgumentsInterceptor(
			final Map cacheMap) {
		super(cacheMap);
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.db.MethodCacheInvalidateInterceptor#getMethodCacheKey(java.lang.Object[])
	 */
	@Override
	public List getMethodCacheKey(final Object args[]) {
		final List cacheKey = new ArrayList();
		for (int i = 0; i < this.argumentIndexes.length; i++) {
			final int argumentIndex = this.argumentIndexes[i];
			cacheKey.add(args[argumentIndex]);
		}
		return cacheKey;
	}
}
