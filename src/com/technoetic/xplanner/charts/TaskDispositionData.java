package com.technoetic.xplanner.charts;

import java.util.Hashtable;

import com.technoetic.xplanner.db.IterationStatisticsQuery;

/**
 * The Class TaskDispositionData.
 */
public class TaskDispositionData extends XplannerPieChartData {
    
    /* (non-Javadoc)
     * @see com.technoetic.xplanner.charts.XplannerPieChartData#getData(com.technoetic.xplanner.db.IterationStatisticsQuery)
     */
    @Override
    protected Hashtable getData(final IterationStatisticsQuery statistics) {
        return statistics.getTaskCountByDisposition();
    }
}
