package com.technoetic.xplanner.tags.db;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class UseBeanTEI extends TagExtraInfo {
	@Override
	public VariableInfo[] getVariableInfo(final TagData data) {
		return new VariableInfo[] { new VariableInfo(
				data.getAttributeString("id"), data.getAttributeString("type"),
				true, VariableInfo.AT_END) };
	}
}
