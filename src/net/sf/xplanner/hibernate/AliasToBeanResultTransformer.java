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
 * 			.createAlias("student", "st")
 * 			.createAlias("course", "co")
 * 			.setProjection( Projections.projectionList()
 * 					.add( Projections.property("co.description"), "courseDescription" )
 * 			)
 * 			.setResultTransformer( new AliasToBeanResultTransformer(StudentDTO.class) )
 * 			.list();
 * <p/>
 *  StudentDTO dto = (StudentDTO)resultWithAliasedBean.get(0);
 * </pre>
 * 
 * @author max
 */
public class AliasToBeanResultTransformer<T extends Identifiable> extends
		BasicTransformerAdapter {

	// IMPL NOTE : due to the delayed population of setters (setters cached
	// for performance), we really cannot pro0perly define equality for
	// this transformer

	private final Class<T> resultClass;
	private final PropertyAccessor propertyAccessor;
	private Setter[] setters;
	private final List<String> subentities = new ArrayList<String>();
	private String[] aliases = null;
	private int indexOfId;

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

	@Override
	public Object transformTuple(final Object[] tuple, final String[] aliases) {
		if (this.aliases == null) {
			this.aliases = aliases;
		}
		return tuple;
	}

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

	@Override
	public boolean equals(final Object obj) {
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		int result;
		result = this.resultClass.hashCode();
		result = 31 * result + this.propertyAccessor.hashCode();
		return result;
	}
}
