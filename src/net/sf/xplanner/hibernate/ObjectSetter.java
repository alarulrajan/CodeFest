package net.sf.xplanner.hibernate;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.property.ChainedPropertyAccessor;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.PropertyAccessorFactory;
import org.hibernate.property.Setter;

public class ObjectSetter implements Setter {
	private static final long serialVersionUID = -1836514640217855044L;
	private final Getter getter;
	private final boolean isNew;
	private final Class domainClass;
	private final Setter setter;

	public ObjectSetter(final Getter getter, final String property,
			final boolean isNew) {
		this.getter = getter;
		this.domainClass = (Class) ((ParameterizedType) getter.getMethod()
				.getGenericReturnType()).getActualTypeArguments()[0];
		final ChainedPropertyAccessor propertyAccessor = new ChainedPropertyAccessor(
				new PropertyAccessor[] {
						PropertyAccessorFactory.getPropertyAccessor(
								this.domainClass, null),
						PropertyAccessorFactory.getPropertyAccessor("field") });
		this.setter = propertyAccessor.getSetter(this.domainClass, property);
		this.isNew = isNew;

	}

	@Override
	public Method getMethod() {
		return null;
	}

	@Override
	public String getMethodName() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void set(final Object target, final Object value,
			final SessionFactoryImplementor factory) throws HibernateException {
		final Object object = this.getter.get(target);
		if (object instanceof List<?>) {
			final List list = (List) object;
			if (this.isNew) {
				try {
					final Object newInstance = this.domainClass.newInstance();
					this.setter.set(newInstance, value, factory);
					list.add(newInstance);
				} catch (final InstantiationException e) {
					e.printStackTrace();
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				}
			} else {
				final Object object2 = list.get(list.size() - 1);
				this.setter.set(object2, value, factory);
			}
		}

	}

}
