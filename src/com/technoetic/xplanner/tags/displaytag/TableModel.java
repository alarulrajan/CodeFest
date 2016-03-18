package com.technoetic.xplanner.tags.displaytag;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.model.RowSorter;
import org.displaytag.properties.TableProperties;

/**
 * @author fgiust
 * @version $Revision: 408 $ ($Author: sg0897500 $)
 */
public class TableModel extends org.displaytag.model.TableModel {
	private static Log log = LogFactory
			.getLog(org.displaytag.model.TableModel.class);

	String id;

	public TableModel(final TableProperties tableProperties) {
		super(tableProperties, null);
	}

	/**
	 * sorts the given list of Rows. The method is called internally by
	 * sortFullList() and sortPageList().
	 * 
	 * @param list
	 *            List
	 */
	private void sortRowList(final List list) {
		if (this.isSorted()) {
			final HeaderCell sortedHeaderCell = (HeaderCell) this
					.getSortedColumnHeader();

			if (sortedHeaderCell != null) {
				// If it is an explicit value, then sort by that, otherwise sort
				// by the property...
				final int sortedColumn = this.getSortedColumnNumber();
				if (sortedHeaderCell.getBeanSortPropertyName() != null
						|| sortedColumn != -1
						&& sortedColumn < this.getHeaderCellList().size()) {
					Collections.sort(
							list,
							new RowSorter(sortedColumn, sortedHeaderCell
									.getBeanSortPropertyName(), this
									.getTableDecorator(), this
									.isSortOrderAscending()));
				}
			}

		}

	}

	@Override
	public void setId(final String tableId) {
		this.id = tableId;
		super.setId(tableId);
	}

	/**
	 * sort the list displayed in page.
	 */
	@Override
	public void sortPageList() {
		if (TableModel.log.isDebugEnabled()) {
			TableModel.log.debug("[" + this.id + "] sorting page list");
		}
		this.sortRowList(this.getRowListPage());

	}

	/**
	 * sort the full list of data.
	 */
	@Override
	public void sortFullList() {
		if (TableModel.log.isDebugEnabled()) {
			TableModel.log.debug("[" + this.id + "] sorting full data");
		}
		this.sortRowList(this.getRowListFull());
	}

}