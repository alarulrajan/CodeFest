package com.technoetic.xplanner.metrics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.security.AuthenticationException;

/**
 * The Class IterationMetrics.
 */
public class IterationMetrics {
    
    /** The Constant log. */
    private static final Logger log = Logger.getLogger(IterationMetrics.class);
    
    /** The developer metrics. */
    protected Map developerMetrics = new HashMap();
    
    /** The total hours. */
    private double totalHours;
    
    /** The total paired hours. */
    private double totalPairedHours;
    
    /** The iteration id. */
    private int iterationId;
    
    /** The Constant ITERATION_NAME. */
    private static final String ITERATION_NAME = null;
    
    /** The max developer hours. */
    private double maxDeveloperHours;
    
    /** The names. */
    protected Map names = new HashMap();
    
    /** The Constant UNASSIGNED_ID. */
    public static final int UNASSIGNED_ID = 0;
    
    /** The Constant UNASSIGNED_NAME. */
    public static final String UNASSIGNED_NAME = "Unassigned";
    
    /** The session. */
    private Session session;
    
    /** The iteration repository. */
    private ObjectRepository iterationRepository;

    /**
     * Sets the iteration id.
     *
     * @param iterationId
     *            the new iteration id
     */
    public void setIterationId(final int iterationId) {
        this.iterationId = iterationId;
    }

    /**
     * Analyze.
     */
    public void analyze() {
        // DEBT Spring load
        this.session = ThreadSession.get();
        try {
            try {
                this.names.clear();
                this.developerMetrics.clear();
                this.getNamesMap(this.session);
                this.calculateDeveloperMetrics();
                this.getHoursWorked(this.session, "iterationHoursWorkedQuery",
                        this.names);
            } catch (final Exception ex) {
                if (this.session.isConnected()) {
                    this.session.connection().rollback();
                }
                this.log.error("error", ex);
            }
        } catch (final Exception ex) {
            this.log.error("error", ex);
        }
    }

    /**
     * Calculate developer metrics.
     *
     * @throws HibernateException
     *             the hibernate exception
     * @throws AuthenticationException
     *             the authentication exception
     * @throws RepositoryException
     *             the repository exception
     */
    public void calculateDeveloperMetrics() throws HibernateException,
            AuthenticationException, RepositoryException {
        final Iteration iteration = this.getIterationObject();
        final Collection stories = iteration.getUserStories();
        this.getMetricsData(stories);
    }

    /**
     * Gets the iteration object.
     *
     * @return the iteration object
     * @throws HibernateException
     *             the hibernate exception
     * @throws AuthenticationException
     *             the authentication exception
     * @throws RepositoryException
     *             the repository exception
     */
    protected Iteration getIterationObject() throws HibernateException,
            AuthenticationException, RepositoryException {
        final ObjectRepository repository = this.getIterationRepository();
        return (Iteration) repository.load(this.getIterationId());
    }

    /**
     * Gets the metrics data.
     *
     * @param stories
     *            the stories
     * @return the metrics data
     */
    protected Map getMetricsData(final Collection stories) {
        for (final Iterator iterator = stories.iterator(); iterator.hasNext();) {
            final UserStory story = (UserStory) iterator.next();
            final Collection tasks = story.getTasks();
            if (tasks.isEmpty()) {
                this.assignHoursToUser(story.getTrackerId(),
                        story.getEstimatedHours(), true);
            } else {
                for (final Iterator iterator1 = tasks.iterator(); iterator1
                        .hasNext();) {
                    final Task task = (Task) iterator1.next();
                    this.assignHoursToUser(task.getAcceptorId(),
                            task.getEstimatedHours(), false);
                }
            }
        }
        return this.developerMetrics;
    }

    /**
     * Assign hours to user.
     *
     * @param personId
     *            the person id
     * @param estimatedHours
     *            the estimated hours
     * @param isStoryHour
     *            the is story hour
     */
    protected void assignHoursToUser(final int personId,
            final double estimatedHours, final boolean isStoryHour) {
        if (estimatedHours == 0) {
            return;
        }
        final DeveloperMetrics developerMetrics = this.getDeveloperMetrics(
                this.getName(personId), personId, this.iterationId);
        if (isStoryHour) {
            final double prevAcceptedStoryHours = developerMetrics
                    .getAcceptedStoryHours();
            developerMetrics.setAcceptedStoryHours(prevAcceptedStoryHours
                    + estimatedHours);
        } else {
            final double prevAcceptedTaskHours = developerMetrics
                    .getAcceptedTaskHours();
            developerMetrics.setAcceptedTaskHours(prevAcceptedTaskHours
                    + estimatedHours);
        }
    }

