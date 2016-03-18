package com.technoetic.xplanner.actions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.domain.repository.ObjectRepository;
import com.technoetic.xplanner.metrics.IterationMetrics;

public class ViewIterationMetricsAction extends ViewObjectAction<Iteration> {
	private IterationMetrics iterationMetrics;

	@Override
	protected ActionForward doExecute(final ActionMapping actionMapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse reply) throws Exception {
		// DEBT(SPRING) Should have been injected directly from the spring
		// context file

		this.iterationMetrics.setIterationRepository(this.getRepository(
				actionMapping, request));
		this.iterationMetrics.analyze(Integer.parseInt(request
				.getParameter("oid")));
		request.setAttribute("metrics", this.iterationMetrics);
		return super.doExecute(actionMapping, form, request, reply);
	}

	public void setIterationMetrics(final IterationMetrics iterationMetrics) {
		this.iterationMetrics = iterationMetrics;
	}

	protected ObjectRepository getRepository(final ActionMapping actionMapping,
			final HttpServletRequest request) throws ClassNotFoundException,
			ServletException {
		this.getObjectType(actionMapping, request);
		final ObjectRepository objectRepository = null;// getRepository(objectClass);
		return objectRepository;
	}

}
