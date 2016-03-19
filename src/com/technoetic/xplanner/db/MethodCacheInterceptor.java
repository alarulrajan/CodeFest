package com.technoetic.xplanner.db;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * User: Mateusz Prokopowicz Date: Sep 6, 2005 Time: 6:40:13 AM.
 */
public class MethodCacheInterceptor implements MethodInterceptor {
    
    /** The result by args by method name. */
    private final Map resultByArgsByMethodName;
    
    /** The log. */
    Logger LOG = Logger.getLogger(MethodCacheInterceptor.class);

    /**
     * Instantiates a new method cache interceptor.
     *
     * @param cacheMap
     *            the cache map
     */
    public MethodCacheInterceptor(final Map cacheMap) {
        this.resultByArgsByMethodName = cacheMap;
    }

    /* (non-Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @Override
    public Object invoke(final MethodInvocation invocation) throws Throwable {
        final String methodName = invocation.getMethod().getName();
        final List argumentList = Arrays.asList(invocation.getArguments());
        return this.cache(methodName, argumentList, invocation);
    }

    /**
     * Cache.
     *
     * @param methodName
     *            the method name
     * @param argumentList
     *            the argument list
     * @param invocation
     *            the invocation
     * @return the object
     * @throws Throwable
     *             the throwable
     */
    Object cache(final String methodName, final List argumentList,
            final MethodInvocation invocation) throws Throwable {
        Object retVal;
        this.LOG.debug("Processing caching for method " + methodName + "("
                + StringUtils.join(argumentList.iterator(), ", ") + ")");
        Map resultByArgs = (Map) this.resultByArgsByMethodName.get(methodName);
        if (resultByArgs == null) {
            this.LOG.debug("No method cache");
            resultByArgs = new HashMap();
            this.resultByArgsByMethodName.put(methodName, resultByArgs);
        }
        if (!resultByArgs.containsKey(argumentList)) {
            this.LOG.debug("No arguments cache. Calling the orginal method and cache the result");
            retVal = invocation.proceed();
            resultByArgs.put(argumentList, retVal);
        } else {
            this.LOG.debug("The cache found. Returning the cached result");
            retVal = resultByArgs.get(argumentList);
        }
        return retVal;
    }
}
