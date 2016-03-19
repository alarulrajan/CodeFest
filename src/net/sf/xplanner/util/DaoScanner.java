package net.sf.xplanner.util;

import java.util.Map;
import java.util.Set;

import net.sf.xplanner.dao.Dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;

/**
 * The Class DaoScanner.
 */
public class DaoScanner {
    
    /** The application context. */
    private ApplicationContext applicationContext;

    /**
     * Inits the.
     */
    public void init() {
        final Map<String, Dao> beansOfType = this.applicationContext
                .getBeansOfType(Dao.class);
        final Set<String> keySet = beansOfType.keySet();
        for (final String key : keySet) {
            System.out.println(key + "=" + beansOfType.get(key));
        }
    }

    /**
     * Sets the application context.
     *
     * @param applicationContext
     *            the new application context
     */
    @Autowired
    @Required
    public void setApplicationContext(
            final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
