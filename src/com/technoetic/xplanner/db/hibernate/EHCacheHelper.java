package com.technoetic.xplanner.db.hibernate;

import java.util.Iterator;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.hibernate.cache.QueryCache;
import org.hibernate.cache.UpdateTimestampsCache;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;

/**
 * The Class EHCacheHelper.
 */
public class EHCacheHelper {
	
	/** The log. */
	private static Logger log = Logger.getLogger(EHCacheHelper.class);
	
	/** The Constant DEFAULT_MAX_CACHE_SIZE. */
	private static final int DEFAULT_MAX_CACHE_SIZE = 1000;
	
	/** The Constant DEFAULT_OVERFLOW_TO_DISK. */
	private static final boolean DEFAULT_OVERFLOW_TO_DISK = false;
	
	/** The default eternal. */
	private static boolean DEFAULT_ETERNAL = false;
	
	/** The Constant DEFAULT_TIME_TO_LIVE. */
	private static final int DEFAULT_TIME_TO_LIVE = 120;
	
	/** The Constant DEFAULT_TIME_TO_IDLE. */
	private static final int DEFAULT_TIME_TO_IDLE = 120;

	/**
     * Configure.
     *
     * @param hibernateConfig
     *            the hibernate config
     */
	public static void configure(final Configuration hibernateConfig) {
		final Iterator classMappings = hibernateConfig.getClassMappings();
		try {
			while (classMappings.hasNext()) {
				final PersistentClass persistentClass = (PersistentClass) classMappings
						.next();
				EHCacheHelper.configureClassCache(persistentClass
						.getMappedClass());
			}
			EHCacheHelper.configureClassCache(QueryCache.class);
			EHCacheHelper.configureClassCache(UpdateTimestampsCache.class);
		} catch (final Exception e) {
			EHCacheHelper.log.error("error", e);
		}
	}

	/**
     * Configure class cache.
     *
     * @param theClass
     *            the the class
     * @throws CacheException
     *             the cache exception
     */
	private static void configureClassCache(final Class theClass)
			throws CacheException {
		final String name = theClass.getName();
		if (!CacheManager.getInstance().cacheExists(name)) {
			final Cache ehcache = new Cache(name,
					EHCacheHelper.DEFAULT_MAX_CACHE_SIZE,
					EHCacheHelper.DEFAULT_OVERFLOW_TO_DISK,
					EHCacheHelper.DEFAULT_ETERNAL,
					EHCacheHelper.DEFAULT_TIME_TO_LIVE,
					EHCacheHelper.DEFAULT_TIME_TO_IDLE);
			CacheManager.getInstance().addCache(ehcache);
		}
	}
}
