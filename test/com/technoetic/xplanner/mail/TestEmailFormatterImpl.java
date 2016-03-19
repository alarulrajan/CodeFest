package com.technoetic.xplanner.mail;

/**
 * User: Mateusz Prokopowicz
 * Date: May 19, 2005
 * Time: 4:23:04 PM
 */

import static org.easymock.EasyMock.expect;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.XPlannerProperties;
import com.technoetic.xplanner.util.HttpClient;

/**
 * The Class TestEmailFormatterImpl.
 */
public class TestEmailFormatterImpl extends AbstractUnitTestCase
{
   
   /** The email formatter impl. */
   EmailFormatterImpl emailFormatterImpl;
   
   /** The mock http client. */
   HttpClient mockHttpClient;
   
   /** The properties. */
   XPlannerProperties properties = new XPlannerProperties();

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      mockHttpClient = createLocalMock(HttpClient.class);
      emailFormatterImpl = new EmailFormatterImpl();
   }

   /** Test format email entry.
     *
     * @throws Exception
     *             the exception
     */
   public void testFormatEmailEntry() throws Exception
   {
      List entryList = new ArrayList();
      final MockTemplate mockTemplate = new MockTemplate();
      VelocityEngine mockVelocityEngine = new VelocityEngine()
      {
         public Template getTemplate(String name)
         {
            return mockTemplate;
         }
      };

      emailFormatterImpl.setVelocityEngine(mockVelocityEngine);
      emailFormatterImpl.setHttpClient(mockHttpClient);
      expect(
         mockHttpClient.getPage(properties.getProperty("xplanner.application.url") + "/css/email.css")).andReturn("");
      replay();
      emailFormatterImpl.formatEmailEntry("header",
                                          "footer",
                                          "story",
                                          "task",
                                          entryList);
      verify();
      assertTrue("Velocity has not been called", mockTemplate.mergeCalled);
   }

   /** The Class MockTemplate.
     */
   private static class MockTemplate extends Template
   {
      
      /** The merge called. */
      boolean mergeCalled = false;

      /* (non-Javadoc)
       * @see org.apache.velocity.Template#merge(org.apache.velocity.context.Context, java.io.Writer)
       */
      public void merge(Context context, Writer writer)
      {
         mergeCalled = true;
      }
   }
}