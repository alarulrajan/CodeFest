package com.technoetic.xplanner.tags;

import javax.servlet.jsp.tagext.Tag;

/**
 * The Interface WritableTag.
 */
public interface WritableTag extends Tag {
	
	/**
     * Checks if is writable.
     *
     * @return true, if is writable
     * @throws Exception
     *             the exception
     */
	boolean isWritable() throws Exception;
}
