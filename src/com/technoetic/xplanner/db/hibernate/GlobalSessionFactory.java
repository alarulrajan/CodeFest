package com.technoetic.xplanner.db.hibernate;

import org.hibernate.SessionFactory;

public class GlobalSessionFactory {
	private static SessionFactory factory;

	public static SessionFactory get() {
		return GlobalSessionFactory.factory;
	}

	public static void set(final SessionFactory factory) {
		GlobalSessionFactory.factory = factory;
	}
}