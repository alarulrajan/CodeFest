/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.db;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Person;

/**
 * User: mprokopowicz Date: Mar 31, 2006 Time: 4:03:21 PM.
 */
public class CacheInvalidateForPersonAnywhereInAttrListInterceptor extends
        MethodCacheInvalidateInterceptor {

    /**
     * Instantiates a new cache invalidate for person anywhere in attr list
     * interceptor.
     *
     * @param cacheMap
     *            the cache map
     */
    public CacheInvalidateForPersonAnywhereInAttrListInterceptor(
            final Map cacheMap) {
        super(cacheMap);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.db.MethodCacheInvalidateInterceptor#getMethodCacheKey(java.lang.Object[])
     */
    @Override
    public List getMethodCacheKey(final Object args[]) {
        for (int i = 0; i < args.length; i++) {
            final Object arg = args[i];
            if (arg instanceof Person) {
                return Arrays.asList(new Object[] { new Integer(((Person) arg)
                        .getId()) });
            }
        }
        return Collections.EMPTY_LIST;
    }
}
