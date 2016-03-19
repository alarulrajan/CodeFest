package com.technoetic.xplanner.db;

import com.technoetic.xplanner.domain.Nameable;

/**
 * The Class FakeNameable.
 */
public class FakeNameable implements Nameable {
    
    /** The id. */
    private int id;
    
    /** The title. */
    private String title;
    
    /** The description. */
    private String description;

    /** Instantiates a new fake nameable.
     *
     * @param id
     *            the id
     * @param title
     *            the title
     * @param description
     *            the description
     */
    public FakeNameable(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Identifiable#getId()
     */
    public int getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Nameable#getName()
     */
    public String getName() {
        return title;
    }

    /* (non-Javadoc)
     * @see com.technoetic.xplanner.domain.Nameable#getDescription()
     */
    public String getDescription() {
        return description;
    }

	/* (non-Javadoc)
	 * @see com.technoetic.xplanner.domain.Nameable#getAttribute(java.lang.String)
	 */
	@Override
	public String getAttribute(String attributeName) {
		// ChangeSoon 
		return null;
	}
}
