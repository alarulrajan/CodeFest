/*
 * @(#)IterationDataSource.java
 *
 * Copyright (c) 2003 Sabre Inc. All rights reserved.
 */

package com.technoetic.xplanner.export;

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

public class IterationDataSource implements JRDataSource {
	private Iterator iterator = null;
	private Task task = null;
	private UserStory story = null;
	private String acceptor = null;
	private Session session = null;

	public IterationDataSource(final Iteration iteration, final Session session)
			throws HibernateException {

		final List data = session
				.find("select story, task"
						+ " from "
						+ UserStory.class.getName()
						+ " story"
						+ " left join story.tasks as task"
						+ " where story.iteration.id = ? order by story.priority, story.name, task.name",
						new Integer(iteration.getId()), Hibernate.INTEGER);
		this.session = session;
		if (data != null) {
			this.iterator = data.iterator();
		}
	}

	@Override
	public boolean next() throws JRException {
		if (this.iterator == null || !this.iterator.hasNext()) {
			return false;
		}

		final Object[] result = (Object[]) this.iterator.next();

		this.story = (UserStory) result[0];
		this.task = (Task) result[1];
		if (this.task != null) {
			this.acceptor = PdfReportExporter.getPersonName(this.session,
					new Integer(this.task.getAcceptorId()));
		}
		return true;
	}

	@Override
	public Object getFieldValue(final JRField field) throws JRException {
		return this.getFieldValue(field.getName());
	}

	public Object getFieldValue(final String fieldName) throws JRException {
		if ("StoryName".equals(fieldName)) {
			return this.story.getName();
		}

		if ("StoryCustomerName".equals(fieldName)) {
			final Person cust = this.story.getCustomer();
			return cust != null ? cust.getName() : "<no customer>";
		}

		if ("StoryEstimatedHours".equals(fieldName)) {
			return new Double(this.story.getEstimatedHours());
		}

		if ("TaskName".equals(fieldName)) {
			if (this.task == null) {
				return "No Tasks Defined";
			}
			return this.task.getName();
		}

		if ("TaskPercentage".equals(fieldName)) {
			if (this.task == null) {
				return new Integer(0);
			}
			final double actual = this.task.getActualHours();
			return new Integer(
					(int) (actual * 100 / (actual + this.task
							.getRemainingHours())));
		}

		if ("TaskDisposition".equals(fieldName)) {
			if (this.task == null) {
				return "";
			}
			return this.task.getDispositionName();
		}

		if ("TaskType".equals(fieldName)) {
			if (this.task == null) {
				return "";
			}
			return this.task.getType();
		}

		if ("TaskEstimate".equals(fieldName)) {
			if (this.task == null) {
				return new Double(0.0);
			}
			return new Double(this.task.getEstimatedHours());
		}

		if ("TaskCompleted".equals(fieldName)) {
			if (this.task == null) {
				return new Boolean(false);
			}
			return new Boolean(this.task.isCompleted());
		}

		if ("TaskAcceptor".equals(fieldName)) {
			if (this.task == null) {
				return null;
			}
			return this.acceptor;
		}

		throw new JRException("Unexpected field name '" + fieldName + "'");
	}
}
