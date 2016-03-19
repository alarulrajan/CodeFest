package com.technoetic.xplanner.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * The Class AuthenticatedUserTEI.
 */
public class AuthenticatedUserTEI extends TagExtraInfo {
    
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(javax.servlet.jsp.tagext.TagData)
     */
    @Override
    public VariableInfo[] getVariableInfo(final TagData data) {
        return new VariableInfo[] { new VariableInfo(
                data.getAttributeString("id"), "net.sf.xplanner.domain.Person",
                true, VariableInfo.AT_END) };
    }
}
