package com.technoetic.xplanner.domain.repository;

/**
 * The Class ObjectNotFoundException.
 */
public class ObjectNotFoundException extends RepositoryException {
    
    /**
     * Instantiates a new object not found exception.
     *
     * @param message
     *            the message
     */
    public ObjectNotFoundException(final String message) {
        super(message);
    }
}
