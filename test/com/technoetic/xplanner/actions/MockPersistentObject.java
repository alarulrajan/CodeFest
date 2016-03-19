package com.technoetic.xplanner.actions;

import com.technoetic.xplanner.domain.DummyDomainObject;

/**
 * The Class MockPersistentObject.
 */
public class MockPersistentObject extends DummyDomainObject {
    
    /** The name. */
    private String name;
    
    /** The integer variable. */
    private int integerVariable;
    
    /** The string variable. */
    private String stringVariable;
    
    /** The another variable. */
    private MockPersistentObject anotherVariable = null;

    /** Gets the integer variable.
     *
     * @return the integer variable
     */
    public int getIntegerVariable() {
        return integerVariable;
    }

    /** Sets the integer variable.
     *
     * @param i
     *            the new integer variable
     */
    public void setIntegerVariable(int i) {
        integerVariable = i;
    }

    /** Gets the string variable.
     *
     * @return the string variable
     */
    public String getStringVariable() {
        return stringVariable;
    }

    /** Sets the string variable.
     *
     * @param s
     *            the new string variable
     */
    public void setStringVariable(String s) {
        stringVariable = s;
    }

    /** Gets the another variable.
     *
     * @return the another variable
     */
    public MockPersistentObject getAnotherVariable() {
        return anotherVariable;
    }

    /** Sets the another variable.
     *
     * @param anotherVariable
     *            the new another variable
     */
    public void setAnotherVariable(MockPersistentObject anotherVariable) {
        this.anotherVariable = anotherVariable;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.DummyDomainObject#getName()
     */
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.domain.NamedObject#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }
}
