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

/**
 * The Class ProgressBarImageTag.
 */
public class ProgressBarImageTag extends ChartImgTag implements ProgressBarTag {
    
    /** The quality. */
    private float quality = 0.75F;
    
    /** The actual. */
    private double actual;
    
    /** The estimate. */
    private double estimate;
    
    /** The complete. */
    private boolean complete;

    /** The Constant UNCOMPLETED_COLOR. */
    public static final Color UNCOMPLETED_COLOR = new Color(0x80, 0x80, 0xFF);
    
    /** The Constant COMPLETED_COLOR. */
    public static final Color COMPLETED_COLOR = new Color(0xA0, 0xFF, 0xA0);
    
    /** The Constant EXCEEDED_COLOR. */
    public static final Color EXCEEDED_COLOR = new Color(0xFF, 0xA0, 0xA0);
    
    /** The Constant UNWORKED_COLOR. */
    public static final Color UNWORKED_COLOR = new Color(0xC0, 0xC0, 0xC0);

    /* (non-Javadoc)
     * @see de.laures.cewolf.taglib.tags.ChartImgTag#doStartTag()
     */
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

    /**
     * Gets the chart image.
     *
     * @return the chart image
     * @throws CewolfException
     *             the cewolf exception
     */
    private ChartImage getChartImage() throws CewolfException {
        final ProgressBarImage image = new ProgressBarImage(this.createModel());
        return new SerializableChartImage(
                new CewolfProgressBarChartImage(image));
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.ProgressBarTag#setActual(double)
     */
    @Override
    public void setActual(final double actual) {
        this.actual = actual;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.ProgressBarTag#setEstimate(double)
     */
    @Override
    public void setEstimate(final double estimate) {
        this.estimate = estimate;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.ProgressBarTag#setComplete(boolean)
     */
    @Override
    public void setComplete(final boolean complete) {
        this.complete = complete;
    }

    /* (non-Javadoc)
     * @see de.laures.cewolf.taglib.html.HTMLImgTag#setHeight(int)
     */
    @Override
    public void setHeight(final int height) {
        super.setHeight(13);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.tags.ProgressBarTag#setWidth(java.lang.String)
     */
    @Override
    public void setWidth(final String width) {
        super.setWidth(NumberUtils.toInt(width));
    }

    /**
     * Sets the quality.
     *
     * @param quality
     *            the new quality
     */
    public void setQuality(final String quality) {
        this.quality = Float.parseFloat(quality);
        if (this.quality < 0.0F || this.quality > 1.0F) {
            throw new IllegalArgumentException("quality \"" + quality
                    + "\" out of range");
        }
    }

    /**
     * Gets the quality.
     *
     * @return the quality
     */
    public String getQuality() {
        return Float.toString(this.quality);
    }

    /**
     * Gets the caption value.
     *
     * @return the caption value
     */
    private double getCaptionValue() {
        return Math.min(this.actual, this.estimate);
    }

    /**
     * Checks if is complete.
     *
     * @return true, if is complete
     */
    private boolean isComplete() {
        return this.complete;
    }

    /**
     * Gets the foreground color.
     *
     * @return the foreground color
     */
    public Color getForegroundColor() {
        if (this.isComplete()) {
            return PrintLinkTag.isInPrintMode(this.pageContext) ? Color.GRAY
                    : ProgressBarImageTag.COMPLETED_COLOR;
        } else {
            return PrintLinkTag.isInPrintMode(this.pageContext) ? Color.DARK_GRAY
                    : ProgressBarImageTag.UNCOMPLETED_COLOR;
        }
    }

    /**
     * Gets the background color.
     *
     * @return the background color
     */
    public Color getBackgroundColor() {

        if (this.actual > this.estimate) {
            return PrintLinkTag.isInPrintMode(this.pageContext) ? Color.BLACK
                    : ProgressBarImageTag.EXCEEDED_COLOR;
        } else {
            return ProgressBarImageTag.UNWORKED_COLOR;
        }
    }

    /**
     * Gets the bar value.
     *
     * @return the bar value
     */
    public double getBarValue() {
        if (Double.doubleToLongBits(this.actual) == 0 && Double.doubleToLongBits(this.estimate) == 0 && this.complete) {
            return this.width;
        }
        return Math.min(this.actual, this.estimate);
    }

    /**
     * Gets the max value.
     *
     * @return the max value
     */
    public double getMaxValue() {
        if (Double.doubleToLongBits(this.actual) == 0 && Double.doubleToLongBits(this.estimate) == 0 && this.complete) {
            return this.width;
        }
        return Math.max(this.actual, this.estimate);
    }

    /**
     * Creates the model.
     *
     * @return the progress bar image. model
     */
    public ProgressBarImage.Model createModel() {
        return new ProgressBarImage.Model(this.width, this.height,
                this.getBarValue(), this.getForegroundColor(),
                this.getMaxValue(), this.getBackgroundColor(),
                new DecimalFormat((HttpServletRequest) this.pageContext
                        .getRequest()), this.getCaptionValue());
    }

}
