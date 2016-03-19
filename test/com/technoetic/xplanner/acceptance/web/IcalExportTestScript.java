package com.technoetic.xplanner.acceptance.web;



/**
 * The Class IcalExportTestScript.
 */
public class IcalExportTestScript extends AbstractPageTestScript {

    /** This is a very simple "smoke test" for the iCal export functionality.
     *
     * @throws Exception
     *             the exception
     */
    public void testExport() throws Exception {
        tester.getTestContext().setAuthorization(
                tester.getXPlannerLoginId(), tester.getXPlannerLoginPassword());

        tester.beginAt("ical/sysadmin.ics");

        assertHeaderValue("Content-type", "application/x-msoutlook");
        assertHeaderValue("Content-disposition", "inline");
        assertHeaderValue("Content-disposition", "filename=sysadmin.ics");

        tester.assertTextPresent("BEGIN:VCALENDAR");
        tester.assertTextPresent("END:VCALENDAR");
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#traverseLinkWithKeyAndReturn(java.lang.String)
     */
    protected void traverseLinkWithKeyAndReturn(String key) throws Exception {
        tester.clickLinkWithKey(key);
        tester.gotoPage("view", "projects", 0);
    }


}
