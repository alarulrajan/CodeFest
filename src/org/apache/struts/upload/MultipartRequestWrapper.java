/*
 * $Header$
 * $Revision: 70 $
 * $Date: 2004-09-27 13:26:11 -0500 (Mon, 27 Sep 2004) $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Struts", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.struts.upload;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * This class functions as a wrapper around HttpServletRequest to
 * provide working getParameter methods for multipart requests.  Once
 * Struts requires Servlet 2.3, this class will definately be changed to
 * extend javax.servlet.http.HttpServletRequestWrapper instead of
 * implementing HttpServletRequest.  Servlet 2.3 methods are implemented
 * to return <code>null</code> or do nothing if called on.  Use
 * {@link #getRequest() getRequest} to retrieve the underlying HttpServletRequest
 * object and call on the 2.3 method there, the empty methods are here only
 * so that this will compile with the Servlet 2.3 jar.  This class exists temporarily
 * in the process() method of ActionServlet, just before the ActionForward is processed
 * and just after the Action is performed, the request is set back to the original
 * HttpServletRequest object.
 */
public class MultipartRequestWrapper implements HttpServletRequest {

    /** The parameters for this multipart request. */
    protected Map parameters;

    /** The underlying HttpServletRequest. */
    protected HttpServletRequest request;

    /** Instantiates a new multipart request wrapper.
     *
     * @param request
     *            the request
     */
    public MultipartRequestWrapper(HttpServletRequest request) {
        this.request = request;
        this.parameters = new HashMap();
    }

    /** Sets a parameter for this request. The parameter is actually separate
     * from the request parameters, but calling on the getParameter() methods of
     * this class will work as if they weren't.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     */
    public void setParameter(String name, String value) {
        String[] mValue = (String[]) parameters.get(name);
        if (mValue == null) {
            mValue = new String[0];
        }
        String[] newValue = new String[mValue.length + 1];
        System.arraycopy(mValue, 0, newValue, 0, mValue.length);
        newValue[mValue.length] = value;

        parameters.put(name, newValue);
    }

    /** Attempts to get a parameter for this request. It first looks in the
     * underlying HttpServletRequest object for the parameter, and if that
     * doesn't exist it looks for the parameters retrieved from the multipart
     * request
     *
     * @param name
     *            the name
     * @return the parameter
     */
    @Override
    public String getParameter(String name) {
        String value = request.getParameter(name);
        if (value == null) {
            String[] mValue = (String[]) parameters.get(name);
            if ((mValue != null) && (mValue.length > 0)) {
                value = mValue[0];
            }
        }
        return value;
    }

