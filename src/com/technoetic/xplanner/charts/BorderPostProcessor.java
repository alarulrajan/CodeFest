package com.technoetic.xplanner.charts;

import java.awt.Color;
import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;

import de.laures.cewolf.ChartPostProcessor;

/**
 * Post processor which is used change the border colour of a pie chart.
 */
public class BorderPostProcessor implements ChartPostProcessor {
	@Override
	public void processChart(final Object chart, final Map params) {
		final PiePlot plot = (PiePlot) ((JFreeChart) chart).getPlot();
		plot.setOutlinePaint(Color.WHITE);
	}
}
