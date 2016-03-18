package com.technoetic.xplanner.actions;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * Created by IntelliJ IDEA. User: sg620641 Date: Oct 24, 2005 Time: 2:49:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditPropertiesAction extends Action {
	protected static final String PROPERTY_NAME_PARAM = "propertyName";
	protected static final String PROPERTY_VALUE_PARAM = "propertyValue";
	protected static final String RETURN_TO_PARAM = "returnto";

	@Override
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final XPlannerProperties properties = new XPlannerProperties();
		final String propertyName = request
				.getParameter(EditPropertiesAction.PROPERTY_NAME_PARAM);
		final String propertyValue = request
				.getParameter(EditPropertiesAction.PROPERTY_VALUE_PARAM);
		final String returnTo = request
				.getParameter(EditPropertiesAction.RETURN_TO_PARAM);
		if (!StringUtils.isEmpty(propertyName) && propertyValue != null) {
			this.getPropertiesToUpdate(properties).setProperty(propertyName,
					propertyValue);
		}
		if (returnTo != null) {
			return new ActionForward(returnTo, true);
		} else {
			return mapping.findForward("view/projects");
		}
	}

	protected Properties getPropertiesToUpdate(
			final XPlannerProperties properties) {
		return properties.get();
	}
}