    /** Returns the names of the parameters for this request. The enumeration
     * consists of the normal request parameter names plus the parameters read
     * from the multipart request
     *
     * @return the parameter names
     */
    public Enumeration getParameterNames() {
        Enumeration baseParams = request.getParameterNames();
        Vector list = new Vector();
        while (baseParams.hasMoreElements()) {
            list.add(baseParams.nextElement());
        }
        Collection multipartParams = parameters.keySet();
        Iterator iterator = multipartParams.iterator();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        return Collections.enumeration(list);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getParameterValues(java.lang.String)
     */
    public String[] getParameterValues(String name) {
        String[] value = request.getParameterValues(name);
        if (value == null) {
            value = (String[]) parameters.get(name);
        }
        return value;
    }

    /** Returns the underlying HttpServletRequest for this wrapper.
     *
     * @return the request
     */
    public HttpServletRequest getRequest() {
        return new HttpServletRequestWrapper(this);
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getAttribute(java.lang.String)
     */
    //WRAPPER IMPLEMENTATIONS OF SERVLET REQUEST METHODS
    public Object getAttribute(String name) {
        return request.getAttribute(name);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getAttributeNames()
     */
    public Enumeration getAttributeNames() {
        return request.getAttributeNames();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getCharacterEncoding()
     */
    public String getCharacterEncoding() {
        return request.getCharacterEncoding();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getContentLength()
     */
    public int getContentLength() {
        return request.getContentLength();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getContentType()
     */
    public String getContentType() {
        return request.getContentType();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getInputStream()
     */
    public ServletInputStream getInputStream() throws IOException {
        return request.getInputStream();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getProtocol()
     */
    public String getProtocol() {
        return request.getProtocol();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getScheme()
     */
    public String getScheme() {
        return request.getScheme();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getServerName()
     */
    public String getServerName() {
        return request.getServerName();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getServerPort()
     */
    public int getServerPort() {
        return request.getServerPort();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getReader()
     */
    public BufferedReader getReader() throws IOException {
        return request.getReader();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRemoteAddr()
     */
    public String getRemoteAddr() {
        return request.getRemoteAddr();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRemoteHost()
     */
    public String getRemoteHost() {
        return request.getRemoteHost();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String name, Object o) {
        request.setAttribute(name, o);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name) {
        request.removeAttribute(name);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getLocale()
     */
    public Locale getLocale() {
        return request.getLocale();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getLocales()
     */
    public Enumeration getLocales() {
        return request.getLocales();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#isSecure()
     */
    public boolean isSecure() {
        return request.isSecure();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String path) {
        return request.getRequestDispatcher(path);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.ServletRequest#getRealPath(java.lang.String)
     */
    public String getRealPath(String path) {
        return request.getRealPath(path);
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

    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getAuthType()
     */
    //WRAPPER IMPLEMENTATIONS OF HTTPSERVLETREQUEST METHODS
    public String getAuthType() {
        return request.getAuthType();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getCookies()
     */
    public Cookie[] getCookies() {
        return request.getCookies();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getDateHeader(java.lang.String)
     */
    public long getDateHeader(String name) {
        return request.getDateHeader(name);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeader(java.lang.String)
     */
    public String getHeader(String name) {
        return request.getHeader(name);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeaders(java.lang.String)
     */
    public Enumeration getHeaders(String name) {
        return request.getHeaders(name);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getHeaderNames()
     */
    public Enumeration getHeaderNames() {
        return request.getHeaderNames();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getIntHeader(java.lang.String)
     */
    public int getIntHeader(String name) {
        return request.getIntHeader(name);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getMethod()
     */
    public String getMethod() {
        return request.getMethod();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getPathInfo()
     */
    public String getPathInfo() {
        return request.getPathInfo();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getPathTranslated()
     */
    public String getPathTranslated() {
        return request.getPathTranslated();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getContextPath()
     */
    public String getContextPath() {
        return request.getContextPath();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getQueryString()
     */
    public String getQueryString() {
        return request.getQueryString();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRemoteUser()
     */
    public String getRemoteUser() {
        return request.getRemoteUser();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isUserInRole(java.lang.String)
     */
    public boolean isUserInRole(String user) {
        return request.isUserInRole(user);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getUserPrincipal()
     */
    public Principal getUserPrincipal() {
        return request.getUserPrincipal();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRequestedSessionId()
     */
    public String getRequestedSessionId() {
        return request.getRequestedSessionId();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getRequestURI()
     */
    public String getRequestURI() {
        return request.getRequestURI();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getServletPath()
     */
    public String getServletPath() {
        return request.getServletPath();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getSession(boolean)
     */
    public HttpSession getSession(boolean create) {
        return request.getSession(create);
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#getSession()
     */
    public HttpSession getSession() {
        return request.getSession();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdValid()
     */
    public boolean isRequestedSessionIdValid() {
        return request.isRequestedSessionIdValid();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromURL()
     */
    public boolean isRequestedSessionIdFromURL() {
        return request.isRequestedSessionIdFromURL();
    }
    
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServletRequest#isRequestedSessionIdFromUrl()
     */
    public boolean isRequestedSessionIdFromUrl() {
        return request.isRequestedSessionIdFromUrl();
    }

    //SERVLET 2.3 EMPTY METHODS
    /** This method returns null. To use any Servlet 2.3 methods, call on
     * getRequest() and use that request object. Once Servlet 2.3 is required to
     * build Struts, this will no longer be an issue.
     *
     * @return the parameter map
     */
    public Map getParameterMap() {
        return parameters;
    }
    
    /** This method does nothing. To use any Servlet 2.3 methods, call on
     * getRequest() and use that request object. Once Servlet 2.3 is required to
     * build Struts, this will no longer be an issue.
     *
     * @param encoding
     *            the new character encoding
     */
    public void setCharacterEncoding(String encoding) {
        ;
    }
    
    /** This method returns null. To use any Servlet 2.3 methods, call on
     * getRequest() and use that request object. Once Servlet 2.3 is required to
     * build Struts, this will no longer be an issue.
     *
     * @return the request url
     */
    public StringBuffer getRequestURL() {
        return null;
    }
    
    /** This method returns false. To use any Servlet 2.3 methods, call on
     * getRequest() and use that request object. Once Servlet 2.3 is required to
     * build Struts, this will no longer be an issue.
     *
     * @return true, if is requested session id from cookie
     */
    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }








}
