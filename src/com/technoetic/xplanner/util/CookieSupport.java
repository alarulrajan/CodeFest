package com.technoetic.xplanner.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Mateusz Prokopowicz Date: Jun 13, 2005 Time: 12:15:00 PM.
 */
public class CookieSupport {
    
    /** Instantiates a new cookie support.
     */
    private CookieSupport() {
        
    }
    
    /** Creates the cookie.
     *
     * @param name
     *            the name
     * @param value
     *            the value
     * @param response
     *            the response
     */
    public static void createCookie(final String name, final String value,
            final HttpServletResponse response) {
        final Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    /** Delete cookie.
     *
     * @param name
     *            the name
     * @param response
     *            the response
     */
    public static void deleteCookie(final String name,
            final HttpServletResponse response) {
        final Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        response.addCookie(cookie);
    }

    /** Gets the cookie.
     *
     * @param name
     *            the name
     * @param request
     *            the request
     * @return the cookie
     */
    public static Cookie getCookie(final String name,
            final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                final Cookie cookie = cookies[i];
                if (cookie.getName().equals(name)) {
                    return cookie;
                }
            }
        }
        return null;
    }
}
