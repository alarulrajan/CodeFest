package com.technoetic.xplanner.actions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.dao.PersonDao;
import net.sf.xplanner.domain.Person;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

import com.technoetic.xplanner.domain.repository.DuplicateUserIdException;
import com.technoetic.xplanner.forms.ImportPeopleForm;
import com.technoetic.xplanner.security.auth.AuthorizationException;
import com.technoetic.xplanner.util.Callable;

/**
 * User: Mateusz Prokopowicz Date: Dec 2, 2004 Time: 5:59:21 PM.
 */
public class ImportPeopleAction extends AbstractAction {
	
	/** The Constant NBR_OF_COLUMNS. */
	private static final int NBR_OF_COLUMNS = 5;
	
	/** The Constant STATUS_SUCCESS_KEY. */
	private static final String STATUS_SUCCESS_KEY = "people.import.status.success";
	
	/** The Constant STATUS_WRONG_ENTRY_KEY. */
	private static final String STATUS_WRONG_ENTRY_KEY = "people.import.status.wrong_entry_format";
	
	/** The Constant STATUS_EMPTY_USERID_KEY. */
	private static final String STATUS_EMPTY_USERID_KEY = "people.import.status.empty_userId";
	
	/** The Constant STATUS_USERID_EXISTS_KEY. */
	private static final String STATUS_USERID_EXISTS_KEY = "people.import.status.userId_exists";
	
	/** The Constant EDIT_PERSON_ACTION_NAME. */
	static final String EDIT_PERSON_ACTION_NAME = "/edit/person";
	
	/** The person dao. */
	private PersonDao personDao;

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final ImportPeopleForm importForm = (ImportPeopleForm) form;
		final FormFile formFile = importForm.getFormFile();
		if (formFile != null) {
			final String filename = formFile.getFileName();
			if (StringUtils.isNotEmpty(filename)) {
				final String contentType = formFile.getContentType();
				final InputStream input = formFile.getInputStream();
				final int fileSize = formFile.getFileSize();
				final BufferedReader importReader = new BufferedReader(
						new InputStreamReader(input));
				for (String line = importReader.readLine(); line != null; line = importReader
						.readLine()) {
					final List entry = new ArrayList();
					String status = null;
					String id = "";
					entry.addAll(Arrays.asList(line.split(",")));
					try {
						if (entry.size() < ImportPeopleAction.NBR_OF_COLUMNS) {
							throw new PeopleImportException(
									ImportPeopleAction.STATUS_WRONG_ENTRY_KEY);
						}
						if (StringUtils.isEmpty((String) entry.get(0))) {
							throw new PeopleImportException(
									ImportPeopleAction.STATUS_EMPTY_USERID_KEY);
						}
						final Person person = new Person();
						person.setUserId((String) entry.get(0));
						person.setName((String) entry.get(1));
						person.setEmail((String) entry.get(2));
						person.setInitials((String) entry.get(3));
						person.setPhone((String) entry.get(4));
						id = (String) this.transactionTemplate
								.execute(new Callable() {
									@Override
									public Object run() throws Exception {
										ImportPeopleAction.this.personDao
												.save(person);
										return "" + person.getId();
									}
								});
						status = ImportPeopleAction.STATUS_SUCCESS_KEY;

					} catch (final DuplicateUserIdException duie) {
						status = ImportPeopleAction.STATUS_USERID_EXISTS_KEY;
					} catch (final PeopleImportException ex) {
						status = ex.getMessage();
					} catch (final AuthorizationException e) {
						request.setAttribute("exception", e);
						return mapping.findForward("security/notAuthorized");
					} finally {
						importForm.addResult(id, (String) entry.get(0),
								(String) entry.get(1), status);
					}
				}
				Logger.getLogger(ImportPeopleAction.class).debug(
						"Importing people: filename=" + filename
								+ ", fileSize=" + fileSize + ", contentType="
								+ contentType);
			}
		}
		return new ActionForward(mapping.getInput());
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.actions.AbstractAction#doExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ActionForward doExecute(final ActionMapping mapping,
			final ActionForm form, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		return null;
	}

	/**
     * The Class PeopleImportException.
     */
	class PeopleImportException extends Exception {
		
		/**
         * Instantiates a new people import exception.
         *
         * @param msg
         *            the msg
         */
		public PeopleImportException(final String msg) {
			super(msg);
		}
	}

	/**
     * Sets the person dao.
     *
     * @param personDao
     *            the new person dao
     */
	public void setPersonDao(final PersonDao personDao) {
		this.personDao = personDao;
	}

}
