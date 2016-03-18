package com.technoetic.xplanner.domain;

import java.text.MessageFormat;

public class StoryStatus extends CharacterEnum {

	public static final String DRAFT_NAME = "draft";
	public static final String DEFINED_NAME = "defined";
	public static final String ESTIMATED_NAME = "estimated";
	public static final String PLANNED_NAME = "planned";
	public static final String IMPLEMENTED_NAME = "implemented";
	public static final String VERIFIED_NAME = "verified";
	public static final String ACCEPTED_NAME = "accepted";

	private static final String NAME_KEY_TEMPLATE = "story.status.{0}.name";
	private static final String ABBREVIATION_KEY_TEMPLATE = "story.status.{0}.abbreviation";

	public static final StoryStatus DRAFT = new StoryStatus('d',
			StoryStatus.DRAFT_NAME);
	public static final StoryStatus DEFINED = new StoryStatus('D',
			StoryStatus.DEFINED_NAME);
	public static final StoryStatus ESTIMATED = new StoryStatus('e',
			StoryStatus.ESTIMATED_NAME);
	public static final StoryStatus PLANNED = new StoryStatus('p',
			StoryStatus.PLANNED_NAME);
	public static final StoryStatus IMPLEMENTED = new StoryStatus('i',
			StoryStatus.IMPLEMENTED_NAME);
	public static final StoryStatus VERIFIED = new StoryStatus('v',
			StoryStatus.VERIFIED_NAME);
	public static final StoryStatus ACCEPTED = new StoryStatus('a',
			StoryStatus.ACCEPTED_NAME);

	private static transient final StoryStatus[] enums = { StoryStatus.DRAFT,
			StoryStatus.DEFINED, StoryStatus.ESTIMATED, StoryStatus.PLANNED,
			StoryStatus.IMPLEMENTED, StoryStatus.VERIFIED, StoryStatus.ACCEPTED };

	StoryStatus(final char code, final String name) {
		super(code, name);
	}

	@Override
	public String getAbbreviationKey() {
		return MessageFormat.format(StoryStatus.ABBREVIATION_KEY_TEMPLATE,
				new String[] { this.name });
	}

	// protected StoryStatus(char code, String name) {
	// this(code, name, name, "" + name.charAt(0));
	// }

	@Override
	public String getNameKey() {
		return MessageFormat.format(StoryStatus.NAME_KEY_TEMPLATE,
				new String[] { this.name });
	}

	@Override
	public CharacterEnum[] listEnums() {
		return StoryStatus.enums;
	}

	public static StoryStatus valueOf(final String key) {
		return (StoryStatus) CharacterEnum.valueOf(key, StoryStatus.enums);
	}

	public static StoryStatus fromNameKey(final String key) {
		return (StoryStatus) CharacterEnum.fromNameKey(key, StoryStatus.enums);
	}

	public static StoryStatus fromCode(final char code) {
		return (StoryStatus) CharacterEnum.fromCode(code, StoryStatus.enums);
	}

	public static StoryStatus fromName(final String name) {
		return (StoryStatus) CharacterEnum.fromName(name, StoryStatus.enums);
	}

}
