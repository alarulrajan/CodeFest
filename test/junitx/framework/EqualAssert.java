package junitx.framework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;

import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class EqualAssert.
 *
 * @noinspection ErrorNotRethrown,OverlyBroadCatchBlock,UnusedCatchParameter
 */
public class EqualAssert {
    
    /** The Constant LOG. */
    protected static final Logger LOG = LogUtil.getLogger();

    /** The Constant PROPERTIES. */
    public static final MemberAccessStrategy PROPERTIES = new PropertyAccessStrategy();
    
    /** The Constant FIELDS. */
    public static final MemberAccessStrategy FIELDS = new FieldAccessStrategy();

    /** The equals asserts. */
    private List equalsAsserts = new ArrayList();
    
    /** The access strategy. */
    private MemberAccessStrategy accessStrategy;

    /** Instantiates a new equal assert.
     */
    public EqualAssert() {
        this(new MemberEqualAssert[0]);
    }

   /** Instantiates a new equal assert.
     *
     * @param accessStrategy
     *            the access strategy
     */
   public EqualAssert(MemberAccessStrategy accessStrategy) {
      this(new MemberEqualAssert[0], accessStrategy);
   }

    /** Instantiates a new equal assert.
     *
     * @param equalAsserts
     *            the equal asserts
     */
    public EqualAssert(MemberEqualAssert[] equalAsserts) {
       this(equalAsserts, new PropertyAccessStrategy());
    }

   /** Instantiates a new equal assert.
     *
     * @param equalAsserts
     *            the equal asserts
     * @param accessStrategy
     *            the access strategy
     */
   public EqualAssert(MemberEqualAssert[] equalAsserts, MemberAccessStrategy accessStrategy) {
      this.accessStrategy = accessStrategy;
      this.equalsAsserts.addAll(Arrays.asList(equalAsserts));
      this.equalsAsserts.add(new LoggingMemberEqualAssert());
      this.equalsAsserts.add(new ExcludeClassPropertyMemberEqualAssert());
      this.equalsAsserts.add(new CalendarDateMemberEqualAssert());
      this.equalsAsserts.add(new ArrayMemberEqualAssert());
      this.equalsAsserts.add(new CollectionMemberEqualAssert(this));
      this.equalsAsserts.add(new DoubleMemberEqualAssert());
      this.equalsAsserts.add(new SimplePropertyMemberEqualAssert());
   }

   /** Assert equals.
     *
     * @param expected
     *            the expected
     * @param actual
     *            the actual
     * @param properties
     *            the properties
     */
   public void assertEquals(Object expected, Object actual, String[] properties) {
       try {
           for (int i = 0; i < properties.length; i++) {
               String propertyName = properties[i];
               for (int j = 0; j < equalsAsserts.size(); j++) {
                   MemberEqualAssert memberEqualAssert = (MemberEqualAssert) equalsAsserts.get(j);
                   if (memberEqualAssert.assertEquals(propertyName, expected, actual, accessStrategy)) break;
               }
           }
       } catch (junit.framework.AssertionFailedError e) {
           throw new AssertionFailedError(
               e.getMessage() +
               "\nexpected <" + objectToString(expected, properties) + ">" +
               "\nactual <" + objectToString(actual, properties) + ">", e);
       } catch (Exception e) {
           throw new RuntimeException(e);
       }
   }

   /** Assert equals.
     *
     * @param expected
     *            the expected
     * @param actual
     *            the actual
     */
   public void assertEquals(Object expected, Object actual) {
       String[] properties = accessStrategy.getCommonMembers(expected, actual);
       LOG.debug("asserting equality of properties " + listToString(Arrays.asList(properties), null));
       assertEquals(expected, actual, properties);
   }

    /** Assert equals.
     *
     * @param expectedObjects
     *            the expected objects
     * @param actualObjects
     *            the actual objects
     * @param properties
     *            the properties
     * @throws Exception
     *             the exception
     */
    public void assertEquals(Object[] expectedObjects, Object[] actualObjects, final String[] properties)
            throws Exception {
        assertEquals(Arrays.asList(expectedObjects), Arrays.asList(actualObjects), properties);
    }

