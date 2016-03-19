package com.technoetic.xplanner.security.auth;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.xplanner.domain.DomainObject;

import com.technoetic.xplanner.security.AuthenticationException;

/**
 * The Class MockAuthorizer.
 */
public class MockAuthorizer implements Authorizer {
   
   /** The has permission call count. */
   public int hasPermissionCallCount;
   
   /** The get people for principal on project count. */
   public int getPeopleForPrincipalOnProjectCount = 0;
   
   /** The has permission person id. */
   public int hasPermissionPersonId;
   
   /** The has permission resource type. */
   public String hasPermissionResourceType;
   
   /** The has permission resource id. */
   public int hasPermissionResourceId;
   
   /** The has permission permission. */
   public String hasPermissionPermission;
   
   /** The has permission return. */
   public Boolean hasPermissionReturn;
   
   /** The has permission returns. */
   public Boolean[] hasPermissionReturns;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.security.auth.Authorizer#hasPermission(int, int, java.lang.String, int, java.lang.String)
    */
   public boolean hasPermission(int projectId, int personId, String resourceType,
                                int resourceId, String permission) throws AuthenticationException {
      if (mapPersonToResources != null) {
         Map map = (Map) mapPersonToResources.get(new Integer(personId));
         if (map == null) {
            return false;
         }
         String actualPermission = (String) map.get(new Integer(resourceId));
         return permission.equalsIgnoreCase(actualPermission);
      }

      hasPermissionCallCount++;
      hasPermissionPersonId = personId;
      hasPermissionResourceType = resourceType;
      hasPermissionResourceId = resourceId;
      hasPermissionPermission = permission;
      return hasPermissionReturns != null
             ? (hasPermissionReturns[hasPermissionCallCount - 1]).booleanValue()
             : hasPermissionReturn.booleanValue();
   }

   /** The has permission2 count. */
   public int hasPermission2Count;
   
   /** The has permission2 project id. */
   public int hasPermission2ProjectId;
   
   /** The has permission2 person id. */
   public int hasPermission2PersonId;
   
   /** The has permission2 domain object. */
   public Object hasPermission2DomainObject;
   
   /** The has permission2 permission. */
   public String hasPermission2Permission;
   
   /** The has permission2 return. */
   public Boolean hasPermission2Return;
   
   /** The has permission2 returns. */
   public Boolean[] hasPermission2Returns;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.security.auth.Authorizer#hasPermission(int, int, java.lang.Object, java.lang.String)
    */
   public boolean hasPermission(int projectId, int personId, Object resource,
                                String permission) throws AuthenticationException {
      if (mapPersonToResources != null) {
         // ChangeSoon delegate to the other implementation even if the map is null
         return hasPermission(projectId, personId, "", ((DomainObject) resource).getId(), permission);
      }
      hasPermission2Count++;
      hasPermission2ProjectId = projectId;
      hasPermission2PersonId = personId;
      hasPermission2Permission = permission;
      hasPermission2DomainObject = resource;
      return hasPermission2Returns != null
             ? (hasPermission2Returns[hasPermission2Count - 1]).booleanValue()
             : hasPermission2Return.booleanValue();
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.security.auth.Authorizer#getRolesForPrincipalOnProject(int, int, boolean)
    */
   public Collection getRolesForPrincipalOnProject(int principalId, int projectId, boolean wildcard)
         throws AuthenticationException {
      throw new UnsupportedOperationException();
   }

   /** The invalidate cache called. */
   public boolean invalidateCacheCalled;
   
   /** The invalidate cache principal id. */
   public int invalidateCachePrincipalId;

   /** Logout.
     *
     * @param principalId
     *            the principal id
     */
   public void logout(int principalId) {
      invalidateCacheCalled = true;
      invalidateCachePrincipalId = principalId;
   }

   /** Invalidate cache.
     */
   public void invalidateCache() {
      invalidateCacheCalled = true;
      invalidateCachePrincipalId = -1;
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.security.auth.Authorizer#hasPermissionForSomeProject(int, java.lang.String, int, java.lang.String)
    */
   public boolean hasPermissionForSomeProject(int personlId,
                                              String resourceType, int resourceId, String permissions)
         throws AuthenticationException {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.security.auth.Authorizer#hasPermissionForSomeProject(java.util.Collection, int, java.lang.String, int, java.lang.String)
    */
   public boolean hasPermissionForSomeProject(Collection projects,
                                              int personId,
                                              String resourceType,
                                              int resourceId,
                                              String permission) throws AuthenticationException {
      throw new UnsupportedOperationException();
   }

   /** The get people for principal on project return. */
   public Collection getPeopleForPrincipalOnProjectReturn;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.security.auth.Authorizer#getPeopleWithPermissionOnProject(java.util.List, int)
    */
   public Collection getPeopleWithPermissionOnProject(List allPeople, int projectId) throws AuthenticationException {
      getPeopleForPrincipalOnProjectCount++;
      return getPeopleForPrincipalOnProjectReturn;
   }

   /** The map person to resources. */
   HashMap mapPersonToResources = null;

   /** Give permission.
     *
     * @param personId
     *            the person id
     * @param resource
     *            the resource
     * @param permission
     *            the permission
     */
   public void givePermission(int personId, DomainObject resource, String permission) {
      Integer personKey = new Integer(personId);
      if (mapPersonToResources == null) {
         mapPersonToResources = new HashMap();
      }
      Map map = (Map) mapPersonToResources.get(personKey);
      if (map == null) {
         map = new HashMap();
         mapPersonToResources.put(personKey, map);
      }
      map.put(new Integer(resource.getId()), permission);
   }
}
