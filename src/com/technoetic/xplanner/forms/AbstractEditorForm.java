package com.technoetic.xplanner.forms;

import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.util.MessageResources;

import com.technoetic.xplanner.format.DecimalFormat;

public abstract class AbstractEditorForm extends ActionForm {
	private String action;
	private String oid;
	private boolean merge;
	private int id;

	protected static SimpleDateFormat dateTimeConverter;
	protected static SimpleDateFormat dateConverter;
	protected static DecimalFormat decimalConverter;
	protected static NumberFormat numberConverter;

	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
		this.action = null;
		this.oid = null;
		this.merge = false;
		this.id = 0;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(final String action) {
		this.action = action;
	}

	public boolean isSubmitted() {
		return AbstractEditorForm.isPresent(this.action);
	}

	public String getMode() {
		return this.oid == null ? "create" : "modify";
	}

	public void setOid(final String oid) {
		this.oid = oid;
	}

	public String getOid() {
		return this.oid;
	}

	public static void error(final ActionErrors errors, final String key) {
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(key));
	}

	public static void error(final ActionErrors errors, final String key,
			final Object[] values) {
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(key, values));
	}

	public static boolean isPresent(final String value) {
		return value != null && !value.equals("") && !value.equals("null");
	}

	public static void require(final ActionErrors errors, final String value,
			final String msgkey) {
		if (!AbstractEditorForm.isPresent(value)) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	public static void require(final ActionErrors errors, final Date value,
			final String msgkey) {
		if (value.getTime() == 0) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	public static void require(final ActionErrors errors, final int value,
			final String msgkey) {
		if (value == 0) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	public static void require(final ActionErrors errors, final boolean valid,
			final String msgkey) {
		if (!valid) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	public static void require(final ActionErrors errors, final FormFile value,
			final String msgkey) {
		if (value == null || value.getFileSize() == 0) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	public static void require(final ActionErrors errors, final byte[] value,
			final String msgkey) {
		if (value == null || value.length == 0) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	public void setMerge(final boolean merge) {
		this.merge = merge;
	}

	public boolean isMerge() {
		return this.merge;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public static MessageResources getResources(final HttpServletRequest request) {
		MessageResources resources = (MessageResources) request
				.getAttribute(Globals.MESSAGES_KEY);
		if (resources == null) {
			resources = (MessageResources) request.getSession().getAttribute(
					Globals.MESSAGES_KEY);
		}
		if (resources == null) {
			resources = (MessageResources) request.getSession()
					.getServletContext().getAttribute(Globals.MESSAGES_KEY);
		}

		return resources;
	}

	public static void ensureSize(final List<String> list, final int size) {
		if (size > list.size()) {
			for (int i = list.size(); i < size; i++) {
				list.add(null);
			}
		}
	}

	public static void initConverters(final HttpServletRequest request) {
		if (AbstractEditorForm.dateTimeConverter == null) {
			final String format = AbstractEditorForm.getResources(request)
					.getMessage("format.datetime");
			AbstractEditorForm.dateTimeConverter = new SimpleDateFormat(format);
		}

		if (AbstractEditorForm.dateConverter == null) {
			final String format = AbstractEditorForm.getResources(request)
					.getMessage("format.date");
			AbstractEditorForm.dateConverter = new SimpleDateFormat(format);
		}

		if (AbstractEditorForm.decimalConverter == null) {
			AbstractEditorForm.decimalConverter = new DecimalFormat(request);
		}

		if (AbstractEditorForm.numberConverter == null) {
			AbstractEditorForm.numberConverter = NumberFormat
					.getIntegerInstance();
		}
	}

	public static Date convertToDate(final String date,
			final String errorMessageKey, final ActionErrors errors) {
		return (Date) AbstractEditorForm.convert(date, errorMessageKey, errors,
				AbstractEditorForm.dateConverter);
	}

	public static Date convertToDateTime(final String date,
			final String errorMessageKey, final ActionErrors errors) {
		return (Date) AbstractEditorForm.convert(date, errorMessageKey, errors,
				AbstractEditorForm.dateTimeConverter);
	}

	public static Number convertToInt(final String integer,
			final String errorMessageKey, final ActionErrors errors) {
		return (Number) AbstractEditorForm.convert(integer, errorMessageKey,
				errors, AbstractEditorForm.numberConverter);
	}

	public static Number convertToInt(final String integer) {
		return (Number) AbstractEditorForm.convert(integer,
				AbstractEditorForm.numberConverter);
	}

	public static Object convert(final String stringValue, final Format format) {
		if (AbstractEditorForm.isPresent(stringValue)) {
			try {
				return format.parseObject(stringValue);
			} catch (final ParseException ex) {
				// don't do anything
			}
		}
		return null;
	}

	public static Object convert(final String stringValue,
			final String errorMessageKey, final ActionErrors errors,
			final Format format) {
		final Object o = AbstractEditorForm.convert(stringValue, format);
		if (o == null && AbstractEditorForm.isPresent(stringValue)) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					errorMessageKey));
		}
		return o;
	}
}
