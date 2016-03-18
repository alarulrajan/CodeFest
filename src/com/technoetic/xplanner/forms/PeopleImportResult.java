package com.technoetic.xplanner.forms;

public class PeopleImportResult {
	private int lineNbr;
	private String id;
	private String loginId;
	private String name;
	private String status;

	public int getLineNbr() {
		return this.lineNbr;
	}

	public void setLineNbr(final int lineNbr) {
		this.lineNbr = lineNbr;
	}

	public String getId() {
		return this.id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public String getLoginId() {
		return this.loginId;
	}

	public void setLoginId(final String loginId) {
		this.loginId = loginId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(final String status) {
		this.status = status;
	}

}
