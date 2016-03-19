package com.technoetic.xplanner.domain;

import junit.framework.TestCase;

/**
 * The Class TestStoryDisposition.
 */
public class TestStoryDisposition extends TestCase {
   
   /** The story disposition. */
   private StoryDisposition storyDisposition;

   /* (non-Javadoc)
    * @see junit.framework.TestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      storyDisposition = StoryDisposition.ADDED;
   }

   /** Test get keys.
     *
     * @throws Exception
     *             the exception
     */
   public void testGetKeys() throws Exception {
      assertEquals("story.disposition.added.name", storyDisposition.getNameKey());
      assertEquals("story.disposition.added.abbreviation", storyDisposition.getAbbreviationKey());
   }

   /** Test value of.
     *
     * @throws Exception
     *             the exception
     */
   public void testValueOf() throws Exception {
      assertEquals(StoryDisposition.ADDED_NAME, storyDisposition.getName());
   }

   /** Test from code.
     *
     * @throws Exception
     *             the exception
     */
   public void testFromCode() throws Exception {
      assertEquals(storyDisposition, StoryDisposition.fromCode('a'));
   }

   /** Test from name.
     *
     * @throws Exception
     *             the exception
     */
   public void testFromName() throws Exception {
      assertEquals(storyDisposition, StoryDisposition.fromName(StoryDisposition.ADDED_NAME));
   }
}