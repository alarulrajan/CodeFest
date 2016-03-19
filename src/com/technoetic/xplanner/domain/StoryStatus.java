package com.technoetic.xplanner.domain;

import java.text.MessageFormat;

/**
 * The Class StoryStatus.
 */
public class StoryStatus extends CharacterEnum {

	/** The Constant DRAFT_NAME. */
	public static final String DRAFT_NAME = "draft";
	
	/** The Constant DEFINED_NAME. */
	public static final String DEFINED_NAME = "defined";
	
	/** The Constant ESTIMATED_NAME. */
	public static final String ESTIMATED_NAME = "estimated";
	
	/** The Constant PLANNED_NAME. */
	public static final String PLANNED_NAME = "planned";
	
	/** The Constant IMPLEMENTED_NAME. */
	public static final String IMPLEMENTED_NAME = "implemented";
	
	/** The Constant VERIFIED_NAME. */
	public static final String VERIFIED_NAME = "verified";
	
	/** The Constant ACCEPTED_NAME. */
	public static final String ACCEPTED_NAME = "accepted";

	/** The Constant NAME_KEY_TEMPLATE. */
	private static final String NAME_KEY_TEMPLATE = "story.status.{0}.name";
	
	/** The Constant ABBREVIATION_KEY_TEMPLATE. */
	private static final String ABBREVIATION_KEY_TEMPLATE = "story.status.{0}.abbreviation";

	/** The Constant DRAFT. */
	public static final StoryStatus DRAFT = new StoryStatus('d',
			StoryStatus.DRAFT_NAME);
	
	/** The Constant DEFINED. */
	public static final StoryStatus DEFINED = new StoryStatus('D',
			StoryStatus.DEFINED_NAME);
	
	/** The Constant ESTIMATED. */
	public static final StoryStatus ESTIMATED = new StoryStatus('e',
			StoryStatus.ESTIMATED_NAME);
	
	/** The Constant PLANNED. */
	public static final StoryStatus PLANNED = new StoryStatus('p',
			StoryStatus.PLANNED_NAME);
	
	/** The Constant IMPLEMENTED. */
	public static final StoryStatus IMPLEMENTED = new StoryStatus('i',
			StoryStatus.IMPLEMENTED_NAME);
	
	/** The Constant VERIFIED. */
	public static final StoryStatus VERIFIED = new StoryStatus('v',
			StoryStatus.VERIFIED_NAME);
	
	/** The Constant ACCEPTED. */
	public static final StoryStatus ACCEPTED = new StoryStatus('a',
			StoryStatus.ACCEPTED_NAME);

	/** The Constant enums. */
	private static transient final StoryStatus[] enums = { StoryStatus.DRAFT,
			StoryStatus.DEFINED, StoryStatus.ESTIMATED, StoryStatus.PLANNED,
			StoryStatus.IMPLEMENTED, StoryStatus.VERIFIED, StoryStatus.ACCEPTED };

	/**
     * Instantiates a new story status.
     *
     * @param code
     *            the code
     * @param name
     *            the name
     */
	StoryStatus(final char code, final String name) {
		super(code, name);
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.CharacterEnum#getAbbreviationKey()
	 */
	@Override
	public String getAbbreviationKey() {
		return MessageFormat.format(StoryStatus.ABBREVIATION_KEY_TEMPLATE,
				new String[] { this.name });
	}

	// protected StoryStatus(char code, String name) {
	// this(code, name, name, "" + name.charAt(0));
	// }

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.CharacterEnum#getNameKey()
	 */
	@Override
	public String getNameKey() {
		return MessageFormat.format(StoryStatus.NAME_KEY_TEMPLATE,
				new String[] { this.name });
	}

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.CharacterEnum#listEnums()
	 */
	@Override
	public CharacterEnum[] listEnums() {
		return StoryStatus.enums;
	}

	/**
     * Value of.
     *
     * @param key
     *            the key
     * @return the story status
     */
	public static StoryStatus valueOf(final String key) {
		return (StoryStatus) CharacterEnum.valueOf(key, StoryStatus.enums);
	}

	/**
     * From name key.
     *
     * @param key
     *            the key
     * @return the story status
     */
	public static StoryStatus fromNameKey(final String key) {
		return (StoryStatus) CharacterEnum.fromNameKey(key, StoryStatus.enums);
	}

	/**
     * From code.
     *
     * @param code
     *            the code
     * @return the story status
     */
	public static StoryStatus fromCode(final char code) {
		return (StoryStatus) CharacterEnum.fromCode(code, StoryStatus.enums);
	}

	/**
     * From name.
     *
     * @param name
     *            the name
     * @return the story status
     */
	public static StoryStatus fromName(final String name) {
		return (StoryStatus) CharacterEnum.fromName(name, StoryStatus.enums);
	}

}
