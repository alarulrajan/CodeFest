/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: mprokopowicz Date: Apr 1, 2006 Time: 9:16:06 PM
 */
public class MethodCacheInvalidateForSelectedArgumentsInterceptor extends
		MethodCacheInvalidateInterceptor {

	int[] argumentIndexes;

	public void setArgumentIndexes(final int[] argumentIndexes) {
		this.argumentIndexes = argumentIndexes;
	}

	public MethodCacheInvalidateForSelectedArgumentsInterceptor(
			final Map cacheMap) {
		super(cacheMap);
	}

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
