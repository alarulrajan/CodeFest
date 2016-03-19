package net.sf.xplanner.rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Result.
 */
@XmlRootElement(name = "result")
public class Result {
	
	/** The is error. */
	private boolean isError;
	
	/** The code. */
	private int code;
	
	/** The decription. */
	private String decription;

	/**
     * Instantiates a new result.
     */
	public Result() {
		// Default constructor
	}

	/**
     * Instantiates a new result.
     *
     * @param isError
     *            the is error
     * @param code
     *            the code
     * @param decription
     *            the decription
     */
	public Result(final boolean isError, final int code, final String decription) {
		this.isError = isError;
		this.code = code;
		this.decription = decription;
	}

	/**
     * Checks if is error.
     *
     * @return true, if is error
     */
	public boolean isError() {
		return this.isError;
	}

	/**
     * Sets the error.
     *
     * @param isError
     *            the new error
     */
	public void setError(final boolean isError) {
		this.isError = isError;
	}

	/**
     * Gets the code.
     *
     * @return the code
     */
	public int getCode() {
		return this.code;
	}

	/**
     * Sets the code.
     *
     * @param code
     *            the new code
     */
	public void setCode(final int code) {
		this.code = code;
	}

	/**
     * Gets the decription.
     *
     * @return the decription
     */
	public String getDecription() {
		return this.decription;
	}

	/**
     * Sets the decription.
     *
     * @param decription
     *            the new decription
     */
	public void setDecription(final String decription) {
		this.decription = decription;
	}
}
