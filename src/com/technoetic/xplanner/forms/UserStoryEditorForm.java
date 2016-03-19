package com.technoetic.xplanner.forms;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Person;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * The Class UserStoryEditorForm.
 */
public class UserStoryEditorForm extends AbstractEditorForm {
    
    /** The name. */
    private String name;
    
    /** The description. */
    private String description;
    
    /** The tracker id. */
    private int trackerId;
    
    /** The iteration id. */
    private int iterationId;
    
    /** The target iteration id. */
    private int targetIterationId;
    
    /** The estimated hours field. */
    private double estimatedHoursField;
    
    /** The task based estimated hours. */
    private double taskBasedEstimatedHours;
    
    /** The priority. */
    private int priority;
    
    /** The order no. */
    private int orderNo;
    
    /** The disposition name. */
    private String dispositionName;
    
    /** The status name. */
    private String statusName;
    
    /** The customer id. */
    private int customerId;
    
    /** The Constant DEFAULT_PRIORITY. */
    /* package */
    static final int DEFAULT_PRIORITY = 4;
    
    /** The Constant DEFAULT_PRIORITY_KEY. */
    static final String DEFAULT_PRIORITY_KEY = "xplanner.story.defaultpriority";
    
    /** The Constant INVALID_PRIORITY_ERROR_KEY. */
    static final String INVALID_PRIORITY_ERROR_KEY = "story.editor.invalid_priority";
    
    /** The Constant NEGATIVE_ESTIMATED_HOURS_ERROR_KEY. */
    static final String NEGATIVE_ESTIMATED_HOURS_ERROR_KEY = "story.editor.negative_estimated_hours";
    
    /** The Constant MISSING_NAME_ERROR_KEY. */
    static final String MISSING_NAME_ERROR_KEY = "story.editor.missing_name";
    
    /** The Constant SAME_ITERATION_ERROR_KEY. */
    static final String SAME_ITERATION_ERROR_KEY = "story.editor.same_iteration";
    
    /** The Constant PRIORITY_PARAM. */
    static final String PRIORITY_PARAM = "priority";

    /**
     * Gets the container id.
     *
     * @return the container id
     */
    public String getContainerId() {
        return Integer.toString(this.getIterationId());
    }

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ActionErrors validate(final ActionMapping mapping,
            final HttpServletRequest request) {
        final ActionErrors errors = new ActionErrors();
        if (this.isSubmitted()) {
            if (!this.isMerge()) {
                AbstractEditorForm.require(errors, this.name,
                        UserStoryEditorForm.MISSING_NAME_ERROR_KEY);
                AbstractEditorForm.require(errors,
                        this.estimatedHoursField >= 0,
                        UserStoryEditorForm.NEGATIVE_ESTIMATED_HOURS_ERROR_KEY);
                this.validateIsNumber(errors, request,
                        UserStoryEditorForm.PRIORITY_PARAM,
                        UserStoryEditorForm.INVALID_PRIORITY_ERROR_KEY);
            } else {
                AbstractEditorForm.require(errors,
                        this.targetIterationId != this.iterationId,
                        UserStoryEditorForm.SAME_ITERATION_ERROR_KEY);
            }

        }
        return errors;
    }

    /**
     * Validate is number.
     *
     * @param errors
     *            the errors
     * @param request
     *            the request
     * @param param
     *            the param
     * @param errorKey
     *            the error key
     */
    private void validateIsNumber(final ActionErrors errors,
            final HttpServletRequest request, final String param,
            final String errorKey) {
        final String priority = request.getParameter(param);
        if (priority == null) {
            return;
        }
        try {
            Integer.parseInt(priority);
        } catch (final NumberFormatException e) {
            AbstractEditorForm.error(errors, errorKey);
        }
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.forms.AbstractEditorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {
        final XPlannerProperties props = new XPlannerProperties();
        this.reset(mapping, request, props);
    }

    /**
     * Reset.
     *
     * @param mapping
     *            the mapping
     * @param request
     *            the request
     * @param props
     *            the props
     */
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request, final XPlannerProperties props) {
        super.reset(mapping, request);
        this.name = null;
        this.description = null;
        this.trackerId = 0;
        this.iterationId = 0;
        this.estimatedHoursField = 0;
        this.taskBasedEstimatedHours = 0;
        this.targetIterationId = 0;
        this.customerId = 0;
        this.orderNo = 0;
        this.resetPriority(props);
    }

