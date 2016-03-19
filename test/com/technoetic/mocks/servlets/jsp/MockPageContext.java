package com.technoetic.mocks.servlets.jsp;

import java.io.IOException;
import java.util.HashMap;

import javax.el.ELContext;
import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

/**
 * The Class MockPageContext.
 */
public class MockPageContext extends PageContext {

    /** The page attributes. */
    public HashMap pageAttributes = new HashMap();

    /** The initialize called. */
    public boolean initializeCalled;
    
    /** The initialize io exception. */
    public java.io.IOException initializeIOException;
    
    /** The initialize illegal state exception. */
    public java.lang.IllegalStateException initializeIllegalStateException;
    
    /** The initialize illegal argument exception. */
    public java.lang.IllegalArgumentException initializeIllegalArgumentException;
    
    /** The initialize servlet. */
    public javax.servlet.Servlet initializeServlet;
    
    /** The initialize request. */
    public javax.servlet.ServletRequest initializeRequest;
    
    /** The initialize response. */
    public javax.servlet.ServletResponse initializeResponse;
    
    /** The initialize error page url. */
    public java.lang.String initializeErrorPageURL;
    
    /** The initialize needs session. */
    public boolean initializeNeedsSession;
    
    /** The initialize buffer size. */
    public int initializeBufferSize;
    
    /** The initialize auto flush. */
    public boolean initializeAutoFlush;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#initialize(javax.servlet.Servlet, javax.servlet.ServletRequest, javax.servlet.ServletResponse, java.lang.String, boolean, int, boolean)
     */
    @Override
    public void initialize(javax.servlet.Servlet servlet, javax.servlet.ServletRequest request,
            javax.servlet.ServletResponse response, java.lang.String errorPageURL,
            boolean needsSession, int bufferSize, boolean autoFlush)
            throws java.io.IOException,
            java.lang.IllegalStateException,
            java.lang.IllegalArgumentException {
        initializeCalled = true;
        initializeServlet = servlet;
        initializeRequest = request;
        initializeResponse = response;
        initializeErrorPageURL = errorPageURL;
        initializeNeedsSession = needsSession;
        initializeBufferSize = bufferSize;
        initializeAutoFlush = autoFlush;
        if (initializeIOException != null) {
            throw initializeIOException;
        }
        if (initializeIllegalStateException != null) {
            throw initializeIllegalStateException;
        }
        if (initializeIllegalArgumentException != null) {
            throw initializeIllegalArgumentException;
        }
    }

    /** The release called. */
    public boolean releaseCalled;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#release()
     */
    @Override
    public void release() {
        releaseCalled = true;
    }

    /** The set attribute called. */
    public boolean setAttributeCalled;
    
    /** The set attribute name. */
    public java.lang.String setAttributeName;
    
    /** The set attribute attribute. */
    public java.lang.Object setAttributeAttribute;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#setAttribute(java.lang.String, java.lang.Object)
     */
    @Override
    public void setAttribute(java.lang.String name, java.lang.Object attribute) {
        setAttributeCalled = true;
        setAttributeName = name;
        setAttributeAttribute = attribute;
        pageAttributes.put(name, attribute);
    }

    /** The set attribute2 called. */
    public boolean setAttribute2Called;
    
    /** The set attribute2 name. */
    public String setAttribute2Name;
    
    /** The set attribute2 attribute. */
    public java.lang.Object setAttribute2Attribute;
    
    /** The set attribute2 scope. */
    public int setAttribute2Scope;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#setAttribute(java.lang.String, java.lang.Object, int)
     */
    @Override
    public void setAttribute(java.lang.String name, java.lang.Object o, int scope) {
        setAttribute2Called = true;
        setAttribute2Name = name;
        setAttribute2Attribute = o;
        setAttribute2Scope = scope;
    }

    /** The get attribute called. */
    public boolean getAttributeCalled;
    
    /** The get attribute return. */
    public java.lang.Object getAttributeReturn;
    
    /** The get attribute name. */
    public java.lang.String getAttributeName;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getAttribute(java.lang.String)
     */
    @Override
    public java.lang.Object getAttribute(java.lang.String name) {
        getAttributeCalled = true;
        getAttributeName = name;
        if (getAttributeReturn != null) {
            return getAttributeReturn;
        } else {
            return pageAttributes.get(name);
        }
    }

    /** The get attribute2 called. */
    public boolean getAttribute2Called;
    
    /** The get attribute2 return. */
    public java.lang.Object getAttribute2Return;
    
