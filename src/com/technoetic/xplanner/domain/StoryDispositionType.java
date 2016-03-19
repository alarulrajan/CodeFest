package com.technoetic.xplanner.domain;

/**
 * The Class StoryDispositionType.
 */
public class StoryDispositionType extends CharacterEnumType {

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.CharacterEnumType#returnedClass()
	 */
	@Override
	public Class returnedClass() {
		return StoryDisposition.class;
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.CharacterEnumType#getType(java.lang.String)
	 */
	@Override
	protected CharacterEnum getType(final String code) {
		return StoryDisposition.fromCode(code.charAt(0));
	}

}
