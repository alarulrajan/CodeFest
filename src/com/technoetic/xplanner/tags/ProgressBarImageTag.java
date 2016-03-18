package com.technoetic.xplanner.tags;

import java.awt.Color;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.lang.math.NumberUtils;

import com.technoetic.xplanner.format.DecimalFormat;

import de.laures.cewolf.CewolfException;
import de.laures.cewolf.ChartImage;
import de.laures.cewolf.Configuration;
import de.laures.cewolf.Storage;
import de.laures.cewolf.storage.SerializableChartImage;
import de.laures.cewolf.taglib.tags.ChartImgTag;

public class ProgressBarImageTag extends ChartImgTag implements ProgressBarTag {
	private float quality = 0.75F;
	private double actual;
	private double estimate;
	private boolean complete;

	public static final Color UNCOMPLETED_COLOR = new Color(0x80, 0x80, 0xFF);
	public static final Color COMPLETED_COLOR = new Color(0xA0, 0xFF, 0xA0);
	public static final Color EXCEEDED_COLOR = new Color(0xFF, 0xA0, 0xA0);
	public static final Color UNWORKED_COLOR = new Color(0xC0, 0xC0, 0xC0);

	@Override
	public int doStartTag() throws JspException {
		this.setRenderer("/cewolf");
		this.setChartid(this.getId());
		final Storage storage = Configuration.getInstance(
				this.pageContext.getServletContext()).getStorage();
		try {
			this.sessionKey = storage.storeChartImage(this.getChartImage(),
					this.pageContext);
		} catch (final CewolfException cwex) {
			throw new JspException(cwex.getMessage());
		}
		return Tag.SKIP_BODY;
	}

	private ChartImage getChartImage() throws CewolfException {
		final ProgressBarImage image = new ProgressBarImage(this.createModel());
		return new SerializableChartImage(
				new CewolfProgressBarChartImage(image));
	}

	@Override
	public void setActual(final double actual) {
		this.actual = actual;
	}

	@Override
	public void setEstimate(final double estimate) {
		this.estimate = estimate;
	}

	@Override
	public void setComplete(final boolean complete) {
		this.complete = complete;
	}

	@Override
	public void setHeight(final int height) {
		super.setHeight(13);
	}

	@Override
	public void setWidth(final String width) {
		super.setWidth(NumberUtils.toInt(width));
	}

	public void setQuality(final String quality) {
		this.quality = Float.parseFloat(quality);
		if (this.quality < 0.0F || this.quality > 1.0F) {
			throw new IllegalArgumentException("quality \"" + quality
					+ "\" out of range");
		}
	}

	public String getQuality() {
		return Float.toString(this.quality);
	}

	private double getCaptionValue() {
		return Math.min(this.actual, this.estimate);
	}

	private boolean isComplete() {
		return this.complete;
	}

	public Color getForegroundColor() {
		if (this.isComplete()) {
			return PrintLinkTag.isInPrintMode(this.pageContext) ? Color.GRAY
					: ProgressBarImageTag.COMPLETED_COLOR;
		} else {
			return PrintLinkTag.isInPrintMode(this.pageContext) ? Color.DARK_GRAY
					: ProgressBarImageTag.UNCOMPLETED_COLOR;
		}
	}

	public Color getBackgroundColor() {

		if (this.actual > this.estimate) {
			return PrintLinkTag.isInPrintMode(this.pageContext) ? Color.BLACK
					: ProgressBarImageTag.EXCEEDED_COLOR;
		} else {
			return ProgressBarImageTag.UNWORKED_COLOR;
		}
	}

	public double getBarValue() {
		if (this.actual == 0 && this.estimate == 0 && this.complete) {
			return this.width;
		}
		return Math.min(this.actual, this.estimate);
	}

	public double getMaxValue() {
		if (this.actual == 0 && this.estimate == 0 && this.complete) {
			return this.width;
		}
		return Math.max(this.actual, this.estimate);
	}

	public ProgressBarImage.Model createModel() {
		return new ProgressBarImage.Model(this.width, this.height,
				this.getBarValue(), this.getForegroundColor(),
				this.getMaxValue(), this.getBackgroundColor(),
				new DecimalFormat((HttpServletRequest) this.pageContext
						.getRequest()), this.getCaptionValue());
	}

}
