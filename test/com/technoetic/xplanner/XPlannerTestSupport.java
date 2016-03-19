package com.technoetic.xplanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.Subject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import junit.extensions.FieldAccessor;
import junit.framework.Assert;
import net.sf.xplanner.domain.DataSample;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Role;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.struts.Globals;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.impl.ModuleConfigImpl;

import com.kizna.servletunit.HttpServletRequestSimulator;
import com.kizna.servletunit.HttpServletResponseSimulator;
import com.kizna.servletunit.HttpSessionSimulator;
import com.kizna.servletunit.ServletConfigSimulator;
import com.kizna.servletunit.ServletContextSimulator;
import com.technoetic.mocks.hibernate.MockSession;
import com.technoetic.mocks.hibernate.MockSessionFactory;
import com.technoetic.mocks.servlets.jsp.MockJspWriter;
import com.technoetic.mocks.servlets.jsp.MockPageContext;
import com.technoetic.mocks.sql.MockConnection;
import com.technoetic.mocks.sql.MockPreparedStatement;
import com.technoetic.mocks.sql.MockResultSet;
import com.technoetic.mocks.sql.MockStatement;
import com.technoetic.mocks.struts.util.MockMessageResources;
import com.technoetic.mocks.struts.util.MockMessageResourcesFactory;
import com.technoetic.xplanner.db.hibernate.GlobalSessionFactory;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;
import com.technoetic.xplanner.security.PersonPrincipal;
import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.DomainContext;

/**
 * The Class XPlannerTestSupport.
 */
public class XPlannerTestSupport {
    
    /** The Constant DEFAULT_PERSON_USER_ID. */
    public static final String DEFAULT_PERSON_USER_ID = "sombody";
    
    /** The Constant DEFAULT_PERSON_ID. */
    public static final int DEFAULT_PERSON_ID = 44;
    
    /** The appenders. */
    private Enumeration appenders;

    /** The Class XHttpServletResponseSimulator.
     */
    public static class XHttpServletResponseSimulator extends HttpServletResponseSimulator {
        
        /** The redirect. */
        private String redirect;
        
        /** The headers. */
        private HashMap headers = new HashMap();
        
        /** The status. */
        private int status;

        /** Encode url.
         *
         * @param url
         *            the url
         * @return the string
         */
        public String encodeURL(String url) {
            return url;
        }

        /** Gets the redirect.
         *
         * @return the redirect
         */
        public String getRedirect() {
            return redirect;
        }

        /** Send redirect.
         *
         * @param s
         *            the s
         * @throws IOException
         *             Signals that an I/O exception has occurred.
         */
        public void sendRedirect(String s) throws IOException {
            //super.sendRedirect(s);
            redirect = s;
        }

        /** Sets the header.
         *
         * @param name
         *            the name
         * @param value
         *            the value
         */
        public void setHeader(String name, String value) {
            headers.put(name, value);
        }

        /** Gets the header.
         *
         * @param name
         *            the name
         * @return the header
         */
        public String getHeader(String name) {
            return (String) headers.get(name);
        }

        /** Gets the status.
         *
         * @return the status
         */
        public int getStatus() {
            return status;
        }

        /** Sets the status.
         *
         * @param status
         *            the new status
         */
        public void setStatus(int status) {
            this.status = status;
        }

        /** Gets the content type.
         *
         * @return the content type
         */
        public String getContentType() {
            return null;
        }

        /** Sets the character encoding.
         *
         * @param s
         *            the new character encoding
         */
        public void setCharacterEncoding(String s) {

        }
    }

    /** The Class XServletContextSimulator.
     */
    public static class XServletContextSimulator extends ServletContextSimulator {
        
        /** The attributes. */
        private HashMap attributes = new HashMap();

        /** Sets the attribute.
         *
         * @param name
         *            the name
         * @param value
         *            the value
         */
        public void setAttribute(String name, Object value) {
            attributes.put(name, value);
        }

        /** Gets the attribute.
         *
         * @param name
         *            the name
         * @return the attribute
         */
        public Object getAttribute(String name) {
            return attributes.get(name);
        }

