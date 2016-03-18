package org.displaytag.pagination;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.Messages;
import org.displaytag.properties.TableProperties;
import org.displaytag.util.Href;
import org.displaytag.util.ShortToStringStyle;

/**
 * Created by IntelliJ IDEA. User: sg897500 Date: Mar 24, 2005 Time: 3:34:16 PM
 * To change this template use File | Settings | File Templates. Taken from:
 * <p>
 * Utility class that chops up a List of objects into small bite size pieces
 * that are more suitable for display.
 * </p>
 * <p>
 * This class is a stripped down version of the WebListHelper from Tim Dawson
 * (tdawson@is.com)
 * </p>
 * 
 * @author epesh
 * @author Fabrizio Giustina
 * @version $Revision: 421 $ ($Author: sg0897500 $)
 */
public class SmartListHelper {

	/**
	 * logger.
	 */
	private static Log log = LogFactory.getLog(SmartListHelper.class);

	/**
	 * full list.
	 */
	private final List fullList;

	/**
	 * sixe of the full list.
	 */
	private final int fullListSize;

	/**
	 * number of items in a page.
	 */
	private final int pageSize;

	/**
	 * number of pages.
	 */
	private final int pageCount;

	/**
	 * index of current page.
	 */
	private int currentPage;

	/**
	 * TableProperties.
	 */
	private final TableProperties properties;

	/**
	 * Creates a SmarListHelper instance that will help you chop up a list into
	 * bite size pieces that are suitable for display.
	 * 
	 * @param list
	 *            List
	 * @param fullSize
	 *            size of the full list
	 * @param itemsInPage
	 *            number of items in a page (int > 0)
	 * @param tableProperties
	 *            TableProperties
	 */
	public SmartListHelper(final List list, final int fullSize,
			final int itemsInPage, final TableProperties tableProperties) {
		if (SmartListHelper.log.isDebugEnabled()) {
			SmartListHelper.log.debug(Messages.getString(
					"SmartListHelper.debug.instantiated", //$NON-NLS-1$
					new Object[] { new Integer(list.size()),
							new Integer(itemsInPage), new Integer(fullSize) }));
		}

		this.properties = tableProperties;
		this.pageSize = itemsInPage;
		this.fullList = list;
		this.fullListSize = fullSize;
		this.pageCount = this.computedPageCount();
		this.currentPage = 1;
	}

	/**
	 * Returns the computed number of pages it would take to show all the
	 * elements in the list given the pageSize we are working with.
	 * 
	 * @return int computed number of pages
	 */
	protected int computedPageCount() {
		final int size = this.fullListSize;
		final int div = size / this.pageSize;
		final int mod = size % this.pageSize;
		final int result = mod == 0 ? div : div + 1;

		return result;
	}

	/**
	 * Returns the index into the master list of the first object that should
	 * appear on the current page that the user is viewing.
	 * 
	 * @return int index of the first object that should appear on the current
	 *         page
	 */
	public int getFirstIndexForCurrentPage() {
		return this.getFirstIndexForPage(this.currentPage);
	}

	/**
	 * Returns the index into the master list of the last object that should
	 * appear on the current page that the user is viewing.
	 * 
	 * @return int
	 */
	protected int getLastIndexForCurrentPage() {

		return this.getLastIndexForPage(this.currentPage);
	}

	/**
	 * Returns the index into the master list of the first object that should
	 * appear on the given page.
	 * 
	 * @param pageNumber
	 *            page number
	 * @return int index of the first object that should appear on the given
	 *         page
	 */
	protected int getFirstIndexForPage(final int pageNumber) {
		return (pageNumber - 1) * this.pageSize;
	}

	/**
	 * Returns the index into the master list of the last object that should
	 * appear on the given page.
	 * 
	 * @param pageNumber
	 *            page number
	 * @return int index of the last object that should appear on the given page
	 */
	protected int getLastIndexForPage(final int pageNumber) {

		final int firstIndex = this.getFirstIndexForPage(pageNumber);
		final int pageIndex = this.pageSize - 1;
		final int lastIndex = this.fullListSize - 1;

		return Math.min(firstIndex + pageIndex, lastIndex);
	}

	/**
	 * Returns a subsection of the list that contains just the elements that are
	 * supposed to be shown on the current page the user is viewing.
	 * 
	 * @return List subsection of the list that contains the elements that are
	 *         supposed to be shown on the current page
	 */
	public List getListForCurrentPage() {

		return this.getListForPage(this.currentPage);
	}

	/**
	 * Returns a subsection of the list that contains just the elements that are
	 * supposed to be shown on the given page.
	 * 
	 * @param pageNumber
	 *            page number
	 * @return List subsection of the list that contains just the elements that
	 *         are supposed to be shown on the given page
	 */
	protected List getListForPage(final int pageNumber) {
		if (SmartListHelper.log.isDebugEnabled()) {
			SmartListHelper.log.debug(Messages.getString(
					"SmartListHelper.debug.sublist", //$NON-NLS-1$
					new Object[] { new Integer(pageNumber) }));
		}

		final int firstIndex = this.getFirstIndexForPage(pageNumber);
		final int lastIndex = this.getLastIndexForPage(pageNumber);
		return this.fullList.subList(firstIndex, lastIndex + 1);
	}

