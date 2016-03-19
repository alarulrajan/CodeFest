package com.technoetic.xplanner.ical;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Task;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.format.PrintfFormat;
import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * Original implementation contributed by Emiliano Heyns
 * (emiliano@iris-advies.nl)
 */
public class iCalServlet extends HttpServlet {
    
    /** The log. */
    private final Logger log = Logger.getLogger(this.getClass());
    
    /** The pf date. */
    private final PrintfFormat pfDate = new PrintfFormat(
            "%04d%02d%02dT%02d%02d%02d");
    
    /** The calendar. */
    private final Calendar calendar = Calendar.getInstance();
    
    /** The Constant ICAL_LINELENGTH_LIMIT. */
    private final static int ICAL_LINELENGTH_LIMIT = 75;

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(final HttpServletRequest req,
            final HttpServletResponse res) throws ServletException, IOException {
        try {
            int myID = 0;
            String myUsername = null;

            PersonPrincipal me;
            try {
                me = (PersonPrincipal) SecurityHelper.getUserPrincipal(req);
            } catch (final AuthenticationException e) {
                res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        e.getMessage());
                return;
            }
            myID = me.getPerson().getId();
            myUsername = me.getPerson().getUserId();

            String requestedUsername = req.getPathInfo();
            if (requestedUsername == null || requestedUsername.equals("/")) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "No iCal file requested.");
                return;
            }

            if (requestedUsername.startsWith("/")) {
                requestedUsername = requestedUsername.substring(1);
            }

            if (!requestedUsername.endsWith(".ics")) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST,
                        "No .ics suffix for requested iCal file.");
                return;
            }

            requestedUsername = requestedUsername.substring(0,
                    requestedUsername.length() - 4);

            final Session session = ThreadSession.get();

            // verify access of http user vs requested user
            if (!requestedUsername.equals(myUsername)) {
                if (!SystemAuthorizer.get().hasPermissionForSomeProject(myID,
                        "system.person", myID, "admin.edit")) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN,
                            "No authorization for accessing calendar for "
                                    + requestedUsername);
                    return;
                }
            }

            String taskURL = req.getScheme() + "://" + req.getServerName()
                    + ":" + req.getServerPort() + req.getContextPath();
            if (!taskURL.endsWith("/")) {
                taskURL = taskURL + "/";
            }
            taskURL = taskURL + "do/view/task?oid=";
            final String guid = "-xplanner"
                    + req.getContextPath().replace('/', '_') + "@"
                    + req.getServerName();

            // response.setHeader("Content-type", "text/calendar");
            // pacify outlook
            res.setHeader("Content-type", "application/x-msoutlook");
            res.setHeader("Content-disposition", "inline; filename="
                    + requestedUsername + ".ics");

            final PrintWriter out = res.getWriter();

            out.write("BEGIN:VCALENDAR\r\n");
            out.write("VERSION:1.0\r\n");
            out.write("PRODID:-//Iris Advies//XPlanner//EN\r\n");

            this.generateTimeEntryData(out, session, requestedUsername, guid,
                    taskURL);
            this.generateTaskData(out, session, requestedUsername, guid,
                    taskURL);

            out.write("END:VCALENDAR\r\n");

        } catch (final Exception e) {
            this.log.error("ical error", e);
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    e.getMessage());
        }
    }

    /**
     * Generate task data.
     *
     * @param out
     *            the out
     * @param hibernateSession
     *            the hibernate session
     * @param requestedUsername
     *            the requested username
     * @param guid
     *            the guid
     * @param taskURL
     *            the task url
     * @throws HibernateException
     *             the hibernate exception
     */
    private void generateTaskData(final PrintWriter out,
            final Session hibernateSession, final String requestedUsername,
            final String guid, final String taskURL) throws HibernateException {

        final String query = "select task, story.name, story.priority, iteration.name, project.name"
                + " from net.sf.xplanner.domain.Task task,"
                + " person in class net.sf.xplanner.domain.Person,"
                + " story in class net.sf.xplanner.domain.UserStory,"
                + " iteration in class net.sf.xplanner.domain.Iteration,"
                + " project in class net.sf.xplanner.domain.Project"
                + " where"
                + " task.completed = false and task.type <> ?"
                + " and person.userId = ? and (person.id = task.acceptorId)"
                + " and task.story = story.id and story.iteration.id = iteration.id"
                + " and iteration.project.id = project.id";

        String overhead = null;
        try {
            final ResourceBundle resources = ResourceBundle
                    .getBundle("ResourceBundle");
            overhead = resources.getString("task.type.overhead");
        } catch (final Exception e) {
            overhead = null;
        }

        // allow for people to not have overhead tasks
        if (overhead == null) {
            overhead = "";
        }

        final List tasks = hibernateSession.find(query, new Object[] {
                overhead, requestedUsername }, new Type[] { Hibernate.STRING,
                Hibernate.STRING });
        final Iterator iter = tasks.iterator();
        while (iter.hasNext()) {
            final Object[] result = (Object[]) iter.next();
            final Task task = (Task) result[0];

            out.write("BEGIN:VTODO\r\n");
            out.write("UID:task-" + task.getId() + guid + "\r\n");
            out.write(this.quote("SUMMARY:" + task.getName()) + "\r\n");
            // SunBird doesn't support multi-value categories
            out.write(this.quote("CATEGORIES:" + result[4] + "\n" + result[3]
                    + "\n" + result[1])
                    + "\r\n");
            out.write("PERCENT-COMPLETE:"
                    + (int) (task.getActualHours() * 100 / (task
                            .getActualHours() + task.getRemainingHours()))
                    + "\r\n");
            out.write("PRIORITY:" + result[2] + "\r\n");
            out.write("STATUS:IN-PROCESS\r\n");
            out.write(this.quote("URL:" + taskURL + task.getId()) + "\r\n");
            out.write("END:VTODO\r\n");
        }
    }

    /**
     * Generate time entry data.
     *
     * @param out
     *            the out
     * @param hibernateSession
     *            the hibernate session
     * @param requestedUsername
     *            the requested username
     * @param guid
     *            the guid
     * @param taskURL
     *            the task url
     * @throws HibernateException
     *             the hibernate exception
     */
    private void generateTimeEntryData(final PrintWriter out,
            final Session hibernateSession, final String requestedUsername,
            final String guid, final String taskURL) throws HibernateException {

        final String query = "select entry.id, entry.startTime, entry.endTime, entry.duration,"
                + " task.id, task.name,"
                + " story.name"
                + " from"
                + " entry in class net.sf.xplanner.domain.TimeEntry,"
                + " person in class net.sf.xplanner.domain.Person,"
                + " task in class net.sf.xplanner.domain.Task,"
                + " story in class net.sf.xplanner.domain.UserStory"
                + " where"
                + " person.userId = ? and (person.id = entry.person1Id or person.id = entry.person2Id)"
                + " and entry.task.id = task.id" + " and task.story = story.id";

        final List events = hibernateSession.find(query, requestedUsername,
                Hibernate.STRING);
        final Iterator iter = events.iterator();
        while (iter.hasNext()) {
            final Object[] result = (Object[]) iter.next();

            if (result[1] != null && result[3] != null) {
                out.write("BEGIN:VEVENT\r\n");
                out.write("UID:timeentry-" + result[0] + guid + "\r\n");
                out.write(this.quote("SUMMARY:" + result[5] + "\n" + result[6])
                        + "\r\n");
                out.write("DTSTART:" + this.formatDate((Date) result[1])
                        + "\r\n");
                // do-before-release review how to handle null end times on time
                // entries
                if (result[2] != null) {
                    out.write("DTEND:" + this.formatDate((Date) result[2])
                            + "\r\n");
                } else {
                    out.write("DTEND:" + this.formatDate((Date) result[1])
                            + "\r\n");
                }
                out.write(this.quote("URL:" + taskURL + result[4]) + "\r\n");
                out.write("END:VEVENT\r\n");
            }
        }
    }

    /**
     * Format date.
     *
     * @param d
     *            the d
     * @return the string
     */
    private String formatDate(final Date d) {
        this.calendar.setTime(d);
        return this.pfDate.sprintf(new Object[] {
                new Integer(this.calendar.get(Calendar.YEAR)),
                new Integer(this.calendar.get(Calendar.MONTH) + 1),
                new Integer(this.calendar.get(Calendar.DAY_OF_MONTH)),
                new Integer(this.calendar.get(Calendar.HOUR_OF_DAY)),
                new Integer(this.calendar.get(Calendar.MINUTE)),
                new Integer(this.calendar.get(Calendar.SECOND)) });
    }

    /**
     * Quote.
     *
     * @param s
     *            the s
     * @return the string
     */
    private String quote(final String s) {
        final char[] chars = s.toCharArray();
        final int length = s.length();
        // initialize to 1 to account for the first quoted char
        int linelength = 1;
        final StringBuffer sb = new StringBuffer(length);

        for (int i = 0; i < length; i++) {
            if (linelength >= iCalServlet.ICAL_LINELENGTH_LIMIT) {
                sb.append("\r\n ");
                // initialize to 1 to account for the first quoted char
                linelength = 1;
            }

            switch (chars[i]) {
            case ',':
                sb.append("\\,");
                linelength += 2;
                break;
            case ';':
                sb.append("\\;");
                linelength += 2;
                break;
            case '\n':
                // SunBird doesn't support multi-line texts
                // sb.append("\\n"); linelength += 2;
                sb.append(" :: ");
                linelength += 4;
                break;
            default:
                sb.append(chars[i]);
                linelength++;
                break;
            }
        }

        return sb.toString();
    }
}
