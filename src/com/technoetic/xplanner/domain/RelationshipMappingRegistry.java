package com.technoetic.xplanner.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.Note;
import net.sf.xplanner.domain.Task;
import net.sf.xplanner.domain.UserStory;

/**
 * The Class RelationshipMappingRegistry.
 */
public class RelationshipMappingRegistry {
	
	/** The instance. */
	private static RelationshipMappingRegistry instance;

	// DEBT(DATADRIVEN) Move the relationship converters to the Domain meta data
	/** The data to domain relationship mapping map. */
	// repository (relationships should become part of the DomainClass)
	Map dataToDomainRelationshipMappingMap = new HashMap();
	
	/** The domain to action mapping map. */
	Map domainToActionMappingMap = new HashMap();

	/**
     * Inits the default registry.
     */
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

	/**
     * Adds the mapping.
     *
     * @param domainClass
     *            the domain class
     * @param convertor
     *            the convertor
     */
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

	/**
     * Gets the relationship mapping.
     *
     * @param domainObject
     *            the domain object
     * @param propertyName
     *            the property name
     * @return the relationship mapping
     */
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

	/**
     * Gets the relationship mappings.
     *
     * @param domainObject
     *            the domain object
     * @return the relationship mappings
     */
	public Collection getRelationshipMappings(final DomainObject domainObject) {
		final Map classMappings = (Map) this.dataToDomainRelationshipMappingMap
				.get(domainObject.getClass());
		if (classMappings == null) {
			return Collections.EMPTY_LIST;
		}
		return classMappings.values();
	}

	/**
     * Gets the action mapping.
     *
     * @param domainObject
     *            the domain object
     * @param action
     *            the action
     * @return the action mapping
     */
	public ActionMapping getActionMapping(final DomainObject domainObject,
			final String action) {
		final Map classMappings = (Map) this.domainToActionMappingMap
				.get(domainObject.getClass());
		if (classMappings == null) {
			return null;
		}
		return (ActionMapping) classMappings.get(action);
	}

	/**
     * Gets the actions mappings.
     *
     * @param domainObject
     *            the domain object
     * @return the actions mappings
     */
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
     * Gets the single instance of RelationshipMappingRegistry.
     *
     * @return single instance of RelationshipMappingRegistry
     * @deprecated DEBT(SPRING) Should be injected
     */
	@Deprecated
	public static RelationshipMappingRegistry getInstance() {
		if (RelationshipMappingRegistry.instance == null) {
			RelationshipMappingRegistry.initDefaultRegistry();
		}
		return RelationshipMappingRegistry.instance;
	}

	/**
     * Sets the instance for test.
     *
     * @param instance
     *            the new instance for test
     */
	public static void setInstanceForTest(
			final RelationshipMappingRegistry instance) {
		RelationshipMappingRegistry.instance = instance;
	}
}