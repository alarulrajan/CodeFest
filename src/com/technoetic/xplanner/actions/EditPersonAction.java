package com.technoetic.xplanner.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Person;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.forms.PersonEditorForm;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.AuthenticatorImpl;
import com.technoetic.xplanner.security.SecurityHelper;

/**
 * The Class EditPersonAction.
 */
public class EditPersonAction extends EditObjectAction<Person> {
	
	/** The edit person helper. */
	private EditPersonHelper editPersonHelper;

	/**
     * Sets the edits the person helper.
     *
     * @param editPersonHelper
     *            the new edits the person helper
     */
	public void setEditPersonHelper(final EditPersonHelper editPersonHelper) {
		this.editPersonHelper = editPersonHelper;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractAction#beforeObjectCommit(com.technoetic.xplanner.domain.Identifiable, org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void beforeObjectCommit(final Person object,
			final ActionMapping actionMapping, final ActionForm actionForm,
			final HttpServletRequest request, final HttpServletResponse reply)
			throws Exception {
		final PersonEditorForm personForm = (PersonEditorForm) actionForm;
		final Person person = object;
		final Map<String, String> projectRoleMap = this.getProjectRoleMap(
				personForm.getProjectIds(), personForm.getProjectRoles());
		this.editPersonHelper.modifyRoles(projectRoleMap, person,
				personForm.isSystemAdmin(),
				SecurityHelper.getRemoteUserId(request));
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractAction#afterObjectCommit(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void afterObjectCommit(final ActionMapping actionMapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse reply) throws ServletException {
		final PersonEditorForm personForm = (PersonEditorForm) actionForm;
		try {
			this.editPersonHelper.changeUserPassword(
					personForm.getNewPassword(), personForm.getUserId(),
					AuthenticatorImpl.getLoginModule(request));
		} catch (final AuthenticationException e) {
			throw new ServletException(e);
		}
	}

	/**
     * Gets the project role map.
     *
     * @param projectIds
     *            the project ids
     * @param projectRoles
     *            the project roles
     * @return the project role map
     */
	private Map<String, String> getProjectRoleMap(
			final List<String> projectIds, final List<String> projectRoles) {
		final Map<String, String> projectRoleMap = new HashMap<String, String>();
		for (int i = 0; i < projectIds.size() && i < projectRoles.size(); i++) {
			projectRoleMap.put(projectIds.get(i), projectRoles.get(i));
		}
		return projectRoleMap;
	}

}
