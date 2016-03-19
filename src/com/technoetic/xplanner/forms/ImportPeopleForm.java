package com.technoetic.xplanner.forms;

/**
 * The Class ImportPeopleForm.
 */
public class ImportPeopleForm extends ImportForm {
	
	/**
     * Adds the result.
     *
     * @param id
     *            the id
     * @param login
     *            the login
     * @param name
     *            the name
     * @param status
     *            the status
     */
	public void addResult(final String id, final String login,
			final String name, final String status) {
		final PeopleImportResult result = new PeopleImportResult();
		result.setId(id);
		result.setLoginId(login);
		result.setName(name);
		result.setStatus(status);
		result.setLineNbr(this.results.size() + 1);
		this.results.add(result);
	}

}
