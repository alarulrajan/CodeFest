package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.forms.IterationStatusEditorForm;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.util.TimeGenerator;

public class CloseIterationAction extends AbstractIterationAction<Iteration> {
	private TimeGenerator timeGenerator;

	public void setTimeGenerator(final TimeGenerator timeGenerator) {
		this.timeGenerator = timeGenerator;
	}

	@Override
	public void beforeObjectCommit(final Iteration iteration,
			final ActionMapping actionMapping, final ActionForm actionForm,
			final HttpServletRequest request, final HttpServletResponse reply)
			throws Exception {
		this.closeIteration(request, iteration);
		final String event = History.ITERATION_CLOSED;
		this.historySupport.saveEvent(iteration, event, null,
				SecurityHelper.getRemoteUserId(request),
				this.timeGenerator.getCurrentTime());
	}

	@Override
	protected ActionForward doExecute(final ActionMapping mapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final IterationStatusEditorForm form = (IterationStatusEditorForm) actionForm;
		if (StringUtils.isEmpty(form.getOid())) {
			return mapping.getInputForward();
		}

		final String iterationId = form.getOid();
		final Iteration iteration = this.getCommonDao().getById(
				Iteration.class, Integer.parseInt(iterationId));
		if (iteration.isActive()) {
			iteration.setIterationStatus(IterationStatus.INACTIVE);
			this.setTargetObject(request, iteration);
		}
		this.getCommonDao().save(iteration);
		final String returnto = request
				.getParameter(EditObjectAction.RETURNTO_PARAM);
		final ActionForward forward = mapping.findForward("onclose");
		// DEBT refactor the creation of the url get parameter to reuse what the
		// LinkTag is doing.
		// DEBT We need to move to encapsulate how the links are built so we
		// eventually "invoke" new screens like functions and not build link by
		// hand
		return new ActionForward(forward.getPath() + "?iterationId="
				+ iterationId + "&" + returnto + "?oid=" + iterationId
				+ "&fkey=" + iterationId, forward.getRedirect());
	}

	public void closeIteration(final HttpServletRequest request,
			final Iteration iteration) throws Exception {
		iteration.setIterationStatus(IterationStatus.INACTIVE);
		this.dataSampler.generateClosingDataSamples(iteration);
	}
}