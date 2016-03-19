/**
 * 
 */
package net.sf.xplanner.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;

import com.technoetic.xplanner.domain.Identifiable;

/**
 * XplannerPlus, agile planning software.
 *
 * @author Maksym_Chyrkov. Copyright (C) 2009 Maksym Chyrkov This program is
 *         free software: you can redistribute it and/or modify it under the
 *         terms of the GNU General Public License as published by the Free
 *         Software Foundation, either version 3 of the License, or (at your
 *         option) any later version.
 * 
 *         This program is distributed in the hope that it will be useful, but
 *         WITHOUT ANY WARRANTY; without even the implied warranty of
 *         MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *         General Public License for more details.
 * 
 *         You should have received a copy of the GNU General Public License
 *         along with this program. If not, see <http://www.gnu.org/licenses/>
 * @param <E>
 *            the element type
 */
public interface Dao<E extends Identifiable> {

    /**
     * Gets the by id.
     *
     * @param id
     *            the id
     * @return the by id
     */
    E getById(Serializable id);

    /**
     * Creates the criteria.
     *
     * @return the criteria
     */
    Criteria createCriteria();

    /**
     * Save.
     *
     * @param object
     *            the object
     * @return the int
     */
    int save(E object);

    /**
     * Delete.
     *
     * @param objectId
     *            the object id
     */
    void delete(Serializable objectId);

    /**
     * Delete.
     *
     * @param object
     *            the object
     */
    void delete(E object);

    /**
     * Delete all.
     *
     * @param object
     *            the object
     */
    void deleteAll(List<E> object);

    /**
     * Gets the unique object.
     *
     * @param field
     *            the field
     * @param value
     *            the value
     * @return the unique object
     */
    E getUniqueObject(String field, Object value);

    /**
     * Checks if is new object.
     *
     * @param object
     *            the object
     * @return true, if is new object
     */
    boolean isNewObject(E object);

    /**
     * Gets the domain class.
     *
     * @return the domain class
     */
    abstract Class<E> getDomainClass();

    /**
     * Evict.
     *
     * @param object
     *            the object
     */
    void evict(E object);
}
