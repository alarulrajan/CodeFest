package com.technoetic.xplanner.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.forms.AbstractEditorForm;
import com.technoetic.xplanner.forms.UserStoryEditorForm;

public class EditStoryAction extends EditObjectAction<UserStory> {
	public static final String CONTINUED = "continued";
	public static final String MOVED = "moved";
	public static final String OPERATION_PARAM_KEY = "operation";
	public static final String ACTION_KEY = "action";

	@Override
	public void beforeObjectCommit(final UserStory object,
			final ActionMapping actionMapping, final ActionForm actionForm,
			final HttpServletRequest request, final HttpServletResponse reply)
			throws Exception {
		final UserStory story = object;
		final UserStoryEditorForm userStoryEditorForm = (UserStoryEditorForm) actionForm;
		final int iterationId = userStoryEditorForm.getIterationId();
		final Iteration iteration = this.getCommonDao().getById(
				Iteration.class, iterationId);
		story.setIteration(iteration);
		this.getCommonDao().flush();
		final List<UserStory> userStories = iteration.getUserStories();
		if (!userStories.contains(story)) {
			userStories.add(story);
		}
		iteration.modifyStoryOrder(DomainOrderer
				.buildStoryIdNewOrderArray(userStories));
		request.getParameter(EditStoryAction.ACTION_KEY);
	}

	@Override
	protected void populateForm(final AbstractEditorForm form,
			final ActionMapping actionMapping, final HttpServletRequest request)
			throws Exception {
		super.populateForm(form, actionMapping, request);
		if (form.getOid() == null) {
			final UserStoryEditorForm storyForm = (UserStoryEditorForm) form;
			final int iterationId = Integer.parseInt(request
					.getParameter("fkey"));
			final Iteration iteration = this.getCommonDao().getById(
					Iteration.class, iterationId);
			storyForm.setDispositionName(iteration
					.determineNewStoryDisposition().getName());
			storyForm.setOrderNo(iteration.getUserStories().size() + 1);
		}
	}
}