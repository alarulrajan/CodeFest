/*
 * Copyright (c) 2005, Your Corporation. All Rights Reserved.
 */

package com.technoetic.xplanner.charts;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.sf.xplanner.dao.DataSampleDao;
import net.sf.xplanner.domain.DataSample;
import net.sf.xplanner.domain.Iteration;

import org.apache.log4j.Logger;
import org.springframework.orm.hibernate3.HibernateOperations;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.util.TimeGenerator;

/**
 * Created by IntelliJ IDEA. User: sg620641 Date: Dec 9, 2005 Time: 4:07:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSamplerImpl extends HibernateDaoSupport implements DataSampler {
	protected final Logger LOG = Logger.getLogger(DataSamplerImpl.class);
	protected TimeGenerator timeGenerator;
	private HibernateOperations hibernateOperations;
	public static final String AUTOMATICALLY_EXTEND_END_DATE_PROP = "iteration.automatically.extend.endDate";
	private Properties properties;
	private DataSampleDao dataSampleDao;

	public void setProperties(final Properties properties) {
		this.properties = properties;
	}

	private void generateDataSamples(final Iteration iteration, final Date date) {
		this.saveSamples(date, iteration);
		this.extendIterationEndDateIfNeeded(iteration, date);
	}

	@Override
	public void generateDataSamples(final Iteration iteration) {
		final Date todayMidnight = this.timeGenerator.getTodaysMidnight();
		final Date tomorrowMidnight = TimeGenerator.shiftDate(todayMidnight,
				Calendar.DATE, 1);
		this.generateDataSamples(iteration, tomorrowMidnight);
	}

	@Override
	public void generateOpeningDataSamples(final Iteration iteration) {
		final Date date = this.timeGenerator.getTodaysMidnight();
		this.generateDataSamples(iteration, date);
	}

	@Override
	public void generateClosingDataSamples(final Iteration iteration) {
		if (iteration.getEndDate().before(this.timeGenerator.getCurrentTime())) {
			final Date todayMidnight = this.timeGenerator.getTodaysMidnight();
			this.generateDataSamples(iteration, todayMidnight);
		} else {
			this.generateDataSamples(iteration);
		}
	}

	@Override
	public void setTimeGenerator(final TimeGenerator timeGenerator) {
		this.timeGenerator = timeGenerator;
	}

	public HibernateOperations getHibernateOperations() {
		if (this.hibernateOperations != null) {
			return this.hibernateOperations;
		}
		return this.getHibernateTemplate();
	}

	public void setHibernateOperations(
			final HibernateOperations hibernateOperations) {
		this.hibernateOperations = hibernateOperations;
	}

	protected void extendIterationEndDateIfNeeded(final Iteration iteration,
			final Date midnight) {
		final boolean automaticallyExtendEndDate = Boolean.valueOf(
				this.properties.getProperty(
						DataSamplerImpl.AUTOMATICALLY_EXTEND_END_DATE_PROP,
						"false")).booleanValue();
		if (automaticallyExtendEndDate
				&& IterationStatus.ACTIVE.toInt() == iteration.getStatus()
				&& iteration.getEndDate().compareTo(midnight) < 0) {
			this.LOG.debug("Extend iteration end day to " + midnight);
			iteration.setEndDate(midnight);
			this.getHibernateOperations().save(iteration);
		}
	}

	protected void saveSamples(final Date date, final Iteration iteration) {
		this.saveSample(date, iteration, "estimatedHours",
				iteration.getEstimatedHours());
		this.saveSample(date, iteration, "actualHours",
				iteration.getCachedActualHours());
		this.saveSample(date, iteration, "remainingHours",
				iteration.getTaskRemainingHours());
	}

	protected void saveSample(final Date date, final Iteration iteration,
			final String aspect, final double value) {
		DataSample sample;

		final List<DataSample> samples = this.dataSampleDao.getDataSamples(
				date, iteration, aspect);

		if (!samples.isEmpty()) {
			sample = samples.get(0);
			sample.setValue(value);
			this.dataSampleDao.save(sample);
			this.LOG.debug("update existing datasample");
		} else {
			sample = new DataSample(date, iteration.getId(), aspect, value);
			this.dataSampleDao.save(sample);
			this.LOG.debug("Generated a new sample:" + sample);
		}
	}

	public void setDataSampleDao(final DataSampleDao dataSampleDao) {
		this.dataSampleDao = dataSampleDao;
	}

}
