package com.technoetic.xplanner.db;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.util.MessageResources;
import org.hibernate.Hibernate;
import org.hibernate.type.Type;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

/**
 * The Class IterationStatisticsQuery.
 */
public class IterationStatisticsQuery {
    
    /** The log. */
    private final Logger log = Logger.getLogger(this.getClass());

    /** The query. */
    private String query;
    
    /** The iteration id. */
    private int iterationId = -1;
    
    /** The tasks. */
    private Collection tasks = null;
    
    /** The iteration. */
    private Iteration iteration = null;

    /** The task type count. */
    private Hashtable taskTypeCount = null;
    
    /** The task disposition count. */
    private Hashtable taskDispositionCount = null;
    
    /** The task type estimated hours. */
    private Hashtable taskTypeEstimatedHours = null;
    
    /** The task disposition estimated hours. */
    private Hashtable taskDispositionEstimatedHours = null;
    
    /** The task type actual hours. */
    private Hashtable taskTypeActualHours = null;
    
    /** The task disposition actual hours. */
    private Hashtable taskDispositionActualHours = null;

    /** The locale. */
    private Locale locale = null;
    
    /** The resources. */
    private MessageResources resources = null;

    /**
     * Clears any data that this class may be caching.
     */
    private void clearCache() {
        this.tasks = null;
        this.iteration = null;

        this.taskTypeCount = null;
        this.taskDispositionCount = null;
        this.taskTypeEstimatedHours = null;
        this.taskDispositionEstimatedHours = null;
        this.taskTypeActualHours = null;
        this.taskDispositionActualHours = null;
    }

    /**
     * Used to set the HTTP request object. This may be required by other
     * classes to access resource strings.
     * 
     * @param request
     *            the HTTP request object.
     */
    public void setRequest(final HttpServletRequest request) {
        try {
            this.locale = (Locale) request.getSession().getAttribute(
                    Globals.LOCALE_KEY);
        } catch (final IllegalStateException e) { // Invalidated session
            this.locale = null;
        }
        if (this.locale == null) {
            this.locale = Locale.getDefault();
        }

        this.resources = (MessageResources) request
                .getAttribute(Globals.MESSAGES_KEY);
    }

    /**
     * Returns the string, stored using the specified key from the resource
     * bundle. Note, this method should only be invoked after
     * <code>setRequest()</code> has been used to specify the HTTP request
     * object.
     * 
     * @param key
     *            used to idenfity the string to be returned
     * @return the string loaded from the resource bundle (in the request
     *         locale).
     */
    public String getResourceString(final String key) {
        return this.resources.getMessage(this.locale, key);
    }

    /**
     * Specifies the iteration for which data should be gathered.
     * 
     * @param iterationId
     *            the iteration for which data is to be gathered.
     */
    public synchronized void setIterationId(final int iterationId) {
        this.clearCache();
        this.iterationId = iterationId;
    }

    /**
     * Returns the iteration for this class is gathering data.
     * 
     * @return the iteration id for which data is being gathered.
     */
    public synchronized int getIterationId() {
        return this.iterationId;
    }

    /**
     * Returns the set of tasks which make up the iteration. Note, this method
     * should only be invoked after <code>setIterationId(int)</code> has been
     * used to specify the iteration and
     * <code>setHibernateSession(Session)</code> has been used to set the
     * database session.
     * 
     * @return all the tasks which make up the iteration.
     */
    public synchronized java.util.Collection getIterationTasks() {
        if (this.tasks == null) {
            try {
                try {
                    if (this.query == null) {
                        // DEBT: externalize the query
                        this.query = "select distinct task "
                                + " from task in class net.sf.xplanner.domain.Task, "
                                + " iteration in class net.sf.xplanner.domain.Iteration, "
                                + " story in class net.sf.xplanner.domain.UserStory "
                                + " where "
                                + " task.userStory.id = story.id and story.iteration.id = iteration.id and"
                                + " iteration.id = ?";
                    }
                    this.tasks = ThreadSession.get().find(this.query,
                            new Object[] { new Integer(this.iterationId) },
                            new Type[] { Hibernate.INTEGER });
                } catch (final Exception ex) {
                    this.log.error("query error", ex);
                }
            } catch (final Exception ex) {
                this.log.error("error in iteration query", ex);
            }
        }

        return this.tasks;
    }

    /**
     * Returns information about the iteration for which data is being gathered.
     * Note, this method should only be invoked after
     * <code>setIterationId(int)</code> has been used to specify the iteration
     * and <code>setHibernateSession(Session)</code> has been used to set the
     * database session.
     * 
     * @return information about the iteration.
     */
    public synchronized Iteration getIteration() {
        if (this.iteration == null) {
            try {
                this.iteration = (Iteration) ThreadSession.get().load(
                        Iteration.class, new Integer(this.iterationId));
            } catch (final Exception ex) {
                this.log.error("error loading iteration [" + this.iterationId
                        + "]", ex);
            }
        }
        return this.iteration;
    }

    /**
     * Gets the task count by type.
     *
     * @return the task count by type
     */
    public Hashtable getTaskCountByType() {
        if (this.taskTypeCount == null) {
            this.taskTypeCount = new TypeAggregator() {
                @Override
                protected double getValue(final Task task) {
                    return 1;
                }
            }.aggregateByGroup();
        }
        return this.taskTypeCount;
    }

