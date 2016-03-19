package com.technoetic.xplanner.tags;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.jsp.PageContext;

import net.sf.xplanner.domain.Iteration;

import org.hibernate.HibernateException;

import com.technoetic.xplanner.domain.repository.IterationRepository;
import com.technoetic.xplanner.security.AuthenticationException;

/**
 * The Class IterationLoader.
 */
public class IterationLoader {
    
    /** The page context. */
    private PageContext pageContext;

    /**
     * Gets the iteration options.
     *
     * @param projectId
     *            the project id
     * @param onlyCurrentProject
     *            the only current project
     * @param startDate
     *            the start date
     * @return the iteration options
     * @throws HibernateException
     *             the hibernate exception
     * @throws AuthenticationException
     *             the authentication exception
     */
    public List getIterationOptions(final int projectId,
            final boolean onlyCurrentProject, final Date startDate)
            throws HibernateException, AuthenticationException {
        final ContextInitiator contextInitiator = new ContextInitiator(
                this.pageContext);
        // DEBT: roll the initStaticContext into the ctor. Clean up the
        // exception: either everybody should get a jspException for a failed
        // authentication or should get a AuthenticationException
        contextInitiator.initStaticContext();
        // DEBT(Spring) load IterationRepository
        final IterationRepository dao = new IterationRepository(
                contextInitiator.getSession(),
                contextInitiator.getAuthorizer(),
                contextInitiator.getLoggedInUserId());
        List iterations;
        if (onlyCurrentProject) {
            iterations = dao.fetchEditableIterations(projectId, startDate);
        } else {
            iterations = dao.fetchEditableIterations();
        }

        final List options = new ArrayList();
        for (int i = 0; i < iterations.size(); i++) {
            final Iteration it = (Iteration) iterations.get(i);
            options.add(new IterationModel(it));
        }
        return options;
    }

    /**
     * Sets the page context.
     *
     * @param pageContext
     *            the new page context
     */
    public void setPageContext(final PageContext pageContext) {
        this.pageContext = pageContext;
    }
}
