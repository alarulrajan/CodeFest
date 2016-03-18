/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 12, 2005
 * Time: 11:08:51 PM
 */
package com.technoetic.xplanner.actions;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.forms.AbstractEditorForm;
import com.technoetic.xplanner.forms.ImportStoriesForm;
import com.technoetic.xplanner.importer.MissingColumnHeaderSpreadsheetImporterException;
import com.technoetic.xplanner.importer.MissingFieldSpreadsheetImporterException;
import com.technoetic.xplanner.importer.SpreadsheetStoryImporter;
import com.technoetic.xplanner.importer.WrongImportFileSpreadsheetImporterException;
import com.technoetic.xplanner.importer.spreadsheet.MissingWorksheetException;
import com.technoetic.xplanner.importer.spreadsheet.SpreadsheetHeaderConfiguration;
import com.technoetic.xplanner.util.CookieSupport;

public class ImportStoriesAction extends EditObjectAction {
	SpreadsheetStoryImporter importer;
	public static final String WORKSHEET_NAME_PROPERTY_KEY = "import.spreadsheet.worksheet.name";
	public static final String STORY_TITLE_PROPERTY_KEY = "import.spreadsheet.story.title.column.header";
	public static final String STORY_ESTIMATE_PROPERTY_KEY = "import.spreadsheet.story.estimate.column.header";
	public static final String ITERATION_END_DATE_PROPERTY_KEY = "import.spreadsheet.iteration.end.date.column.header";
	public static final String STORY_PRIORITY_PROPERTY_KEY = "import.spreadsheet.story.priority.column.header";
	public static final String STORY_STATUS_PROPERTY_KEY = "import.spreadsheet.story.status.column.header";
	public final static String ONLY_INCOMPLETE_COOKIE_NAME = "import.spreadsheet.onlyIncomplete";
	public final static String COMPLETED_STORY_STATUS_KEY = "import.spreadsheet.completedStoryStatus";

	@Override
	public ActionForward execute(final ActionMapping mapping,
			final ActionForm actionForm, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		final ActionErrors errors = new ActionErrors();
		try {
			return super.execute(mapping, actionForm, request, response);
		} catch (final WrongImportFileSpreadsheetImporterException e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"import.status.corrupted_file"));
		} catch (final MissingFieldSpreadsheetImporterException e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"import.status.missing_required_field", e.getField()));
		} catch (final MissingColumnHeaderSpreadsheetImporterException e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"import.status.wrong_header", e.getColumnName()));
		} catch (final MissingWorksheetException e) {
			errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(
					"import.status.worksheet_not_found", e.getWorksheetName()));
		}
		this.saveErrors(request, errors);
		return mapping.getInputForward();
	}

	@Override
	protected void populateObject(final HttpServletRequest request,
			final Object object, final ActionForm form) throws IOException {
		final ImportStoriesForm importStoriesForm = (ImportStoriesForm) form;
		final SpreadsheetHeaderConfiguration headerConfiguration = new SpreadsheetHeaderConfiguration();
		this.populateHeaderConfiguration(headerConfiguration, importStoriesForm);
		importStoriesForm.setResults(this.importer.importStories(
				(Iteration) object, headerConfiguration, importStoriesForm
						.getFormFile().getInputStream(), importStoriesForm
						.isOnlyIncomplete()));
	}

	private void populateHeaderConfiguration(
			final SpreadsheetHeaderConfiguration headerConfiguration,
			final ImportStoriesForm form) {
		headerConfiguration.setWorksheetName(form.getWorksheetName());
		headerConfiguration.setTitleHeader(form.getTitleColumn());
		headerConfiguration.setEndDateHeader(form.getEndDateColumn());
		headerConfiguration.setEstimateHeader(form.getEstimateColumn());
		headerConfiguration.setPriorityHeader(form.getPriorityColumn());
		headerConfiguration.setStatusHeader(form.getStatusColumn());
	}

	@Override
	protected void setCookies(final AbstractEditorForm form,
			final ActionMapping mapping, final HttpServletRequest request,
			final HttpServletResponse response) {
		final ImportStoriesForm importForm = (ImportStoriesForm) form;
		this.addCookie(ImportStoriesAction.WORKSHEET_NAME_PROPERTY_KEY,
				importForm.getWorksheetName(), response);
		this.addCookie(ImportStoriesAction.STORY_TITLE_PROPERTY_KEY,
				importForm.getTitleColumn(), response);
		this.addCookie(ImportStoriesAction.ITERATION_END_DATE_PROPERTY_KEY,
				importForm.getEndDateColumn(), response);
		this.addCookie(ImportStoriesAction.STORY_ESTIMATE_PROPERTY_KEY,
				importForm.getEstimateColumn(), response);
		this.addCookie(ImportStoriesAction.STORY_PRIORITY_PROPERTY_KEY,
				importForm.getPriorityColumn(), response);
		this.addCookie(ImportStoriesAction.STORY_STATUS_PROPERTY_KEY,
				importForm.getStatusColumn(), response);
		this.addCookie(ImportStoriesAction.ONLY_INCOMPLETE_COOKIE_NAME, ""
				+ importForm.isOnlyIncomplete(), response);
		this.addCookie(ImportStoriesAction.COMPLETED_STORY_STATUS_KEY,
				importForm.getCompletedStatus(), response);
	}

	private void addCookie(final String propertyKey, final String value,
			final HttpServletResponse response) {
		CookieSupport.createCookie(propertyKey, value, response);
	}

	@Override
	protected void populateForm(final AbstractEditorForm actionForm,
			final ActionMapping actionMapping, final HttpServletRequest request) {
		final ImportStoriesForm form = (ImportStoriesForm) actionForm;
		form.setWorksheetName(this.getValueFromCookieOrProperties(
				ImportStoriesAction.WORKSHEET_NAME_PROPERTY_KEY, request));
		form.setTitleColumn(this.getValueFromCookieOrProperties(
				ImportStoriesAction.STORY_TITLE_PROPERTY_KEY, request));
		form.setEndDateColumn(this.getValueFromCookieOrProperties(
				ImportStoriesAction.ITERATION_END_DATE_PROPERTY_KEY, request));
		form.setPriorityColumn(this.getValueFromCookieOrProperties(
				ImportStoriesAction.STORY_PRIORITY_PROPERTY_KEY, request));
		form.setEstimateColumn(this.getValueFromCookieOrProperties(
				ImportStoriesAction.STORY_ESTIMATE_PROPERTY_KEY, request));
		form.setStatusColumn(this.getValueFromCookieOrProperties(
				ImportStoriesAction.STORY_STATUS_PROPERTY_KEY, request));
		form.setOnlyIncomplete(Boolean.valueOf(
				this.getValueFromCookieOrProperties(
						ImportStoriesAction.ONLY_INCOMPLETE_COOKIE_NAME,
						request)).booleanValue());
		form.setCompletedStatus(this.getValueFromCookieOrProperties(
				ImportStoriesAction.COMPLETED_STORY_STATUS_KEY, request));
	}

	public SpreadsheetStoryImporter getImporter() {
		return this.importer;
	}

	public void setImporter(final SpreadsheetStoryImporter importer) {
		this.importer = importer;
	}

	public String getValueFromCookieOrProperties(final String key,
			final HttpServletRequest request) {
		final Cookie cookie = CookieSupport.getCookie(key, request);
		if (cookie != null) {
			return cookie.getValue();
		}
		final XPlannerProperties xplannerProperties = new XPlannerProperties();
		return xplannerProperties.getProperty(key);
	}
}