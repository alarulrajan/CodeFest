/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Apr 20, 2004
 * Time: 1:25:57 AM
 */
package com.technoetic.xplanner.importer.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * A factory for creating IOStream objects.
 */
public class IOStreamFactory {
	
	/**
     * New input stream.
     *
     * @param path
     *            the path
     * @return the input stream
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
	public InputStream newInputStream(final String path) throws IOException {
		return new FileInputStream(path);
	}
}