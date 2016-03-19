package net.sf.xplanner.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;
import org.hibernate.transform.BasicTransformerAdapter;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * Result transformer that allows to transform a result to a user specified
 * class which will be populated via setter methods or fields matching the alias
 * names.
 * <p/>
 * 
 * <pre>
 * List resultWithAliasedBean = s.createCriteria(Enrolment.class)
 *             .createAlias("student", "st")
 *             .createAlias("course", "co")
 *             .setProjection( Projections.projectionList()
 *                     .add( Projections.property("co.description"), "courseDescription" )
 *             )
 *             .setResultTransformer( new AliasToBeanResultTransformer(StudentDTO.class) )
 *             .list();
 * <p/>
 *  StudentDTO dto = (StudentDTO)resultWithAliasedBean.get(0);
 * </pre>
 *
 * @author max
 * @param <T>
 *            the generic type
 */
public class AliasToBeanResultTransformer<T extends Identifiable> extends
        BasicTransformerAdapter {

    // IMPL NOTE : due to the delayed population of setters (setters cached
    // for performance), we really cannot pro0perly define equality for
    // this transformer

    /** The result class. */
    private final Class<T> resultClass;
    
    /** The property accessor. */
    private final PropertyAccessor propertyAccessor;
    
    /** The setters. */
    private Setter[] setters;
    
    /** The subentities. */
    private final List<String> subentities = new ArrayList<String>();
    
    /** The aliases. */
    private String[] aliases = null;
    
    /** The index of id. */
    private int indexOfId;

    /**
     * Instantiates a new alias to bean result transformer.
     *
     * @param resultClass
     *            the result class
     */
    public AliasToBeanResultTransformer(final Class<T> resultClass) {
        if (resultClass == null) {
            throw new IllegalArgumentException("resultClass cannot be null");
        }
        this.resultClass = resultClass;
        this.propertyAccessor = new ChainedPropertyAccessor(
                new PropertyAccessor[] {
                        PropertyAccessorFactory.getPropertyAccessor(
                                resultClass, null),
                        PropertyAccessorFactory.getPropertyAccessor("field") });
    }

    /* (non-Javadoc)
     * @see org.hibernate.transform.BasicTransformerAdapter#transformTuple(java.lang.Object[], java.lang.String[])
     */
    @Override
    public Object transformTuple(final Object[] tuple, final String[] aliases) {
        if (this.aliases == null) {
            this.aliases = aliases;
        }
        return tuple;
    }

    /* (non-Javadoc)
     * @see org.hibernate.transform.BasicTransformerAdapter#transformList(java.util.List)
     */
    @Override
    @SuppressWarnings({ "unchecked", "boxing" })
    public List transformList(final List collection) {
        final Map<Integer, T> map = new HashMap<Integer, T>();

        if (this.setters == null && this.aliases != null) {
            this.setters = new Setter[this.aliases.length];
            for (int i = 0; i < this.aliases.length; i++) {
                final String alias = this.aliases[i];
                if (alias != null) {
                    if (!alias.contains(".")) {
                        this.setters[i] = this.propertyAccessor.getSetter(
                                this.resultClass, alias);
                        if ("id".equals(alias)) {
                            this.indexOfId = i;
                        }
                    } else {
                        final String[] path = alias.split("\\.");
                        if (path.length == 2) {
                            final String subEntityName = path[0];
                            if (this.subentities.contains(subEntityName)) {
                                this.setters[i] = new ObjectSetter(
                                        this.propertyAccessor
                                                .getGetter(this.resultClass,
                                                        subEntityName),
                                        path[1], false);
                            } else {
                                this.setters[i] = new ObjectSetter(
                                        this.propertyAccessor
                                                .getGetter(this.resultClass,
                                                        subEntityName),
                                        path[1], true);
                                this.subentities.add(subEntityName);
                            }
                        }
                    }
                }
            }
        }

        for (final Object[] tuple : (List<Object[]>) collection) {
            T result;
            try {
                final Integer id = (Integer) tuple[this.indexOfId];
                if (map.containsKey(id)) {
                    result = map.get(id);
                } else {
                    result = this.resultClass.newInstance();
                    map.put(id, result);
                }

                for (int i = 0; i < this.aliases.length; i++) {
                    if (tuple[i] != null) {
                        this.setters[i].set(result, tuple[i], null);
                    }
                }
            } catch (final InstantiationException e) {
                throw new HibernateException(
                        "Could not instantiate resultclass: "
                                + this.resultClass.getName());
            } catch (final IllegalAccessException e) {
                throw new HibernateException(
                        "Could not instantiate resultclass: "
                                + this.resultClass.getName());
            }
        }
        return new ArrayList<Identifiable>(map.values());

    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int result;
        result = this.resultClass.hashCode();
        result = 31 * result + this.propertyAccessor.hashCode();
        return result;
    }
}
