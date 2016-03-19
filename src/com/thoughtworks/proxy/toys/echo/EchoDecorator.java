/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.thoughtworks.proxy.toys.echo;

import java.io.PrintWriter;
import java.lang.reflect.Method;

import com.thoughtworks.proxy.Invoker;
import com.thoughtworks.proxy.ProxyFactory;
import com.thoughtworks.proxy.factory.InvokerReference;
import com.thoughtworks.proxy.kit.SimpleInvoker;
import com.thoughtworks.proxy.toys.decorate.Decorating;
import com.thoughtworks.proxy.toys.decorate.DecoratingInvoker;
import com.thoughtworks.proxy.toys.decorate.InvocationDecoratorSupport;

/**
 * A {@link com.thoughtworks.proxy.toys.decorate.InvocationDecoratorSupport}
 * implementation that echoes any invocation to a {@link java.io.PrintWriter}.
 * <p>
 * The implementation will try to create new proxies for every return value,
 * that can be proxied by the {@link com.thoughtworks.proxy.ProxyFactory} in
 * use.
 * </p>
 * 
 * @author <a href="mailto:dan.north@thoughtworks.com">Dan North</a>
 * @author J&ouml;rg Schaible
 * @since 0.1
 */
public class EchoDecorator extends InvocationDecoratorSupport {
    
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /** The out. */
    private final PrintWriter out;
    
    /** The factory. */
    private final ProxyFactory factory;

    /**
     * Construct an EchoingDecorator.
     * 
     * @param out
     *            the {@link java.io.PrintWriter} receving the logs
     * @param factory
     *            the {@link com.thoughtworks.proxy.ProxyFactory} to use
     * @since 0.2, different arguments in 0.1
     */
    public EchoDecorator(final PrintWriter out, final ProxyFactory factory) {
        this.out = out;
        this.factory = factory;
    }

    /* (non-Javadoc)
     * @see com.thoughtworks.proxy.toys.decorate.InvocationDecoratorSupport#beforeMethodStarts(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
     */
    @Override
    public Object[] beforeMethodStarts(final Object proxy, final Method method,
            final Object[] args) {
        if (this.isIgnored(method)) {
            return args;
        }
        this.printMethodCall(proxy, method, args);
        return super.beforeMethodStarts(proxy, method, args);
    }

    /* (non-Javadoc)
     * @see com.thoughtworks.proxy.toys.decorate.InvocationDecoratorSupport#decorateResult(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Object)
     */
    @Override
    public Object decorateResult(final Object proxy, final Method method,
            final Object[] args, Object result) {
        if (this.isIgnored(method)) {
            return result;
        }
        this.printMethodResult(result);
        final Class returnType = method.getReturnType();
        if (returnType != Object.class && this.factory.canProxy(returnType)) {
            result = Decorating.object(new Class[] { returnType }, result,
                    this, this.factory);
        } else if (result != null && returnType == Object.class
                && this.factory.canProxy(result.getClass())) {
            result = Decorating.object(new Class[] { result.getClass() },
                    result, this, this.factory);
        }
        return result;
    }

    /* (non-Javadoc)
     * @see com.thoughtworks.proxy.toys.decorate.InvocationDecoratorSupport#decorateTargetException(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Throwable)
     */
    @Override
    public Throwable decorateTargetException(final Object proxy,
            final Method method, final Object[] args, final Throwable cause) {
        if (this.isIgnored(method)) {
            return cause;
        }
        this.printTargetException(cause);
        return super.decorateTargetException(proxy, method, args, cause);
    }

    /* (non-Javadoc)
     * @see com.thoughtworks.proxy.toys.decorate.InvocationDecoratorSupport#decorateInvocationException(java.lang.Object, java.lang.reflect.Method, java.lang.Object[], java.lang.Exception)
     */
    @Override
    public Exception decorateInvocationException(final Object proxy,
            final Method method, final Object[] args, final Exception cause) {
        if (this.isIgnored(method)) {
            return cause;
        }
        this.printInvocationException(cause);
        return super.decorateInvocationException(proxy, method, args, cause);
    }

    /**
     * Prints the method call.
     *
     * @param proxy
     *            the proxy
     * @param method
     *            the method
     * @param args
     *            the args
     */
    private void printMethodCall(final Object proxy, final Method method,
            Object[] args) {
        final StringBuffer buf = new StringBuffer();
        // buf.append("[");
        // buf.append(Thread.currentThread().getName());
        // buf.append("] ");
        // buf.append(proxy);
        final Object target = this.getTarget(proxy);
        if (target != null) {
            buf.append(target.getClass().getName() + "@"
                    + Integer.toHexString(target.hashCode()));
        } else {
            buf.append(method.getDeclaringClass().getName());
        }
        buf.append(".").append(method.getName());

        if (args == null) {
            args = new Object[0];
        }
        buf.append("(");
        for (int i = 0; i < args.length; i++) {
            buf.append(i == 0 ? "<" : ", <").append(args[i].toString())
                    .append(">");
        }
        buf.append(") ");
        this.out.print(buf);
        this.out.flush();
    }

    /**
     * Gets the target.
     *
     * @param proxy
     *            the proxy
     * @return the target
     */
    private Object getTarget(final Object proxy) {
        Object target = null;
        Invoker invoker = ((InvokerReference) proxy).getInvoker();
        if (invoker instanceof DecoratingInvoker) {
            invoker = ((DecoratingInvoker) invoker).getDecorated();
        }
        if (invoker instanceof SimpleInvoker) {
            target = ((SimpleInvoker) invoker).getTarget();
        }
        return target;
    }

    /**
     * Checks if is ignored.
     *
     * @param method
     *            the method
     * @return true, if is ignored
     */
    private boolean isIgnored(final Method method) {
        return method.getName().equals("toString");
    }

    /**
     * Prints the method result.
     *
     * @param result
     *            the result
     */
    private void printMethodResult(final Object result) {
        final StringBuffer buf = new StringBuffer("--> <");
        buf.append(result == null ? "NULL" : result.toString());
        buf.append(">");
        this.out.println(buf);
        this.out.flush();
    }

    /**
     * Prints the target exception.
     *
     * @param throwable
     *            the throwable
     */
    private void printTargetException(final Throwable throwable) {
        final StringBuffer buf = new StringBuffer("throws ");
        buf.append(throwable.getClass().getName());
        buf.append(": ");
        buf.append(throwable.getMessage());
        this.out.println(buf);
        this.out.flush();
    }

    /**
     * Prints the invocation exception.
     *
     * @param throwable
     *            the throwable
     */
    private void printInvocationException(final Throwable throwable) {
        this.out.print("INTERNAL ERROR, ");
        this.printTargetException(throwable);
    }
}
