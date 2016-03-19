package com.technoetic.xplanner.tags;

import javax.servlet.jsp.tagext.Tag;

/**
 * The Interface ProgressBarTag.
 */
public interface ProgressBarTag extends Tag {
	
	/**
     * Sets the actual.
     *
     * @param actual
     *            the new actual
     */
	void setActual(double actual);

	/**
     * Sets the estimate.
     *
     * @param estimate
     *            the new estimate
     */
	void setEstimate(double estimate);

	/**
     * Sets the complete.
     *
     * @param complete
     *            the new complete
     */
	void setComplete(boolean complete);

	/**
     * Sets the width.
     *
     * @param width
     *            the new width
     */
	void setWidth(String width);

	/**
     * Sets the height.
     *
     * @param height
     *            the new height
     */
	void setHeight(int height);
}
