/**
 * 
 */
package net.sf.xplanner.dao.impl;

import java.util.List;

import net.sf.xplanner.dao.RoleDao;
import net.sf.xplanner.domain.Role;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

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
 */
public class RoleDaoImpl extends BaseDao<Role> implements RoleDao {

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.RoleDao#getRoles(int, int, boolean)
     */
    @Override
    public List<Role> getRoles(final int personId, final int projectId,
            final boolean includeWildcardProject) {
        final Criteria criteria = this.createCriteria();
        criteria.add(Restrictions.isNotEmpty("right"));
        DetachedCriteria.forEntityName("Role");

        // ChangeSoon 
        return null;
    }

}