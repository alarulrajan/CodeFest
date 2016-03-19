package net.sf.xplanner.web;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import net.sf.xplanner.dao.impl.CommonDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 * The Class BasePage.
 *
 * @param <T>
 *            the generic type
 */
public class BasePage<T> {
	
	/** The domain class. */
	private final Class<T> domainClass = (Class<T>) ((ParameterizedType) this
			.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	
	/** The common dao. */
	@Autowired
	private CommonDao<?> commonDao;

	/**
     * Gets the model and view.
     *
     * @param view
     *            the view
     * @param id
     *            the id
     * @return the model and view
     */
	public ModelAndView getModelAndView(final String view, final Serializable id) {
		final ModelAndView modelAndView = new ModelAndView(view);
		modelAndView.addObject(this.commonDao.getById(this.domainClass, id));
		return modelAndView;
	}

	/**
     * Sets the common dao.
     *
     * @param commonDao
     *            the new common dao
     */
	public void setCommonDao(final CommonDao<?> commonDao) {
		this.commonDao = commonDao;
	}
}
