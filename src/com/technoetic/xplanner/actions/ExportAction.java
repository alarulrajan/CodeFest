package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.export.ExportException;
import com.technoetic.xplanner.export.Exporter;

public class ExportAction extends AbstractAction {
	private Exporter exporter;

	@Override
	protected ActionForward doExecute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final Session session = this.getSession(request);
		try {
			final Class objectClass = this.getObjectType(mapping, request);
			final Object object = this.getCommonDao().getById(objectClass,
					Integer.parseInt(request.getParameter("oid")));
			final byte[] data = this.exporter.export(session, object);
			this.exporter.initializeHeaders(response);
			response.getOutputStream().write(data);
		} catch (final Exception ex) {
			throw new ExportException(ex);
		} finally {
			session.connection().rollback();
		}

		// Optional forward
		return mapping.findForward("display");
	}

	public void setExporter(final Exporter exporter) {
		this.exporter = exporter;
	}
}
