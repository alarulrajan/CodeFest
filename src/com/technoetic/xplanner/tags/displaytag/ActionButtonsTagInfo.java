package com.technoetic.xplanner.tags.displaytag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.technoetic.xplanner.views.ActionRenderer;

/**
 * The Class ActionButtonsTagInfo.
 */
public class ActionButtonsTagInfo extends TagExtraInfo {

	/**
     * Instantiates a new action buttons tag info.
     */
	public ActionButtonsTagInfo() {
	}

	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(javax.servlet.jsp.tagext.TagData)
	 */
	@Override
	public VariableInfo[] getVariableInfo(final TagData data) {
		final List<VariableInfo> variables = new ArrayList<VariableInfo>(4);
		final Object tagId = data.getAttributeString("id");
		if (tagId != null) {
			variables.add(new VariableInfo(tagId.toString(),
					ActionRenderer.class.getName(), true, 0));
		}
		return variables.toArray(new VariableInfo[0]);
	}
}
