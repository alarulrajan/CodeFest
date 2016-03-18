package com.technoetic.xplanner.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class AuthenticatedUserTEI extends TagExtraInfo {
	@Override
	public VariableInfo[] getVariableInfo(final TagData data) {
		return new VariableInfo[] { new VariableInfo(
				data.getAttributeString("id"), "net.sf.xplanner.domain.Person",
				true, VariableInfo.AT_END) };
	}
}
