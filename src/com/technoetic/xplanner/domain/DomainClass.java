package com.technoetic.xplanner.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DomainClass {
	private final Class javaClass;
	private final String typeName;
	private String parentProperty;
	private Class parentClass;
	private String childrenProperty;
	private Map actionByName = new HashMap();
	private final List actions = new ArrayList();

	public DomainClass(final String typeName, final Class myClass) {
		this.javaClass = myClass;
		this.typeName = typeName;
	}

	public DomainClass(final String typeName, final Class myClass,
			final String parentProperty, final Class parentClass,
			final String childrenProperty) {
		this(typeName, myClass);
		this.parentProperty = parentProperty;
		this.parentClass = parentClass;
		this.childrenProperty = childrenProperty;
	}

	public String getParentProperty() {
		return this.parentProperty;
	}

	public String getChildrenProperty() {
		return this.childrenProperty;
	}

	public String getTypeName() {
		return this.typeName;
	}

	public Class getParentClass() {
		return this.parentClass;
	}

	public Class getJavaClass() {
		return this.javaClass;
	}

	public List getActions() {
		return this.actions;
	}

	public Map getActionByName() {
		return this.actionByName;
	}

	public void setActionByName(final Map actionByName) {
		this.actionByName = actionByName;
	}

	public void addMapping(final ActionMapping action) {
		this.actions.add(action);
		this.actionByName.put(action.getName(), action);
	}

}
