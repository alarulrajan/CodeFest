package com.technoetic.xplanner.domain;

import java.util.Comparator;

import net.sf.xplanner.domain.Person;

public class NotificationReceiversComparator implements Comparator {
	@Override
	public int compare(final Object o1, final Object o2) {
		return ((Person) o1).getName().compareTo(((Person) o2).getName());
	}
}
