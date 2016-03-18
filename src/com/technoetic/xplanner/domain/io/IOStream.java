/*
 * Created by IntelliJ IDEA.
 * User: sg426575
 * Date: May 20, 2006
 * Time: 3:38:29 AM
 */
package com.technoetic.xplanner.domain.io;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Person;

import com.technoetic.xplanner.domain.CharacterEnum;
import com.technoetic.xplanner.domain.DomainClass;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.domain.IterationStatusPersistent;
import com.technoetic.xplanner.domain.StoryDisposition;
import com.technoetic.xplanner.domain.StoryStatus;
import com.technoetic.xplanner.domain.TaskDisposition;
import com.technoetic.xplanner.util.ClassUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractBasicConverter;

public class IOStream {

	public class Document {
		public DomainObject domainObject;
		public Person[] persons;
	}

	public String toXML(final Object object) {
		final XStream stream = this.newXStream();
		return stream.toXML(object);
	}

	public Object fromXML(final String xml) {
		final XStream stream = this.newXStream();
		return stream.fromXML(xml);
	}

	private XStream newXStream() {
		final XStream stream = new XStream();
		stream.setMode(XStream.ID_REFERENCES);
		final Map metadataByTypeName = DomainMetaDataRepository.getInstance()
				.getMetadataByTypeName();
		final Iterator iterator = metadataByTypeName.entrySet().iterator();
		while (iterator.hasNext()) {
			final Entry e = (Entry) iterator.next();
			final DomainClass domainClass = (DomainClass) e.getValue();
			stream.alias((String) e.getKey(), domainClass.getJavaClass());
		}
		stream.registerConverter(new IterationStatusConverter());
		stream.registerConverter(new CharacterEnumConverter(StoryStatus.class));
		stream.registerConverter(new CharacterEnumConverter(
				TaskDisposition.class));
		stream.registerConverter(new CharacterEnumConverter(
				StoryDisposition.class));
		stream.alias("status", IterationStatus.class,
				IterationStatusPersistent.class);
		stream.addImmutableType(IterationStatusPersistent.class);
		stream.addImmutableType(StoryStatus.class);
		stream.addImmutableType(TaskDisposition.class);
		stream.addImmutableType(StoryDisposition.class);
		stream.addImmutableType(Date.class);
		return stream;
	}

	class IterationStatusConverter extends AbstractBasicConverter {

		@Override
		protected Object fromString(final String str) {
			return IterationStatus.fromKey(str);
		}

		@Override
		protected String toString(final Object obj) {
			return ((IterationStatus) obj).getKey();
		}

		@Override
		public boolean canConvert(final Class type) {
			return IterationStatus.class.isAssignableFrom(type);
		}

	}

	class CharacterEnumConverter extends AbstractBasicConverter {
		Class enumClass;
		Method fromName;

		protected CharacterEnumConverter(final Class enumClass) {
			this.enumClass = enumClass;
			try {
				this.fromName = enumClass.getMethod("fromName",
						new Class[] { String.class });
			} catch (final NoSuchMethodException e) {
				throw new IllegalArgumentException(enumClass.getName()
						+ " does not have a method fromName()");
			}
		}

		@Override
		protected String toString(final Object obj) {
			return ((CharacterEnum) obj).getName();
		}

		@Override
		protected Object fromString(final String str) {
			try {
				return this.fromName.invoke(null, new Object[] { str });
			} catch (final IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (final InvocationTargetException e) {
				throw new RuntimeException(e.getCause());
			}
		}

		@Override
		public boolean canConvert(final Class type) {
			return this.enumClass.isAssignableFrom(type);
		}

	}

	class ReferencedPersonList extends ArrayList {
		public ReferencedPersonList(final DomainObject object) {
			this.init(object);
		}

		private void init(final DomainObject object) {
			try {
				final List fields = ClassUtil.getAllFields(object);
				for (int i = 0; i < fields.size(); i++) {
					final Field field = (Field) fields.get(i);
					if (Person.class.isAssignableFrom(field.getType())) {
						this.add(field.get(object));
					}

				}
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}