    /**
     * Gets the names map.
     *
     * @param session
     *            the session
     * @return the names map
     * @throws HibernateException
     *             the hibernate exception
     */
    protected void getNamesMap(final Session session) throws HibernateException {
        final List nameResults = session.getNamedQuery("namesQuery").list();
        final Iterator iter = nameResults.iterator();
        while (iter.hasNext()) {
            final Object[] result = (Object[]) iter.next();
            this.names.put(result[1], result[0]);
        }
        this.addUnassignedName();
    }

    /**
     * Adds the unassigned name.
     */
    protected void addUnassignedName() {
        this.names.put(new Integer(IterationMetrics.UNASSIGNED_ID),
                IterationMetrics.UNASSIGNED_NAME);
    }

    /**
     * Gets the name.
     *
     * @param personId
     *            the person id
     * @return the name
     */
    protected String getName(final int personId) {
        return (String) this.names.get(new Integer(personId));
    }

    /**
     * Gets the hours worked.
     *
     * @param session
     *            the session
     * @param hoursQuery
     *            the hours query
     * @param names
     *            the names
     * @return the hours worked
     * @throws HibernateException
     *             the hibernate exception
     */
    void getHoursWorked(final Session session, final String hoursQuery,
            final Map names) throws HibernateException {
        this.totalHours = 0.0;
        this.totalPairedHours = 0.0;
        this.maxDeveloperHours = 0.0;
        final List hoursResults = session.getNamedQuery(hoursQuery)
                .setInteger("iterationId", this.iterationId).list();
        final Iterator hoursIterator = hoursResults.iterator();
        while (hoursIterator.hasNext()) {
            final Object[] result = (Object[]) hoursIterator.next();
            final int person1Id = this.toInt(result[0]);
            final int person2Id = this.toInt(result[1]);
            final Date startTime = (Date) result[2];
            final Date endTime = (Date) result[3];
            final double duration = this.toDouble(result[4]);
            final int trackerId = this.toInt(result[5]);
            if (endTime != null && startTime != null || duration != 0) {
                final double hours = duration == 0 ? (endTime.getTime() - startTime
                        .getTime()) / 3600000.0 : duration;
                final boolean isPaired = person1Id != 0 && person2Id != 0;
                if (person1Id != 0) {
                    final boolean isOwnTask = person1Id == trackerId;
                    this.updateWorkedHours(this.iterationId,
                            (String) names.get(result[0]), person1Id, hours,
                            isPaired, isOwnTask);
                    this.totalHours += hours;
                }
                if (person2Id != 0) {
                    final boolean isOwnTask = person2Id == trackerId;
                    this.updateWorkedHours(this.iterationId,
                            (String) names.get(result[1]), person2Id, hours,
                            isPaired, isOwnTask);
                    this.totalHours += hours;
                }
                if (isPaired) {
                    this.totalPairedHours += hours;
                }
            }
        }
    }

    /**
     * To int.
     *
     * @param object
     *            the object
     * @return the int
     */
    private int toInt(final Object object) {
        return ((Integer) object).intValue();
    }

    /**
     * To double.
     *
     * @param object
     *            the object
     * @return the double
     */
    private double toDouble(final Object object) {
        return object != null ? ((Double) object).doubleValue() : 0;
    }

    /**
     * Gets the developer metrics.
     *
     * @param name
     *            the name
     * @param id
     *            the id
     * @param iterationId
     *            the iteration id
     * @return the developer metrics
     */
    protected DeveloperMetrics getDeveloperMetrics(final String name,
            final int id, final int iterationId) {
        DeveloperMetrics dm = (DeveloperMetrics) this.developerMetrics
                .get(name);
        if (dm == null) {
            dm = new DeveloperMetrics();
            dm.setId(id);
            dm.setName(name);
            dm.setIterationId(iterationId);
            this.developerMetrics.put(name, dm);
        }
        return dm;
    }

    /**
     * Update worked hours.
     *
     * @param iterationId
     *            the iteration id
     * @param name
     *            the name
     * @param id
     *            the id
     * @param hours
     *            the hours
     * @param isPaired
     *            the is paired
     * @param isOwnTask
     *            the is own task
     */
    private void updateWorkedHours(final int iterationId, final String name,
            final int id, final double hours, final boolean isPaired,
            final boolean isOwnTask) {
        final DeveloperMetrics dm = this.getDeveloperMetrics(name, id,
                iterationId);
        dm.setHours(dm.getHours() + hours);
        if (isOwnTask) {
            dm.setOwnTasksWorkedHours(dm.getOwnTaskHours() + hours);
        }
        if (dm.getHours() > this.maxDeveloperHours) {
            this.maxDeveloperHours = dm.getHours();
        }
        if (isPaired) {
            dm.setPairedHours(dm.getPairedHours() + hours);
        }
    }

    /**
     * Gets the total hours.
     *
     * @return the total hours
     */
    public double getTotalHours() {
        return this.totalHours;
    }