        /** Gets the resource paths.
         *
         * @param s
         *            the s
         * @return the resource paths
         */
        public Set getResourcePaths(String s) {
            throw new UnsupportedOperationException();
        }
    }

    /** The Class XHttpServletRequestSimulator.
     */
    public static class XHttpServletRequestSimulator extends HttpServletRequestSimulator {
        
        /** The locale. */
        private Locale locale;
        
        /** The servlet path. */
        private String servletPath;
        
        /** The context path. */
        private String contextPath;
        
        /** The remote addr. */
        private String remoteAddr;
        
        /** The cookies. */
        private Cookie[] cookies;

        /** Sets the locale.
         *
         * @param locale
         *            the new locale
         */
        public void setLocale(Locale locale) {
            this.locale = locale;
        }

        /** Gets the locale.
         *
         * @return the locale
         */
        public Locale getLocale() {
            return locale;
        }

        /** Gets the remote port.
         *
         * @return the remote port
         */
        public int getRemotePort() {
            return 0;
        }

        /** Gets the local name.
         *
         * @return the local name
         */
        public String getLocalName() {
            return null;
        }

        /** Gets the local addr.
         *
         * @return the local addr
         */
        public String getLocalAddr() {
            return null;
        }

        /** Gets the local port.
         *
         * @return the local port
         */
        public int getLocalPort() {
            return 0;
        }

        /** Gets the parameter map.
         *
         * @return the parameter map
         */
        public Map getParameterMap() {
            return parameters;
        }

        /** Gets the servlet path.
         *
         * @return the servlet path
         */
        public String getServletPath() {
            return servletPath;
        }

        /** Sets the servlet path.
         *
         * @param servletPath
         *            the new servlet path
         */
        public void setServletPath(String servletPath) {
            this.servletPath = servletPath;
        }

        /** Gets the context path.
         *
         * @return the context path
         */
        public String getContextPath() {
            return contextPath;
        }

        /** Sets the context path.
         *
         * @param contextPath
         *            the new context path
         */
        public void setContextPath(String contextPath) {
            this.contextPath = contextPath;
        }

        /** Sets the cookies.
         *
         * @param cookies
         *            the new cookies
         */
        public void setCookies(Cookie[] cookies) {
            this.cookies = cookies;
        }

        /** Gets the cookies.
         *
         * @return the cookies
         */
        public Cookie[] getCookies() {
            return cookies;
        }

        /** Sets the remote addr.
         *
         * @param remoteAddr
         *            the new remote addr
         */
        public void setRemoteAddr(String remoteAddr) {
           this.remoteAddr = remoteAddr;
        }

        /** Gets the remote addr.
         *
         * @return the remote addr
         */
        public String getRemoteAddr() {
           return remoteAddr;
        }

        /** Sets the query string.
         *
         * @param queryString
         *            the new query string
         */
        public void setQueryString(String queryString) {
           this.queryString = queryString;
        }
    }

    /** The mapping. */
    public ActionMapping mapping;
    
    /** The form. */
    public ActionForm form;
    
    /** The action servlet. */
    public ActionServlet actionServlet;
    
    /** The resources. */
    public MockMessageResources resources;
    
    /** The page context. */
    public MockPageContext pageContext;
    
    /** The jsp writer. */
    public MockJspWriter jspWriter;
    
    /** The request. */
    public XHttpServletRequestSimulator request;
    
    /** The response. */
    public XHttpServletResponseSimulator response;
    
    /** The servlet session. */
    public HttpSessionSimulator servletSession;
    
    /** The servlet config. */
    public ServletConfigSimulator servletConfig;
    
    /** The servlet context. */
    public XServletContextSimulator servletContext;
    
    /** The mock prepared statement. */
    public MockPreparedStatement mockPreparedStatement;
    
    /** The mock statement. */
    public MockStatement mockStatement;
    
    /** The mock result set. */
    public MockResultSet mockResultSet;
    
    /** The connection. */
    public MockConnection connection;
    
    /** The hibernate session factory. */
    public MockSessionFactory hibernateSessionFactory;
    
    /** The hibernate session. */
    public MockSession hibernateSession;

