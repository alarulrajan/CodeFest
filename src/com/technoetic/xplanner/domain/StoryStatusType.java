package com.technoetic.xplanner.domain;

public class StoryStatusType extends CharacterEnumType {

	@Override
	public Class returnedClass() {
		return StoryStatus.class;
	}

	@Override
	protected CharacterEnum getType(final String code) {
		return StoryStatus.fromCode(code.charAt(0));
	}

}
