package com.technoetic.xplanner.tags;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

/**
 * The Class IterationModel.
 */
public class IterationModel {
    
    /** The iteration. */
    private final Iteration iteration;

    /**
     * Instantiates a new iteration model.
     *
     * @param iteration
     *            the iteration
     */
    public IterationModel(final Iteration iteration) {
        this.iteration = iteration;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.getProject().getName() + " :: " + this.iteration.getName();
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return this.iteration.getId();
    }

    /**
     * Gets the project.
     *
     * @return the project
     */
    protected Project getProject() {
        try {
            return (Project) ThreadSession.get().load(Project.class,
                    new Integer(this.iteration.getProject().getId()));
        } catch (final HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IterationModel)) {
            return false;
        }

        final IterationModel option = (IterationModel) o;

        if (!this.iteration.equals(option.iteration)) {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.iteration.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Option{" + "iteration=" + this.iteration + "}";
    }
}
