package com.technoetic.xplanner.util;

import java.io.Serializable;

/**
 * The half-open interval [low, high) inclusive on the low end. This class is
 * derived from the Interval class developed at limegroup.org
 */
public class Interval implements Serializable, Comparable {

	private final long low;
	private final long high;

	/** @requires low<=high */
	public Interval(final long low, final long high) {
		if (high < low) {
			throw new IllegalArgumentException("high, low" + high + ", " + low);
		}
		this.low = low;
		this.high = high;
	}

	public Interval(final long singleton) {
		this.low = singleton;
		this.high = singleton;
	}

	/**
	 * Compares this to another interval by the 'low' element of the interval.
	 * If the low elements are the same, then the high element is compared.
	 */
	@Override
	public int compareTo(final Object o) {
		final Interval other = (Interval) o;
		if (this.low != other.low) {
			return (int) (this.low - other.low);
		} else {
			return (int) (this.high - other.high);
		}
	}

	/**
	 * True if this and other are adjacent, i.e. the high end of one equals the
	 * low end of the other.
	 */
	public boolean adjacent(final Interval other) {
		return this.high == other.low || this.low == other.high;
	}

	/**
	 * True if this and other overlap.
	 */
	public boolean overlaps(final Interval other) {
		return this.low < other.high && other.low < this.high
				|| other.low < this.high && this.low < other.high;
	}

	@Override
	public String toString() {
		if (this.low == this.high) {
			return String.valueOf(this.low);
		} else {
			return String.valueOf(this.low) + "-" + String.valueOf(this.high);
		}
	}

	@Override
	public boolean equals(final Object o) {
		if (!(o instanceof Interval)) {
			return false;
		}
		final Interval other = (Interval) o;
		return this.low == other.low && this.high == other.high;
	}

	@Override
	public int hashCode() {
		return Long.valueOf(Long.valueOf(this.low) * Long.valueOf(this.high))
				.intValue();
	}

	public long getLow() {
		return this.low;
	}

	public long getHigh() {
		return this.high;
	}
}