	/**
	 * Set's the page number that the user is viewing.
	 * 
	 * @param pageNumber
	 *            page number
	 */
	public void setCurrentPage(final int pageNumber) {
		if (SmartListHelper.log.isDebugEnabled()) {
			SmartListHelper.log.debug(Messages.getString(
					"SmartListHelper.debug.currentpage", //$NON-NLS-1$
					new Object[] { new Integer(pageNumber),
							new Integer(this.pageCount) }));
		}

		if (pageNumber == 0 || pageNumber != 1 && pageNumber > this.pageCount) {
			// invalid page: better don't throw an exception, since this could
			// easily happen
			// (list changed, user bookmarked the page)
			this.currentPage = 1;
		} else {
			this.currentPage = pageNumber;
		}
	}

	/**
	 * Return the little summary message that lets the user know how many
	 * objects are in the list they are viewing, and where in the list they are
	 * currently positioned. The message looks like: nnn [item(s)] found,
	 * displaying nnn to nnn. [item(s)] is replaced by either itemName or
	 * itemNames depending on if it should be signular or plural.
	 * 
	 * @return String
	 */
	public String getSearchResultsSummary() {

		Object[] objs;
		String message;

		if (this.fullListSize == 0) {
			objs = new Object[] { this.properties.getPagingItemsName() };
			message = this.properties.getPagingFoundNoItems();

		} else if (this.fullListSize == 1) {
			objs = new Object[] { this.properties.getPagingItemName() };
			message = this.properties.getPagingFoundOneItem();
		} else if (this.computedPageCount() == 1 || this.currentPage == -1) {
			objs = new Object[] { new Integer(this.fullListSize),
					this.properties.getPagingItemsName(),
					this.properties.getPagingItemsName() };
			message = this.properties.getPagingFoundAllItems();
		} else {
			objs = new Object[] { new Integer(this.fullListSize),
					this.properties.getPagingItemsName(),
					new Integer(this.getFirstIndexForCurrentPage() + 1),
					new Integer(this.getLastIndexForCurrentPage() + 1),
					new Integer(this.currentPage), new Integer(this.pageCount) };
			message = this.properties.getPagingFoundSomeItems();
		}

		return MessageFormat.format(message, objs);
	}

	/**
	 * Returns a string containing the nagivation bar that allows the user to
	 * move between pages within the list. The urlFormatString should be a URL
	 * that looks like the following: somepage.page?page={0}
	 * 
	 * @param baseHref
	 *            Href used for links
	 * @param pageParameter
	 *            name for the page parameter
	 * @return String
	 */
	public String getPageNavigationBar(final Href baseHref,
			final String pageParameter) {

		final int groupSize = this.properties.getPagingGroupSize();
		int startPage;
		int endPage;

		final Pagination pagination = new Pagination(baseHref, pageParameter);
		pagination.setCurrent(new Integer(this.currentPage));

		// if no items are found still add pagination?
		if (this.pageCount == 0) {
			pagination.addPage(1, true);
		}

		// center the selected page, but only if there are {groupSize} pages
		// available after it, and check that the
		// result is not < 1
		startPage = Math.max(
				Math.min(this.currentPage - groupSize / 2, this.pageCount
						- groupSize), 1);
		endPage = Math.min(startPage + groupSize - 1, this.pageCount);

		if (SmartListHelper.log.isDebugEnabled()) {
			SmartListHelper.log.debug("Displaying pages from " + startPage
					+ " to " + endPage);
		}

		if (this.currentPage != 1) {
			pagination.setFirst(new Integer(1));
			pagination.setPrevious(new Integer(this.currentPage - 1));
		}

		for (int j = startPage; j <= endPage; j++) {
			if (SmartListHelper.log.isDebugEnabled()) {
				SmartListHelper.log.debug("adding page " + j); //$NON-NLS-1$
			}
			pagination.addPage(j, j == this.currentPage);
		}

		if (this.currentPage != this.pageCount) {
			pagination.setNext(new Integer(this.currentPage + 1));
			pagination.setLast(new Integer(this.pageCount));
		}

		// format for previous/next banner
		String bannerFormat;

		if (pagination.isOnePage()) {
			bannerFormat = this.properties.getPagingBannerOnePage();
		} else if (pagination.isFirst()) {
			bannerFormat = this.properties.getPagingBannerFirst();
		} else if (pagination.isLast()) {
			bannerFormat = this.properties.getPagingBannerLast();
		} else if (this.currentPage == -1) {
			bannerFormat = this.properties.getPagingBannerAll();
		} else {
			bannerFormat = this.properties.getPagingBannerFull();
		}

		return pagination.getFormattedBanner(
				this.properties.getPagingPageLink(),
				this.properties.getPagingPageSelected(),
				this.properties.getPagingPageSeparator(), bannerFormat);
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ShortToStringStyle.SHORT_STYLE) //
				.append("fullList", this.fullList) //$NON-NLS-1$
				.append("fullListSize", this.fullListSize) //$NON-NLS-1$
				.append("pageSize", this.pageSize) //$NON-NLS-1$
				.append("pageCount", this.pageCount) //$NON-NLS-1$
				.append("properties", this.properties) //$NON-NLS-1$
				.append("currentPage", this.currentPage) //$NON-NLS-1$
				.toString();
	}
}
