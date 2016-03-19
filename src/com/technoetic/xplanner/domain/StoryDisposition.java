package com.technoetic.xplanner.domain;

import java.text.MessageFormat;

/**
 * The Class StoryDisposition.
 */
public class StoryDisposition extends CharacterEnum {

    /** The Constant PLANNED_NAME. */
    public static final String PLANNED_NAME = "planned";
    
    /** The Constant CARRIED_OVER_NAME. */
    public static final String CARRIED_OVER_NAME = "carriedOver";
    
    /** The Constant DISCOVERED_NAME. */
    public static final String DISCOVERED_NAME = "discovered";
    
    /** The Constant ADDED_NAME. */
    public static final String ADDED_NAME = "added";
    
    /** The Constant OVERHEAD_NAME. */
    public static final String OVERHEAD_NAME = "overhead";

    /** The Constant NAME_KEY_TEMPLATE. */
    private static final String NAME_KEY_TEMPLATE = "story.disposition.{0}.name";
    
    /** The Constant ABBREVIATION_KEY_TEMPLATE. */
    private static final String ABBREVIATION_KEY_TEMPLATE = "story.disposition.{0}.abbreviation";

    /** The Constant PLANNED. */
    public static final StoryDisposition PLANNED = new StoryDisposition('p',
            StoryDisposition.PLANNED_NAME);
    
    /** The Constant CARRIED_OVER. */
    public static final StoryDisposition CARRIED_OVER = new StoryDisposition(
            'c', StoryDisposition.CARRIED_OVER_NAME);
    
    /** The Constant ADDED. */
    public static final StoryDisposition ADDED = new StoryDisposition('a',
            StoryDisposition.ADDED_NAME);

    /** The Constant enums. */
    private static final StoryDisposition[] enums = { StoryDisposition.PLANNED,
            StoryDisposition.CARRIED_OVER, StoryDisposition.ADDED };

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.CharacterEnum#getNameKey()
     */
    @Override
    public String getNameKey() {
        return MessageFormat.format(StoryDisposition.NAME_KEY_TEMPLATE,
                new String[] { this.name });
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.CharacterEnum#getAbbreviationKey()
     */
    @Override
    public String getAbbreviationKey() {
        return MessageFormat.format(StoryDisposition.ABBREVIATION_KEY_TEMPLATE,
                new String[] { this.name });
    }

    /**
     * Instantiates a new story disposition.
     *
     * @param code
     *            the code
     * @param name
     *            the name
     */
    protected StoryDisposition(final char code, final String name) {
        super(code, name);
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.CharacterEnum#listEnums()
     */
    @Override
    public CharacterEnum[] listEnums() {
        return StoryDisposition.enums;
    }

    /**
     * Value of.
     *
     * @param name
     *            the name
     * @return the story disposition
     */
    public static StoryDisposition valueOf(final String name) {
        return (StoryDisposition) CharacterEnum.valueOf(name,
                StoryDisposition.enums);
    }

    /**
     * From code.
     *
     * @param code
     *            the code
     * @return the story disposition
     */
    public static StoryDisposition fromCode(final char code) {
        return (StoryDisposition) CharacterEnum.fromCode(code,
                StoryDisposition.enums);
    }

    /**
     * From name key.
     *
     * @param key
     *            the key
     * @return the story disposition
     */
    public static StoryDisposition fromNameKey(final String key) {
        return (StoryDisposition) CharacterEnum.fromNameKey(key,
                StoryDisposition.enums);
    }

    /**
     * From name.
     *
     * @param name
     *            the name
     * @return the story disposition
     */
    public static StoryDisposition fromName(final String name) {
        return (StoryDisposition) CharacterEnum.fromName(name,
                StoryDisposition.enums);
    }
}
