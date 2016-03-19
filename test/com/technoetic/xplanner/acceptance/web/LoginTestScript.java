package com.technoetic.xplanner.acceptance.web;

import java.text.MessageFormat;

import com.technoetic.xplanner.security.LoginModule;

/**
 * The Class LoginTestScript.
 */
//ChangeSoon:Remove
public class LoginTestScript extends AbstractPageTestScript
{
    
    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.AbstractDatabaseTestScript#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        tester.login();
        setUpTestPerson();
        tester.logout();
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.acceptance.web.AbstractPageTestScript#tearDown()
     */
    protected void tearDown() throws Exception {
       super.tearDown();
    }

    /** Test login_ wrong password.
     */
    public void testLogin_WrongPassword()
    {
        doLogin(developerUserId, "password_xyz");
        tester.assertTextPresent(getErrorMessage(LoginModule.MESSAGE_AUTHENTICATION_FAILED_KEY));
    }

   /** Test login_ no user.
     */
   public void testLogin_NoUser()
   {
       doLogin("userWhoDoesNotExist", "password");
      tester.assertTextPresent(getErrorMessage(LoginModule.MESSAGE_USER_NOT_FOUND_KEY));
   }

    /** Do login.
     *
     * @param userId
     *            the user id
     * @param password
     *            the password
     */
    private void doLogin(String userId, String password)
    {
        tester.beginAt("/do/login");
        tester.setFormElement("userId", userId);
        tester.setFormElement("password", password);
        tester.submit();
    }

   /** Gets the error message.
     *
     * @param key
     *            the key
     * @return the error message
     */
   private String getErrorMessage(String key) {
      MessageFormat messageFormat = new MessageFormat(tester.getMessage(key));
      return messageFormat.format(new Object[]{"XPlanner"});
   }

}
