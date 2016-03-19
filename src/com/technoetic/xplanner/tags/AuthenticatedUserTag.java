package com.technoetic.xplanner.tags;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import net.sf.xplanner.domain.Person;

import com.technoetic.xplanner.security.SecurityHelper;
import com.technoetic.xplanner.tags.db.DatabaseTagSupport;

/**
 * The Class AuthenticatedUserTag.
 */
public class AuthenticatedUserTag extends DatabaseTagSupport {

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.tags.RequestContextAwareTag#doStartTagInternal()
     */
    @Override
    protected int doStartTagInternal() throws Exception {
        if (SecurityHelper
                .isUserAuthenticated((HttpServletRequest) this.pageContext
                        .getRequest())) {
            final Principal userPrincipal = SecurityHelper
                    .getUserPrincipal((HttpServletRequest) this.pageContext
                            .getRequest());
            // DEBT(DAO) : Move this to a dao
            if (this.getSession() != null) {
                final List users = this
                        .getSession()
                        .createQuery(
                                "from p in " + Person.class
                                        + " where p.userId = :userId")
                        .setString("userId", userPrincipal.getName())
                        .setCacheable(true).list();
                if (users.size() > 0) {
                    this.pageContext.setAttribute(this.id, users.get(0));
                }
            }
        }
        return Tag.SKIP_BODY;
    }

    /* (non-Javadoc)
     * @see org.springframework.web.servlet.tags.RequestContextAwareTag#doCatch(java.lang.Throwable)
     */
    @Override
    public void doCatch(final Throwable throwable) throws Throwable {
        this.pageContext.getServletContext().log(
                "error getting authenticated user", throwable);
        throw new JspException(throwable.getMessage());
    }

    /* (non-Javadoc)
     * @see javax.servlet.jsp.tagext.TagSupport#release()
     */
    @Override
    public void release() {
        super.release();
    }
}