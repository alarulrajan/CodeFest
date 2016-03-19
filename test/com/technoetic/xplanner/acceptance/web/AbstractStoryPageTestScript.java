/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: Jun 18, 2005
 * Time: 3:14:57 PM
 */
package com.technoetic.xplanner.acceptance.web;

import com.technoetic.xplanner.domain.CharacterEnum;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.TaskDisposition;

/**
 * The Class AbstractStoryPageTestScript.
 */
public abstract class AbstractStoryPageTestScript extends AbstractPageTestScript {
   
   /** Instantiates a new abstract story page test script.
     *
     * @param test
     *            the test
     */
   public AbstractStoryPageTestScript(String test) {
      super(test);
   }

   /** Assert disposition equals.
     *
     * @param storyName
     *            the story name
     * @param disposition
     *            the disposition
     */
   protected void assertDispositionEquals(String storyName, StoryDisposition disposition) {
      assertEnumFieldEquals(storyName, disposition);
   }

   /** Assert disposition equals.
     *
     * @param taskName
     *            the task name
     * @param disposition
     *            the disposition
     */
   protected void assertDispositionEquals(String taskName, TaskDisposition disposition) {
      assertEnumFieldEquals(taskName, disposition);
   }

  /**
     * Assert enum field equals.
     *
     * @param object
     *            the object
     * @param e
     *            the e
     */
  private void assertEnumFieldEquals(String object, CharacterEnum e) {
     tester.assertTextPresent(object);
     tester.assertTextPresent(tester.getMessage(e.getNameKey()));
  }

}