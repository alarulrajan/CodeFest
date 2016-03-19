package com.technoetic.xplanner.domain;

import org.hibernate.type.EnumType;

//FIXME: Rewrite it's just stub for compilation

/**
 * The Class IterationStatusPersistent.
 */
public class IterationStatusPersistent extends EnumType {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7341092190386630551L;

	/* (non-Javadoc)
	 * @see org.hibernate.type.EnumType#returnedClass()
	 */
	@Override
	public Class<IterationStatus> returnedClass() {
		return IterationStatus.class;
	}

}
