package com.technoetic.xplanner.domain;

import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA. User: sg897500 Date: Aug 9, 2005 Time: 11:16:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskDisposition extends CharacterEnum {

    /** The original. */
    boolean original;

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
    private static final String NAME_KEY_TEMPLATE = "task.disposition.{0}.name";
    
    /** The Constant ABBREVIATION_KEY_TEMPLATE. */
    private static final String ABBREVIATION_KEY_TEMPLATE = "task.disposition.{0}.abbreviation";

    /** The Constant PLANNED. */
    public static final TaskDisposition PLANNED = new TaskDisposition('p',
            TaskDisposition.PLANNED_NAME, true);
    
    /** The Constant CARRIED_OVER. */
    public static final TaskDisposition CARRIED_OVER = new TaskDisposition('c',
            TaskDisposition.CARRIED_OVER_NAME, true);
    
    /** The Constant ADDED. */
    public static final TaskDisposition ADDED = new TaskDisposition('a',
            TaskDisposition.ADDED_NAME, false);
    
    /** The Constant DISCOVERED. */
    public static final TaskDisposition DISCOVERED = new TaskDisposition('d',
            TaskDisposition.DISCOVERED_NAME, false);
    
    /** The Constant OVERHEAD. */
    public static final TaskDisposition OVERHEAD = new TaskDisposition('o',
            TaskDisposition.OVERHEAD_NAME, false);

    /** The Constant enums. */
    private static final TaskDisposition[] enums = { TaskDisposition.PLANNED,
            TaskDisposition.CARRIED_OVER, TaskDisposition.ADDED,
            TaskDisposition.DISCOVERED, TaskDisposition.OVERHEAD };

    /**
     * Instantiates a new task disposition.
     *
     * @param code
     *            the code
     * @param name
     *            the name
     * @param original
     *            the original
     */
    TaskDisposition(final char code, final String name, final boolean original) {
        super(code, name);
        this.original = original;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.CharacterEnum#getAbbreviationKey()
     */
    @Override
    public String getAbbreviationKey() {
        return MessageFormat.format(TaskDisposition.ABBREVIATION_KEY_TEMPLATE,
                new String[] { this.name });
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.CharacterEnum#getNameKey()
     */
    @Override
    public String getNameKey() {
        return MessageFormat.format(TaskDisposition.NAME_KEY_TEMPLATE,
                new String[] { this.name });
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.CharacterEnum#listEnums()
     */
    @Override
    public CharacterEnum[] listEnums() {
        return TaskDisposition.enums;
    }

    /**
     * Checks if is original.
     *
     * @return true, if is original
     */
    public boolean isOriginal() {
        return this.original;
    }

    /**
     * Value of.
     *
     * @param key
     *            the key
     * @return the task disposition
     */
    public static TaskDisposition valueOf(final String key) {
        return (TaskDisposition) CharacterEnum.valueOf(key,
                TaskDisposition.enums);
    }

    /**
     * From name key.
     *
     * @param key
     *            the key
     * @return the task disposition
     */
    public static TaskDisposition fromNameKey(final String key) {
        return (TaskDisposition) CharacterEnum.fromNameKey(key,
                TaskDisposition.enums);
    }

    /**
     * From code.
     *
     * @param code
     *            the code
     * @return the task disposition
     */
    public static TaskDisposition fromCode(final char code) {
        return (TaskDisposition) CharacterEnum.fromCode(code,
                TaskDisposition.enums);
    }

    /**
     * From name.
     *
     * @param name
     *            the name
     * @return the task disposition
     */
    public static TaskDisposition fromName(final String name) {
        return (TaskDisposition) CharacterEnum.fromName(name,
                TaskDisposition.enums);
    }

}
