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

/**
 * The Class AbstractEditorForm.
 */
public abstract class AbstractEditorForm extends ActionForm {
	
	/** The action. */
	private String action;
	
	/** The oid. */
	private String oid;
	
	/** The merge. */
	private boolean merge;
	
	/** The id. */
	private int id;

	/** The date time converter. */
	protected static SimpleDateFormat dateTimeConverter;
	
	/** The date converter. */
	protected static SimpleDateFormat dateConverter;
	
	/** The decimal converter. */
	protected static DecimalFormat decimalConverter;
	
	/** The number converter. */
	protected static NumberFormat numberConverter;

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public void reset(final ActionMapping mapping,
			final HttpServletRequest request) {
		super.reset(mapping, request);
		this.action = null;
		this.oid = null;
		this.merge = false;
		this.id = 0;
	}

	/**
     * Gets the action.
     *
     * @return the action
     */
	public String getAction() {
		return this.action;
	}

	/**
     * Sets the action.
     *
     * @param action
     *            the new action
     */
	public void setAction(final String action) {
		this.action = action;
	}

	/**
     * Checks if is submitted.
     *
     * @return true, if is submitted
     */
	public boolean isSubmitted() {
		return AbstractEditorForm.isPresent(this.action);
	}

	/**
     * Gets the mode.
     *
     * @return the mode
     */
	public String getMode() {
		return this.oid == null ? "create" : "modify";
	}

	/**
     * Sets the oid.
     *
     * @param oid
     *            the new oid
     */
	public void setOid(final String oid) {
		this.oid = oid;
	}

	/**
     * Gets the oid.
     *
     * @return the oid
     */
	public String getOid() {
		return this.oid;
	}

	/**
     * Error.
     *
     * @param errors
     *            the errors
     * @param key
     *            the key
     */
	public static void error(final ActionErrors errors, final String key) {
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(key));
	}

	/**
     * Error.
     *
     * @param errors
     *            the errors
     * @param key
     *            the key
     * @param values
     *            the values
     */
	public static void error(final ActionErrors errors, final String key,
			final Object[] values) {
		errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(key, values));
	}

	/**
     * Checks if is present.
     *
     * @param value
     *            the value
     * @return true, if is present
     */
	public static boolean isPresent(final String value) {
		return value != null && !value.equals("") && !value.equals("null");
	}

	/**
     * Require.
     *
     * @param errors
     *            the errors
     * @param value
     *            the value
     * @param msgkey
     *            the msgkey
     */
	public static void require(final ActionErrors errors, final String value,
			final String msgkey) {
		if (!AbstractEditorForm.isPresent(value)) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	/**
     * Require.
     *
     * @param errors
     *            the errors
     * @param value
     *            the value
     * @param msgkey
     *            the msgkey
     */
	public static void require(final ActionErrors errors, final Date value,
			final String msgkey) {
		if (value.getTime() == 0) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	/**
     * Require.
     *
     * @param errors
     *            the errors
     * @param value
     *            the value
     * @param msgkey
     *            the msgkey
     */
	public static void require(final ActionErrors errors, final int value,
			final String msgkey) {
		if (value == 0) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	/**
     * Require.
     *
     * @param errors
     *            the errors
     * @param valid
     *            the valid
     * @param msgkey
     *            the msgkey
     */
	public static void require(final ActionErrors errors, final boolean valid,
			final String msgkey) {
		if (!valid) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	/**
     * Require.
     *
     * @param errors
     *            the errors
     * @param value
     *            the value
     * @param msgkey
     *            the msgkey
     */
	public static void require(final ActionErrors errors, final FormFile value,
			final String msgkey) {
		if (value == null || value.getFileSize() == 0) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	/**
     * Require.
     *
     * @param errors
     *            the errors
     * @param value
     *            the value
     * @param msgkey
     *            the msgkey
     */
	public static void require(final ActionErrors errors, final byte[] value,
			final String msgkey) {
		if (value == null || value.length == 0) {
			AbstractEditorForm.error(errors, msgkey);
		}
	}

	/**
     * Sets the merge.
     *
     * @param merge
     *            the new merge
     */
	public void setMerge(final boolean merge) {
		this.merge = merge;
	}

	/**
     * Checks if is merge.
     *
     * @return true, if is merge
     */
	public boolean isMerge() {
		return this.merge;
	}

	/**
     * Sets the id.
     *
     * @param id
     *            the new id
     */
	public void setId(final int id) {
		this.id = id;
	}

	/**
     * Gets the id.
     *
     * @return the id
     */
	public int getId() {
		return this.id;
	}

	/**
     * Gets the resources.
     *
     * @param request
     *            the request
     * @return the resources
     */
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

	/**
     * Ensure size.
     *
     * @param list
     *            the list
     * @param size
     *            the size
     */
	public static void ensureSize(final List<String> list, final int size) {
		if (size > list.size()) {
			for (int i = list.size(); i < size; i++) {
				list.add(null);
			}
		}
	}

	/**
     * Inits the converters.
     *
     * @param request
     *            the request
     */
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

	/**
     * Convert to date.
     *
     * @param date
     *            the date
     * @param errorMessageKey
     *            the error message key
     * @param errors
     *            the errors
     * @return the date
     */
	public static Date convertToDate(final String date,
			final String errorMessageKey, final ActionErrors errors) {
		return (Date) AbstractEditorForm.convert(date, errorMessageKey, errors,
				AbstractEditorForm.dateConverter);
	}

	/**
     * Convert to date time.
     *
     * @param date
     *            the date
     * @param errorMessageKey
     *            the error message key
     * @param errors
     *            the errors
     * @return the date
     */
	public static Date convertToDateTime(final String date,
			final String errorMessageKey, final ActionErrors errors) {
		return (Date) AbstractEditorForm.convert(date, errorMessageKey, errors,
				AbstractEditorForm.dateTimeConverter);
	}

	/**
     * Convert to int.
     *
     * @param integer
     *            the integer
     * @param errorMessageKey
     *            the error message key
     * @param errors
     *            the errors
     * @return the number
     */
	public static Number convertToInt(final String integer,
			final String errorMessageKey, final ActionErrors errors) {
		return (Number) AbstractEditorForm.convert(integer, errorMessageKey,
				errors, AbstractEditorForm.numberConverter);
	}

	/**
     * Convert to int.
     *
     * @param integer
     *            the integer
     * @return the number
     */
	public static Number convertToInt(final String integer) {
		return (Number) AbstractEditorForm.convert(integer,
				AbstractEditorForm.numberConverter);
	}

	/**
     * Convert.
     *
     * @param stringValue
     *            the string value
     * @param format
     *            the format
     * @return the object
     */
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

	/**
     * Convert.
     *
     * @param stringValue
     *            the string value
     * @param errorMessageKey
     *            the error message key
     * @param errors
     *            the errors
     * @param format
     *            the format
     * @return the object
     */
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
