package com.technoetic.xplanner.charts.timesheet;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import org.jfree.data.general.DefaultPieDataset;

import com.technoetic.xplanner.domain.virtual.Timesheet;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

/**
 * The Class PersonTimesheetProjectData.
 */
public class PersonTimesheetProjectData implements DatasetProducer {
	
	/** The data set. */
	private final DefaultPieDataset dataSet = new DefaultPieDataset();

	/**
     * Sets the timesheet.
     *
     * @param timesheet
     *            the new timesheet
     */
	public void setTimesheet(final Timesheet timesheet) {
		final Hashtable projectData = timesheet.getProjectData();
		for (final Enumeration keys = projectData.keys(); keys
				.hasMoreElements();) {
			final String project = (String) keys.nextElement();
			final BigDecimal value = ((BigDecimal) projectData.get(project))
					.setScale(0, BigDecimal.ROUND_HALF_EVEN);
			this.dataSet.setValue(project + " (" + value + ")", value);
		}
	}

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
