/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Dec 21, 2005
 * Time: 2:24:07 AM
 */
package com.thoughtworks.proxy.toys.decorate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.thoughtworks.proxy.Invoker;
import com.thoughtworks.proxy.kit.PrivateInvoker;

/**
 * Invoker implementation for the decorating proxy. The implementation may
 * decorate an object or another {@link com.thoughtworks.proxy.Invoker}.
 * 
 * @author <a href="mailto:dan.north@thoughtworks.com">Dan North</a>
 * @author Aslak Helles&oslash;y
 * @author J&ouml;rg Schaible
 * @since 0.1
 */
public class DecoratingInvoker implements Invoker {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 8293471912861497447L;
	
	/** The decorated. */
	private final Invoker decorated;
	
	/** The decorator. */
	private final InvocationDecorator decorator;

	/**
	 * Construct a DecoratingInvoker decorating another Invoker.
	 * 
	 * @param decorated
	 *            the decorated {@link com.thoughtworks.proxy.Invoker}.
	 * @param decorator
	 *            the decorating instance.
	 * @since 0.1
	 */
	public DecoratingInvoker(final Invoker decorated,
			final InvocationDecorator decorator) {
		this.decorated = decorated;
		this.decorator = decorator;
	}

	/**
	 * Construct a DecoratingInvoker decorating another object.
	 * 
	 * @param delegate
	 *            the decorated object.
	 * @param decorator
	 *            the decorating instance.
	 * @since 0.1
	 */
	public DecoratingInvoker(final Object delegate,
			final InvocationDecorator decorator) {
		this(new PrivateInvoker(delegate), decorator);
	}

	/* (non-Javadoc)
	 * @see com.thoughtworks.proxy.Invoker#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(final Object proxy, final Method method,
			final Object[] args) throws Throwable {
		final Object[] decoratedArgs = this.decorator.beforeMethodStarts(proxy,
				method, args);
		try {
			final Object result = this.decorated.invoke(proxy, method,
					decoratedArgs);
			return this.decorator.decorateResult(proxy, method, decoratedArgs,
					result);
		} catch (final InvocationTargetException e) {
			final Throwable decoratedException = this.decorator
					.decorateTargetException(proxy, method, decoratedArgs,
							e.getTargetException());
			if (decoratedException != null) {
				throw decoratedException;
			}
		} catch (final Exception e) {
			final Exception decoratedException = this.decorator
					.decorateInvocationException(proxy, method, decoratedArgs,
							e);
			if (decoratedException != null) {
				throw decoratedException;
			}
		}
		return null;
	}

	/**
     * Gets the decorated.
     *
     * @return the decorated
     */
	public Invoker getDecorated() {
		return this.decorated;
	}
}