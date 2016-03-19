package com.technoetic.xplanner.tags.displaytag;

/**
 * The Interface RowDecorator.
 */
public interface RowDecorator {
	
	/**
     * Gets the css classes.
     *
     * @param row
     *            the row
     * @return the css classes
     */
	String getCssClasses(Row row);
}
