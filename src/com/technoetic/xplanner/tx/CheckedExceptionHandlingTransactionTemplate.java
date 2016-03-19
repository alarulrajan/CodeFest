/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Jan 3, 2006
 * Time: 5:32:09 AM
 */
package com.technoetic.xplanner.tx;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.technoetic.xplanner.util.Callable;

/**
 * The Class CheckedExceptionHandlingTransactionTemplate.
 */
public class CheckedExceptionHandlingTransactionTemplate {
	
	/** The spring transaction template. */
	private TransactionTemplate springTransactionTemplate;

	/**
     * Sets the spring transaction template.
     *
     * @param springTransactionTemplate
     *            the new spring transaction template
     */
	public void setSpringTransactionTemplate(
			final TransactionTemplate springTransactionTemplate) {
		this.springTransactionTemplate = springTransactionTemplate;
	}

	/**
     * Execute.
     *
     * @param callable
     *            the callable
     * @return the object
     * @throws Exception
     *             the exception
     */
	public Object execute(final Callable callable) throws Exception {
		try {
			return this.springTransactionTemplate
					.execute(new TransactionCallback() {
						@Override
						public Object doInTransaction(
								final TransactionStatus status) {
							try {
								final Object res = callable.run();
								// ThreadSession.get().flush();
								return res;
							} catch (final Exception e) {
								throw new RuntimeException(e);
							}
						}
					});
			// DEBT(SPRING) Remove all this exception handling by doing spring
			// declarative transaction demarkation
			// DEBT(SPRING) Convert all xplanner exception to Runtime so we can
			// remove this crap!
		} catch (final RuntimeException re) {
			final Throwable e = re.getCause();
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			}
			if (e instanceof Error) {
				throw (Error) e;
			}
			if (e instanceof Exception) {
				throw (Exception) e;
			}
			return null;
		}
	}

}