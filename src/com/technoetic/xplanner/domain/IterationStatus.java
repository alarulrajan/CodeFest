package com.technoetic.xplanner.domain;

/**
 * The Enum IterationStatus.
 */
//ChangeSoon refactor to use Enum
public enum IterationStatus {
    
    /** The active. */
    ACTIVE(new Short("0")), 
 /** The inactive. */
 INACTIVE(new Short("1"));
    
    /** The code. */
    protected final short code;

    /** The Constant ACTIVE_KEY. */
    public static final String ACTIVE_KEY = "active";
    
    /** The Constant INACTIVE_KEY. */
    public static final String INACTIVE_KEY = "inactive";

    /** The Constant KEYS. */
    public static final String[] KEYS = { IterationStatus.ACTIVE_KEY,
            IterationStatus.INACTIVE_KEY };

    /**
     * Instantiates a new iteration status.
     *
     * @param code
     *            the code
     */
    private IterationStatus(final short code) {
        this.code = code;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return IterationStatus.KEYS[this.code];
    }

    /**
     * To int.
     *
     * @return the short
     */
    public short toInt() {
        return this.code;
    }

    /**
     * From key.
     *
     * @param key
     *            the key
     * @return the iteration status
     */
    public static IterationStatus fromKey(final String key) {
        if (key == null) {
            return INACTIVE;
        } else if (IterationStatus.ACTIVE_KEY.equals(key)) {
            return ACTIVE;
        } else if (IterationStatus.INACTIVE_KEY.equals(key)) {
            return INACTIVE;
        } else {
            throw new RuntimeException("Unknown iteration status key");
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return this.getKey();
    }

    /**
     * From int.
     *
     * @param statusShort
     *            the status short
     * @return the iteration status
     */
    public static IterationStatus fromInt(final Short statusShort) {
        for (final IterationStatus iterationStatus : IterationStatus.values()) {
            if (iterationStatus.code == statusShort) {
                return iterationStatus;
            }
        }
        return INACTIVE;
    }

}
