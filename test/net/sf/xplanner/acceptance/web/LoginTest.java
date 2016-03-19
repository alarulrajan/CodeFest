package net.sf.xplanner.acceptance.web;

import java.text.MessageFormat;

import org.displaytag.util.HtmlTagUtil;
import org.junit.Test;

import com.technoetic.xplanner.security.LoginModule;

/**
 * The Class LoginTest.
 */
public class LoginTest extends BaseTest {
    
    /** The developer user id. */
    private String developerUserId = "sysadmin";

    /**
     * Test login_ wrong password.
     */
    @Test
    public void testLogin_WrongPassword() {
        doLogin(developerUserId, "password_xyz");
        assertTextPresent(getErrorMessage(LoginModule.MESSAGE_AUTHENTICATION_FAILED_KEY));
    }

    /**
     * Test login_ no user.
     */
    @Test
    public void testLogin_NoUser() {
        doLogin("userWhoDoesNotExist", "password");
        assertTextPresent(getErrorMessage(LoginModule.MESSAGE_USER_NOT_FOUND_KEY));
    }
    
    /**
     * Test login admin.
     */
    @Test
    public void testLoginAdmin() {
        loginAsAdmin();
    }

    /**
     * Gets the error message.
     *
     * @param key
     *            the key
     * @return the error message
     */
    private String getErrorMessage(String key) {
        MessageFormat messageFormat = new MessageFormat(tester.getMessage(key));
        return HtmlTagUtil.stripHTMLTags(messageFormat.format(new Object[] { "XPlanner" }));
    }
}
