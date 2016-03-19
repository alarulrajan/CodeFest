package com.technoetic.xplanner.domain;

/**
 * The Class StoryStatusType.
 */
public class StoryStatusType extends CharacterEnumType {

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.CharacterEnumType#returnedClass()
     */
    @Override
    public Class returnedClass() {
        return StoryStatus.class;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.CharacterEnumType#getType(java.lang.String)
     */
    @Override
    protected CharacterEnum getType(final String code) {
        return StoryStatus.fromCode(code.charAt(0));
    }

}
