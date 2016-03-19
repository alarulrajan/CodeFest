package com.technoetic.xplanner.charts;

import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

import de.laures.cewolf.ChartPostProcessor;

/**
 * Post processor which is used to add shapes to the datapoints of a line graph.
 */
public class CategoryPostProcessor implements ChartPostProcessor {
	
	/* (non-Javadoc)
	 * @see de.laures.cewolf.ChartPostProcessor#processChart(java.lang.Object, java.util.Map)
	 */
	@Override
	public void processChart(final Object chart, final Map params) {
		final CategoryPlot plot = (CategoryPlot) ((JFreeChart) chart).getPlot();
		plot.setRenderer(new LineAndShapeRenderer());
	}
}
