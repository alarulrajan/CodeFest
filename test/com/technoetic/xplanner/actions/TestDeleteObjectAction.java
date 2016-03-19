package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.expect;
import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Project;

import org.apache.struts.action.ActionForward;

import com.technoetic.xplanner.domain.repository.RepositoryException;

/**
 * The Class TestDeleteObjectAction.
 */
public class TestDeleteObjectAction extends AbstractActionTestCase {

   /** The Constant OID. */
   public static final int OID = 11;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.actions.AbstractActionTestCase#setUp()
    */
   public void setUp() throws Exception {
       action = new DeleteObjectAction();
       super.setUp();
       expectObjectRepositoryAccess(Project.class);
    }

    /** Test delete object action.
     *
     * @throws Exception
     *             the exception
     */
    public void testDeleteObjectAction() throws Exception {
        setUpProjectDeletion();
        replay();

        support.executeAction(action);

        verify();

    }

    /** Sets the up project deletion.
     *
     * @throws RepositoryException
     *             the repository exception
     */
    private void setUpProjectDeletion() throws RepositoryException {
        support.setForward(AbstractAction.TYPE_KEY, Project.class.getName());
        support.request.setParameterValue("oid", new String[]{Integer.toString(OID)});
        DomainObject domainObject = createLocalMock(DomainObject.class);
		expect(mockObjectRepository.load(OID)).andReturn(domainObject);
        eventBus.publishDeleteEvent(domainObject, null);
        mockObjectRepository.delete(OID);
    }

    /** Test delete object action with return to.
     *
     * @throws Exception
     *             the exception
     */
    public void testDeleteObjectActionWithReturnTo() throws Exception {
        setUpProjectDeletion();
        support.request.setParameterValue("returnto", new String[]{ "RETURN"});
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertNotNull("null forward", forward);
        assertEquals("RETURN", forward.getPath());
        assertTrue(forward.getRedirect());
    }

    /** Test delete object action without return to.
     *
     * @throws Exception
     *             the exception
     */
    public void testDeleteObjectActionWithoutReturnTo() throws Exception {
        setUpProjectDeletion();
        support.setForward("view/projects", "projects.jsp");
        replay();

        ActionForward forward = support.executeAction(action);

        verify();
        assertNotNull("null forward", forward);
        assertEquals("view/projects", forward.getName());
        assertEquals("projects.jsp", forward.getPath());
        assertFalse(forward.getRedirect());
    }
}

