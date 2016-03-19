/*
 * Copyright (c) 2006 Your Corporation. All Rights Reserved.
 */

/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: Apr 1, 2006
 * Time: 12:17:27 AM
 */
package junitx.framework;

import java.util.ArrayList;
import java.util.Collection;

/**
 * The Class CollectionMemberEqualAssert.
 */
public class CollectionMemberEqualAssert extends ValueMemberEqualAssert {
   
   /** The equal assert. */
   private EqualAssert equalAssert;

   /** Instantiates a new collection member equal assert.
     *
     * @param equalAssert
     *            the equal assert
     */
   public CollectionMemberEqualAssert(EqualAssert equalAssert) {
      this.equalAssert = equalAssert;
   }

   /* (non-Javadoc)
    * @see junitx.framework.ValueMemberEqualAssert#assertValueEquals(java.lang.String, java.lang.Object, java.lang.Object)
    */
   @Override
   public boolean assertValueEquals(String propertyName, Object expectedValue, Object actualValue) {
       if (expectedValue != null && Collection.class.isAssignableFrom(expectedValue.getClass())) {
          equalAssert.assertEquals(new ArrayList((Collection)expectedValue), new ArrayList((Collection)actualValue));
          return true;
       }
       return false;
   }
}