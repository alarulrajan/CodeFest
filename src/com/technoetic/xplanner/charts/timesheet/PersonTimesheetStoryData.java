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

public class PersonTimesheetStoryData implements DatasetProducer {
	private final DefaultPieDataset dataSet = new DefaultPieDataset();

	public void setTimesheet(final Timesheet timesheet) {
		final Hashtable storyData = timesheet.getStoryData();
		for (final Enumeration keys = storyData.keys(); keys.hasMoreElements();) {
			final String story = (String) keys.nextElement();
			final BigDecimal value = ((BigDecimal) storyData.get(story))
					.setScale(0, BigDecimal.ROUND_HALF_EVEN);
			this.dataSet.setValue(story + " (" + value + ")", value);
		}
	}

	@Override
	public Object produceDataset(final Map params)
			throws DatasetProduceException {
		return this.dataSet;
	}

	@Override
	public boolean hasExpired(final Map params, final Date since) {
		return true;
	}

	@Override
	public String getProducerId() {
		return this.getClass().getName();
	}
}
