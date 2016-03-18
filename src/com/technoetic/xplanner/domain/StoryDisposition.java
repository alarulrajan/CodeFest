package com.technoetic.xplanner.domain;

import java.text.MessageFormat;

public class StoryDisposition extends CharacterEnum {

	public static final String PLANNED_NAME = "planned";
	public static final String CARRIED_OVER_NAME = "carriedOver";
	public static final String DISCOVERED_NAME = "discovered";
	public static final String ADDED_NAME = "added";
	public static final String OVERHEAD_NAME = "overhead";

	private static final String NAME_KEY_TEMPLATE = "story.disposition.{0}.name";
	private static final String ABBREVIATION_KEY_TEMPLATE = "story.disposition.{0}.abbreviation";

	public static final StoryDisposition PLANNED = new StoryDisposition('p',
			StoryDisposition.PLANNED_NAME);
	public static final StoryDisposition CARRIED_OVER = new StoryDisposition(
			'c', StoryDisposition.CARRIED_OVER_NAME);
	public static final StoryDisposition ADDED = new StoryDisposition('a',
			StoryDisposition.ADDED_NAME);

	public static final StoryDisposition[] enums = { StoryDisposition.PLANNED,
			StoryDisposition.CARRIED_OVER, StoryDisposition.ADDED };

	@Override
	public String getNameKey() {
		return MessageFormat.format(StoryDisposition.NAME_KEY_TEMPLATE,
				new String[] { this.name });
	}

	@Override
	public String getAbbreviationKey() {
		return MessageFormat.format(StoryDisposition.ABBREVIATION_KEY_TEMPLATE,
				new String[] { this.name });
	}

	protected StoryDisposition(final char code, final String name) {
		super(code, name);
	}

	@Override
	public CharacterEnum[] listEnums() {
		return StoryDisposition.enums;
	}

	public static StoryDisposition valueOf(final String name) {
		return (StoryDisposition) CharacterEnum.valueOf(name,
				StoryDisposition.enums);
	}

	public static StoryDisposition fromCode(final char code) {
		return (StoryDisposition) CharacterEnum.fromCode(code,
				StoryDisposition.enums);
	}

	public static StoryDisposition fromNameKey(final String key) {
		return (StoryDisposition) CharacterEnum.fromNameKey(key,
				StoryDisposition.enums);
	}

	public static StoryDisposition fromName(final String name) {
		return (StoryDisposition) CharacterEnum.fromName(name,
				StoryDisposition.enums);
	}
}
