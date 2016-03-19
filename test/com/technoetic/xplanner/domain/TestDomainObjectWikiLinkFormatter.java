package com.technoetic.xplanner.domain;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

/**
 * The Class TestDomainObjectWikiLinkFormatter.
 */
public class TestDomainObjectWikiLinkFormatter extends TestCase {
   
   /** The formatter. */
   DomainObjectWikiLinkFormatter formatter;

   /** Test format story.
     *
     * @throws Exception
     *             the exception
     */
   public void testFormatStory() throws Exception {
      formatter = new DomainObjectWikiLinkFormatter();
      assertLinkEquals("project:1", new Project());
      assertLinkEquals("iteration:1", new Iteration());
      assertLinkEquals("story:1", new UserStory());
      assertLinkEquals("task:1", new Task());
//      assertLinkEquals("feature:1", new Feature());
   }

   /** Assert link equals.
     *
     * @param expected
     *            the expected
     * @param object
     *            the object
     */
   private void assertLinkEquals(String expected, net.sf.xplanner.domain.DomainObject object) {
      object.setId(1);
      assertEquals(expected, formatter.format(object));
   }
}