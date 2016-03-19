package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.expect;
import net.sf.xplanner.domain.Project;

import org.apache.struts.action.ActionForward;

import com.technoetic.xplanner.tags.DomainContext;

/**
 * The Class TestViewObjectAction.
 */
public class TestViewObjectAction extends AbstractActionTestCase {
   
   /** The oid string. */
   private final String OID_STRING = "11";

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.actions.AbstractActionTestCase#setUp()
    */
   protected void setUp() throws Exception {
      action = new ViewObjectAction();
      super.setUp();
      support.request.setParameterValue("oid", new String[]{OID_STRING});
   }

   /** Test authorized access without return to.
     *
     * @throws Exception
     *             the exception
     */
   public void testAuthorizedAccessWithoutReturnTo() throws Exception {
      expectObjectRepositoryAccess(Object.class);
      support.setForward("display", "editor.jsp");
      expect(mockObjectRepository.load(11)).andReturn(new Object());
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      assertEquals("wrong forward", "editor.jsp", forward.getPath());
      assertNotNull("no domain context", DomainContext.get(support.request));
   }

   /** Test authorized access with return to.
     *
     * @throws Exception
     *             the exception
     */
   public void testAuthorizedAccessWithReturnTo() throws Exception {
      expectObjectRepositoryAccess(Object.class);
      support.setForward(AbstractAction.TYPE_KEY, Object.class.getName());
      support.setForward("display", "editor.jsp");
      support.request.setParameterValue("returnto", new String[]{"FOO"});
      expect(mockObjectRepository.load(11)).andReturn(new Object());
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      assertEquals("wrong forward", "editor.jsp?returnto=FOO", forward.getPath());
      assertNotNull("no domain context", DomainContext.get(support.request));
   }

   /** Test nonauthorized object access.
     *
     * @throws Exception
     *             the exception
     */
   public void testNonauthorizedObjectAccess() throws Exception {
      expectObjectRepositoryAccess(Object.class);
      support.setForward(AbstractAction.TYPE_KEY, Object.class.getName());
      support.setForward("display", "editor.jsp");
      ((ViewObjectAction) action).setAuthorizationRequired(false);
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      assertEquals("wrong forward", "editor.jsp", forward.getPath());
      assertNull("unexpected domain context", DomainContext.get(support.request));
   }

   /** Test type in request parameter.
     *
     * @throws Exception
     *             the exception
     */
   public void testTypeInRequestParameter() throws Exception {
      expectObjectRepositoryAccess(Project.class);
      support.request.setParameterValue(AbstractAction.TYPE_KEY,
                                        new String[]{Project.class.getName()});
      support.setForward("display", "editor.jsp");
      expect(mockObjectRepository.load(11)).andReturn(new Project());
      replay();

      ActionForward forward = support.executeAction(action);

      verify();
      assertEquals("wrong forward", "editor.jsp", forward.getPath());
      assertNotNull("no domain context", DomainContext.get(support.request));
   }


}
