package com.technoetic.xplanner.acceptance.soap;

import junitx.framework.EqualAssert;
import junitx.framework.MemberEqualAssert;
import junitx.framework.MultiLineStringMemberEqualAssert;
import junitx.framework.TimeOnlyCalendarMemberEqualAssert;

/**
 * The Class SoapAdapterEqualAssert.
 */
public class SoapAdapterEqualAssert extends EqualAssert {
    
    /** Instantiates a new soap adapter equal assert.
     */
    public SoapAdapterEqualAssert() {
        super(new MemberEqualAssert[] {
            new IdToReferenceMemberEqualAssert(),
            new MultiLineStringMemberEqualAssert(),
            new TimeOnlyCalendarMemberEqualAssert()
        });
    }

}