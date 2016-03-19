package com.technoetic.xplanner.charts;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import org.jfree.data.general.DefaultPieDataset;

import com.technoetic.xplanner.db.IterationStatisticsQuery;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

/**
 * User: Mateusz Prokopowicz Date: Apr 12, 2005 Time: 3:25:26 PM.
 */
public abstract class XplannerPieChartData implements DatasetProducer {
	
	/** The data set. */
	protected DefaultPieDataset dataSet = new DefaultPieDataset();

	/**
     * Sets the statistics.
     *
     * @param statistics
     *            the new statistics
     */
	public void setStatistics(final IterationStatisticsQuery statistics) {
		final Hashtable data = this.getData(statistics);
		final Enumeration enumeration = data.keys();

		while (enumeration.hasMoreElements()) {

			final Object group = enumeration.nextElement();
			final String groupName = group != null ? group.toString() : "null";

			final double value = ((Double) data.get(groupName)).doubleValue();

			// Note, doubles are rounded to the nearest integer for clarity and
			// ease of display
			final Long roundedValue = new Long(Math.round(value));
			if (roundedValue.longValue() != 0) {
				this.dataSet.setValue(groupName + " (" + roundedValue + ")",
						roundedValue);
			}
		}
	}

	/**
     * Gets the data.
     *
     * @param statistics
     *            the statistics
     * @return the data
     */
	protected abstract Hashtable getData(IterationStatisticsQuery statistics);

	/* (non-Javadoc)
	 * @see de.laures.cewolf.DatasetProducer#produceDataset(java.util.Map)
	 */
	@Override
	public Object produceDataset(final Map params)
			throws DatasetProduceException {
		return this.dataSet;
	}

	/* (non-Javadoc)
	 * @see de.laures.cewolf.DatasetProducer#hasExpired(java.util.Map, java.util.Date)
	 */
	@Override
	public boolean hasExpired(final Map params, final Date since) {
		return true;
	}

	/* (non-Javadoc)
	 * @see de.laures.cewolf.DatasetProducer#getProducerId()
	 */
	@Override
	public String getProducerId() {
		return this.getClass().getName();
	}
}
