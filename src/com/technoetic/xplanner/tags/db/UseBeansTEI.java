package com.technoetic.xplanner.tags.db;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * The Class UseBeansTEI.
 */
public class UseBeansTEI extends TagExtraInfo {
    
    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagExtraInfo#getVariableInfo(javax.servlet.jsp.tagext.TagData)
     */
    @Override
    public VariableInfo[] getVariableInfo(final TagData data) {
        return new VariableInfo[] { new VariableInfo(
                data.getAttributeString("id"), "java.util.Collection", true,
                VariableInfo.AT_END) };
    }
}