    /**
     * Gets the task count by disposition.
     *
     * @return the task count by disposition
     */
    public Hashtable getTaskCountByDisposition() {
        if (this.taskDispositionCount == null) {
            this.taskDispositionCount = new DispositionAggregator() {
                @Override
                protected double getValue(final Task task) {
                    return 1;
                }
            }.aggregateByGroup();
        }
        return this.taskDispositionCount;
    }

    /**
     * Gets the task estimated hours by type.
     *
     * @return the task estimated hours by type
     */
    public Hashtable getTaskEstimatedHoursByType() {
        if (this.taskTypeEstimatedHours == null) {
            this.taskTypeEstimatedHours = new TypeAggregator(true) {
                @Override
                protected double getValue(final Task task) {
                    return task.getEstimatedHours();
                }
            }.aggregateByGroup();
        }
        return this.taskTypeEstimatedHours;
    }

    /**
     * Gets the task estimated hours by disposition.
     *
     * @return the task estimated hours by disposition
     */
    public Hashtable getTaskEstimatedHoursByDisposition() {
        if (this.taskDispositionEstimatedHours == null) {
            this.taskDispositionEstimatedHours = new DispositionAggregator(true) {
                @Override
                protected double getValue(final Task task) {
                    return task.getEstimatedHours();
                }
            }.aggregateByGroup();
        }
        return this.taskDispositionEstimatedHours;
    }

    /**
     * Gets the task actual hours by type.
     *
     * @return the task actual hours by type
     */
    public Hashtable getTaskActualHoursByType() {
        if (this.taskTypeActualHours == null) {
            this.taskTypeActualHours = new TypeAggregator(true) {
                @Override
                protected double getValue(final Task task) {
                    return task.getActualHours();
                }
            }.aggregateByGroup();
        }
        return this.taskTypeActualHours;
    }

    /**
     * Gets the task actual hours by disposition.
     *
     * @return the task actual hours by disposition
     */
    public Hashtable getTaskActualHoursByDisposition() {
        if (this.taskDispositionActualHours == null) {
            this.taskDispositionActualHours = new DispositionAggregator(true) {
                @Override
                protected double getValue(final Task task) {
                    return task.getActualHours();
                }
            }.aggregateByGroup();
        }
        return this.taskDispositionActualHours;
    }

    /**
     * The Class Aggregator.
     */
    abstract class Aggregator {
        
        /** The only completed task. */
        protected boolean onlyCompletedTask;

        /**
         * Instantiates a new aggregator.
         */
        public Aggregator() {
        }

        /**
         * Instantiates a new aggregator.
         *
         * @param onlyCompletedTask
         *            the only completed task
         */
        public Aggregator(final boolean onlyCompletedTask) {
            this.onlyCompletedTask = onlyCompletedTask;
        }

        /**
         * Aggregate by group.
         *
         * @return the hashtable
         */
        public Hashtable aggregateByGroup() {
            final Hashtable valuesByGroup = new Hashtable();

            final Iterator taskItr = IterationStatisticsQuery.this
                    .getIterationTasks().iterator();

            while (taskItr.hasNext()) {
                final Task task = (Task) taskItr.next();

                if (!this.apply(task)) {
                    continue;
                }

                Double sum = (Double) valuesByGroup.get(this.getGroup(task));
                if (sum == null) {
                    sum = new Double(0);
                }

                final Double newSum = new Double(sum.intValue()
                        + this.getValue(task));
                valuesByGroup.put(this.getGroup(task), newSum);
            }
            return valuesByGroup;
        }

        /**
         * Apply.
         *
         * @param task
         *            the task
         * @return true, if successful
         */
        protected boolean apply(final Task task) {
            return !this.onlyCompletedTask || task.isCompleted();
        }

        /**
         * Gets the value.
         *
         * @param task
         *            the task
         * @return the value
         */
        abstract protected double getValue(Task task);

        /**
         * Gets the group.
         *
         * @param task
         *            the task
         * @return the group
         */
        abstract protected String getGroup(Task task);
    }

    /**
     * The Class TypeAggregator.
     */
    private abstract class TypeAggregator extends Aggregator {

        /**
         * Instantiates a new type aggregator.
         */
        public TypeAggregator() {
        }

        /**
         * Instantiates a new type aggregator.
         *
         * @param onlyCompletedTask
         *            the only completed task
         */
        public TypeAggregator(final boolean onlyCompletedTask) {
            super(onlyCompletedTask);
        }

        /* (non-Javadoc)
         * @see com.technoetic.xplanner.db.IterationStatisticsQuery.Aggregator#getGroup(net.sf.xplanner.domain.Task)
         */
        @Override
        protected String getGroup(final Task task) {
            return task.getType();
        }
    }

    /**
     * The Class DispositionAggregator.
     */
    private abstract class DispositionAggregator extends Aggregator {

        /**
         * Instantiates a new disposition aggregator.
         */
        public DispositionAggregator() {
        }

        /**
         * Instantiates a new disposition aggregator.
         *
         * @param onlyCompletedTask
         *            the only completed task
         */
        public DispositionAggregator(final boolean onlyCompletedTask) {
            super(onlyCompletedTask);
        }

        /* (non-Javadoc)
         * @see com.technoetic.xplanner.db.IterationStatisticsQuery.Aggregator#getGroup(net.sf.xplanner.domain.Task)
         */
        @Override
        protected String getGroup(final Task task) {
            return IterationStatisticsQuery.this.getResourceString(task
                    .getDispositionNameKey());
        }
    }
}
