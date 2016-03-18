package com.technoetic.xplanner.tags;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.db.hibernate.ThreadSession;

public class IterationModel {
	private final Iteration iteration;

	public IterationModel(final Iteration iteration) {
		this.iteration = iteration;
	}

	public String getName() {
		return this.getProject().getName() + " :: " + this.iteration.getName();
	}

	public int getId() {
		return this.iteration.getId();
	}

	protected Project getProject() {
		try {
			return (Project) ThreadSession.get().load(Project.class,
					new Integer(this.iteration.getProject().getId()));
		} catch (final HibernateException e) {
			throw new RuntimeException(e);
		}
	}

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

	@Override
	public int hashCode() {
		return this.iteration.hashCode();
	}

	@Override
	public String toString() {
		return "Option{" + "iteration=" + this.iteration + "}";
	}
}
