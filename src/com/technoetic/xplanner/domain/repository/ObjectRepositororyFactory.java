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
 * User: mprokopowicz Date: Feb 8, 2006 Time: 10:05:47 AM.
 */
public class ObjectRepositororyFactory implements BeanFactoryAware {
    
    /** The bean factory. */
    private AutowireCapableBeanFactory beanFactory;
    
    /** The delegates. */
    private List delegates;

    /**
     * Sets the delegates.
     *
     * @param delegateList
     *            the new delegates
     */
    public void setDelegates(final List delegateList) {
        this.delegates = delegateList;
    }

    /**
     * Creates the.
     *
     * @param objectClass
     *            the object class
     * @return the object repository
     * @throws HibernateException
     *             the hibernate exception
     * @throws NoSuchMethodException
     *             the no such method exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws InstantiationException
     *             the instantiation exception
     */
    public ObjectRepository create(final Class objectClass)
            throws HibernateException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException,
            InstantiationException {
        return this.create(objectClass, HibernateObjectRepository.class);
    }

    /**
     * Creates the.
     *
     * @param objectClass
     *            the object class
     * @param objectRepositoryClass
     *            the object repository class
     * @return the object repository
     * @throws HibernateException
     *             the hibernate exception
     * @throws NoSuchMethodException
     *             the no such method exception
     * @throws IllegalAccessException
     *             the illegal access exception
     * @throws InvocationTargetException
     *             the invocation target exception
     * @throws InstantiationException
     *             the instantiation exception
     */
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

    /* (non-Javadoc)
     * @see org.springframework.beans.factory.BeanFactoryAware#setBeanFactory(org.springframework.beans.factory.BeanFactory)
     */
    @Override
    public void setBeanFactory(final BeanFactory beanFactory)
            throws BeansException {
        this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
    }
}
