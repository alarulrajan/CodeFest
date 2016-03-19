package com.technoetic.xplanner.mail;

import java.util.List;
import java.util.Map;

/**
 * The Interface EmailFormatter.
 */
public interface EmailFormatter {

    /**
     * Format email entry.
     *
     * @param bodyEntryList
     *            the body entry list
     * @param params
     *            the params
     * @return the string
     * @throws Exception
     *             the exception
     */
    String formatEmailEntry(List bodyEntryList, Map<String, Object> params)
            throws Exception;

    // void setVelocityEngine(VelocityEngine velocityEngine);
    //
    // void setHttpClient(HttpClient httpClient);

    /**
     * Format email entry.
     *
     * @param header
     *            the header
     * @param footer
     *            the footer
     * @param storyLabel
     *            the story label
     * @param taskLabel
     *            the task label
     * @param bodyEntryList
     *            the body entry list
     * @return the string
     * @throws Exception
     *             the exception
     */
    String formatEmailEntry(String header, String footer, String storyLabel,
            String taskLabel, List bodyEntryList) throws Exception;
}