    /** Instantiates a new x planner test support.
     *
     * @throws Exception
     *             the exception
     */
    public XPlannerTestSupport() throws Exception {
        hibernateSessionFactory = new MockSessionFactory();
        GlobalSessionFactory.set(hibernateSessionFactory);
        hibernateSession = new MockSession();
        hibernateSessionFactory.openSessionReturn = hibernateSession;
        connection = new MockConnection();
        hibernateSession.connectionReturn = connection;
        mockPreparedStatement = new MockPreparedStatement();
        connection.prepareStatementReturn = mockPreparedStatement;
        mockResultSet = new MockResultSet();
        mockPreparedStatement.executeQueryReturn = mockResultSet;
        mapping = new ActionMapping();
        request = new XHttpServletRequestSimulator();
        request.setLocale(Locale.getDefault());
        response = new XHttpServletResponseSimulator();
        servletSession = (HttpSessionSimulator) request.getSession();
        actionServlet = new ActionServlet();
        MockMessageResourcesFactory factory = new MockMessageResourcesFactory();
        resources = new MockMessageResources(factory, "");
        request.setAttribute(Globals.MESSAGES_KEY, resources);
        HibernateHelper.setSession(request, hibernateSession);
        pageContext = new MockPageContext();
        pageContext.getRequestReturn = request;
        pageContext.getResponseReturn = response;
        servletConfig = new ServletConfigSimulator();
        FieldAccessor.set(actionServlet, "config", servletConfig);
        pageContext.getServletConfigReturn = servletConfig;
        servletContext = new XServletContextSimulator();
        servletContext.setAttribute(Globals.MODULE_KEY, new ModuleConfigImpl(""));
        FieldAccessor.set(servletConfig, "context", servletContext);
        servletContext.setAttribute(Globals.MESSAGES_KEY, resources);
        pageContext.getServletContextReturn = servletContext;
        pageContext.getSessionReturn = request.getSession();
        jspWriter = new MockJspWriter();
        pageContext.getOutReturn = jspWriter;
    }

    /** Sets the forward.
     *
     * @param name
     *            the name
     * @param path
     *            the path
     */
    public void setForward(String name, String path) {
        ModuleConfig config = mapping.getModuleConfig();
        if (config == null) {
            config = new ModuleConfigImpl("");
            mapping.setModuleConfig(config);
        }
        ForwardConfig forwardConfig = config.findForwardConfig(name);
        if (forwardConfig == null) {
            config.addForwardConfig(new ActionForward(name, path, false));
        } else {
            forwardConfig.setPath(path);
        }
    }

    /** Execute action.
     *
     * @param action
     *            the action
     * @return the action forward
     * @throws Exception
     *             the exception
     */
    public ActionForward executeAction(Action action) throws Exception {
        return action.execute(mapping, form, request, response);
    }

    /** Sets the up subject in role.
     *
     * @param role
     *            the role
     * @return the subject
     */
    public Subject setUpSubjectInRole(String role) {
        return setUpSubject(DEFAULT_PERSON_USER_ID, new String[]{role});
    }

    /** Sets the up subject.
     *
     * @param userId
     *            the user id
     * @param roles
     *            the roles
     * @return the subject
     */
    public Subject setUpSubject(String userId, String[] roles) {
        Person person = new Person(userId);
        person.setId(DEFAULT_PERSON_ID);
        return setUpSubject(person, roles);
    }

    /** Sets the up subject.
     *
     * @param person
     *            the person
     * @param roles
     *            the roles
     * @return the subject
     */
    public Subject setUpSubject(Person person, String[] roles) {
        Subject subject = new Subject();
        subject.getPrincipals().add(new PersonPrincipal(person));
        for (int i = 0; i < roles.length; i++) {
            if (roles[i] != null) {
                subject.getPrincipals().add(new Role(roles[i]));
            }
        }
        SecurityHelper.setSubject(request, subject);
        return subject;
    }

    /** Assert history in object.
     *
     * @param objectId
     *            the object id
     * @param action
     *            the action
     * @param description
     *            the description
     * @param personId
     *            the person id
     */
    public void assertHistoryInObject(int objectId, String action, String description, int personId) {
        assertHistory(hibernateSession, objectId, action, description, personId);
    }

