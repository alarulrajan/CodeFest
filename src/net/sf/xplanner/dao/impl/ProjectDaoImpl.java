package net.sf.xplanner.dao.impl;

import java.util.List;

import net.sf.xplanner.dao.ProjectDao;
import net.sf.xplanner.domain.Person;
import net.sf.xplanner.domain.Project;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

/**
 * The Class ProjectDaoImpl.
 */
public class ProjectDaoImpl extends BaseDao<Project> implements ProjectDao {

	/* (non-Javadoc)
	 * @see net.sf.xplanner.dao.ProjectDao#getAllProjects(net.sf.xplanner.domain.Person)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Project> getAllProjects(final Person user) {
		final Criteria criteria = this.createCriteria();
		criteria.add(Restrictions.eq(Project.HIDDEN, Boolean.FALSE));
		return criteria.list();
	}

}
