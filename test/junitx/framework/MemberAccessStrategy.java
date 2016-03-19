/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Mar 31, 2006
 * Time: 11:52:56 PM
 */
package junitx.framework;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * The Class MemberAccessStrategy.
 */
public abstract class MemberAccessStrategy {
   
   /** Gets the valid member value.
     *
     * @param object
     *            the object
     * @param member
     *            the member
     * @return the valid member value
     */
   public Object getValidMemberValue(Object object, String member) {
      try {
         return getMemberValue(object, member);
      } catch (Exception e) {
         String message = e.getMessage();
         if (object != null) {
            message += " accessing field " + object.getClass().getName() + "." + member;
         }
         throw new RuntimeException(message, e);
      }
   }

   /** Gets the common members.
     *
     * @param object1
     *            the object1
     * @param object2
     *            the object2
     * @return the common members
     */
   public String[] getCommonMembers(Object object1, Object object2) {
      List list = new ArrayList();
      try {
         List object1Members = getMembers(object1);
         List object2Members = getMembers(object2);
         //noinspection ForLoopWithMissingComponent
         for (Iterator iterator = object1Members.iterator(); iterator.hasNext();) {
            String name = (String) iterator.next();
            if (object2Members.contains(name)) {
               list.add(name);
            }
         }
         return (String[]) list.toArray(new String[list.size()]);
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   /** Object to string.
     *
     * @param object
     *            the object
     * @param properties
     *            the properties
     * @return the string
     */
   public String objectToString(Object object, String[] properties) {
       StringBuffer buf = new StringBuffer();
       if (object == null) return "null";
       if (object.getClass().isPrimitive() ||
           object.getClass() == String.class ||
           properties == null || properties.length == 0)
          return object.toString();

       buf.append(object.getClass().getName());
       buf.append("{");
       for (int i = 0; i < properties.length; i++) {
           if (i > 0) buf.append(",");
           String property = properties[i];
           Object value = getValidMemberValue(object, property);
           if (value != null && value instanceof Calendar) {
               value = ((Calendar) value).getTime();
           }
           if (value != null && value instanceof Date) {
               value = new Date(((Date) value).getTime());
           }
           if (value != null && value.getClass().isArray()) {
               buf.append(value.getClass().getName() + "[]{");
               for (int j = 0; j < Array.getLength(value); j++) {
                   if (j > 0) buf.append(",");
                   buf.append(Array.get(value, j));
               }
               buf.append("}");
               continue;
           }
           buf.append(property + "=" + value);
       }
       buf.append("}");
       return buf.toString();
   }

   /** Gets the member value.
     *
     * @param object
     *            the object
     * @param member
     *            the member
     * @return the member value
     * @throws Exception
     *             the exception
     */
   protected abstract Object getMemberValue(Object object, String member) throws Exception;

   /** Gets the members.
     *
     * @param object1
     *            the object1
     * @return the members
     * @throws Exception
     *             the exception
     */
   protected abstract List getMembers(Object object1) throws Exception;
}