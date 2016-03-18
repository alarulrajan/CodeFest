package com.technoetic.xplanner.tags;

import java.io.IOException;
import java.util.ResourceBundle;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.util.RequestUtils;

import com.technoetic.xplanner.domain.CharacterEnum;

public class CharacterEnumSelectTag extends SelectTag {
	public static final String EDIT_MODE = "edit";
	public static final String VIEW_MODE = "view";
	private String mode;
	private String enumProperty;
	private Object object;
	private String objectName;
	private ResourceBundle resourceBundle;
	private String resourceType;

	@Override
	public int doStartTag() throws JspException {
		this.resourceBundle = ResourceBundle.getBundle("ResourceBundle");
		if (CharacterEnumSelectTag.EDIT_MODE.equals(this.mode)) {
			return super.doStartTag();
		} else {
			this.renderHtmlText();
			return BodyTag.EVAL_BODY_BUFFERED;
		}
	}

	@Override
	public int doEndTag() throws JspException {
		try {
			if (CharacterEnumSelectTag.EDIT_MODE.equals(this.mode)) {
				final JspWriter out = this.pageContext.getOut();
				final CharacterEnum[] enums = this.getEnumObject().listEnums();
				for (int i = 0; i < enums.length; i++) {
					final CharacterEnum enumm = enums[i];
					out.println(this.renderSelectOptionElement(enumm));
				}
				out.println("</select>");
			}
		} catch (final IOException e) {
			throw new JspException(e);
		}
		return Tag.EVAL_PAGE;
	}

	private void renderHtmlText() throws JspException {
		try {
			final JspWriter out = this.pageContext.getOut();
			final CharacterEnum e = this.getEnumObject();
			out.write(this.resourceBundle.getString(e.getNameKey()));
		} catch (final IOException e) {
			throw new JspException(e);
		}
	}

	private String renderSelectOptionElement(final CharacterEnum anEnum) {
		final StringBuffer sb = new StringBuffer();
		sb.append("<option ");
		sb.append("value='").append(anEnum.getCode()).append("'");
		if (this.isMatched(new String(String.valueOf(anEnum.getCode())))) {
			sb.append(" selected='selected'");
		}
		sb.append(">");
		sb.append(this.resourceBundle.getString(anEnum.getNameKey()));
		sb.append("</option>");
		return sb.toString();
	}

	private CharacterEnum getEnumObject() throws JspException {
		final Object resource = this.getResource();
		try {
			return (CharacterEnum) PropertyUtils.getProperty(resource,
					this.enumProperty);
		} catch (final Exception e) {
			throw new JspException(e);
		}
	}

	private Object getResource() throws JspException {
		Object resource = this.object;
		if (this.object instanceof String) {
			resource = this.pageContext.findAttribute((String) this.object);
		}
		if (resource == null && this.name != null) {
			resource = RequestUtils.lookup(this.pageContext, this.objectName,
					this.enumProperty, null);
		}
		if (resource == null) {
			resource = this.pageContext.findAttribute("project");
		}
		if (resource == null && this.resourceType == null) {
			throw new JspException(
					"object or resource type/id must be specified");
		}
		return resource;
	}

	public void setMode(final String mode) {
		this.mode = mode;
	}

	public void setEnumProperty(final String enumProperty) {
		this.enumProperty = enumProperty;
	}

	public void setObjectName(final String objectName) {
		this.objectName = objectName;
	}

	public void setObject(final Object object) {
		this.object = object;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	public void setResourceType(final String resourceType) {
		this.resourceType = resourceType;
	}
}
