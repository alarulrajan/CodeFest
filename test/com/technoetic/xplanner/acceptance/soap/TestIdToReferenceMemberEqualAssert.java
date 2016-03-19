package com.technoetic.xplanner.acceptance.soap;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junitx.framework.PropertyAccessStrategy;

/**
 * The Class TestIdToReferenceMemberEqualAssert.
 */
public class TestIdToReferenceMemberEqualAssert extends TestCase {
    
    /** The equals assert. */
    IdToReferenceMemberEqualAssert equalsAssert = new IdToReferenceMemberEqualAssert();

    /** Test not interesting property.
     *
     * @throws Exception
     *             the exception
     */
    public void testNotInterestingProperty() throws Exception {
        assertFalse(equalsAssert.assertEquals("reference", new Object(), new Object(), new PropertyAccessStrategy()));
    }

    /** Test id property matching equal properties.
     *
     * @throws Exception
     *             the exception
     */
    public void testIdPropertyMatchingEqualProperties() throws Exception {
        assertTrue(equalsAssert.assertEquals("referredId", new ReferenceIdOwner(1), new Referrer(new Referree(1)), new PropertyAccessStrategy()));
        assertTrue(equalsAssert.assertEquals("referredId", new Referrer(new Referree(1)), new ReferenceIdOwner(1), new PropertyAccessStrategy()));
    }

    /** Test id property matching not equal properties.
     *
     * @throws Exception
     *             the exception
     */
    public void testIdPropertyMatchingNotEqualProperties() throws Exception {
        try {
            equalsAssert.assertEquals("referredId", new ReferenceIdOwner(1), new Referrer(new Referree(2)), new PropertyAccessStrategy());
            fail("Did not throw an assertion");
        } catch (AssertionFailedError e) {
            return;
        }
        try {
            equalsAssert.assertEquals("referredId", new Referrer(new Referree(2)),new ReferenceIdOwner(1), new PropertyAccessStrategy());
            fail("Did not throw an assertion");
        } catch (AssertionFailedError e) {
            return;
        }
    }

    /** Test id property not matching properties.
     *
     * @throws Exception
     *             the exception
     */
    public void testIdPropertyNotMatchingProperties() throws Exception {
        assertFalse(equalsAssert.assertEquals("referredId", new ReferenceIdOwner(1), new Object(), new PropertyAccessStrategy()));
    }

    /** The Class ReferenceIdOwner.
     */
    public class ReferenceIdOwner {
        
        /** The referred id. */
        int referredId;

        /** Instantiates a new reference id owner.
         *
         * @param referreeId
         *            the referree id
         */
        public ReferenceIdOwner(int referreeId) {this.referredId = referreeId; }

        /** Gets the referred id.
         *
         * @return the referred id
         */
        public int getReferredId() {return referredId;}

        /** Sets the referred id.
         *
         * @param referredId
         *            the new referred id
         */
        public void setReferredId(int referredId) {this.referredId = referredId; }

    }

    /** The Class Referrer.
     */
    public class Referrer {
        
        /** The referred. */
        Referree referred;

        /** Instantiates a new referrer.
         *
         * @param reference
         *            the reference
         */
        public Referrer(Referree reference) { this.referred = reference; }

        /** Gets the referred.
         *
         * @return the referred
         */
        public Referree getReferred() {return referred;}

        /** Sets the referred.
         *
         * @param referred
         *            the new referred
         */
        public void setReferred(Referree referred) {this.referred = referred; }
    }

    /** The Class Referree.
     */
    public class Referree {
        
        /** The id. */
        int id;

        /** Instantiates a new referree.
         *
         * @param id
         *            the id
         */
        public Referree(int id) {this.id = id; }

        /** Gets the id.
         *
         * @return the id
         */
        public int getId() {return id;}

        /** Sets the id.
         *
         * @param id
         *            the new id
         */
        public void setId(int id) {this.id = id; }
    }

}