    /** Assert equals.
     *
     * @param expectedObjects
     *            the expected objects
     * @param actualObjects
     *            the actual objects
     */
    public void assertEquals(List expectedObjects, List actualObjects) {
       if (expectedObjects.size() == 0 || actualObjects.size() == 0) {
          Assert.assertEquals("collection size ",expectedObjects.size(), actualObjects.size());
          return;
       }
       String[] properties = accessStrategy.getCommonMembers(expectedObjects.get(0), actualObjects.get(0));
       assertEquals(expectedObjects, actualObjects, properties);
    }

    /** Assert equals.
     *
     * @param expectedObjects
     *            the expected objects
     * @param actualObjects
     *            the actual objects
     * @param properties
     *            the properties
     */
    public void assertEquals(List expectedObjects, List actualObjects, final String[] properties) {
        Assert.assertEquals("wrong number of items", expectedObjects.size(), actualObjects.size());
        for (int i = 0; i < expectedObjects.size(); i++) {
            final Object expectedObject = expectedObjects.get(i);
            Object actualObject = CollectionUtils.find(actualObjects, new Predicate() {
                public boolean evaluate(Object o) {
                    return isEqual(o, expectedObject, properties);
                }
            });
            Assert.assertNotNull("Could not find " + objectToString(expectedObject, properties) + " in \n" +
                                 listToString(actualObjects, properties), actualObject);
        }
    }

    /** List to string.
     *
     * @param objects
     *            the objects
     * @param properties
     *            the properties
     * @return the string
     */
    private String listToString(List objects, String[] properties) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < objects.size(); i++) {
            buf.append("[" + i + "]={\n");
            buf.append(objectToString(objects.get(i), properties));
            buf.append("\n}\n");
        }
        return buf.toString();
    }

    /** Checks if is equal.
     *
     * @param o1
     *            the o1
     * @param o2
     *            the o2
     * @param properties
     *            the properties
     * @return true, if is equal
     */
    public boolean isEqual(Object o1, Object o2, final String[] properties) {
        try {
            assertEquals(o1, o2, properties);
        } catch (AssertionFailedError e) {
            return false;
        }
        return true;
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
      return accessStrategy.objectToString(object, properties);
   }

   /** The Class LoggingMemberEqualAssert.
     */
   public class LoggingMemberEqualAssert extends ValueMemberEqualAssert {
      
      /** The class of member. */
      Class classOfMember;
      
      /* (non-Javadoc)
       * @see junitx.framework.ValueMemberEqualAssert#assertEquals(java.lang.String, java.lang.Object, java.lang.Object, junitx.framework.MemberAccessStrategy)
       */
      public boolean assertEquals(String memberName,
                                  Object expectedObject,
                                  Object actualObject,
                                  MemberAccessStrategy accessStrategy) {
         classOfMember = expectedObject.getClass();
         return super.assertEquals(memberName, expectedObject, actualObject, accessStrategy);
      }

      /* (non-Javadoc)
       * @see junitx.framework.ValueMemberEqualAssert#assertValueEquals(java.lang.String, java.lang.Object, java.lang.Object)
       */
      public boolean assertValueEquals(String memberName, Object expectedValue, Object actualValue) {
         LOG.debug("asserting equality of property '" +
                   classOfMember.getName() + "." + memberName +
                   "' expected value (" +
                   expectedValue +
                   ") and actual value (" +
                   actualValue +
                   ")");
         return false;
      }
   }
    
    /** The Class ExcludeClassPropertyMemberEqualAssert.
     */
    public class ExcludeClassPropertyMemberEqualAssert extends ValueMemberEqualAssert {
        
        /* (non-Javadoc)
         * @see junitx.framework.ValueMemberEqualAssert#assertValueEquals(java.lang.String, java.lang.Object, java.lang.Object)
         */
        public boolean assertValueEquals(String memberName, Object expectedValue, Object actualValue) {
            return "class".equals(memberName);
        }
    }

    /** The Class SimplePropertyMemberEqualAssert.
     */
    public class SimplePropertyMemberEqualAssert extends ValueMemberEqualAssert {
        
        /* (non-Javadoc)
         * @see junitx.framework.ValueMemberEqualAssert#assertValueEquals(java.lang.String, java.lang.Object, java.lang.Object)
         */
        public boolean assertValueEquals(String memberName, Object expectedValue, Object actualValue) {
            Assert.assertEquals(memberName, expectedValue, actualValue);
            return true;
        }
    }
}