    /**
     * Gets the total paired hours.
     *
     * @return the total paired hours
     */
    public double getTotalPairedHours() {
        return this.totalPairedHours;
    }

    /**
     * Gets the total paired percentage.
     *
     * @return the total paired percentage
     */
    public double getTotalPairedPercentage() {
        double result = this.totalPairedHours * 100
                / (this.totalHours - this.totalPairedHours);
        if (Double.isNaN(result)) {
            result = 0.0;
        }
        return result;
    }

    /**
     * Sets the total hours.
     *
     * @param totalHours
     *            the new total hours
     */
    public void setTotalHours(final double totalHours) {
        this.totalHours = totalHours;
    }

    /**
     * Sets the total paired hours.
     *
     * @param totalPairedHours
     *            the new total paired hours
     */
    public void setTotalPairedHours(final double totalPairedHours) {
        this.totalPairedHours = totalPairedHours;
    }

    /**
     * Gets the developer total time.
     *
     * @return the developer total time
     */
    public Collection getDeveloperTotalTime() {
        final ArrayList metrics = new ArrayList(this.developerMetrics.values());
        Collections.sort(metrics, new Comparator() {
            @Override
            public int compare(final Object object1, final Object object2) {
                final DeveloperMetrics dm1 = (DeveloperMetrics) object1;
                final DeveloperMetrics dm2 = (DeveloperMetrics) object2;
                return dm1.getHours() < dm2.getHours() ? 1
                        : dm1.getHours() == dm2.getHours() ? 0 : -1;
            }
        });
        return metrics;
    }

    /**
     * Gets the developer own tasks worked time.
     *
     * @return the developer own tasks worked time
     */
    public Collection getDeveloperOwnTasksWorkedTime() {
        final ArrayList metrics = new ArrayList(this.developerMetrics.values());
        Collections.sort(metrics, new Comparator() {
            @Override
            public int compare(final Object object1, final Object object2) {
                final DeveloperMetrics dm1 = (DeveloperMetrics) object1;
                final DeveloperMetrics dm2 = (DeveloperMetrics) object2;
                return dm1.getOwnTaskHours() < dm2.getOwnTaskHours() ? 1 : dm1
                        .getOwnTaskHours() == dm2.getOwnTaskHours() ? 0 : -1;
            }
        });
        return metrics;
    }

    /**
     * Gets the max total time.
     *
     * @return the max total time
     */
    public double getMaxTotalTime() {
        double maxTotalTime = 0;
        final Iterator itr = this.developerMetrics.values().iterator();
        while (itr.hasNext()) {
            final double hours = ((DeveloperMetrics) itr.next()).getHours();
            if (hours > maxTotalTime) {
                maxTotalTime = hours;
            }
        }
        return maxTotalTime;
    }

    /**
     * Gets the developer accepted time.
     *
     * @return the developer accepted time
     */
    public Collection getDeveloperAcceptedTime() {
        final ArrayList metrics = new ArrayList(this.developerMetrics.values());
        Collections.sort(metrics, new Comparator() {
            @Override
            public int compare(final Object object1, final Object object2) {
                final DeveloperMetrics dm1 = (DeveloperMetrics) object1;
                final DeveloperMetrics dm2 = (DeveloperMetrics) object2;
                return dm1.getAcceptedHours() < dm2.getAcceptedHours() ? 1
                        : dm1.getAcceptedHours() == dm2.getAcceptedHours() ? 0
                                : -1;
            }
        });
        return metrics;
    }

    /**
     * Gets the max accepted time.
     *
     * @return the max accepted time
     */
    public double getMaxAcceptedTime() {
        double maxAcceptedTime = 0;
        final Iterator itr = this.developerMetrics.values().iterator();
        while (itr.hasNext()) {
            final double hours = ((DeveloperMetrics) itr.next())
                    .getAcceptedHours();
            if (hours > maxAcceptedTime) {
                maxAcceptedTime = hours;
            }
        }
        return maxAcceptedTime;
    }

    /**
     * Gets the iteration id.
     *
     * @return the iteration id
     */
    public int getIterationId() {
        return this.iterationId;
    }

    /**
     * Gets the iteration name.
     *
     * @return the iteration name
     */
    public String getIterationName() {
        return this.ITERATION_NAME;
    }

    /**
     * Analyze.
     *
     * @param iterationId
     *            the iteration id
     */
    public void analyze(final int iterationId) {
        this.setIterationId(iterationId);
        this.analyze();
    }

    /**
     * Gets the iteration repository.
     *
     * @return the iteration repository
     */
    private ObjectRepository getIterationRepository() {
        return this.iterationRepository;
    }

    /**
     * Sets the iteration repository.
     *
     * @param iterationRepository
     *            the new iteration repository
     */
    public void setIterationRepository(
            final ObjectRepository iterationRepository) {
        this.iterationRepository = iterationRepository;
    }
}
