package com.technoetic.xplanner.domain;

public class TaskDispositionType extends CharacterEnumType {

	@Override
	public Class returnedClass() {
		return TaskDisposition.class;
	}

	@Override
	protected CharacterEnum getType(final String code) {
		return TaskDisposition.fromCode(code.charAt(0));
	}

}
