package com.technoetic.xplanner.charts;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.DataSample;
import net.sf.xplanner.domain.Iteration;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;
import org.jfree.data.category.DefaultCategoryDataset;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.util.TimeGenerator;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

/**
 * The Class DataSampleData.
 */
public class DataSampleData implements DatasetProducer {
    
    /** The log. */
    private final Logger log = Logger.getLogger(this.getClass());
    
    /** The iteration. */
    private Iteration iteration;
    
    /** The aspects. */
    private String aspects;
    
    /** The categories. */
    private String categories;
    
    /** The include weekends. */
    private boolean includeWeekends;
    
    /** The Constant DAY. */
    public static final int DAY = 60 * 60 * 1000;

    /* (non-Javadoc)
     * @see de.laures.cewolf.DatasetProducer#produceDataset(java.util.Map)
     */
    @Override
    public Object produceDataset(final Map map) throws DatasetProduceException {
        final DefaultCategoryDataset data = new DefaultCategoryDataset();
        final String[] aspectArray = this.aspects.split(",");
        final String[] categoryArray = this.categories.split(",");
        for (int i = 0; i < categoryArray.length; i++) {
            final String category = categoryArray[i];
            final String aspect = aspectArray[i];
            this.addData(data, aspect, category);
        }

        return data;
    }

    /**
     * Adds the data.
     *
     * @param data
     *            the data
     * @param aspect
     *            the aspect
     * @param category
     *            the category
     */
    private void addData(final DefaultCategoryDataset data,
            final String aspect, final String category) {
        try {
            final List samples = ThreadSession
                    .get()
                    .find(" from s in "
                            + DataSample.class
                            + " where s.id.referenceId = ? and s.id.aspect = ? order by id.sampleTime",
                            new Object[] { new Integer(this.iteration.getId()),
                                    aspect },
                            new Type[] { Hibernate.INTEGER, Hibernate.STRING });
            this.log.debug("retrieved " + samples.size() + " samples");

            final Calendar endDay = Calendar.getInstance();
            if (samples.size() > 0) {
                final DataSample latestDataSample = (DataSample) samples
                        .get(samples.size() - 1);
                endDay.setTime(this.getLatestDate(latestDataSample
                        .getSampleTime(), TimeGenerator.shiftDate(
                        this.iteration.getEndDate(), Calendar.DATE, 1)));
            } else {
                endDay.setTime(TimeGenerator.shiftDate(
                        this.iteration.getEndDate(), Calendar.DATE, 1));
            }
            final Calendar currentDay = this.getMidnightOnIterationStart();

            while (currentDay.getTimeInMillis() <= endDay.getTimeInMillis()) {
                if (this.includeWeekends || !this.isWeekendDay(currentDay)) {
                    final DataSample dataSample = this.closestSample(samples,
                            currentDay, 2 * DataSampleData.DAY);
                    final Number value = dataSample != null ? new Double(
                            dataSample.getValue()) : null;
                    data.addValue(value, category, this.formatDay(currentDay));
                }
                currentDay.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (final Exception e) {
            this.log.error("error loading data samples", e);
        }
    }

    /**
     * Gets the latest date.
     *
     * @param date1
     *            the date1
     * @param date2
     *            the date2
     * @return the latest date
     */
    protected Date getLatestDate(final Date date1, final Date date2) {
        return date1.getTime() > date2.getTime() ? date1 : date2;
    }

    /**
     * Gets the midnight on iteration start.
     *
     * @return the midnight on iteration start
     */
    private Calendar getMidnightOnIterationStart() {
        final Calendar currentDay = Calendar.getInstance();
        currentDay.setTime(this.iteration.getStartDate());
        currentDay.set(Calendar.HOUR_OF_DAY, 0);
        currentDay.set(Calendar.MINUTE, 0);
        currentDay.set(Calendar.SECOND, 0);
        currentDay.set(Calendar.MILLISECOND, 0);
        return currentDay;
    }

    /** The date format. */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyyy-MM-dd");

    /**
     * Format day.
     *
     * @param currentDay
     *            the current day
     * @return the string
     */
    private String formatDay(final Calendar currentDay) {
        if (currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            return this.dateFormat.format(currentDay.getTime());
        } else {
            return Integer.toString(currentDay.get(Calendar.DAY_OF_MONTH));
        }
    }

    /**
     * Checks if is weekend day.
     *
     * @param currentDay
     *            the current day
     * @return true, if is weekend day
     */
    private boolean isWeekendDay(final Calendar currentDay) {
        return currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
    }

    /**
     * Closest sample.
     *
     * @param samples
     *            the samples
     * @param currentDay
     *            the current day
     * @param precision
     *            the precision
     * @return the data sample
     */
    private DataSample closestSample(final List samples,
            final Calendar currentDay, final long precision) {
        final long now = currentDay.getTimeInMillis();
        long delta = Long.MAX_VALUE;
        DataSample sample = null;
        for (int i = 0; i < samples.size(); i++) {
            final DataSample dataSample = (DataSample) samples.get(i);
            final long d = Math.abs(now - dataSample.getSampleTime().getTime());
            if (d < precision && d < delta) {
                delta = d;
                sample = dataSample;
            }
        }
        return sample;
    }

    /* (non-Javadoc)
     * @see de.laures.cewolf.DatasetProducer#hasExpired(java.util.Map, java.util.Date)
     */
    @Override
    public boolean hasExpired(final Map map, final Date date) {
        return true;
    }

    /* (non-Javadoc)
     * @see de.laures.cewolf.DatasetProducer#getProducerId()
     */
    @Override
    public String getProducerId() {
        return Long.toString(System.currentTimeMillis());
    }

    /**
     * Sets the iteration.
     *
     * @param iteration
     *            the new iteration
     */
    public void setIteration(final Iteration iteration) {
        this.iteration = iteration;
    }

    /**
     * Sets the aspects.
     *
     * @param aspects
     *            the new aspects
     */
    public void setAspects(final String aspects) {
        this.aspects = aspects;
    }

    /**
     * Sets the categories.
     *
     * @param categories
     *            the new categories
     */
    public void setCategories(final String categories) {
        this.categories = categories;
    }

    /**
     * Sets the include weekends.
     *
     * @param includeWeekends
     *            the new include weekends
     */
    public void setIncludeWeekends(final boolean includeWeekends) {
        this.includeWeekends = includeWeekends;
    }
}
