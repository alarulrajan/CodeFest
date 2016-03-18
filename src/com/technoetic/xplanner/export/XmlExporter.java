package com.technoetic.xplanner.export;

import java.beans.IntrospectionException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.Role;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.TimeEntry;
import net.sf.xplanner.domain.UserStory;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.betwixt.ElementDescriptor;
import org.apache.commons.betwixt.XMLBeanInfo;
import org.apache.commons.betwixt.XMLIntrospector;
import org.apache.commons.betwixt.expression.Context;
import org.apache.commons.betwixt.expression.Expression;
import org.apache.commons.betwixt.io.BeanWriter;
import org.apache.commons.betwixt.strategy.DecapitalizeNameMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.log4j.Logger;
import org.hibernate.classic.Session;

public class XmlExporter implements Exporter {
	private final Logger log = Logger.getLogger(this.getClass());

	@Override
	public void initializeHeaders(final HttpServletResponse response) {
		response.setHeader("Content-type", "text/xml");
		response.setHeader("Content-disposition", "inline; filename=export.xml");
	}

	@Override
	public byte[] export(final Session session, final Object object)
			throws ExportException {
		try {
			final StringWriter outputWriter = new StringWriter();
			outputWriter.write("<?xml version='1.0' ?>");
			final BeanWriter beanWriter = new BeanWriter(outputWriter);
			beanWriter.getXMLIntrospector().setAttributesForPrimitives(false);
			beanWriter.setWriteIDs(false);
			beanWriter.enablePrettyPrint();

			beanWriter.getXMLIntrospector().setElementNameMapper(
					new DecapitalizeNameMapper());
			this.configureBindings(beanWriter);
			this.installCircularRelationshipsHack(beanWriter);
			final ElementDescriptor descriptor = this.getElementDescriptor(
					beanWriter.getXMLIntrospector(), XPlannerData.class,
					"objects");
			String collectionName = "objects";
			if (object instanceof Project) {
				collectionName = "projects";
			} else if (object instanceof Iteration) {
				collectionName = "iterations";
			}
			descriptor.setLocalName(collectionName);
			final List people = session.find("from person in class "
					+ Person.class.getName());
			final XPlannerData data = new XPlannerData();
			data.setPeople(people);
			data.setObjects(new ArrayList());
			data.getObjects().add(object);

			beanWriter.write("xplanner", data);
			return outputWriter.toString().getBytes();
		} catch (final Exception e) {
			this.log.error("error formatting XML export", e);
			return null;
		}
	}

	// todo - Find a way to eliminate this hack as Hibernate dependencies are
	// added.
	private void installCircularRelationshipsHack(final BeanWriter beanWriter)
			throws IntrospectionException {
		this.installRelationshipMapper(beanWriter, TimeEntry.class, "task",
				"task.id");
		this.installRelationshipMapper(beanWriter, Task.class, "userStory",
				"userStory.id");
		this.installRelationshipMapper(beanWriter, UserStory.class,
				"iteration", "iteration.id");
		this.installRelationshipMapper(beanWriter, Iteration.class, "project",
				"project.id");
	}

