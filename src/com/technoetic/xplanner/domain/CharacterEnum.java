package com.technoetic.xplanner.domain;

import java.io.InvalidObjectException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: sg897500 Date: Aug 9, 2005 Time: 2:49:51 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CharacterEnum implements Serializable {

	protected final char code;
	protected final String name;

	public abstract String getAbbreviationKey();

	public CharacterEnum(final char code, final String name) {
		this.code = code;
		this.name = name;
	}

	public abstract String getNameKey();

	public String getName() {
		return this.name;
	}

	public char getCode() {
		return this.code;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof CharacterEnum)) {
			return false;
		}
		return this.getName().equals(((CharacterEnum) obj).getName());
	}

	@Override
	public int hashCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return this.getName();
	}

	// See http://www.javaworld.com/javaworld/javatips/jw-javatip122.html for
	// the reason of this method
	private Object readResolve() throws InvalidObjectException {
		final CharacterEnum[] enums = this.listEnums();
		for (int i = 0; i < enums.length; i++) {
			if (enums[i].getName().equals(this.getName())) {
				return enums[i];
			}
		}
		throw new InvalidObjectException("Wrong enum value (" + this.code + ","
				+ this.name + ")");
	}

	public abstract CharacterEnum[] listEnums();

	protected static CharacterEnum valueOf(final String name,
			final CharacterEnum[] enums) {
		if (name == null) {
			return null;
		}
		for (int i = 0; i < enums.length; i++) {
			final CharacterEnum enumeration = enums[i];
			if (enumeration.getName().equals(name)) {
				return enumeration;
			}
		}

		throw new RuntimeException("Unknown enum name " + name);
	}

	protected static CharacterEnum fromName(final String name,
			final CharacterEnum[] enums) {
		return CharacterEnum.valueOf(name, enums);
	}

	protected static CharacterEnum fromNameKey(final String key,
			final CharacterEnum[] enums) {
		if (key == null) {
			return null;
		}
		for (int i = 0; i < enums.length; i++) {
			final CharacterEnum enumeration = enums[i];
			if (enumeration.getNameKey().equals(key)) {
				return enumeration;
			}
		}
		throw new RuntimeException("Unknown enum key " + key);
	}

	protected static CharacterEnum fromCode(final char code,
			final CharacterEnum[] enums) {
		for (int i = 0; i < enums.length; i++) {
			final CharacterEnum enumeration = enums[i];
			if (enumeration.code == code) {
				return enumeration;
			}
		}
		throw new RuntimeException("Unknown enum code " + code);
	}
}
