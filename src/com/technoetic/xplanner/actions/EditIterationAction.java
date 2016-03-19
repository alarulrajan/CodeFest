package com.technoetic.xplanner.actions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.charts.DataSampler;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.forms.IterationEditorForm;

/**
 * The Class EditIterationAction.
 */
public class EditIterationAction extends EditObjectAction<Iteration> {
    
    /** The Constant ACTION_KEY. */
    public static final String ACTION_KEY = "action";
    
    /** The data sampler. */
    private DataSampler dataSampler;

    /**
     * Gets the data sampler.
     *
     * @return the data sampler
     */
    public DataSampler getDataSampler() {
        return this.dataSampler;
    }

    /**
     * Sets the data sampler.
     *
     * @param dataSampler
     *            the new data sampler
     */
    public void setDataSampler(final DataSampler dataSampler) {
        this.dataSampler = dataSampler;
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
        final String action = request
                .getParameter(EditIterationAction.ACTION_KEY);
        try {
            if (StringUtils.equals(action, EditObjectAction.CREATE_ACTION)) {
                final IterationEditorForm iterationEditorForm = (IterationEditorForm) actionForm;
                final Project project = this.getCommonDao().getById(
                        Project.class, iterationEditorForm.getProjectId());
                iteration.setProject(project);
                iteration.setIterationStatus(IterationStatus.INACTIVE);
                iteration.setDaysWorked(0.0d);
            }
        } catch (final Exception e) {
            throw new ServletException(e);
        }
    }

}