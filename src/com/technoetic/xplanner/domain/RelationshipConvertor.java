package com.technoetic.xplanner.domain;

import java.lang.reflect.InvocationTargetException;

import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.DomainObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;

//DEBT(METADATA) Rename this class to illustrate its responsability

/**
 * The Class RelationshipConvertor.
 *
 * @resp Convert a DTO (Form, SOAP data object) attribute to a Domain object
 *       relationship and vise-a-versa
 */
public class RelationshipConvertor {
    
    /** The adapter property. */
    private final String adapterProperty;
    
    /** The domain property. */
    private final String domainProperty;

    /**
     * Instantiates a new relationship convertor.
     *
     * @param adapterProperty
     *            the adapter property
     * @param domainObjectProperty
     *            the domain object property
     */
    public RelationshipConvertor(final String adapterProperty,
            final String domainObjectProperty) {
        this.adapterProperty = adapterProperty;
        this.domainProperty = domainObjectProperty;
    }

    /**
     * Gets the adapter property.
     *
     * @return the adapter property
     */
    public String getAdapterProperty() {
        return this.adapterProperty;
    }

    /**
     * Gets the domain property.
     *
     * @return the domain property
     */
    public String getDomainProperty() {
        return this.domainProperty;
    }

    /**
     * Populate domain object.
     *
     * @param destination
     *            the destination
     * @param source
     *            the source
     * @param commonDao
     *            the common dao
     * @throws HibernateException
     *             the hibernate exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws NoSuchMethodException
     *             the no such method exception
     */
    public void populateDomainObject(final DomainObject destination,
            final Object source, final CommonDao<?> commonDao)
            throws HibernateException, IllegalAccessException,
            InvocationTargetException, NoSuchMethodException {
        if (PropertyUtils.isReadable(source, this.adapterProperty)
                && PropertyUtils.isWriteable(destination, this.domainProperty)) {
            final Integer referredId = (Integer) PropertyUtils.getProperty(
                    source, this.adapterProperty);
            final Class destinationType = PropertyUtils.getPropertyType(
                    destination, this.domainProperty);
            final Object referred = this.findObjectById(commonDao,
                    destinationType, referredId);
            PropertyUtils.setProperty(destination, this.domainProperty,
                    referred);
        }
    }

    /**
     * Find object by id.
     *
     * @param commonDao
     *            the common dao
     * @param aClass
     *            the a class
     * @param id
     *            the id
     * @return the object
     * @throws HibernateException
     *             the hibernate exception
     */
    private Object findObjectById(final CommonDao<?> commonDao,
            final Class aClass, final Integer id) throws HibernateException {
        if (id.intValue() == 0) {
            return null;
        }
        return commonDao.getById(aClass, id);
    }

    /**
     * Populate adapter.
     *
     * @param adapter
     *            the adapter
     * @param domainObject
     *            the domain object
     * @throws NoSuchMethodException
     *             the no such method exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     */
    public void populateAdapter(final Object adapter,
            final DomainObject domainObject) throws NoSuchMethodException,
            IllegalAccessException, InvocationTargetException {
        final Object referred = PropertyUtils.getProperty(domainObject,
                this.domainProperty);
        final Integer id = referred == null ? new Integer(0)
                : (Integer) PropertyUtils.getProperty(referred, "id");
        PropertyUtils.setProperty(adapter, this.adapterProperty, id);
    }

}