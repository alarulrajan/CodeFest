package com.technoetic.xplanner.charts;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.classic.Session;
import org.jfree.data.general.DefaultPieDataset;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

/**
 * The Class PieChartQuery.
 */
public class PieChartQuery implements DatasetProducer {
    
    /** The producer id. */
    private final String producerId = Long.toString(System.currentTimeMillis());
    
    /** The query. */
    private String query;
    
    /** The include count in label. */
    private boolean includeCountInLabel;

    /* (non-Javadoc)
     * @see de.laures.cewolf.DatasetProducer#produceDataset(java.util.Map)
     */
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

    /**
     * Gets the count.
     *
     * @param objects
     *            the objects
     * @return the count
     */
    private int getCount(final Object[] objects) {
        return ((Number) objects[1]).intValue();
    }

    /**
     * Gets the label.
     *
     * @param objects
     *            the objects
     * @return the label
     */
    private Comparable getLabel(final Object[] objects) {
        return objects[0]
                + (this.includeCountInLabel ? " (" + this.getCount(objects)
                        + ")" : "");
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
        return this.producerId;
    }

    /**
     * Sets the query.
     *
     * @param query
     *            the new query
     */
    public void setQuery(final String query) {
        this.query = query;
    }

    /**
     * Sets the include count in label.
     *
     * @param includeCountInLabel
     *            the new include count in label
     */
    public void setIncludeCountInLabel(final boolean includeCountInLabel) {
        this.includeCountInLabel = includeCountInLabel;
    }
}
