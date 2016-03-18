package com.technoetic.xplanner.domain;

public class StoryDispositionType extends CharacterEnumType {

	@Override
	public Class returnedClass() {
		return StoryDisposition.class;
	}

	@Override
	protected CharacterEnum getType(final String code) {
		return StoryDisposition.fromCode(code.charAt(0));
	}

}
