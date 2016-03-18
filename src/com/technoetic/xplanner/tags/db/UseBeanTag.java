package com.technoetic.xplanner.tags.db;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;
import org.hibernate.classic.Session;

public class UseBeanTag extends DatabaseTagSupport {
	public static final String DEFAULT_OID_PARAMETER = "oid";

	private final Logger log = Logger.getLogger(this.getClass());
	private String id;
	private String type;
	private Object oid;
	private String oidParameter;
	private String scope;

	@Override
	public void setId(final String id) {
		this.id = id;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public void setOid(final Object oid) {
		this.oid = oid;
	}

	public void setOidParameter(final String oidParameter) {
		this.oidParameter = oidParameter;
	}

	public void setScope(final String scope) {
		this.scope = scope;
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			final Session session = this.getSession();
			try {
				final Class clazz = Class.forName(this.type);
				final Object object = session.load(clazz, this.getObjectId());
				this.pageContext.setAttribute(this.id, object, this.getScope());
				if (this.log.isDebugEnabled()) {
					this.log.debug("bean loaded: " + this.id + " " + object);
				}
			} catch (final Exception ex) {
				this.log.error("error", ex);
				throw new JspTagException(ex.toString());
			}
		} catch (final JspTagException ex) {
			throw ex;
		} catch (final Exception ex) {
			throw new JspTagException(ex.toString());
		}
		return Tag.EVAL_PAGE;
	}

	private Integer getObjectId() {
		Integer objectId = null;
		if (this.oid instanceof Integer) {
			objectId = (Integer) this.oid;
		} else if (this.oid instanceof String) {
			objectId = new Integer((String) this.oid);
		} else if (this.oidParameter != null) {
			objectId = new Integer(this.pageContext.getRequest().getParameter(
					this.oidParameter));
		} else {
			final String oid = this.pageContext.getRequest().getParameter(
					UseBeanTag.DEFAULT_OID_PARAMETER);
			if (oid != null) {
				objectId = new Integer(oid);
			}
		}
		return objectId;
	}

	private int getScope() {
		if ("application".equals(this.scope)) {
			return PageContext.APPLICATION_SCOPE;
		}
		if ("session".equals(this.scope)) {
			return PageContext.SESSION_SCOPE;
		}
		if ("request".equals(this.scope)) {
			return PageContext.REQUEST_SCOPE;
		}
		return PageContext.PAGE_SCOPE;
	}

	@Override
	public void release() {
		super.release();
		this.scope = null;
	}

	@Override
	protected int doStartTagInternal() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
}