    /** The get attribute2 name. */
    public java.lang.String getAttribute2Name;
    
    /** The get attribute2 scope. */
    public int getAttribute2Scope;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getAttribute(java.lang.String, int)
     */
    @Override
    public java.lang.Object getAttribute(java.lang.String name, int scope) {
        getAttribute2Called = true;
        getAttribute2Name = name;
        getAttribute2Scope = scope;
        return getAttribute2Return;
    }

    /** The find attribute called. */
    public boolean findAttributeCalled;
    
    /** The find attribute return. */
    public java.lang.Object findAttributeReturn;
    
    /** The find attribute name. */
    public java.lang.String findAttributeName;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#findAttribute(java.lang.String)
     */
    @Override
    public java.lang.Object findAttribute(java.lang.String name) {
        findAttributeCalled = true;
        findAttributeName = name;
        if (findAttributeReturn != null) {
            return findAttributeReturn;
        } else {
            return pageAttributes.get(name);
        }
    }

    /** The remove attribute called. */
    public boolean removeAttributeCalled;
    
    /** The remove attribute name. */
    public java.lang.String removeAttributeName;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#removeAttribute(java.lang.String)
     */
    @Override
    public void removeAttribute(java.lang.String name) {
        removeAttributeCalled = true;
        removeAttributeName = name;
    }

    /** The remove attribute2 called. */
    public boolean removeAttribute2Called;
    
    /** The remove attribute2 name. */
    public java.lang.String removeAttribute2Name;
    
    /** The remove attribute2 scope. */
    public int removeAttribute2Scope;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#removeAttribute(java.lang.String, int)
     */
    @Override
    public void removeAttribute(java.lang.String name, int scope) {
        removeAttribute2Called = true;
        removeAttribute2Name = name;
        removeAttribute2Scope = scope;
    }

    /** The get attributes scope called. */
    public boolean getAttributesScopeCalled;
    
    /** The get attributes scope return. */
    public Integer getAttributesScopeReturn;
    
    /** The get attributes scope name. */
    public java.lang.String getAttributesScopeName;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getAttributesScope(java.lang.String)
     */
    @Override
    public int getAttributesScope(java.lang.String name) {
        getAttributesScopeCalled = true;
        getAttributesScopeName = name;
        return getAttributesScopeReturn.intValue();
    }

    /** The get attribute names in scope called. */
    public boolean getAttributeNamesInScopeCalled;
    
    /** The get attribute names in scope return. */
    public java.util.Enumeration getAttributeNamesInScopeReturn;
    
    /** The get attribute names in scope scope. */
    public int getAttributeNamesInScopeScope;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getAttributeNamesInScope(int)
     */
    @Override
    public java.util.Enumeration getAttributeNamesInScope(int scope) {
        getAttributeNamesInScopeCalled = true;
        getAttributeNamesInScopeScope = scope;
        return getAttributeNamesInScopeReturn;
    }

    /** The get out called. */
    public boolean getOutCalled;
    
    /** The get out return. */
    public javax.servlet.jsp.JspWriter getOutReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getOut()
     */
    @Override
    public javax.servlet.jsp.JspWriter getOut() {
        getOutCalled = true;
        return getOutReturn;
    }

    /** Gets the expression evaluator.
     *
     * @return the expression evaluator
     */
    @Override
    public ExpressionEvaluator getExpressionEvaluator() {
        return null;
    }

    /** Gets the variable resolver.
     *
     * @return the variable resolver
     */
    @Override
    public VariableResolver getVariableResolver() {
        return null;
    }

    /** The get session called. */
    public boolean getSessionCalled;
    
    /** The get session return. */
    public javax.servlet.http.HttpSession getSessionReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getSession()
     */
    @Override
    public javax.servlet.http.HttpSession getSession() {
        getSessionCalled = true;
        return getSessionReturn;
    }

    /** The get page called. */
    public boolean getPageCalled;
    
    /** The get page return. */
    public java.lang.Object getPageReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getPage()
     */
    @Override
    public java.lang.Object getPage() {
        getPageCalled = true;
        return getPageReturn;
    }

    /** The get request called. */
    public boolean getRequestCalled;
    
    /** The get request return. */
    public javax.servlet.ServletRequest getRequestReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getRequest()
     */
    @Override
    public javax.servlet.ServletRequest getRequest() {
        getRequestCalled = true;
        return getRequestReturn;
    }

    /** The get response called. */
    public boolean getResponseCalled;
    
