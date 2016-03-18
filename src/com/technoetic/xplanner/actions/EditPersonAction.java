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

public class EditPersonAction extends EditObjectAction<Person> {
	private EditPersonHelper editPersonHelper;

	public void setEditPersonHelper(final EditPersonHelper editPersonHelper) {
		this.editPersonHelper = editPersonHelper;
	}

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

	private Map<String, String> getProjectRoleMap(
			final List<String> projectIds, final List<String> projectRoles) {
		final Map<String, String> projectRoleMap = new HashMap<String, String>();
		for (int i = 0; i < projectIds.size() && i < projectRoles.size(); i++) {
			projectRoleMap.put(projectIds.get(i), projectRoles.get(i));
		}
		return projectRoleMap;
	}

}