    /** Assert history.
     *
     * @param hibernateSession
     *            the hibernate session
     * @param objectId
     *            the object id
     * @param action
     *            the action
     * @param description
     *            the description
     * @param authorId
     *            the author id
     */
    public void assertHistory(MockSession hibernateSession,
                                      int objectId,
                                      String action,
                                      String description,
                                      int authorId) {

        List eventList = getHistoryList(hibernateSession, objectId, action);
        Assert.assertFalse("no historical events", eventList.size() == 0);
        if (eventList.size() > 0) {
            Iterator it = eventList.iterator();
            while (it.hasNext()) {
                History event = (History) it.next();
               if (matchesExpectedEvent(description, event, authorId)) {
                  assertEventDetails(description, event, authorId);
                  return;
               }
            }
           Assert.fail("Expected historical event not found");
        }
    }

   /** Matches expected event.
     *
     * @param description
     *            the description
     * @param event
     *            the event
     * @param authorId
     *            the author id
     * @return true, if successful
     */
   private boolean matchesExpectedEvent(String description, History event, int authorId) {
      return
            assertEventDescriptionMatches(description, event) &&
             authorId == event.getPersonId() &&
             event.getWhenHappened() != null &&
             !event.isNotified();
   }

   /** Assert event description matches.
     *
     * @param expectedDescription
     *            the expected description
     * @param event
     *            the event
     * @return true, if successful
     */
   private boolean assertEventDescriptionMatches(String expectedDescription, History event) {
      return (event.getDescription() == null && expectedDescription == null) ||
             event.getDescription().equals(expectedDescription);
   }

   /** Assert event details.
     *
     * @param description
     *            the description
     * @param event
     *            the event
     * @param authorId
     *            the author id
     */
   private void assertEventDetails(String description, History event, int authorId) {
      Assert.assertEquals("wrong description", description, event.getDescription());
      Assert.assertEquals("wrong authorId", authorId, event.getPersonId().intValue());
      Assert.assertNotNull("wrong date", event.getWhenHappened());
      Assert.assertFalse("wrong notified flag", event.isNotified());
   }

   /** Assert no history.
     *
     * @param hibernateSession
     *            the hibernate session
     */
   public void assertNoHistory(MockSession hibernateSession) {
       List HistoryList = getHistoryList(hibernateSession, 0, null);
       Assert.assertFalse("unexpected historical event", HistoryList.size() == 0);
   }

    /** Gets the history list.
     *
     * @param hibernateSession
     *            the hibernate session
     * @param targetObjectId
     *            the target object id
     * @param action
     *            the action
     * @return the history list
     */
    private List getHistoryList(MockSession hibernateSession, int targetObjectId, String action) {
        List HistoryList = new ArrayList();
        Iterator it = getObjectListWithType(hibernateSession, History.class).iterator();
        while (it.hasNext()) {
            History event = (History) it.next();
            if (targetObjectId == 0 || (event.getTargetId() == targetObjectId &&
                    StringUtils.equals(event.getAction(), action))) {
                HistoryList.add(event);
            }
        }
        return HistoryList;
    }


    /** Gets the object list with type.
     *
     * @param hibernateSession
     *            the hibernate session
     * @param objectClass
     *            the object class
     * @return the object list with type
     */
    public List getObjectListWithType(MockSession hibernateSession, Class objectClass) {
        List objectList = new ArrayList();
        for (int i = 0; i < hibernateSession.saveObjects.size(); i++) {
            if (objectClass.isAssignableFrom(hibernateSession.saveObjects.get(i).getClass())) {
                objectList.add(hibernateSession.saveObjects.get(i));
            }
        }
        return objectList;
    }

    /** Sets the up mock appender.
     */
    public void setUpMockAppender() {
        appenders = Logger.getRootLogger().getAllAppenders();
        Logger.getRootLogger().removeAllAppenders();
        Logger.getRootLogger().addAppender(new AppenderSkeleton() {
            protected void append(LoggingEvent event) {
            }

            public void close() {
            }

            public boolean requiresLayout() {
                return false;
            }
        });
    }

