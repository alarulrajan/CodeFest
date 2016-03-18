/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.aop.AfterReturningAdvice;

/**
 * User: Mateusz Prokopowicz Date: Sep 6, 2005 Time: 6:40:13 AM
 */
public class MethodCacheInvalidateInterceptor implements AfterReturningAdvice {
	private final Map resultByArgsByMethodName;
	private List methodNamesToInvalidate;
	Logger LOG = Logger.getLogger(MethodCacheInvalidateInterceptor.class);

	public void setMethodsToInvalidate(final List methodNamesToInvalidate) {
		this.methodNamesToInvalidate = methodNamesToInvalidate;
	}

	public MethodCacheInvalidateInterceptor(final Map cacheMap) {
		this.resultByArgsByMethodName = cacheMap;
	}

	public List getMethodCacheKey(final Object args[]) {
		return Arrays.asList(args);
	}

	@Override
	public void afterReturning(final Object returnValue, final Method method,
			final Object[] args, final Object target) throws Throwable {
		final List argumentList = this.getMethodCacheKey(args);
		this.invalidate(argumentList);
	}

	void invalidate(List argumentList) {
		for (int i = 0; i < this.methodNamesToInvalidate.size(); i++) {
			final String methodName = (String) this.methodNamesToInvalidate
					.get(i);
			this.LOG.debug("Invalidate cache for method " + methodName + "("
					+ StringUtils.join(argumentList.iterator(), ", ") + ")");
			final Map methodCache = (Map) this.resultByArgsByMethodName
					.get(methodName);
			if (methodCache != null) {
				if (!methodCache.containsKey(argumentList)) {
					argumentList = Collections.EMPTY_LIST;
				}
				if (methodCache.containsKey(argumentList)) {
					methodCache.remove(argumentList);
				}
			}
		}
	}
}
