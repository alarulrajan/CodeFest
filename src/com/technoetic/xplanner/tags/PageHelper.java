package com.technoetic.xplanner.tags;

import javax.servlet.ServletRequest;

import net.sf.xplanner.domain.Project;

import org.apache.commons.lang.StringUtils;

//DEBT Should be a spring loaded service bean 

/**
 * The Class PageHelper.
 */
public class PageHelper {
    
    /**
     * Gets the project id.
     *
     * @param resource
     *            the resource
     * @param request
     *            the request
     * @return the project id
     */
    public static int getProjectId(final Object resource,
            final ServletRequest request) {
        int projectId = 0;
        final DomainContext context = DomainContext.get(request);
        if (context != null) {
            projectId = context.getProjectId();
        }
        if (projectId == 0 && resource instanceof Project) {
            projectId = ((Project) resource).getId();
        }
        final String id = request.getParameter("projectId");
        if (projectId == 0 && !StringUtils.isEmpty(id)) {
            projectId = Integer.parseInt(id);
        }
        return projectId;
    }
}
