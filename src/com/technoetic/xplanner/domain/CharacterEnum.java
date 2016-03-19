package com.technoetic.xplanner.domain;

import java.io.InvalidObjectException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: sg897500 Date: Aug 9, 2005 Time: 2:49:51 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class CharacterEnum implements Serializable {

    /** The code. */
    protected final char code;
    
    /** The name. */
    protected final String name;

    /**
     * Gets the abbreviation key.
     *
     * @return the abbreviation key
     */
    public abstract String getAbbreviationKey();

    /**
     * Instantiates a new character enum.
     *
     * @param code
     *            the code
     * @param name
     *            the name
     */
    public CharacterEnum(final char code, final String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Gets the name key.
     *
     * @return the name key
     */
    public abstract String getNameKey();

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public char getCode() {
        return this.code;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
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

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return this.code;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.getName();
    }

    // See http://www.javaworld.com/javaworld/javatips/jw-javatip122.html for
    /**
     * Read resolve.
     *
     * @return the object
     * @throws InvalidObjectException
     *             the invalid object exception
     */
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

    /**
     * List enums.
     *
     * @return the character enum[]
     */
    public abstract CharacterEnum[] listEnums();

    /**
     * Value of.
     *
     * @param name
     *            the name
     * @param enums
     *            the enums
     * @return the character enum
     */
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

    /**
     * From name.
     *
     * @param name
     *            the name
     * @param enums
     *            the enums
     * @return the character enum
     */
    protected static CharacterEnum fromName(final String name,
            final CharacterEnum[] enums) {
        return CharacterEnum.valueOf(name, enums);
    }

    /**
     * From name key.
     *
     * @param key
     *            the key
     * @param enums
     *            the enums
     * @return the character enum
     */
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

    /**
     * From code.
     *
     * @param code
     *            the code
     * @param enums
     *            the enums
     * @return the character enum
     */
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
