package com.technoetic.xplanner.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * The Class HttpClient.
 */
public class HttpClient {
    
    /**
     * Gets the page.
     *
     * @param urlString
     *            the url string
     * @return the page
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public String getPage(final String urlString) throws IOException {
        final URL url = new URL(urlString);
        final URLConnection connection = url.openConnection();
        connection.connect();
        final InputStream stream = connection.getInputStream();
        final byte[] readBuffer = new byte[256];
        final StringBuffer page = new StringBuffer();
        int pos = stream.read(readBuffer, 0, 255);
        while (pos > 0) {
            page.append(new String(readBuffer, 0, pos));
            pos = stream.read(readBuffer, 0, 255);
        }
        return page.toString();
    }
}
