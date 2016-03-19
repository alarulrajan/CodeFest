package com.technoetic.xplanner.util;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Dec 15, 2004
 * Time: 3:14:30 PM
 */

import junit.framework.TestCase;

/**
 * The Class TestStringUtilities.
 */
public class TestStringUtilities extends TestCase {

    /** Test get short prefix_no space.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetShortPrefix_noSpace() throws Exception {
        String wholePrefix = "abc";
        assertPrefixMatches(wholePrefix, 1, "c");
        assertPrefixMatches(wholePrefix, 2, "bc");
        assertPrefixMatches(wholePrefix, 3, wholePrefix);
        assertPrefixMatches(wholePrefix, 4, wholePrefix);
    }

    /** Test get short prefix_with space.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetShortPrefix_withSpace() throws Exception {
        String wholePrefix = "a b c";
        assertPrefixMatches(wholePrefix, 1, "c");
        assertPrefixMatches(wholePrefix, 2, "b c");
        assertPrefixMatches(wholePrefix, 3, wholePrefix);
        assertPrefixMatches(wholePrefix, 4, wholePrefix);
        assertPrefixMatches("a   b", 4, "a   b");
    }

    /** Test get short prefix_with new line.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetShortPrefix_withNewLine() throws Exception {
        String wholePrefix = "a\nb\nc";
        assertPrefixMatches(wholePrefix, 1, "c");
        assertPrefixMatches(wholePrefix, 2, "b\nc");
        assertPrefixMatches(wholePrefix, 3, wholePrefix);
        assertPrefixMatches(wholePrefix, 4, wholePrefix);
    }

    /** Assert prefix matches.
     *
     * @param wholePrefix
     *            the whole prefix
     * @param maxPrefixLength
     *            the max prefix length
     * @param expectedPrefix
     *            the expected prefix
     */
    private void assertPrefixMatches(String wholePrefix, int maxPrefixLength,
                                     String expectedPrefix) {
        assertEquals(expectedPrefix, StringUtilities.getShortPrefix(wholePrefix, maxPrefixLength));
    }

    /** Assert suffix matches.
     *
     * @param wholeSuffix
     *            the whole suffix
     * @param maxSuffixLength
     *            the max suffix length
     * @param expectedSuffix
     *            the expected suffix
     */
    private void assertSuffixMatches(String wholeSuffix, int maxSuffixLength,
                                     String expectedSuffix) {
        assertEquals(expectedSuffix, StringUtilities.getShortSuffix(wholeSuffix, maxSuffixLength));
    }

    /** Test get short suffix_no space.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetShortSuffix_noSpace() throws Exception {
        String wholeSuffix = "abc";
        assertSuffixMatches(wholeSuffix, 1, "a");
        assertSuffixMatches(wholeSuffix, 2, "ab");
        assertSuffixMatches(wholeSuffix, 3, wholeSuffix);
        assertSuffixMatches(wholeSuffix, 4, wholeSuffix);
    }

    /** Test get short suffix_with space.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetShortSuffix_withSpace() throws Exception {
        String wholeSuffix = "a b c";
        assertSuffixMatches(wholeSuffix, 1, "a");
        assertSuffixMatches(wholeSuffix, 2, "a b");
        assertSuffixMatches(wholeSuffix, 3, wholeSuffix);
        assertSuffixMatches(wholeSuffix, 4, wholeSuffix);
    }

    /** Test get short suffix_with new line.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetShortSuffix_withNewLine() throws Exception {
        String wholeSuffix = "a\nb\nc";
        assertSuffixMatches(wholeSuffix, 1, "a");
        assertSuffixMatches(wholeSuffix, 2, "a\nb");
        assertSuffixMatches(wholeSuffix, 3, wholeSuffix);
        assertSuffixMatches(wholeSuffix, 4, wholeSuffix);
    }
}