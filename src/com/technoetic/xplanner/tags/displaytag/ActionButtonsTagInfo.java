package com.technoetic.xplanner.tags.displaytag;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

import com.technoetic.xplanner.views.ActionRenderer;

public class ActionButtonsTagInfo extends TagExtraInfo {

	public ActionButtonsTagInfo() {
	}

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