	private void installRelationshipMapper(final BeanWriter beanWriter,
			final Class aClass, final String property, final String propertyPath)
			throws IntrospectionException {
		final ElementDescriptor taskParentDescriptor = this
				.getElementDescriptor(beanWriter.getXMLIntrospector(), aClass,
						property);
		taskParentDescriptor.setContextExpression(new Expression() {
			@Override
			public Object evaluate(final Context context) {
				try {
					return PropertyUtils.getProperty(context.getBean(),
							propertyPath);
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}

			@Override
			public void update(final Context context, final String s) {
				// no op
			}
		});
	}

	private ElementDescriptor getElementDescriptor(
			final XMLIntrospector xmlIntrospector, final Class beanClass,
			final String property) throws IntrospectionException {
		final ElementDescriptor[] descriptors = xmlIntrospector
				.introspect(beanClass).getElementDescriptor()
				.getElementDescriptors();
		ElementDescriptor descriptor = null;
		for (int i = 0; i < descriptors.length; i++) {
			String propertyName = descriptors[i].getPropertyName();
			if (propertyName == null) {
				propertyName = descriptors[i].getElementDescriptors()[0]
						.getPropertyName();
			}
			if (propertyName.equals(property)) {
				descriptor = descriptors[i];
			}
		}
		return descriptor;
	}

	private void configureBindings(final BeanWriter beanWriter)
			throws IntrospectionException {
		XMLBeanInfo beanInfo;
		beanInfo = this.configureBeanInfo(beanWriter, Project.class);
		XmlExporter.hideProperty(beanInfo, "currentIteration");
		beanInfo = this.configureBeanInfo(beanWriter, Iteration.class);
		beanInfo = this.configureBeanInfo(beanWriter, UserStory.class);
		beanInfo = this.configureBeanInfo(beanWriter, Task.class);
		beanInfo = this.configureBeanInfo(beanWriter, TimeEntry.class);
		beanInfo = this.configureBeanInfo(beanWriter, Person.class);
		XmlExporter.hideProperty(beanInfo, "lastUpdateTime");
		XmlExporter.hideProperty(beanInfo, "password");
		beanInfo = this.configureBeanInfo(beanWriter, Role.class);
		XmlExporter.hideProperty(beanInfo, "personId");
		XmlExporter.hideProperty(beanInfo, "id");
		XmlExporter.hideProperty(beanInfo, "hibernateLazyInitializer");
	}

	private XMLBeanInfo configureBeanInfo(final BeanWriter beanWriter,
			final Class beanClass) throws IntrospectionException {
		XMLBeanInfo beanInfo;
		beanInfo = beanWriter.getXMLIntrospector().introspect(beanClass);
		XmlExporter.hideProperty(beanInfo, "lastUpdateTime");
		return beanInfo;
	}

	// private void renderAsAttribute(XMLBeanInfo beanInfo, String property) {
	// hideProperty(beanInfo, property);
	// AttributeDescriptor[] originalAttributeDescriptors =
	// beanInfo.getElementDescriptor().getAttributeDescriptors();
	// AttributeDescriptor[] attributeDescriptors = new
	// AttributeDescriptor[originalAttributeDescriptors.length+1];
	// System.arraycopy(originalAttributeDescriptors, 0, attributeDescriptors,
	// 0, originalAttributeDescriptors.length);
	// AttributeDescriptor attributeDescriptor = new
	// AttributeDescriptor(property);
	// attributeDescriptor.setPropertyName(property);
	// attributeDescriptor.setPropertyType(Object.class);
	// BeanInfo bi = null;
	// try {
	// bi = Introspector.getBeanInfo(beanInfo.getBeanClass());
	// } catch (IntrospectionException e) {
	// e.printStackTrace(); //To change body of catch statement use Options |
	// File Templates.
	// }
	// PropertyDescriptor propertyDescriptor = null;
	// PropertyDescriptor propertyDescriptors[] = bi.getPropertyDescriptors();
	// for (int i = 0; i < propertyDescriptors.length; i++) {
	// if (propertyDescriptors[i].fromNameKey().equals(property)) {
	// propertyDescriptor = propertyDescriptors[i];
	// break;
	// }
	// }
	// attributeDescriptor.setTextExpression(new
	// MethodExpression(propertyDescriptor.getReadMethod()));
	// attributeDescriptors[originalAttributeDescriptors.length] =
	// attributeDescriptor;
	// beanInfo.getElementDescriptor().setAttributeDescriptors(attributeDescriptors);
	//
	// }

	private static void hideProperty(final XMLBeanInfo beanInfo,
			final String property) {
		final ArrayList elementDescriptors = new ArrayList();
		final ElementDescriptor elementDescriptor = beanInfo
				.getElementDescriptor();
		CollectionUtils.addAll(elementDescriptors,
				elementDescriptor.getElementDescriptors());
		elementDescriptors.remove(CollectionUtils.find(elementDescriptors,
				new Predicate() {
					@Override
					public boolean evaluate(final Object o) {
						String propertyName = ((ElementDescriptor) o)
								.getPropertyName();
						if (propertyName == null) {
							propertyName = ((ElementDescriptor) o)
									.getElementDescriptors()[0]
									.getPropertyName();
						}
						return propertyName.equals(property);
					}
				}));
		elementDescriptor
				.setElementDescriptors((ElementDescriptor[]) elementDescriptors
						.toArray(new ElementDescriptor[elementDescriptors
								.size()]));
	}

	public static class XPlannerData {
		private List people;
		private List objects;

		public List getPeople() {
			return this.people;
		}

		public void setPeople(final List people) {
			this.people = people;
		}

		public void setObjects(final List objects) {
			this.objects = objects;
		}

		public List getObjects() {
			return this.objects;
		}
	}
}
