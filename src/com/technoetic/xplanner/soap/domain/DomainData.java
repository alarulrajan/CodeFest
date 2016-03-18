package com.technoetic.xplanner.soap.domain;

import java.io.Serializable;
import java.util.Calendar;

import com.technoetic.xplanner.domain.Identifiable;

public abstract class DomainData implements Identifiable, Serializable {
	private Calendar lastUpdateTime;
	private int id;

	public Calendar getLastUpdateTime() {
		return this.lastUpdateTime;
	}

	public void setLastUpdateTime(final Calendar lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public int getId() {
		return this.id;
	}

	public void setId(final int id) {
		this.id = id;
	}
}
