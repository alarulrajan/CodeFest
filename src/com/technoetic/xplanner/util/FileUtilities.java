/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 12, 2005
 * Time: 11:39:17 PM
 */
package com.technoetic.xplanner.util;

import java.io.InputStream;

/**
 * The Class FileUtilities.
 */
public class FileUtilities {
	
	/**
     * Gets the file from class path.
     *
     * @param filePath
     *            the file path
     * @return the file from class path
     */
	public static InputStream getFileFromClassPath(final String filePath) {
		return FileUtilities.class.getClassLoader().getResourceAsStream(
				filePath);
	}
}