    /**
     * Reset priority.
     *
     * @param props
     *            the props
     */
    public void resetPriority(final XPlannerProperties props) {
        if (props.getProperty(UserStoryEditorForm.DEFAULT_PRIORITY_KEY) != null) {
            this.priority = Integer.parseInt(props
                    .getProperty(UserStoryEditorForm.DEFAULT_PRIORITY_KEY));
        } else {
            this.priority = UserStoryEditorForm.DEFAULT_PRIORITY;
        }
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name.
     *
     * @param name
     *            the new name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Sets the description.
     *
     * @param description
     *            the new description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the iteration id.
     *
     * @param iterationId
     *            the new iteration id
     */
    public void setIterationId(final int iterationId) {
        if (this.targetIterationId == 0) {
            this.targetIterationId = iterationId;
        }
        this.iterationId = iterationId;
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
     * Sets the tracker id.
     *
     * @param trackerId
     *            the new tracker id
     */
    public void setTrackerId(final int trackerId) {
        this.trackerId = trackerId;
    }

    /**
     * Gets the tracker id.
     *
     * @return the tracker id
     */
    public int getTrackerId() {
        return this.trackerId;
    }

    /**
     * Sets the estimated hours field.
     *
     * @param estimatedHours
     *            the new estimated hours field
     */
    public void setEstimatedHoursField(final double estimatedHours) {
        this.estimatedHoursField = estimatedHours;
    }

    /**
     * Gets the estimated hours field.
     *
     * @return the estimated hours field
     */
    public double getEstimatedHoursField() {
        return this.estimatedHoursField;
    }

    /**
     * Sets the priority.
     *
     * @param priority
     *            the new priority
     */
    public void setPriority(final int priority) {
        this.priority = priority;
    }

    /**
     * Gets the priority.
     *
     * @return the priority
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * Gets the disposition name.
     *
     * @return the disposition name
     */
    public String getDispositionName() {
        return this.dispositionName;
    }

    /**
     * Sets the disposition name.
     *
     * @param dispositionName
     *            the new disposition name
     */
    public void setDispositionName(final String dispositionName) {
        this.dispositionName = dispositionName;
    }

    /**
     * Gets the status name.
     *
     * @return the status name
     */
    public String getStatusName() {
        return this.statusName;
    }

    /**
     * Sets the status name.
     *
     * @param statusName
     *            the new status name
     */
    public void setStatusName(final String statusName) {
        this.statusName = statusName;
    }

    /**
     * Sets the customer id.
     *
     * @param customerId
     *            the new customer id
     */
    public void setCustomerId(final int customerId) {
        this.customerId = customerId;
    }

    /**
     * Gets the customer id.
     *
     * @return the customer id
     */
    public int getCustomerId() {
        return this.customerId;
    }

    /**
     * Gets the task based estimated hours.
     *
     * @return the task based estimated hours
     */
    public double getTaskBasedEstimatedHours() {
        return this.taskBasedEstimatedHours;
    }

    /**
     * Sets the task based estimated hours.
     *
     * @param hours
     *            the new task based estimated hours
     */
    public void setTaskBasedEstimatedHours(final double hours) {
        this.taskBasedEstimatedHours = hours;
    }

    /**
     * Gets the target iteration id.
     *
     * @return the target iteration id
     */
    public int getTargetIterationId() {
        return this.targetIterationId;
    }

    /**
     * Sets the target iteration id.
     *
     * @param targetIterationId
     *            the new target iteration id
     */
    public void setTargetIterationId(final int targetIterationId) {
        this.targetIterationId = targetIterationId;
    }

    /**
     * Gets the order no.
     *
     * @return the order no
     */
    public int getOrderNo() {
        return this.orderNo;
    }

    /**
     * Sets the order no.
     *
     * @param orderNo
     *            the new order no
     */
    public void setOrderNo(final int orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * Sets the customer.
     *
     * @param customer
     *            the new customer
     */
    public void setCustomer(final Person customer) {
        if (customer != null) {
            this.setCustomerId(customer.getId());
        }
    }
}