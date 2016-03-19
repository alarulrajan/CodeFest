package com.technoetic.xplanner.domain;

/**
 * The Class TaskDispositionType.
 */
public class TaskDispositionType extends CharacterEnumType {

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.CharacterEnumType#returnedClass()
     */
    @Override
    public Class returnedClass() {
        return TaskDisposition.class;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.CharacterEnumType#getType(java.lang.String)
     */
    @Override
    protected CharacterEnum getType(final String code) {
        return TaskDisposition.fromCode(code.charAt(0));
    }

}
