package com.technoetic.mocks.servlets;

import java.util.HashMap;

import com.kizna.servletunit.HttpServletRequestSimulator;
import com.kizna.servletunit.HttpServletResponseSimulator;
import com.kizna.servletunit.HttpSessionSimulator;
import com.kizna.servletunit.ServletConfigSimulator;
import com.kizna.servletunit.ServletContextSimulator;
import com.technoetic.mocks.servlets.jsp.MockJspWriter;
import com.technoetic.mocks.servlets.jsp.MockPageContext;

/**
 * The Class ServletTestSupport.
 */
public class ServletTestSupport {
    
    /** The Class XHttpServletResponseSimulator.
     */
    public static class XHttpServletResponseSimulator extends HttpServletResponseSimulator {
        
        /** Encode url.
         *
         * @param url
         *            the url
         * @return the string
         */
        public String encodeURL(String url) {
            return url;
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
    }

    /** The page context. */
    public MockPageContext pageContext;
    
    /** The request. */
    public HttpServletRequestSimulator request;
    
    /** The session. */
    public HttpSessionSimulator session;
    
    /** The response. */
    public XHttpServletResponseSimulator response;
    
    /** The servlet config. */
    public ServletConfigSimulator servletConfig;
    
    /** The servlet context. */
    public XServletContextSimulator servletContext;
    
    /** The mock jsp writer. */
    public MockJspWriter mockJspWriter;

    /** Instantiates a new servlet test support.
     */
    public ServletTestSupport() {
        request = new HttpServletRequestSimulator();
        response = new XHttpServletResponseSimulator();
        session = (HttpSessionSimulator)request.getSession();
        pageContext = new MockPageContext();
        pageContext.getRequestReturn = request;
        pageContext.getResponseReturn = response;
        servletConfig = new ServletConfigSimulator();
        pageContext.getServletConfigReturn = servletConfig;
        servletContext = new XServletContextSimulator();
        pageContext.getServletContextReturn = servletContext;
        pageContext.getSessionReturn = request.getSession();
        mockJspWriter = new MockJspWriter();
        pageContext.getOutReturn = mockJspWriter;
    }

}
