package net.sf.xplanner.struts;

import java.util.Locale;

import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

/**
 * The Class XPlannerMessageResources.
 */
public class XPlannerMessageResources extends MessageResources {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1599914839332648038L;

	/** The message source. */
	private MessageSource messageSource;

	/**
     * Instantiates a new x planner message resources.
     *
     * @param factory
     *            the factory
     * @param config
     *            the config
     */
	public XPlannerMessageResources(final MessageResourcesFactory factory,
			final String config) {
		super(factory, config);
	}

	/**
     * Instantiates a new x planner message resources.
     */
	public XPlannerMessageResources() {
		super(null, null);
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.util.MessageResources#getMessage(java.util.Locale, java.lang.String)
	 */
	@Override
	public String getMessage(final Locale locale, final String key) {
		String keyToResolve = key;
		if (keyToResolve != null
				&& keyToResolve.startsWith("org.apache.struts.taglib.bean")) {
			keyToResolve = keyToResolve.replace(
					"org.apache.struts.taglib.bean.", "");
		}
		return this.messageSource.getMessage(keyToResolve, null, locale);
	}

	/**
     * Sets the message source.
     *
     * @param messageSource
     *            the new message source
     */
	public void setMessageSource(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/* (non-Javadoc)
	 * @see org.apache.struts.util.MessageResources#isPresent(java.util.Locale, java.lang.String)
	 */
	@Override
	public boolean isPresent(final Locale locale, final String key) {
		try {
			return super.isPresent(locale, key);
		} catch (final NoSuchMessageException e) {
			return false;
		}
	}
}
