package com.technoetic.xplanner.tags.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.type.Type;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.db.hibernate.HibernateHelper;

/**
 * The Class UseBeansTag.
 */
public class UseBeansTag extends BodyTagSupport {
    
    /** The log. */
    private final Logger log = Logger.getLogger(this.getClass());
    
    /** The query translations. */
    private static HashMap queryTranslations;

    /** The id. */
    private String id;
    
    /** The qname. */
    private String qname;
    
    /** The type. */
    private String type;
    
    /** The where. */
    private String where;
    
    /** The order. */
    private String order;
    
    /** The cache. */
    private String cache;
    
    /** The size. */
    private int size;
    
    /** The parameter values. */
    private ArrayList parameterValues = new ArrayList();
    
    /** The parameter types. */
    private ArrayList parameterTypes = new ArrayList();
    
    /** The named parameter values. */
    private HashMap namedParameterValues = new HashMap();
    
    /** The named parameter types. */
    private HashMap namedParameterTypes = new HashMap();

    /**
     * Instantiates a new use beans tag.
     */
    public UseBeansTag() {
        this.initializeQueryTranslations();
    }

    /**
     * Initialize query translations.
     */
    private synchronized void initializeQueryTranslations() {
        if (UseBeansTag.queryTranslations == null) {
            UseBeansTag.queryTranslations = new HashMap();
            final String translations = new XPlannerProperties()
                    .getProperty("hibernate.query.substitutions");
            final String[] translation = translations.split("\\s*,\\s*");
            for (int i = 0; i < translation.length; i++) {
                final String[] keyAndValue = translation[i].split("=");
                if (keyAndValue.length == 2) {
                    String value = keyAndValue[1];
                    if (value.startsWith("'")) {
                        value = value.replaceAll("^'|'$", "");
                    }
                    UseBeansTag.queryTranslations.put(keyAndValue[0], value);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#setId(java.lang.String)
     */
    @Override
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * Sets the type.
     *
     * @param type
     *            the new type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Sets the where.
     *
     * @param where
     *            the new where
     */
    public void setWhere(final String where) {
        this.where = where;
    }

    /**
     * Sets the order.
     *
     * @param order
     *            the new order
     */
    public void setOrder(final String order) {
        this.order = order;
    }

    /**
     * Sets the size.
     *
     * @param size
     *            the new size
     */
    public void setSize(final int size) {
        this.size = size;
    }

    /**
     * Sets the qname.
     *
     * @param qname
     *            the new qname
     */
    public void setQname(final String qname) {
        this.qname = qname;
    }

    /**
     * Sets the cache.
     *
     * @param cache
     *            the new cache
     */
    public void setCache(final String cache) {
        this.cache = cache;
    }

    /**
     * Adds the parameter.
     *
     * @param value
     *            the value
     * @param type
     *            the type
     */
    /* package */
    void addParameter(final Object value, final Type type) {
        this.parameterValues.add(value);
        this.parameterTypes.add(type);
    }

    /**
     * Adds the parameter.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     * @param type
     *            the type
     */
    void addParameter(final String name, final Object value, final Type type) {
        this.namedParameterValues.put(name, value);
        this.namedParameterTypes.put(name, type);
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doStartTag()
     */
    @Override
    public int doStartTag() {
        return Tag.EVAL_BODY_INCLUDE;
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        try {
            final Session session = this.getSession();
            try {
                List objects = null;
                if (this.qname == null) {
                    String hql = null;
                    if (this.getBodyContent() == null) {
                        hql = "from " + this.type;
                        if (this.where != null) {
                            hql += " where " + this.where;
                        }
                        if (this.order != null) {
                            hql += " order by " + this.order;
                        }
                    } else {
                        hql = this.getBodyContent().getString();
                    }
                    final Query query = session.createQuery(hql);
                    objects = this.bindParametersAndExecute(query);
                } else {
                    objects = new ArrayList();
                    final String[] queryNames = this.qname.split(",");
                    for (int i = 0; i < queryNames.length; i++) {
                        objects.addAll(this.bindParametersAndExecute(session
                                .getNamedQuery(queryNames[i])));
                    }
                }
                this.pageContext.setAttribute(this.id, objects);
                if (this.log.isDebugEnabled()) {
                    this.log.debug("loaded beans: " + this.id + " " + objects);
                }
            } catch (final Exception ex) {
                this.log.error("error", ex);
                throw new JspTagException(ex.toString());
            } finally {
                this.parameterTypes.clear();
                this.parameterValues.clear();
            }
        } catch (final JspTagException ex) {
            throw ex;
        } catch (final Exception ex) {
            throw new JspException(ex.toString());
        }
        return Tag.EVAL_PAGE;
    }

    /**
     * Bind parameters and execute.
     *
     * @param query
     *            the query
     * @return the list
     * @throws Exception
     *             the exception
     */
    private List bindParametersAndExecute(final Query query) throws Exception {
        if (this.cache != null) {
            query.setCacheable(true);
            query.setCacheRegion(this.cache);
        }
        for (int i = 0; i < this.parameterValues.size(); i++) {
            query.setParameter(i, this.translate(this.parameterValues.get(i)),
                    (Type) this.parameterTypes.get(i));
        }
        for (final Iterator iterator = this.namedParameterValues.keySet()
                .iterator(); iterator.hasNext();) {
            final String key = (String) iterator.next();
            query.setParameter(key,
                    this.translate(this.namedParameterValues.get(key)),
                    (Type) this.namedParameterTypes.get(key));
        }
        if (this.size > 0) {
            query.setMaxResults(this.size);
        }
        final List objects = query.list();
        return objects;
    }

    /**
     * Translate.
     *
     * @param value
     *            the value
     * @return the object
     */
    private Object translate(final Object value) {
        return UseBeansTag.queryTranslations.containsKey(value) ? UseBeansTag.queryTranslations
                .get(value) : value;
    }

    /**
     * Gets the session.
     *
     * @return the session
     * @throws Exception
     *             the exception
     */
    private Session getSession() throws Exception {
        return HibernateHelper.getSession(this.pageContext.getRequest());
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.BodyTagSupport#release()
     */
    @Override
    public void release() {
        this.id = null;
        this.qname = null;
        this.type = null;
        this.where = null;
        this.order = null;
        this.cache = null;
        this.size = 0;
        this.parameterValues = new ArrayList();
        this.parameterTypes = new ArrayList();
        this.namedParameterValues = new HashMap();
        this.namedParameterTypes = new HashMap();
        super.release();
    }
}