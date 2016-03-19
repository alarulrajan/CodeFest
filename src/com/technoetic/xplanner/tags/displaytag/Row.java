package com.technoetic.xplanner.tags.displaytag;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.displaytag.model.Cell;
import org.displaytag.model.ColumnIterator;
import org.displaytag.model.TableModel;
import org.displaytag.util.TagConstants;

/**
 * Holds informations for a table row.
 * 
 * @author fgiust
 * @version $Revision: 33 $ ($Author: sg0620641 $)
 */
public class Row extends org.displaytag.model.Row {

    /**
     * Object holding values for the current row.
     */
    private final Object rowObject;

    /**
     * List of cell objects.
     */
    private final List staticCells;

    /**
     * Row number.
     */
    private int rowNumber;

    /**
     * TableModel which the row belongs to.
     */
    private TableModel tableModel;

    /** Default Row Decorator. */
    private RowDecorator decorator;

    /**
     * Constructor for Row.
     * 
     * @param object
     *            Object
     * @param number
     *            int
     */
    public Row(final Object object, final int number) {
        super(object, number);
        this.decorator = new DefaultRowDecorator();
        this.rowObject = object;
        this.rowNumber = number;
        this.staticCells = new ArrayList();
    }

    /**
     * Constructor that allows specification of a row decorator.
     *
     * @param rowObject
     *            the row object
     * @param rowNumber
     *            the row number
     * @param decorator
     *            the decorator
     */
    public Row(final Object rowObject, final int rowNumber,
            final RowDecorator decorator) {
        this(rowObject, rowNumber);
        this.decorator = decorator != null ? decorator
                : new DefaultRowDecorator();
    }

    /**
     * Setter for the row number.
     * 
     * @param number
     *            row number
     */
    @Override
    public void setRowNumber(final int number) {
        this.rowNumber = number;
    }

    /**
     * Checks if is odd row.
     *
     * @return true if the current row number is odd
     */
    @Override
    public boolean isOddRow() {
        return this.rowNumber % 2 == 0;
    }

    /**
     * Getter for the row number.
     * 
     * @return row number
     */
    @Override
    public int getRowNumber() {
        return this.rowNumber;
    }

    /**
     * Adds a cell to the row.
     * 
     * @param cell
     *            Cell
     */
    @Override
    public void addCell(final Cell cell) {
        this.staticCells.add(cell);
    }

    /**
     * getter for the list of Cell object.
     * 
     * @return List containing Cell objects
     */
    @Override
    public List getCellList() {
        return this.staticCells;
    }

    /**
     * getter for the object holding values for the current row.
     * 
     * @return Object object holding values for the current row
     */
    @Override
    public Object getObject() {
        return this.rowObject;
    }

    /**
     * Iterates on columns.
     * 
     * @param columns
     *            List
     * @return ColumnIterator
     */
    @Override
    public ColumnIterator getColumnIterator(final List columns) {
        return new ColumnIterator(columns, this);
    }

    /**
     * Setter for the table model the row belongs to.
     * 
     * @param table
     *            TableModel
     */
    @Override
    protected void setParentTable(final TableModel table) {
        this.tableModel = table;
    }

    /**
     * Getter for the table model the row belongs to.
     * 
     * @return TableModel
     */
    @Override
    protected TableModel getParentTable() {
        return this.tableModel;
    }

    /**
     * Writes the open &lt;tr> tag.
     * 
     * @return String &lt;tr> tag with the appropriate css class attribute
     */
    @Override
    public String getOpenTag() {
        final String css = this.decorator.getCssClasses(this);

        if (StringUtils.isNotEmpty(css)) {
            return TagConstants.TAG_OPEN + TagConstants.TAGNAME_ROW + " "
                    + TagConstants.ATTRIBUTE_CLASS + "=\"" + css + "\""
                    + TagConstants.TAG_CLOSE;
        } else {
            return TagConstants.TAG_OPEN + TagConstants.TAGNAME_ROW
                    + TagConstants.TAG_CLOSE;
        }
    }

    /**
     * Gets the table model.
     *
     * @return the table model
     */
    public TableModel getTableModel() {
        return this.tableModel;
    }

    /**
     * The Class DefaultRowDecorator.
     */
    private static class DefaultRowDecorator implements RowDecorator {
        
        /* (non-Javadoc)
         * @see com.technoetic.xplanner.tags.displaytag.RowDecorator#getCssClasses(com.technoetic.xplanner.tags.displaytag.Row)
         */
        @Override
        public String getCssClasses(final Row row) {
            return row.getTableModel().getProperties()
                    .getCssRow(row.getRowNumber());
        }
    }

    /**
     * writes the &lt;/tr> tag.
     * 
     * @return String &lt;/tr> tag
     */
    @Override
    public String getCloseTag() {
        return TagConstants.TAG_TR_CLOSE;
    }

    /**
     * To string.
     *
     * @return the string
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("rowNumber", this.rowNumber)
                .append("rowObject", this.rowObject).toString();
    }
}