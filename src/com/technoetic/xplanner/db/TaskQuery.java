package com.technoetic.xplanner.db;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * User: Mateusz Prokopowicz Date: Aug 25, 2005 Time: 10:30:10 AM.
 */
public interface TaskQuery {
    
    /**
     * Query.
     *
     * @param cachedTasks
     *            the cached tasks
     * @param personId
     *            the person id
     * @param completed
     *            the completed
     * @param active
     *            the active
     * @return the collection
     */
    Collection query(Collection cachedTasks, int personId, Boolean completed,
            Boolean active);

    /**
     * Query tasks.
     *
     * @param queryName
     *            the query name
     * @param personId
     *            the person id
     * @return the list
     */
    List queryTasks(String queryName, int personId);

    /**
     * Query tasks.
     *
     * @param queryName
     *            the query name
     * @param date
     *            the date
     * @return the list
     */
    List queryTasks(String queryName, Date date);
}
