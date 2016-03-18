package com.technoetic.xplanner.tags.db;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.hibernate.type.Type;
import org.hibernate.type.TypeResolver;

public class UseBeansParameterTag extends TagSupport {
	private static final TypeResolver TYPE_RESOLVER = new TypeResolver();
	private String name;
	private Object value;
	private Type type;

	public void setValue(final Object value) {
		this.value = value;
	}

	public void setType(final String type) {
		this.type = UseBeansParameterTag.TYPE_RESOLVER.basic(type);
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public int doEndTag() throws JspException {
		final Tag parent = this.getParent();
		if (parent instanceof UseBeansTag) {
			if (this.name == null) {
				((UseBeansTag) parent).addParameter(this.value, this.type);
			} else {
				((UseBeansTag) parent).addParameter(this.name, this.value,
						this.type);
			}
		}
		return Tag.EVAL_PAGE;
	}

	@Override
	public void release() {
		this.name = null;
		this.value = null;
		this.type = null;
		super.release();
	}
}
