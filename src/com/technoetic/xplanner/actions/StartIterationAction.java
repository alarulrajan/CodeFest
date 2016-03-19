package com.technoetic.xplanner.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.charts.DataSampler;
import com.technoetic.xplanner.domain.repository.RepositoryException;
import com.technoetic.xplanner.forms.IterationStatusEditorForm;
import com.technoetic.xplanner.forms.TimeEditorForm;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.util.RequestUtils;
import com.technoetic.xplanner.util.TimeGenerator;

/**
 * The Class StartIterationAction.
 */
public class StartIterationAction extends AbstractIterationAction<Iteration> {
	
	/** The time generator. */
	private TimeGenerator timeGenerator;

	/**
     * Sets the time generator.
     *
     * @param timeGenerator
     *            the new time generator
     */
	public void setTimeGenerator(final TimeGenerator timeGenerator) {
		this.timeGenerator = timeGenerator;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractAction#beforeObjectCommit(com.technoetic.xplanner.domain.Identifiable, org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public void beforeObjectCommit(final Iteration object,
			final ActionMapping actionMapping, final ActionForm actionForm,
			final HttpServletRequest request, final HttpServletResponse reply)
			throws Exception {
		final Iteration iteration = object;
		iteration.start();
		this.dataSampler.generateOpeningDataSamples(iteration);
		this.historySupport.saveEvent(iteration, History.ITERATION_STARTED,
				null, SecurityHelper.getRemoteUserId(request),
				this.timeGenerator.getCurrentTime());
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward doExecute(final ActionMapping mapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final IterationStatusEditorForm form = (IterationStatusEditorForm) actionForm;
		Iteration iteration;

		ActionForward nextPage = mapping.getInputForward();
		if (!form.isIterationStartConfirmed()) {
			if (UpdateTimeAction.isFromUpdateTime(request)) {
				// DEBT Extract constants for all these strings. from_edit/time
				// -> UpdateTimeAction, edit/iteration -> this class
				iteration = this.getCommonDao().getById(
						Iteration.class,
						((Iteration) request.getAttribute("edit/iteration"))
								.getId());
			} else {
				final String iterationId = form.getOid();
				iteration = this.getCommonDao().getById(Iteration.class,
						Integer.parseInt(iterationId));
			}
			if (iteration != null) {
				request.setAttribute("edit/iteration", iteration);
				final Project project = iteration.getProject();
				final Collection<Iteration> startedIterationsList = this
						.getStartedIterations(project);
				final int startedIterationsCount = startedIterationsList.size();
				request.setAttribute("startedIterationsNr", new Integer(
						startedIterationsCount));
				if (startedIterationsCount > 0) {
					// DEBT These should not be strings but constant used both
					// in java code and jsp.
					nextPage = mapping.findForward("start/iteration");
				} else if (!UpdateTimeAction.isFromUpdateTime(request)) {
					iteration.close();
					this.getCommonDao().save(iteration);
					this.setTargetObject(request, iteration);
					final String returnto = request
							.getParameter(EditObjectAction.RETURNTO_PARAM);
					nextPage = returnto != null ? new ActionForward(returnto,
							true) : mapping.findForward("view/projects");
				}
			}
		} else {
			final String iterationId = form.getOid();
			iteration = this.getCommonDao().getById(Iteration.class,
					Integer.parseInt(iterationId));
			if (form.isCloseIterations()) {
				final Project project = iteration.getProject();
				this.closeStartedIterations(project);
			}
			iteration.close();
			this.getCommonDao().save(iteration);

			this.setTargetObject(request, iteration);

			// DEBT: Use the normal returnto mechanism to send the control back
			// to edit/time w/o embedding knowledge of it inside this action
			if (RequestUtils.isParameterTrue(request,
					IterationStatusEditorForm.SAVE_TIME_ATTR)) {
				request.setAttribute(TimeEditorForm.WIZARD_MODE_ATTR,
						Boolean.TRUE);
				nextPage = mapping.findForward("edit/time");
			} else {
				final String returnto = request
						.getParameter(EditObjectAction.RETURNTO_PARAM);
				nextPage = returnto != null ? new ActionForward(returnto, true)
						: mapping.findForward("view/projects");
			}
		}
		return nextPage;
	}

	/**
     * Close started iterations.
     *
     * @param project
     *            the project
     * @throws RepositoryException
     *             the repository exception
     */
	private void closeStartedIterations(final Project project)
			throws RepositoryException {
		final Collection<Iteration> startedIterationsList = this
				.getStartedIterations(project);
		final Iterator<Iteration> iterator = startedIterationsList.iterator();
		while (iterator.hasNext()) {
			final Iteration iteration = iterator.next();
			iteration.close();
			this.getCommonDao().save(iteration);
		}
	}

	/**
     * Gets the started iterations.
     *
     * @param project
     *            the project
     * @return the started iterations
     */
	private Collection<Iteration> getStartedIterations(final Project project) {
		final Collection<Iteration> startedIterationList = new ArrayList<Iteration>();
		final Collection<Iteration> iterationList = project.getIterations();
		final Iterator<Iteration> iterator = iterationList.iterator();
		while (iterator.hasNext()) {
			final Iteration iteration = iterator.next();
			if (iteration.isActive()) {
				startedIterationList.add(iteration);
			}
		}
		return startedIterationList;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractIterationAction#setDataSampler(com.technoetic.xplanner.charts.DataSampler)
	 */
	@Override
	public void setDataSampler(final DataSampler dataSampler) {
		this.dataSampler = dataSampler;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractIterationAction#getDataSampler()
	 */
	@Override
	public DataSampler getDataSampler() {
		return this.dataSampler;
	}
}