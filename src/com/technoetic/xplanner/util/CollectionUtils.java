package com.technoetic.xplanner.util;

import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.beanutils.PropertyUtils;

public class CollectionUtils {
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

	public interface DoubleFilter {
		double filter(Object o);
	}

	public static class DoublePropertyFilter implements DoubleFilter {
		private final String name;

		public DoublePropertyFilter(final String name) {
			this.name = name;
		}

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
