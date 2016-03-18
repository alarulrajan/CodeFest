package com.technoetic.xplanner.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

public class RelationshipMappingRegistry {
	private static RelationshipMappingRegistry instance;

	// DEBT(DATADRIVEN) Move the relationship converters to the Domain meta data
	// repository (relationships should become part of the DomainClass)
	Map dataToDomainRelationshipMappingMap = new HashMap();
	Map domainToActionMappingMap = new HashMap();

	public static void initDefaultRegistry() {
		RelationshipMappingRegistry.instance = new RelationshipMappingRegistry();
		// Relations mapping
		RelationshipMappingRegistry.instance.addMapping(UserStory.class,
				new RelationshipConvertor("customerId", "customer"));
		RelationshipMappingRegistry.instance.addMapping(Task.class,
				new RelationshipConvertor("userStoryId", "userStory"));
		RelationshipMappingRegistry.instance.addMapping(Note.class,
				new RelationshipConvertor("attachmentId", "file"));
	}

	public void addMapping(final Class domainClass,
			final RelationshipConvertor convertor) {
		HashMap classMappings = (HashMap) this.dataToDomainRelationshipMappingMap
				.get(domainClass);
		if (classMappings == null) {
			classMappings = new HashMap();
			this.dataToDomainRelationshipMappingMap.put(domainClass,
					classMappings);
		}
		classMappings.put(convertor.getAdapterProperty(), convertor);
	}

	public RelationshipConvertor getRelationshipMapping(
			final net.sf.xplanner.domain.DomainObject domainObject,
			final String propertyName) {
		final Map classMappings = (Map) this.dataToDomainRelationshipMappingMap
				.get(domainObject.getClass());
		if (classMappings == null) {
			return null;
		}
		return (RelationshipConvertor) classMappings.get(propertyName);
	}

	public Collection getRelationshipMappings(final DomainObject domainObject) {
		final Map classMappings = (Map) this.dataToDomainRelationshipMappingMap
				.get(domainObject.getClass());
		if (classMappings == null) {
			return Collections.EMPTY_LIST;
		}
		return classMappings.values();
	}

	public ActionMapping getActionMapping(final DomainObject domainObject,
			final String action) {
		final Map classMappings = (Map) this.domainToActionMappingMap
				.get(domainObject.getClass());
		if (classMappings == null) {
			return null;
		}
		return (ActionMapping) classMappings.get(action);
	}

	// FIXME Should sort the actions => add an order to the definition
	public Collection getActionsMappings(final DomainObject domainObject) {
		final Map classMappings = (Map) this.domainToActionMappingMap
				.get(domainObject.getClass());
		if (classMappings == null) {
			return Collections.EMPTY_LIST;
		}
		return classMappings.values();
	}

	/**
	 * @deprecated DEBT(SPRING) Should be injected
	 */
	@Deprecated
	public static RelationshipMappingRegistry getInstance() {
		if (RelationshipMappingRegistry.instance == null) {
			RelationshipMappingRegistry.initDefaultRegistry();
		}
		return RelationshipMappingRegistry.instance;
	}

	public static void setInstanceForTest(
			final RelationshipMappingRegistry instance) {
		RelationshipMappingRegistry.instance = instance;
	}
}