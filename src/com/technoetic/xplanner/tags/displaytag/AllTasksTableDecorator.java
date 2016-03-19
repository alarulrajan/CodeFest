package com.technoetic.xplanner.tags.displaytag;

import net.sf.xplanner.domain.Task;

import org.displaytag.decorator.TableDecorator;

/**
 * The Class AllTasksTableDecorator.
 */
public class AllTasksTableDecorator extends TableDecorator {
    
    /**
     * Gets the status order.
     *
     * @return the status order
     */
    public TaskOrdering getStatusOrder() {
        return new TaskOrdering((Task) this.getCurrentRowObject());
    }
}
