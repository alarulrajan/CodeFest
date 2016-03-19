package net.sf.xplanner.acceptance.web;

import org.apache.commons.lang.time.DateUtils;

import com.technoetic.xplanner.testing.DateHelper;


/**
 * The Class ProjectPageTest.
 */
public class ProjectPageTest extends BaseTest {
	
	/* (non-Javadoc)
	 * @see net.sf.xplanner.acceptance.web.BaseTest#setUp()
	 */
	public void setUp() throws Exception {
		super.setUp();
//		startDate = DateHelper.getDateStringDaysFromToday(0);
//		endDate = DateHelper.getDateStringDaysFromToday(14);
//		tester.login();
//		setUpTestPerson();
//		testProject = newProject();
//		testProject.setName(testProjectName);
//		testProject.setDescription(testProjectDescription);
//		commitSession();
//		tester.gotoProjectsPage();
//		tester.clickLinkWithText("" + testProject.getId());
	}
	
	/**
     * Test project.
     */
	public void testProject() {
		System.out.println(getAndLoginAsUser());
	}
}
