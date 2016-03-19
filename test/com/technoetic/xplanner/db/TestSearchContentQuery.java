package com.technoetic.xplanner.db;

/**
 * Created by IntelliJ IDEA.
 * User: tkmower
 * Date: Nov 24, 2004
 * Time: 12:19:47 PM
 */

import static org.easymock.EasyMock.expect;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.db.hibernate.ThreadSession;

/**
 * The Class TestSearchContentQuery.
 */
public class TestSearchContentQuery extends AbstractUnitTestCase {
   
   /** The search content query. */
   SearchContentQuery searchContentQuery;
   
   /** The mock session. */
   private Session mockSession;
   
   /** The Constant RESTRICT_TO_PROJECT_ID. */
   protected static final int RESTRICT_TO_PROJECT_ID = 1;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
    */
   protected void setUp() throws Exception {
      super.setUp();
      mockSession = createLocalMock(Session.class);
      ThreadSession.set(mockSession);
      searchContentQuery = new SearchContentQuery();
   }

   /** Test find_ where name or description contains.
     *
     * @throws Exception
     *             the exception
     */
   public void testFind_WhereNameOrDescriptionContains() throws Exception {
      String query = Object.class.getName() + "SearchQuery";
      Query mockQuery = createLocalMock(Query.class);
      expect(mockSession.getNamedQuery(query)).andReturn(mockQuery);
      List value = Arrays.asList(new String[]{"Value 1", "Value 2"});
      expect(mockQuery.setString("contents", "%whatever%")).andReturn(null);
      expect(mockQuery.list()).andReturn(value);
      replay();

      Object loadedObject = searchContentQuery.findWhereNameOrDescriptionContains("whatever", Object.class);

      verify();
      assertEquals(value, loadedObject);
   }

   /** Test find_with restricted scope to project id.
     *
     * @throws Exception
     *             the exception
     */
   public void testFind_withRestrictedScopeToProjectId() throws Exception {
      searchContentQuery.setRestrictedProjectId(RESTRICT_TO_PROJECT_ID);
      String query = Object.class.getName() + "RestrictedSearchQuery";
      Query mockQuery = createLocalMock(Query.class);
      expect(mockSession.getNamedQuery(query)).andReturn(mockQuery);
      List value = Arrays.asList(new String[]{"Value 1", "Value 2"});
      expect(mockQuery.setString("contents", "%whatever%")).andReturn(null);
      expect(mockQuery.setInteger("projectId", RESTRICT_TO_PROJECT_ID)).andReturn(null);
      expect(mockQuery.list()).andReturn(value);
      replay();

      Object loadedObject = searchContentQuery.findWhereNameOrDescriptionContains("whatever", Object.class);

      verify();
      assertEquals(value, loadedObject);
   }
}