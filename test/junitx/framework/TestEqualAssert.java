package junitx.framework;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

/**
 * The Class TestEqualAssert.
 *
 * @noinspection ErrorNotRethrown
 */
public class TestEqualAssert extends TestCase {
    
    /** The assert object. */
    EqualAssert assertObject = new EqualAssert();

    /** Test assert equals default equals assert0 property and exclude class
     * property.
     *
     * @throws Exception
     *             the exception
     */
    public void testAssertEqualsDefaultEqualsAssert0PropertyAndExcludeClassProperty() throws Exception {
        Test0PropertyBean bean1 = new Test0PropertyBean();
        Test0PropertyBean bean2 = new Test0PropertyBean(){};
        assertAssertEqualsFindObjectsEqual(bean1, bean2);
    }

    /** Test assert equals default equals assert1 property.
     *
     * @throws Exception
     *             the exception
     */
    public void testAssertEqualsDefaultEqualsAssert1Property() throws Exception {
        Test1PropertyBean bean1 = new Test1PropertyBean();
        Test1PropertyBean bean2 = new Test1PropertyBean();
        bean1.setId(1);
        bean2.setId(1);
        assertAssertEqualsFindObjectsEqual(bean1, bean2);
        bean2.setId(2);
        assertAssertEqualsFindObjectsNotEqual(bean1, bean2);
    }

    /** Test assert equals default equals assert3 property.
     *
     * @throws Exception
     *             the exception
     */
    public void testAssertEqualsDefaultEqualsAssert3Property() throws Exception {
        Test3PropertyBean bean1 = new Test3PropertyBean();
        Test3PropertyBean bean2 = new Test3PropertyBean();
        bean1.setId(1);
        bean2.setId(1);
        bean1.setOid(2);
        bean2.setOid(2);
        bean1.setStr("test");
        bean2.setStr("test");
        assertAssertEqualsFindObjectsEqual(bean1, bean2);
        bean2.setId(2);
        bean2.setOid(3);
        bean2.setStr("Test2");
        assertAssertEqualsFindObjectsNotEqual(bean1, bean2);
    }

    /** Test assert equals with custom equals assert.
     *
     * @throws Exception
     *             the exception
     */
    public void testAssertEqualsWithCustomEqualsAssert() throws Exception {
        TestDoublePropertyBean bean1 = new TestDoublePropertyBean();
        TestDoublePropertyBean bean2 = new TestDoublePropertyBean();
        assertObject = new EqualAssert(new MemberEqualAssert[]{new DoubleMemberEqualAssert(5.0)});
        bean1.setDouble(10.0);
        bean2.setDouble(14.9);
        assertAssertEqualsFindObjectsEqual(bean1, bean2);
        bean2.setDouble(15.0);
        assertAssertEqualsFindObjectsNotEqual(bean1, bean2);
    }

    /** Assert assert equals find objects not equal.
     *
     * @param bean1
     *            the bean1
     * @param bean2
     *            the bean2
     */
    private void assertAssertEqualsFindObjectsNotEqual(Object bean1, Object bean2) {
        try {
            assertObject.assertEquals(bean1, bean2);
            fail("Did not throw an assertion failure");
        } catch (junit.framework.AssertionFailedError e) {
        }
    }

    /** Assert assert equals find objects equal.
     *
     * @param bean1
     *            the bean1
     * @param bean2
     *            the bean2
     */
    private void assertAssertEqualsFindObjectsEqual(Object bean1, Object bean2) {
        assertObject.assertEquals(bean1, bean2);
    }

    /** The Class Test0PropertyBean.
     */
    public static class Test0PropertyBean {}
    
    /** The Class Test1PropertyBean.
     */
    public static class Test1PropertyBean {
        
        /** The id. */
        private int id;

        /** Gets the id.
         *
         * @return the id
         */
        public int getId() { return id; }
        
        /** Sets the id.
         *
         * @param id
         *            the new id
         */
        public void setId(int id) { this.id = id; }
    }
    
    /** The Class Test3PropertyBean.
     */
    public static class Test3PropertyBean {
        
        /** The id. */
        private int id;
        
        /** The oid. */
        private int oid;
        
        /** The str. */
        private String str;

        /** Gets the id.
         *
         * @return the id
         */
        public int getId() { return id; }
        
        /** Sets the id.
         *
         * @param id
         *            the new id
         */
        public void setId(int id) { this.id = id; }
        
        /** Gets the oid.
         *
         * @return the oid
         */
        public int getOid() { return oid; }
        
        /** Sets the oid.
         *
         * @param oid
         *            the new oid
         */
        public void setOid(int oid) { this.oid = oid; }
        
        /** Gets the str.
         *
         * @return the str
         */
        public String getStr() { return str; }
        
        /** Sets the str.
         *
         * @param str
         *            the new str
         */
        public void setStr(String str) { this.str = str; }
    }
    
    /** The Class TestDateBean.
     */
    public static class TestDateBean {
        
        /** The date. */
        private Date date;

        /** Gets the date.
         *
         * @return the date
         */
        public Date getDate() { return date; }
        
        /** Sets the date.
         *
         * @param date
         *            the new date
         */
        public void setDate(Date date) { this.date = date; }
    }
    
    /** The Class TestCalendarBean.
     */
    public static class TestCalendarBean {
        
        /** The date. */
        private Calendar date;

        /** Gets the date.
         *
         * @return the date
         */
        public Calendar getDate() { return date; }
        
        /** Sets the date.
         *
         * @param date
         *            the new date
         */
        public void setDate(Calendar date) { this.date = date; }
    }
    
    /** The Class TestDoublePropertyBean.
     */
    public static class TestDoublePropertyBean {
        
        /** The d. */
        private double d;

        /** Gets the double.
         *
         * @return the double
         */
        public double getDouble() { return d; }
        
        /** Sets the double.
         *
         * @param d
         *            the new double
         */
        public void setDouble(double d) { this.d = d; }
    }

}