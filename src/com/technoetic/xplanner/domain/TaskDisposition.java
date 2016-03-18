package com.technoetic.xplanner.domain;

import java.text.MessageFormat;

/**
 * Created by IntelliJ IDEA. User: sg897500 Date: Aug 9, 2005 Time: 11:16:08 AM
 * To change this template use File | Settings | File Templates.
 */
public class TaskDisposition extends CharacterEnum {

	boolean original;

	public static final String PLANNED_NAME = "planned";
	public static final String CARRIED_OVER_NAME = "carriedOver";
	public static final String DISCOVERED_NAME = "discovered";
	public static final String ADDED_NAME = "added";
	public static final String OVERHEAD_NAME = "overhead";

	private static final String NAME_KEY_TEMPLATE = "task.disposition.{0}.name";
	private static final String ABBREVIATION_KEY_TEMPLATE = "task.disposition.{0}.abbreviation";

	public static final TaskDisposition PLANNED = new TaskDisposition('p',
			TaskDisposition.PLANNED_NAME, true);
	public static final TaskDisposition CARRIED_OVER = new TaskDisposition('c',
			TaskDisposition.CARRIED_OVER_NAME, true);
	public static final TaskDisposition ADDED = new TaskDisposition('a',
			TaskDisposition.ADDED_NAME, false);
	public static final TaskDisposition DISCOVERED = new TaskDisposition('d',
			TaskDisposition.DISCOVERED_NAME, false);
	public static final TaskDisposition OVERHEAD = new TaskDisposition('o',
			TaskDisposition.OVERHEAD_NAME, false);

	public static final TaskDisposition[] enums = { TaskDisposition.PLANNED,
			TaskDisposition.CARRIED_OVER, TaskDisposition.ADDED,
			TaskDisposition.DISCOVERED, TaskDisposition.OVERHEAD };

	TaskDisposition(final char code, final String name, final boolean original) {
		super(code, name);
		this.original = original;
	}

	@Override
	public String getAbbreviationKey() {
		return MessageFormat.format(TaskDisposition.ABBREVIATION_KEY_TEMPLATE,
				new String[] { this.name });
	}

	@Override
	public String getNameKey() {
		return MessageFormat.format(TaskDisposition.NAME_KEY_TEMPLATE,
				new String[] { this.name });
	}

	@Override
	public CharacterEnum[] listEnums() {
		return TaskDisposition.enums;
	}

	public boolean isOriginal() {
		return this.original;
	}

	public static TaskDisposition valueOf(final String key) {
		return (TaskDisposition) CharacterEnum.valueOf(key,
				TaskDisposition.enums);
	}

	public static TaskDisposition fromNameKey(final String key) {
		return (TaskDisposition) CharacterEnum.fromNameKey(key,
				TaskDisposition.enums);
	}

	public static TaskDisposition fromCode(final char code) {
		return (TaskDisposition) CharacterEnum.fromCode(code,
				TaskDisposition.enums);
	}

	public static TaskDisposition fromName(final String name) {
		return (TaskDisposition) CharacterEnum.fromName(name,
				TaskDisposition.enums);
	}

}
