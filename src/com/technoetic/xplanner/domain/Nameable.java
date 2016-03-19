package com.technoetic.xplanner.domain;

//DEBT Should include setters
/**
 * The Interface Nameable.
 */
//DEBT Should det
public interface Nameable extends Identifiable {
    
    /**
     * Gets the name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets the description.
     *
     * @return the description
     */
    String getDescription();

    /**
     * Gets the attribute.
     *
     * @param attributeName
     *            the attribute name
     * @return the attribute
     */
    String getAttribute(String attributeName);
}
