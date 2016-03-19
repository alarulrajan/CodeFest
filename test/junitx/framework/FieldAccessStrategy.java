/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Mar 31, 2006
 * Time: 11:54:33 PM
 */
package junitx.framework;

import java.util.List;

import com.technoetic.xplanner.util.ClassUtil;

/**
 * The Class FieldAccessStrategy.
 */
public class FieldAccessStrategy extends MemberAccessStrategy {

   /* (non-Javadoc)
    * @see junitx.framework.MemberAccessStrategy#getMemberValue(java.lang.Object, java.lang.String)
    */
   protected Object getMemberValue(Object object, String member) throws Exception {
      return ClassUtil.getFieldValue(object, member);
   }

   /* (non-Javadoc)
    * @see junitx.framework.MemberAccessStrategy#getMembers(java.lang.Object)
    */
   protected List getMembers(Object object) throws Exception {
     return ClassUtil.getAllFieldNames(object);
   }
}
