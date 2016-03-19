package net.sf.xplanner.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.xplanner.domain.enums.IterationType;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.technoetic.xplanner.XPlannerProperties;

/**
 * The listener interface for receiving contextLoader events. The class that is
 * interested in processing a contextLoader event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addContextLoaderListener<code> method. When
 * the contextLoader event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ContextLoaderEvent
 */
public class ContextLoaderListener implements ServletContextListener {
    
    /** The required web application context. */
    @Deprecated
    private static WebApplicationContext requiredWebApplicationContext;

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextInitialized(final ServletContextEvent sce) {
        sce.getServletContext().setAttribute("iterationTypes",
                IterationType.getAllValues());
        sce.getServletContext().setAttribute(
                "appUrl",
                new XPlannerProperties()
                        .getProperty(XPlannerProperties.APPLICATION_URL_KEY));
        ContextLoaderListener.requiredWebApplicationContext = WebApplicationContextUtils
                .getRequiredWebApplicationContext(sce.getServletContext());

    }

    /**
     * Gets the context.
     *
     * @return the context
     */
    public static WebApplicationContext getContext() {
        return ContextLoaderListener.requiredWebApplicationContext;
    }

    /* (non-Javadoc)
     * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
     */
    @Override
    public void contextDestroyed(final ServletContextEvent sce) {
    }
}
