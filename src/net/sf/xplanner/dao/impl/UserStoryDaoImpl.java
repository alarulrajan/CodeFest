package net.sf.xplanner.dao.impl;

import java.util.Date;
import java.util.List;

import net.sf.xplanner.dao.UserStoryDao;
import net.sf.xplanner.domain.UserStory;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

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

public class UserStoryDaoImpl extends BaseDao<UserStory> implements
        UserStoryDao {

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.UserStoryDao#getAllUserStrories(int)
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<UserStory> getAllUserStrories(final int iterationId) {
        final Criteria criteria = this.createCriteria();
        criteria.createCriteria("iteration").add(
                Restrictions.eq("iteration.id", iterationId));
        return criteria.list();
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.UserStoryDao#getStoriesForPersonWhereCustomer(int)
     */
    @Override
    @Transactional()
    public List<UserStory> getStoriesForPersonWhereCustomer(final int personId) {
        final Criteria criteria = this.createCriteria();
        criteria.createAlias("customer", "customer");
        criteria.createAlias("iteration", "iteration");
        criteria.add(Restrictions.eq("customer.id", personId));
        criteria.add(Restrictions.gt("iteration.endDate", new Date()));
        criteria.setFetchMode("tasks", FetchMode.SELECT);
        criteria.setFetchMode("tasks.timeEntries", FetchMode.SELECT);
        return criteria.list();
    }

    /* (non-Javadoc)
     * @see net.sf.xplanner.dao.UserStoryDao#getStoriesForPersonWhereTracker(int)
     */
    @Override
    @Transactional()
    public List<UserStory> getStoriesForPersonWhereTracker(final int personId) {
        final Criteria criteria = this.createCriteria();
        criteria.createAlias("iteration", "iteration");
        criteria.add(Restrictions.eq("trackerId", personId));
        criteria.add(Restrictions.gt("iteration.endDate", new Date()));
        criteria.setFetchMode("tasks", FetchMode.SELECT);
        criteria.setFetchMode("tasks.timeEntries", FetchMode.SELECT);
        return criteria.list();
    }

}
