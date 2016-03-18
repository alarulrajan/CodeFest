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

public class DataSampleData implements DatasetProducer {
	private final Logger log = Logger.getLogger(this.getClass());
	private Iteration iteration;
	private String aspects;
	private String categories;
	private boolean includeWeekends;
	public static final int DAY = 60 * 60 * 1000;

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

	protected Date getLatestDate(final Date date1, final Date date2) {
		return date1.getTime() > date2.getTime() ? date1 : date2;
	}

	private Calendar getMidnightOnIterationStart() {
		final Calendar currentDay = Calendar.getInstance();
		currentDay.setTime(this.iteration.getStartDate());
		currentDay.set(Calendar.HOUR_OF_DAY, 0);
		currentDay.set(Calendar.MINUTE, 0);
		currentDay.set(Calendar.SECOND, 0);
		currentDay.set(Calendar.MILLISECOND, 0);
		return currentDay;
	}

	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	private String formatDay(final Calendar currentDay) {
		if (currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
			return this.dateFormat.format(currentDay.getTime());
		} else {
			return Integer.toString(currentDay.get(Calendar.DAY_OF_MONTH));
		}
	}

	private boolean isWeekendDay(final Calendar currentDay) {
		return currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
				|| currentDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
	}

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

	@Override
	public boolean hasExpired(final Map map, final Date date) {
		return true;
	}

	@Override
	public String getProducerId() {
		return Long.toString(System.currentTimeMillis());
	}

	public void setIteration(final Iteration iteration) {
		this.iteration = iteration;
	}

	public void setAspects(final String aspects) {
		this.aspects = aspects;
	}

	public void setCategories(final String categories) {
		this.categories = categories;
	}

	public void setIncludeWeekends(final boolean includeWeekends) {
		this.includeWeekends = includeWeekends;
	}
}
