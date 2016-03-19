package com.technoetic.xplanner.export;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.classic.Session;

/**
 * The Interface Exporter.
 */
public interface Exporter {
    
    /**
     * Export.
     *
     * @param session
     *            the session
     * @param object
     *            the object
     * @return the byte[]
     * @throws ExportException
     *             the export exception
     */
    byte[] export(Session session, Object object) throws ExportException;

    /**
     * Initialize headers.
     *
     * @param response
     *            the response
     */
    void initializeHeaders(HttpServletResponse response);
}
