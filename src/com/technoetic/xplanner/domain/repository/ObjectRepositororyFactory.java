/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.domain.repository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.hibernate.HibernateException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

/**
 * User: mprokopowicz Date: Feb 8, 2006 Time: 10:05:47 AM
 */
public class ObjectRepositororyFactory implements BeanFactoryAware {
	private AutowireCapableBeanFactory beanFactory;
	private List delegates;

	public void setDelegates(final List delegateList) {
		this.delegates = delegateList;
	}

	public ObjectRepository create(final Class objectClass)
			throws HibernateException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException,
			InstantiationException {
		return this.create(objectClass, HibernateObjectRepository.class);
	}

	public ObjectRepository create(final Class objectClass,
			final Class objectRepositoryClass) throws HibernateException,
			NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, InstantiationException {
		ObjectRepository repository = (ObjectRepository) objectRepositoryClass
				.getConstructor(new Class[] { Class.class }).newInstance(
						new Object[] { objectClass });
		this.beanFactory.autowireBeanProperties(repository,
				AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		for (int i = 0; i < this.delegates.size(); i++) {
			final Class aClass = (Class) this.delegates.get(i);
			repository = (ObjectRepository) aClass.getConstructor(
					new Class[] { Class.class, ObjectRepository.class })
					.newInstance(new Object[] { objectClass, repository });
			this.beanFactory.autowireBeanProperties(repository,
					AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, false);
		}
		return repository;
	}

	@Override
	public void setBeanFactory(final BeanFactory beanFactory)
			throws BeansException {
		this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
	}
}
