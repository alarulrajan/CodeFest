package com.technoetic.xplanner.charts;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.classic.Session;
import org.jfree.data.general.DefaultPieDataset;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

public class PieChartQuery implements DatasetProducer {
	private final String producerId = Long.toString(System.currentTimeMillis());
	private String query;
	private boolean includeCountInLabel;

	@Override
	public Object produceDataset(final Map parameters)
			throws DatasetProduceException {
		List results = null;
		try {
			results = null;
			final Session session = ThreadSession.get();
			results = session.find(this.query);
		} catch (final Exception e) {
			throw new DatasetProduceException(e.getMessage());
		}
		final DefaultPieDataset data = new DefaultPieDataset();
		for (int i = 0; i < results.size(); i++) {
			final Object[] objects = (Object[]) results.get(i);
			data.setValue(this.getLabel(objects), this.getCount(objects));
		}
		return data;
	}

	private int getCount(final Object[] objects) {
		return ((Number) objects[1]).intValue();
	}

	private Comparable getLabel(final Object[] objects) {
		return objects[0]
				+ (this.includeCountInLabel ? " (" + this.getCount(objects)
						+ ")" : "");
	}

	@Override
	public boolean hasExpired(final Map map, final Date date) {
		return true;
	}

	@Override
	public String getProducerId() {
		return this.producerId;
	}

	public void setQuery(final String query) {
		this.query = query;
	}

	public void setIncludeCountInLabel(final boolean includeCountInLabel) {
		this.includeCountInLabel = includeCountInLabel;
	}
}
