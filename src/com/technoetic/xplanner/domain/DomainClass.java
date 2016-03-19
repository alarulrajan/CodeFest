package com.technoetic.xplanner.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Class DomainClass.
 */
public class DomainClass {
    
    /** The java class. */
    private final Class javaClass;
    
    /** The type name. */
    private final String typeName;
    
    /** The parent property. */
    private String parentProperty;
    
    /** The parent class. */
    private Class parentClass;
    
    /** The children property. */
    private String childrenProperty;
    
    /** The action by name. */
    private Map actionByName = new HashMap();
    
    /** The actions. */
    private final List actions = new ArrayList();

    /**
     * Instantiates a new domain class.
     *
     * @param typeName
     *            the type name
     * @param myClass
     *            the my class
     */
    public DomainClass(final String typeName, final Class myClass) {
        this.javaClass = myClass;
        this.typeName = typeName;
    }

    /**
     * Instantiates a new domain class.
     *
     * @param typeName
     *            the type name
     * @param myClass
     *            the my class
     * @param parentProperty
     *            the parent property
     * @param parentClass
     *            the parent class
     * @param childrenProperty
     *            the children property
     */
    public DomainClass(final String typeName, final Class myClass,
            final String parentProperty, final Class parentClass,
            final String childrenProperty) {
        this(typeName, myClass);
        this.parentProperty = parentProperty;
        this.parentClass = parentClass;
        this.childrenProperty = childrenProperty;
    }

    /**
     * Gets the parent property.
     *
     * @return the parent property
     */
    public String getParentProperty() {
        return this.parentProperty;
    }

    /**
     * Gets the children property.
     *
     * @return the children property
     */
    public String getChildrenProperty() {
        return this.childrenProperty;
    }

    /**
     * Gets the type name.
     *
     * @return the type name
     */
    public String getTypeName() {
        return this.typeName;
    }

    /**
     * Gets the parent class.
     *
     * @return the parent class
     */
    public Class getParentClass() {
        return this.parentClass;
    }

    /**
     * Gets the java class.
     *
     * @return the java class
     */
    public Class getJavaClass() {
        return this.javaClass;
    }

    /**
     * Gets the actions.
     *
     * @return the actions
     */
    public List getActions() {
        return this.actions;
    }

    /**
     * Gets the action by name.
     *
     * @return the action by name
     */
    public Map getActionByName() {
        return this.actionByName;
    }

    /**
     * Sets the action by name.
     *
     * @param actionByName
     *            the new action by name
     */
    public void setActionByName(final Map actionByName) {
        this.actionByName = actionByName;
    }

    /**
     * Adds the mapping.
     *
     * @param action
     *            the action
     */
    public void addMapping(final ActionMapping action) {
        this.actions.add(action);
        this.actionByName.put(action.getName(), action);
    }

}
