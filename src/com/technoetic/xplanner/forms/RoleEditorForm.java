package com.technoetic.xplanner.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import net.sf.xplanner.domain.Role;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.security.AuthenticationException;
import com.technoetic.xplanner.security.auth.SystemAuthorizer;

/**
 * The Class RoleEditorForm.
 */
public class RoleEditorForm extends AbstractEditorForm {
    
    /** The person ids. */
    private final ArrayList personIds = new ArrayList();
    
    /** The person roles. */
    private final ArrayList personRoles = new ArrayList();

    /* (non-Javadoc)
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public ActionErrors validate(final ActionMapping mapping,
            final HttpServletRequest request) {
        final ActionErrors errors = new ActionErrors();
        return errors;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.forms.AbstractEditorForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void reset(final ActionMapping mapping,
            final HttpServletRequest request) {
        super.reset(mapping, request);
    }

    /**
     * Reset.
     */
    public void reset() {

    }

    /**
     * Gets the person count.
     *
     * @return the person count
     */
    public int getPersonCount() {
        return this.personIds.size();
    }

    /**
     * Gets the person id.
     *
     * @param index
     *            the index
     * @return the person id
     */
    public String getPersonId(final int index) {
        return (String) this.personIds.get(index);
    }

    /**
     * Gets the person id as int.
     *
     * @param index
     *            the index
     * @return the person id as int
     */
    public int getPersonIdAsInt(final int index) {
        if (this.getPersonId(index) == null) {
            return -1;
        } else {
            return Integer.parseInt(this.getPersonId(index));
        }
    }

    /**
     * Sets the person id.
     *
     * @param index
     *            the index
     * @param personId
     *            the person id
     */
    public void setPersonId(final int index, final String personId) {
        AbstractEditorForm.ensureSize(this.personIds, index + 1);
        this.personIds.set(index, personId);

    }

    /**
     * Sets the person role.
     *
     * @param index
     *            the index
     * @param role
     *            the role
     */
    /* D�finir le r�le d'une personne */
    public void setPersonRole(final int index, final String role) {
        AbstractEditorForm.ensureSize(this.personRoles, index + 1);
        this.personRoles.set(index, role);
    }

    /**
     * Gets the person role.
     *
     * @param index
     *            the index
     * @return the person role
     */
    /* Renvoi du r�le d'une personne */
    public String getPersonRole(final int index) {
        return (String) this.personRoles.get(index);
    }

    /**
     * Checks for role.
     *
     * @param roles
     *            the roles
     * @param name
     *            the name
     * @return true, if successful
     */
    private boolean hasRole(final Collection roles, final String name) {
        for (final Iterator iterator = roles.iterator(); iterator.hasNext();) {
            final Role role = (Role) iterator.next();
            if (role.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the effective role.
     *
     * @param roles
     *            the roles
     * @return the effective role
     */
    private String getEffectiveRole(final Collection roles) {
        if (this.hasRole(roles, "admin")) {
            return "admin";
        } else if (this.hasRole(roles, "editor")) {
            return "editor";
        } else if (this.hasRole(roles, "viewer")) {
            return "viewer";
        } else {
            return "none";
        }
    }

    /**
     * Checks if is role selected.
     *
     * @param role
     *            the role
     * @param personId
     *            the person id
     * @return the string
     * @throws AuthenticationException
     *             the authentication exception
     */
    public String isRoleSelected(final String role, final int personId)
            throws AuthenticationException {
        return this.getEffectiveRole(this.getRoles(personId)).equals(role) ? "selected='selected'"
                : "";
    }

    /**
     * Gets the roles.
     *
     * @param personId
     *            the person id
     * @return the roles
     * @throws AuthenticationException
     *             the authentication exception
     */
    public Collection getRoles(final int personId)
            throws AuthenticationException {
        return SystemAuthorizer.get().getRolesForPrincipalOnProject(personId,
                this.getId(), false);
    }

    /**
     * Checks if is sys admin.
     *
     * @return true, if is sys admin
     * @throws AuthenticationException
     *             the authentication exception
     */
    public boolean isSysAdmin() throws AuthenticationException {
        return CollectionUtils.find(this.getRoles(0), new Predicate() {
            @Override
            public boolean evaluate(final Object o) {
                return ((Role) o).getName().equals("sysadmin");
            }
        }) != null;
    }

}
