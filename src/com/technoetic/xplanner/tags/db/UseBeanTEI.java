package com.technoetic.xplanner.tags.db;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * The Class UseBeanTEI.
 */
public class UseBeanTEI extends TagExtraInfo {
	
	/* (non-Javadoc)
	 * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(javax.servlet.jsp.tagext.TagData)
	 */
	@Override
	public VariableInfo[] getVariableInfo(final TagData data) {
		return new VariableInfo[] { new VariableInfo(
				data.getAttributeString("id"), data.getAttributeString("type"),
				true, VariableInfo.AT_END) };
	}
}
