package com.technoetic.xplanner.domain;

import java.lang.reflect.InvocationTargetException;

import net.sf.xplanner.dao.impl.CommonDao;
import net.sf.xplanner.domain.DomainObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.HibernateException;

//DEBT(METADATA) Rename this class to illustrate its responsability

/**
 * @resp Convert a DTO (Form, SOAP data object) attribute to a Domain object
 *       relationship and vise-a-versa
 */
public class RelationshipConvertor {
	private final String adapterProperty;
	private final String domainProperty;

	public RelationshipConvertor(final String adapterProperty,
			final String domainObjectProperty) {
		this.adapterProperty = adapterProperty;
		this.domainProperty = domainObjectProperty;
	}

	public String getAdapterProperty() {
		return this.adapterProperty;
	}

	public String getDomainProperty() {
		return this.domainProperty;
	}

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

	private Object findObjectById(final CommonDao<?> commonDao,
			final Class aClass, final Integer id) throws HibernateException {
		if (id.intValue() == 0) {
			return null;
		}
		return commonDao.getById(aClass, id);
	}

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