package net.sf.xplanner.context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.sf.xplanner.domain.enums.IterationType;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.technoetic.xplanner.XPlannerProperties;

public class ContextLoaderListener implements ServletContextListener {
	@Deprecated
	private static WebApplicationContext requiredWebApplicationContext;

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

	public static WebApplicationContext getContext() {
		return ContextLoaderListener.requiredWebApplicationContext;
	}

	@Override
	public void contextDestroyed(final ServletContextEvent sce) {
	}
}
