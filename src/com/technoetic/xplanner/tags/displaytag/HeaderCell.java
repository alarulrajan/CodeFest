package com.technoetic.xplanner.tags.displaytag;

/**
 * DataObject representing the column header. The header cell contains all the
 * properties common to cells in the same column.
 * 
 * @author fgiust
 * @version $Revision: 33 $ ($Author: sg0620641 $)
 */
public class HeaderCell extends org.displaytag.model.HeaderCell {
	/**
	 * property name to look up in the bean.
	 */
	private String beanSortPropertyName;

	/**
     * Gets the bean sort property name.
     *
     * @return the bean sort property name
     */
	public String getBeanSortPropertyName() {
		return this.beanSortPropertyName != null ? this.beanSortPropertyName
				: this.getBeanPropertyName();
	}

	/**
     * Sets the bean sort property name.
     *
     * @param beanSortPropertyName
     *            the new bean sort property name
     */
	public void setBeanSortPropertyName(final String beanSortPropertyName) {
		this.beanSortPropertyName = beanSortPropertyName;
	}
}