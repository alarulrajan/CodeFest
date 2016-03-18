package net.sf.xplanner.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
public class Result {
	private boolean isError;
	private int code;
	private String decription;

	public Result() {
		// Default constructor
	}

	public Result(final boolean isError, final int code, final String decription) {
		this.isError = isError;
		this.code = code;
		this.decription = decription;
	}

	public boolean isError() {
		return this.isError;
	}

	public void setError(final boolean isError) {
		this.isError = isError;
	}

	public int getCode() {
		return this.code;
	}

	public void setCode(final int code) {
		this.code = code;
	}

	public String getDecription() {
		return this.decription;
	}

	public void setDecription(final String decription) {
		this.decription = decription;
	}
}