    /** The get response return. */
    public javax.servlet.ServletResponse getResponseReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getResponse()
     */
    @Override
    public javax.servlet.ServletResponse getResponse() {
        getResponseCalled = true;
        return getResponseReturn;
    }

    /** The get exception called. */
    public boolean getExceptionCalled;
    
    /** The get exception return. */
    public java.lang.Exception getExceptionReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getException()
     */
    @Override
    public java.lang.Exception getException() {
        getExceptionCalled = true;
        return getExceptionReturn;
    }

    /** The get servlet config called. */
    public boolean getServletConfigCalled;
    
    /** The get servlet config return. */
    public javax.servlet.ServletConfig getServletConfigReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getServletConfig()
     */
    @Override
    public javax.servlet.ServletConfig getServletConfig() {
        getServletConfigCalled = true;
        return getServletConfigReturn;
    }

    /** The get servlet context called. */
    public boolean getServletContextCalled;
    
    /** The get servlet context return. */
    public javax.servlet.ServletContext getServletContextReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#getServletContext()
     */
    @Override
    public javax.servlet.ServletContext getServletContext() {
        getServletContextCalled = true;
        return getServletContextReturn;
    }

    /** The forward called. */
    public boolean forwardCalled;
    
    /** The forward servlet exception. */
    public javax.servlet.ServletException forwardServletException;
    
    /** The forward io exception. */
    public java.io.IOException forwardIOException;
    
    /** The forward relative url path. */
    public java.lang.String forwardRelativeUrlPath;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#forward(java.lang.String)
     */
    @Override
    public void forward(java.lang.String relativeUrlPath) throws javax.servlet.ServletException, java.io.IOException {
        forwardCalled = true;
        forwardRelativeUrlPath = relativeUrlPath;
        if (forwardServletException != null) {
            throw forwardServletException;
        }
        if (forwardIOException != null) {
            throw forwardIOException;
        }
    }

    /** The include called. */
    public boolean includeCalled;
    
    /** The include servlet exception. */
    public javax.servlet.ServletException includeServletException;
    
    /** The include io exception. */
    public java.io.IOException includeIOException;
    
    /** The include relative url path. */
    public java.lang.String includeRelativeUrlPath;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#include(java.lang.String)
     */
    @Override
    public void include(java.lang.String relativeUrlPath) throws javax.servlet.ServletException, java.io.IOException {
        includeCalled = true;
        includeRelativeUrlPath = relativeUrlPath;
        if (includeServletException != null) {
            throw includeServletException;
        }
        if (includeIOException != null) {
            throw includeIOException;
        }
    }

    /** Include.
     *
     * @param string
     *            the string
     * @param b
     *            the b
     * @throws ServletException
     *             the servlet exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    @Override
    public void include(String string, boolean b) throws ServletException, IOException {

    }

    /** The handle page exception called. */
    public boolean handlePageExceptionCalled;
    
    /** The handle page exception servlet exception. */
    public javax.servlet.ServletException handlePageExceptionServletException;
    
    /** The handle page exception io exception. */
    public java.io.IOException handlePageExceptionIOException;
    
    /** The handle page exception e. */
    public java.lang.Exception handlePageExceptionE;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Exception)
     */
    @Override
    public void handlePageException(java.lang.Exception e) throws javax.servlet.ServletException, java.io.IOException {
        handlePageExceptionCalled = true;
        handlePageExceptionE = e;
        if (handlePageExceptionServletException != null) {
            throw handlePageExceptionServletException;
        }
        if (handlePageExceptionIOException != null) {
            throw handlePageExceptionIOException;
        }
    }

    /** The push body called. */
    public boolean pushBodyCalled;
    
    /** The push body return. */
    public javax.servlet.jsp.tagext.BodyContent pushBodyReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#pushBody()
     */
    @Override
    public javax.servlet.jsp.tagext.BodyContent pushBody() {
        pushBodyCalled = true;
        return pushBodyReturn;
    }

    /** The pop body called. */
    public boolean popBodyCalled;
    
    /** The pop body return. */
    public javax.servlet.jsp.JspWriter popBodyReturn;

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#popBody()
     */
    @Override
    public javax.servlet.jsp.JspWriter popBody() {
        popBodyCalled = true;
        return popBodyReturn;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.PageContext#handlePageException(java.lang.Throwable)
     */
    @Override
    public void handlePageException(Throwable ex) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the EL context.
     *
     * @return the EL context
     */
    @Override
    public ELContext getELContext() {
        return null;
    }

}