package com.technoetic.xplanner.domain;

//TODO refactor to use Enum
public enum IterationStatus {
	ACTIVE(new Short("0")), INACTIVE(new Short("1"));
	protected final short code;

	public static final String ACTIVE_KEY = "active";
	public static final String INACTIVE_KEY = "inactive";

	public static final String[] KEYS = { IterationStatus.ACTIVE_KEY,
			IterationStatus.INACTIVE_KEY };

	private IterationStatus(final short code) {
		this.code = code;
	}

	public String getKey() {
		return IterationStatus.KEYS[this.code];
	}

	public short toInt() {
		return this.code;
	}

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

	@Override
	public String toString() {
		return this.getKey();
	}

	public static IterationStatus fromInt(final Short statusShort) {
		for (final IterationStatus iterationStatus : IterationStatus.values()) {
			if (iterationStatus.code == statusShort) {
				return iterationStatus;
			}
		}
		return INACTIVE;
	}

}
