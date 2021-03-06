package com.technoetic.xplanner.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.DomainObject;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.db.IdSearchHelper;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.forms.AbstractEditorForm;

/**
 * The Class IdSearchAction.
 */
public class IdSearchAction extends AbstractAction {
    
    /** The id search helper. */
    private IdSearchHelper idSearchHelper;

    /** The meta data repository. */
    private DomainMetaDataRepository metaDataRepository;

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected ActionForward doExecute(final ActionMapping mapping,
            final ActionForm form, final HttpServletRequest request,
            final HttpServletResponse response) throws Exception {
        AbstractEditorForm.initConverters(request); // DEBT(SPRING) Extract the
                                                    // convert methods out of
                                                    // the AbstractEditorForm
                                                    // (keep delegators methods)
                                                    // and have that object
                                                    // spring loaded using the
                                                    // message bundle from
                                                    // spring
        final String searchedIdString = request.getParameter("searchedId");
        if (StringUtils.isEmpty(searchedIdString)) {
            return this.getGeneralErrorForward(mapping, request,
                    "idsearch.error.missingId");
        }

        final Number integer = AbstractEditorForm
                .convertToInt(searchedIdString);
        if (integer == null) {
            return this.getGeneralErrorForward(mapping, request,
                    "idsearch.error.badId", searchedIdString);
        }

        final int oid = integer.intValue();
        final DomainObject object = this.idSearchHelper.search(oid);
        if (object == null) {
            return this.getGeneralErrorForward(mapping, request,
                    "idsearch.error.idNotFound", new Integer(oid));
        }

        // DEBT(SPRING): DomainMetaDataRep should be made an instance and spring
        // injected
        final String objectType = this.metaDataRepository
                .classToTypeName(object.getClass());
        return new ActionForward("/do/view/" + objectType + "?oid="
                + object.getId(), true);
    }

    /**
     * Sets the meta data repository.
     *
     * @param metaDataRepository
     *            the new meta data repository
     */
    public void setMetaDataRepository(
            final DomainMetaDataRepository metaDataRepository) {
        this.metaDataRepository = metaDataRepository;
    }

    /**
     * Sets the id search helper.
     *
     * @param idSearchHelper
     *            the new id search helper
     */
    public void setIdSearchHelper(final IdSearchHelper idSearchHelper) {
        this.idSearchHelper = idSearchHelper;
    }
}
