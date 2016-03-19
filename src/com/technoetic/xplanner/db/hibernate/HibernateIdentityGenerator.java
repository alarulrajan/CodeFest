package com.technoetic.xplanner.db.hibernate;

import java.util.Properties;

import org.hibernate.dialect.Dialect;
import org.hibernate.id.TableGenerator;
import org.hibernate.id.TableHiLoGenerator;
import org.hibernate.type.Type;

/**
 * The Class HibernateIdentityGenerator.
 */
public class HibernateIdentityGenerator extends TableHiLoGenerator {
    
    /** The Constant TABLE_NAME. */
    public static final String TABLE_NAME = "identifier";
    
    /** The Constant NEXT_ID_COL. */
    public static final String NEXT_ID_COL = "nextId";
    
    /** The Constant SET_NEXT_ID_QUERY. */
    public static final String SET_NEXT_ID_QUERY = "update "
            + HibernateIdentityGenerator.TABLE_NAME + " set "
            + HibernateIdentityGenerator.NEXT_ID_COL + " = ?";
    
    /** The Constant SET_NEXT_ID_ATOMIC_QUERY. */
    public static final String SET_NEXT_ID_ATOMIC_QUERY = HibernateIdentityGenerator.SET_NEXT_ID_QUERY
            + " where " + HibernateIdentityGenerator.NEXT_ID_COL + " = ?";
    
    /** The Constant GET_NEXT_ID_QUERY. */
    public static final String GET_NEXT_ID_QUERY = "select "
            + HibernateIdentityGenerator.NEXT_ID_COL + " from "
            + HibernateIdentityGenerator.TABLE_NAME;

    /* (non-Javadoc)
     * @see org.hibernate.id.TableHiLoGenerator#configure(org.hibernate.type.Type, java.util.Properties, org.hibernate.dialect.Dialect)
     */
    @Override
    public void configure(final Type type, final Properties params,
            final Dialect d) {
        if (!params.containsKey(TableGenerator.TABLE)) {
            params.setProperty(TableGenerator.TABLE,
                    HibernateIdentityGenerator.TABLE_NAME);
        }
        if (!params.containsKey(TableGenerator.COLUMN)) {
            params.setProperty(TableGenerator.COLUMN,
                    HibernateIdentityGenerator.NEXT_ID_COL);
        }
        if (!params.containsKey(TableHiLoGenerator.MAX_LO)) {
            params.setProperty(TableHiLoGenerator.MAX_LO, "10");
        }
        super.configure(type, params, d);
    }

    /* (non-Javadoc)
     * @see org.hibernate.id.TableGenerator#generatorKey()
     */
    @Override
    public Object generatorKey() {

        return super.generatorKey();
    }
}