    /** Sets the up domain context.
     */
    public void setUpDomainContext() {
        DomainContext context = new DomainContext();
        context.setProjectId(10);
        context.save(request);
    }

    /** Tear down mock appender.
     */
    public void tearDownMockAppender() {
        Logger rootLogger = Logger.getRootLogger();
        while (appenders.hasMoreElements()) {
            rootLogger.addAppender((Appender) appenders.nextElement());
        }
    }

    /** Dump request.
     *
     * @param request
     *            the request
     */
    public static void dumpRequest(HttpServletRequest request) {
        Enumeration pnames = request.getParameterNames();
        while (pnames.hasMoreElements()) {
            String key = (String) pnames.nextElement();
            System.out.print(key + "=");
            String[] values = request.getParameterValues(key);
            if (values.length == 1) {
                System.out.println(values[0]);
            } else {
                System.out.println(StringUtils.join(values, ","));
            }
        }
    }

    /** Assert data sample exist.
     *
     * @param hibernateSession
     *            the hibernate session
     * @param aspect
     *            the aspect
     * @param referenceId
     *            the reference id
     * @param value
     *            the value
     * @param date
     *            the date
     */
    public void assertDataSampleExist(MockSession hibernateSession, String aspect, int referenceId, double value, Date date) {
        List dataSampleList = getObjectListWithType(hibernateSession, DataSample.class);
        boolean isDataSample = false;
        Iterator it = dataSampleList.iterator();
        while (it.hasNext()) {
            DataSample ds = (DataSample) it.next();
            if (StringUtils.equals(ds.getAspect(), aspect) && ds.getReferenceId() == referenceId &&
                    ds.getValue() == value && areClose(date, ds.getSampleTime()))
                isDataSample = true;
        }
        Assert.assertTrue("no datasample: aspect=" + aspect + ", sampleDate=" + date +
                ", referenceId=" + referenceId + ", value=" + value, isDataSample);
    }

    /** Are close.
     *
     * @param date1
     *            the date1
     * @param date2
     *            the date2
     * @return true, if successful
     */
    private boolean areClose(Date date1, Date date2) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date2);
        cal.add(Calendar.HOUR, -2);
        Date lowerBorder = cal.getTime();
        cal.add(Calendar.HOUR, 4);
        Date upperBorder = cal.getTime();
        return date1.after(lowerBorder) && date1.before(upperBorder);
    }

    /** Assert no data sample.
     *
     * @param hibernateSession
     *            the hibernate session
     * @param aspect
     *            the aspect
     * @param referenceId
     *            the reference id
     * @param date
     *            the date
     */
    public void assertNoDataSample(MockSession hibernateSession, String aspect, int referenceId, Date date) {
        List dataSampleList = getObjectListWithType(hibernateSession, DataSample.class);
        boolean isDataSample = false;
        Iterator it = dataSampleList.iterator();
        while (it.hasNext()) {
            DataSample ds = (DataSample) it.next();
            if (StringUtils.equals(ds.getAspect(), aspect) && ds.getReferenceId() == referenceId &&
                    areClose(date, ds.getSampleTime()))
                isDataSample = true;
        }
        Assert.assertFalse("datasample: aspect=" + aspect + ", sampleDate=" + date +
                ", referenceId=" + referenceId + " exists", isDataSample);
    }

    /** Gets the absolute test url.
     *
     * @return the absolute test url
     */
    public static String getAbsoluteTestURL(){
        XPlannerProperties properties = new XPlannerProperties();
        return properties.getProperty("xplanner.application.url");
    }

    /** Gets the relative test url.
     *
     * @return the relative test url
     */
    public static String getRelativeTestURL(){
        XPlannerProperties properties = new XPlannerProperties();
        String baseUrl = properties.getProperty("xplanner.application.url");
//        Pattern pattern = Pattern.compile("^(?:\\w+://)?.*?(/.*?)/?$");
        Pattern pattern = Pattern.compile("^(?:\\w+://)?.*?((/.+?)?)/?$");
        Matcher matcher = pattern.matcher(baseUrl);
        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }
    }
}
