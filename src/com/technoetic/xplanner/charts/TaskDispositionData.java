package com.technoetic.xplanner.charts;

import java.util.Hashtable;

import com.technoetic.xplanner.db.IterationStatisticsQuery;

public class TaskDispositionData extends XplannerPieChartData {
	@Override
	protected Hashtable getData(final IterationStatisticsQuery statistics) {
		return statistics.getTaskCountByDisposition();
	}
}
