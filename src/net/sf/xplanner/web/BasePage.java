package net.sf.xplanner.web;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import net.sf.xplanner.dao.impl.CommonDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public class BasePage<T> {
	private final Class<T> domainClass = (Class<T>) ((ParameterizedType) this
			.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	@Autowired
	private CommonDao<?> commonDao;

	public ModelAndView getModelAndView(final String view, final Serializable id) {
		final ModelAndView modelAndView = new ModelAndView(view);
		modelAndView.addObject(this.commonDao.getById(this.domainClass, id));
		return modelAndView;
	}

	public void setCommonDao(final CommonDao<?> commonDao) {
		this.commonDao = commonDao;
	}
}
