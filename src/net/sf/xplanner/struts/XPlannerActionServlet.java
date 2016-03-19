package net.sf.xplanner.struts;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * The Class XPlannerActionServlet.
 */
public class XPlannerActionServlet extends ActionServlet {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7941783949341881332L;

	/* (non-Javadoc)
	 * @see org.apache.struts.action.ActionServlet#initModuleMessageResources(org.apache.struts.config.ModuleConfig)
	 */
	@Override
	protected void initModuleMessageResources(final ModuleConfig config)
			throws ServletException {
		final MessageResources messageResource = (MessageResources) WebApplicationContextUtils
				.getRequiredWebApplicationContext(this.getServletContext())
				.getBean("strutsMessageSource");
		this.getServletContext().setAttribute(Globals.MESSAGES_KEY,
				messageResource);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(final HttpServletRequest req,
			final HttpServletResponse resp) throws ServletException,
			IOException {
		req.setAttribute(Globals.MESSAGES_KEY, WebApplicationContextUtils
				.getRequiredWebApplicationContext(this.getServletContext())
				.getBean("strutsMessageSource"));
		super.service(req, resp);
	}

}
