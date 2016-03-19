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

/**
 * The Class ViewIterationMetricsAction.
 */
public class ViewIterationMetricsAction extends ViewObjectAction<Iteration> {
    
    /** The iteration metrics. */
    private IterationMetrics iterationMetrics;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.ViewObjectAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
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

    /**
     * Sets the iteration metrics.
     *
     * @param iterationMetrics
     *            the new iteration metrics
     */
    public void setIterationMetrics(final IterationMetrics iterationMetrics) {
        this.iterationMetrics = iterationMetrics;
    }

    /**
     * Gets the repository.
     *
     * @param actionMapping
     *            the action mapping
     * @param request
     *            the request
     * @return the repository
     * @throws ClassNotFoundException
     *             the class not found exception
     * @throws ServletException
     *             the servlet exception
     */
    protected ObjectRepository getRepository(final ActionMapping actionMapping,
            final HttpServletRequest request) throws ClassNotFoundException,
            ServletException {
        this.getObjectType(actionMapping, request);
        final ObjectRepository objectRepository = null;// getRepository(objectClass);
        return objectRepository;
    }

}
