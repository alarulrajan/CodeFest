package com.technoetic.xplanner.filters;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.security.auth.Subject;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.technoetic.xplanner.format.PrintfFormat;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;

/**
 * The Class ActivityLogFilterHelper.
 */
public class ActivityLogFilterHelper {

    /** The Constant LOG_LINE_PATTERN. */
    public static final String LOG_LINE_PATTERN = "%-20.20s %-10.10s %-14.14s %-25.25s %-6.6s %-12.12s %s";

    /** The Constant ACTION_START. */
    public static final String ACTION_START = "START";
    
    /** The Constant ACTION_END. */
    public static final String ACTION_END = "END";
    
    /** The Constant ACTION_PREFIX. */
    public static final String ACTION_PREFIX = "/do/";
    
    /** The Constant DATE_FORMAT. */
    public static final String DATE_FORMAT = "MM/dd HH:mm:ss.SSSS";
    
    /** The Constant ACTION_PERIOD_PATTERN. */
    public static final String ACTION_PERIOD_PATTERN = "{0,number,#.####}";
    
    /** The Constant NO_USER_MSG. */
    public static final String NO_USER_MSG = "";
    
    /** The Constant NO_QUERY_MSG. */
    public static final String NO_QUERY_MSG = "";

    /** The user id. */
    private String userId = null;
    
    /** The remote addr. */
    private String remoteAddr = null;
    
    /** The action name. */
    private String actionName = null;
    
    /** The query string. */
    private String queryString = null;
    
    /** The start date. */
    private Date startDate = null;
    
    /** The end date. */
    private Date endDate = null;

    /**
     * Do helper set up.
     *
     * @param request
     *            the request
     */
    public void doHelperSetUp(final ServletRequest request) {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        this.userId = this.getUserId(httpRequest);
        this.remoteAddr = httpRequest.getRemoteAddr();
        this.actionName = this.getActionName(httpRequest);
        this.queryString = this.getQueryString(httpRequest);
    }

    /**
     * Gets the start log record.
     *
     * @return the start log record
     */
    public String getStartLogRecord() {
        this.startDate = new Date();
        final Object[] elements = new Object[] {
                this.getFormatedTime(this.startDate), this.userId,
                this.remoteAddr, this.actionName,
                ActivityLogFilterHelper.ACTION_START, "", this.queryString };
        return this.getLogLine(elements);
    }

    /**
     * Gets the end log record.
     *
     * @return the end log record
     */
    public String getEndLogRecord() {
        this.endDate = new Date();
        final Object[] elements = new Object[] {
                this.getFormatedTime(this.endDate), this.userId,
                this.remoteAddr, this.actionName,
                ActivityLogFilterHelper.ACTION_END, this.getActionPeriod(),
                this.queryString };
        return this.getLogLine(elements);
    }

    /**
     * Gets the formated start date.
     *
     * @return the formated start date
     */
    public String getFormatedStartDate() {
        return this.getFormatedTime(this.startDate);
    }

    /**
     * Gets the formated end date.
     *
     * @return the formated end date
     */
    public String getFormatedEndDate() {
        return this.getFormatedTime(this.endDate);
    }

    /**
     * Gets the formated time.
     *
     * @param date
     *            the date
     * @return the formated time
     */
    private String getFormatedTime(final Date date) {
        final SimpleDateFormat formater = new SimpleDateFormat(
                ActivityLogFilterHelper.DATE_FORMAT);
        return formater.format(date);
    }

    /**
     * Gets the user id.
     *
     * @param httpRequest
     *            the http request
     * @return the user id
     */
    private String getUserId(final HttpServletRequest httpRequest) {
        final Subject subject = (Subject) httpRequest.getSession()
                .getAttribute(SecurityHelper.SECURITY_SUBJECT_KEY);
        if (subject != null) {
            final Set pricipalSet = subject.getPrincipals();
            final Iterator iterator = pricipalSet.iterator();
            return ((PersonPrincipal) iterator.next()).getPerson().getUserId();
        } else {
            return ActivityLogFilterHelper.NO_USER_MSG;
        }
    }

    /**
     * Gets the query string.
     *
     * @param httpRequest
     *            the http request
     * @return the query string
     */
    private String getQueryString(final HttpServletRequest httpRequest) {
        if (httpRequest.getQueryString() != null) {
            return httpRequest.getQueryString();
        } else {
            return ActivityLogFilterHelper.NO_QUERY_MSG;
        }
    }

    /**
     * Gets the action name.
     *
     * @param httpRequest
     *            the http request
     * @return the action name
     */
    private String getActionName(final HttpServletRequest httpRequest) {
        final String url = httpRequest.getRequestURI();
        final int index = url.indexOf(ActivityLogFilterHelper.ACTION_PREFIX);
        return url.substring(index
                + ActivityLogFilterHelper.ACTION_PREFIX.length());
    }

    /**
     * Gets the log line.
     *
     * @param elements
     *            the elements
     * @return the log line
     */
    private String getLogLine(final Object[] elements) {
        final PrintfFormat formater = new PrintfFormat(
                ActivityLogFilterHelper.LOG_LINE_PATTERN);
        return formater.sprintf(elements);
    }

    /**
     * Gets the action period.
     *
     * @return the action period
     */
    public String getActionPeriod() {
        final double mseconds = this.endDate.getTime()
                - this.startDate.getTime();
        final double seconds = mseconds / 1000;
        final Object[] param = new Object[] { new Double(seconds) };
        return MessageFormat.format(
                ActivityLogFilterHelper.ACTION_PERIOD_PATTERN, param);
    }

}
