package com.technoetic.xplanner.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * The Class CollectionUtils.
 */
public class CollectionUtils {
	
	/**
     * Sum.
     *
     * @param collection
     *            the collection
     * @param filter
     *            the filter
     * @return the double
     */
	public static double sum(final Collection collection,
			final DoubleFilter filter) {
		if (collection == null || collection.isEmpty()) {
			return 0.0;
		}
		double value = 0.0;
		final Iterator it = collection.iterator();
		while (it.hasNext()) {
			value += filter.filter(it.next());
		}
		return value;
	}

	/**
     * The Interface DoubleFilter.
     */
	public interface DoubleFilter {
		
		/**
         * Filter.
         *
         * @param o
         *            the o
         * @return the double
         */
		double filter(Object o);
	}

	/**
     * The Class DoublePropertyFilter.
     */
	public static class DoublePropertyFilter implements DoubleFilter {
		
		/** The name. */
		private final String name;

		/**
         * Instantiates a new double property filter.
         *
         * @param name
         *            the name
         */
		public DoublePropertyFilter(final String name) {
			this.name = name;
		}

		/* (non-Javadoc)
		 * @see com.technoetic.xplanner.util.CollectionUtils.DoubleFilter#filter(java.lang.Object)
		 */
		@Override
		public double filter(final Object o) {
			try {
				return ((Double) PropertyUtils.getProperty(o, this.name))
						.doubleValue();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
